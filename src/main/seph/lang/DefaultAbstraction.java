/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.List;
import java.util.ArrayList;

import seph.lang.ast.Abstraction;
import seph.lang.ast.Message;
import seph.lang.compiler.AbstractionCompiler;
import seph.lang.compiler.CompilationAborted;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.ISeq;
import seph.lang.persistent.RT;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultAbstraction extends SimpleSephObject {
    private Message code;
    private LexicalScope capture;

    private IPersistentList argumentNames;

    private DefaultAbstraction(IPersistentList argNames, Message code, LexicalScope capture) {
        super(PersistentArrayMap.EMPTY.associate(activatable, Runtime.TRUE));
        this.argumentNames = argNames;
        this.code = code;
        this.capture = capture;
    }

    public final static SephObject createFrom(Abstraction message, LexicalScope scope) {
        List<String> argNames = new ArrayList<String>();
        ISeq seq = RT.seq(message.arguments());

        try {
            return AbstractionCompiler.compile(seq, scope);
        } catch(CompilationAborted e) {
            // System.err.println("BAILED OUT ON COMPILE (" + e.getMessage() + "): " + message);
        }

        if(seq != null) {
            for(;RT.next(seq) != null; seq = RT.next(seq)) {
                argNames.add(((Message)RT.first(seq)).name());
            }
        }

        Message code = (Message)RT.first(seq);
        return new DefaultAbstraction(PersistentList.create(argNames), code, scope);
    }

    @Override
    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        LexicalScope methodScope = capture.newScopeWith(receiver);

        for(ISeq argNames = argumentNames.seq(), args = arguments.seq(); 
            argNames != null && args != null; 
            argNames = argNames.next(), args = args.next()) {

            String name = (String)argNames.first();
            SephObject val = ControlDefaultBehavior.evaluateArgument(args.first(), scope, thread);

            methodScope.directlyAssign(name, val);
        }

        return methodScope.evaluate(thread, code);
    }
}// DefaultAbstraction
