/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_1_1 extends SephObjectStructure {
    public final SephObject parent0;

    public final String selector0;
    public final SephObject cell0;

    public SephObject_1_1(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0) {
        super(meta);
        this.parent0 = parent0;

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        return this.parent0.get(name);
    }
}
