/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_2_n extends SephObjectStructure {
    public final SephObject parent0;
    public final SephObject parent1;
     
    public final IPersistentMap cells;

    public SephObject_2_n(IPersistentMap meta, SephObject parent0, SephObject parent1, IPersistentMap cells) {
        super(meta);
        this.parent0 = parent0;
        this.parent1 = parent1;

        this.cells = cells;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(this.cells.containsKey(name)) return (SephObject)this.cells.valueAt(name);
        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        return null;
    }
}
