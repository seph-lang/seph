/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.MethodHandle;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SephMethodHandleObject extends SephMethodObject {
    private final MethodHandle handle_n;
    private final MethodHandle handle_0;
    private final MethodHandle handle_1;
    private final MethodHandle handle_2;
    private final MethodHandle handle_3;
    private final MethodHandle handle_4;
    private final MethodHandle handle_5;
    private final MethodHandle handle_n_k;
    private final MethodHandle handle_0_k;
    private final MethodHandle handle_1_k;
    private final MethodHandle handle_2_k;
    private final MethodHandle handle_3_k;
    private final MethodHandle handle_4_k;
    private final MethodHandle handle_5_k;

    public SephMethodHandleObject(MethodHandle handle_n, MethodHandle handle_0, 
                                  MethodHandle handle_1, MethodHandle handle_2, MethodHandle handle_3, MethodHandle handle_4, MethodHandle handle_5,
                                  MethodHandle handle_n_k, MethodHandle handle_0_k,
                                  MethodHandle handle_1_k, MethodHandle handle_2_k, MethodHandle handle_3_k, MethodHandle handle_4_k, MethodHandle handle_5_k) {
        this.handle_n = handle_n;
        this.handle_0 = handle_0;
        this.handle_1 = handle_1;
        this.handle_2 = handle_2;
        this.handle_3 = handle_3;
        this.handle_4 = handle_4;
        this.handle_5 = handle_5;

        this.handle_n_k = handle_n_k;
        this.handle_0_k = handle_0_k;
        this.handle_1_k = handle_1_k;
        this.handle_2_k = handle_2_k;
        this.handle_3_k = handle_3_k;
        this.handle_4_k = handle_4_k;
        this.handle_5_k = handle_5_k;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        switch(arity) {
        case 0:
            return keywords ? handle_0_k : handle_0;
        case 1:
            return keywords ? handle_1_k : handle_1;
        case 2:
            return keywords ? handle_2_k : handle_2;
        case 3:
            return keywords ? handle_3_k : handle_3;
        case 4:
            return keywords ? handle_4_k : handle_4;
        case 5:
            return keywords ? handle_5_k : handle_5;
        default:
            return keywords ? handle_n_k : handle_n;
        }
    }
}// SephMethodHandleObject

