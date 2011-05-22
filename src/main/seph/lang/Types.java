/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class Types {
    private Types() {}
    
    public final static MethodType ACTIVATE_METHOD_TYPE   = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5 = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, MethodHandle.class, 
                                                                                                      MethodHandle.class, MethodHandle.class, MethodHandle.class);

    public final static MethodType ACTIVATE_METHOD_TYPE_K   = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, 
                                                                                                      MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, 
                                                                                                      MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5_K = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, MethodHandle.class, 
                                                                                                      MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
}// Types
