/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_1_0 extends SephObjectStructure {
    public final SephObject parent0;



    public SephObject_1_0(IPersistentMap meta, SephObject parent0) {
        super(meta);
        this.parent0 = parent0;

    }

    public final SephObject get(String name) {
        name = name.intern();

        return this.parent0.get(name);
    }
}
