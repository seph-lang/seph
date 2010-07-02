/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_0_1 extends SephObjectStructure {


    public final String selector0;
    public final SephObject cell0;

    public SephObject_0_1(IPersistentMap meta, String selector0, SephObject cell0) {
        super(meta);

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        SephObject result;
        return null;
    }
}
