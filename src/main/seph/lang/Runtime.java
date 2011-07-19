/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import seph.lang.ast.Abstraction;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.parser.Parser;
import seph.lang.parser.StringUtils;
import seph.lang.compiler.SephCallSite;
import seph.lang.persistent.PersistentList;

import java.lang.invoke.SwitchPoint;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Runtime {
    public final static SephClassLoader LOADER = new SephClassLoader(Runtime.class.getClassLoader());

    public final static SephObject TRUE = new SimpleSephObject() {
            public String toString() {
                return "true";
            }

            public SephObject get(String cellName) {
                return Something.instance.get(cellName);
            }
        };
    public final static SephObject FALSE = new SimpleSephObject() {
            public String toString() {
                return "false";
            }

            public boolean isTrue() {
                return false;
            }

            public SephObject get(String cellName) {
                return Something.instance.get(cellName);
            }
        };
    public final static SephObject NIL  = new SimpleSephObject() {
            public String toString() {
                return "nil";
            }

            public boolean isTrue() {
                return false;
            }

            public SephObject get(String cellName) {
                return Something.instance.get(cellName);
            }
        };
    
    public static void empty() {}
    public static void invalidate(SwitchPoint sp) {
        SwitchPoint.invalidateAll(new SwitchPoint[] {sp});
    }

    public final static MethodHandle INVALIDATE_MH = SephCallSite.findStatic(seph.lang.Runtime.class, "invalidate", MethodType.methodType(void.class, SwitchPoint.class));
    public final static MethodHandle EMPTY_MH      = SephCallSite.findStatic(seph.lang.Runtime.class, "empty", MethodType.methodType(void.class));

    public final SwitchPoint INTRINSIC_TRUE_SP = new SwitchPoint();
    public final SwitchPoint INTRINSIC_FALSE_SP = new SwitchPoint();
    public final SwitchPoint INTRINSIC_NIL_SP = new SwitchPoint();

    public final MethodHandle INVALIDATE_TRUE  = INTRINSIC_TRUE_SP.guardWithTest(INVALIDATE_MH.bindTo(INTRINSIC_TRUE_SP), EMPTY_MH);
    public final MethodHandle INVALIDATE_FALSE = INTRINSIC_FALSE_SP.guardWithTest(INVALIDATE_MH.bindTo(INTRINSIC_FALSE_SP), EMPTY_MH);
    public final MethodHandle INVALIDATE_NIL = INTRINSIC_NIL_SP.guardWithTest(INVALIDATE_MH.bindTo(INTRINSIC_NIL_SP), EMPTY_MH);

    public void checkIntrinsicAssignment(String name) {
        name = name.intern();
        try {
            if(name == "true") {
                INVALIDATE_TRUE.invokeExact();
            } else if(name == "false") {
                INVALIDATE_FALSE.invokeExact();
            } else if(name == "nil") {
                INVALIDATE_NIL.invokeExact();
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    public Text newText(String stringBeforeEscapeMangling) {
        return new Text(new StringUtils().replaceEscapes(stringBeforeEscapeMangling));
    }

    public Text newUnescapedText(String text) {
        return new Text(text);
    }

    public Regexp newRegexp(String pattern, String flags) {
        return Regexp.create(new StringUtils().replaceRegexpEscapes(pattern), flags);
    }

    public Object evaluateStream(String name, Reader reader) throws ControlFlow, IOException {
        Parser p = new Parser(this, reader, name);
        Message msg = (Message)p.parseFully().seq().first();
        Abstraction amsg = (Abstraction)NamedMessage.create("#", new PersistentList(msg), null, "<init>", -1, -1, p.scope);
        SephObject so = DefaultAbstraction.createFrom(amsg, LexicalScope.create(null, this, new String[0]), "toplevel");
        SThread thread = new SThread(this);
        SephObject tmp = null;
        try {
            tmp = (SephObject)so.activationFor(0, false).invokeExact((SephObject)Ground.instance, thread, (LexicalScope)null);
            while(tmp == SThread.TAIL_MARKER) {
                MethodHandle tail = thread.tail;
                thread.tail = null;
                tmp = (SephObject)tail.invokeExact();
            }
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
        return tmp;
    }

    public Object evaluateFile(File f) throws ControlFlow, IOException {
        return evaluateStream(f.getCanonicalPath(), new InputStreamReader(new FileInputStream(f), "UTF-8"));
    }

    public Object evaluateFile(String filename) throws ControlFlow, IOException {
        return evaluateStream(filename, new InputStreamReader(new FileInputStream(new File(filename)), "UTF-8"));
    }

    public Object evaluateString(String string) throws ControlFlow, IOException {
        return evaluateStream("<eval>", new StringReader(string));
    }

    public SephConfig configuration() {
        return config;
    }

    private final SephConfig config;

    public Runtime() {
        this(new SephConfig());
    }

    public Runtime(SephConfig config) {
        this.config = config;
    }
}// Runtime
