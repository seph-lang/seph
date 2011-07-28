/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.structure.SephObjectFactory;
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

    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.BaseBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.BaseBase.IDENTITY;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        return ActivationHelpers.noActivateFor(this, arity, keywords);
    }

    @SephMethod
    public final static SephObject with(SephObject receiver, LexicalScope scope, SThread thread, String[] keywordNames, MethodHandle[] keywordArguments) {
        if(keywordNames.length == 0) {
            return receiver;
        } else {
            IPersistentMap args = PersistentArrayMap.EMPTY;
            try {
                for(int i = 0; i < keywordNames.length; i++) {
                    args = args.associate(keywordNames[i], (SephObject)keywordArguments[i].invokeExact(thread, scope, true, true));
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
    public final static SephObject benchmark(SThread thread, LexicalScope scope, MethodHandle[] args) {
        int count = args.length;
        int repetitions = 10;
        int iterations = 1;
        String label = null;
        int argsLeft = 0;
        Object code = null;
        if(count == 4) {
            label = ((Text)ControlDefaultBehavior.evaluateArgument(args[argsLeft++], scope, thread, true)).text();
        }
        if(count > 1) {
            repetitions = ((gnu.math.IntNum)ControlDefaultBehavior.evaluateArgument(args[argsLeft++], scope, thread, true)).intValue();
        }
        if(count > 2) {
            iterations = ((gnu.math.IntNum)ControlDefaultBehavior.evaluateArgument(args[argsLeft++], scope, thread, true)).intValue();
        }
        code = args[argsLeft];

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
