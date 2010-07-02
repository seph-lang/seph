/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_3_0 extends SephObjectStructure {
    public final SephObject parent0;
    public final SephObject parent1;
    public final SephObject parent2;



    public SephObject_3_0(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2) {
        super(meta);
        this.parent0 = parent0;
        this.parent1 = parent1;
        this.parent2 = parent2;

    }

    public final SephObject get(String name) {
        name = name.intern();

        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        if((result = this.parent2.get(name)) != null) return result;
        return null;
    }
}
