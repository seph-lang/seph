/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_2_2 extends SephObjectStructure {
    public final SephObject parent0;
    public final SephObject parent1;

    public final String selector0;
    public final SephObject cell0;
    public final String selector1;
    public final SephObject cell1;

    public SephObject_2_2(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        super(meta);
        this.parent0 = parent0;
        this.parent1 = parent1;

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
        this.selector1 = selector1.intern();
        this.cell1 = cell1;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        if(name == this.selector1) return this.cell1;
        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        return null;
    }
}
