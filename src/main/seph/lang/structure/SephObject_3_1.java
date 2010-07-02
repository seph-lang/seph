/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_3_1 extends SephObjectStructure {
    public final SephObject parent0;
    public final SephObject parent1;
    public final SephObject parent2;

    public final String selector0;
    public final SephObject cell0;

    public SephObject_3_1(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0) {
        super(meta);
        this.parent0 = parent0;
        this.parent1 = parent1;
        this.parent2 = parent2;

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        if((result = this.parent2.get(name)) != null) return result;
        return null;
    }
}
