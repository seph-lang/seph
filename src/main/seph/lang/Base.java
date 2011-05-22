/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.structure.SephObjectFactory;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.ISeq;
import seph.lang.persistent.MapEntry;
import seph.lang.persistent.PersistentArrayMap;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class Base implements SephObject {
    public final static Base instance = new Base();


    public SephObject get(String cellName) {
        return seph.lang.bim.BaseBase.get(cellName);
    }

    public Object identity() {
        return seph.lang.bim.BaseBase.IDENTITY;
    }

    public boolean isActivatable() {
        return false;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }


    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }



    @SephMethod
    public final static SephObject with(SephObject receiver, LexicalScope scope, SThread thread, String[] keywordNames, MethodHandle[] keywordArguments) {
        if(keywordNames.length == 0) {
            return receiver;
        } else {
            for(String s : keywordNames) {
                thread.runtime.checkIntrinsicAssignment(s);
            }

            IPersistentMap args = PersistentArrayMap.EMPTY;
            try {
                for(int i = 0; i < keywordNames.length; i++) {
                    args = args.associate(keywordNames[i], keywordArguments[i].invoke(thread, scope, true, true));
                }
            } catch(Throwable e) {
                e.printStackTrace();
                return null;
            }

            return SephObjectFactory.spreadAndCreate(null, receiver, args);
        }
    }

    // TODO: move this into an extension once extensions exist
    // Takes one, two, three or four arguments:
    //    benchmark(code_to_bench)                       ; will default to repetitions = 10, loops = 1
    //    benchmark(42, code_to_bench)                   ; will default to loops = 1
    //    benchmark(42, 2, code_to_bench)                ; repetitions = 42, loops = 2
    //    benchmark("code one", 42, 2, code_to_bench)    ; repetitions = 42, loops = 2, label "code one"
    @SephMethod(evaluateArguments=false)
    public final static SephObject benchmark(SThread thread, LexicalScope scope, IPersistentList args) {
        int count = args.count();
        int repetitions = 10;
        int iterations = 1;
        String label = null;
        ISeq argsLeft = args.seq();
        Object code = null;
        if(count == 4) {
            label = ((Text)ControlDefaultBehavior.evaluateArgument(argsLeft.first(), scope, thread, true)).text();
            argsLeft = argsLeft.next();
        }
        if(count > 1) {
            repetitions = ((gnu.math.IntNum)ControlDefaultBehavior.evaluateArgument(argsLeft.first(), scope, thread, true)).intValue();
            argsLeft = argsLeft.next();
        }
        if(count > 2) {
            iterations = ((gnu.math.IntNum)ControlDefaultBehavior.evaluateArgument(argsLeft.first(), scope, thread, true)).intValue();
            argsLeft = argsLeft.next();
        }
        code = argsLeft.first();

        String separatorType = System.getProperty("seph.benchmark.format");
        if(separatorType == null || separatorType.equals("")) {
            separatorType = "human";
        }

        
        for(int i=0;i<repetitions;i++) {
            long before = System.nanoTime();
            for(int j=0;j<iterations;j++) {
                ControlDefaultBehavior.evaluateArgument(code, scope, thread, true);
            }
            long after = System.nanoTime();
            long time = after-before;
            long secs = time/1000000000;
            long rest = time%1000000000;

            if(separatorType.equals("human")) {
                if(label != null) {
                    System.out.println(String.format("%-32.32s %.6s.%09d", label, secs, rest));
                } else {
                    System.out.println(String.format("%.6s.%09d", secs, rest));
                }
            } else {
                if(label != null) {
                    System.out.println(String.format("%s|%.6s.%09d", label, secs, rest));
                } else {
                    System.out.println(String.format("|%.6s.%09d", secs, rest));
                }
            }
        }
        
        return Runtime.NIL;
    }

    @SephMethod(name="==")
    public final static SephObject eq(SephObject receiver, SephObject other) {
        if(receiver.equals(other)) {
            return Runtime.TRUE;
        } else {
            return Runtime.FALSE;
        }
    }
}// Base
