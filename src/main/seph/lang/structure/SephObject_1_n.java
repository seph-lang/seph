/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_1_n extends SephObjectStructure {
    public final SephObject parent0;
     
    public final IPersistentMap cells;

    public SephObject_1_n(IPersistentMap meta, SephObject parent0, IPersistentMap cells) {
        super(meta);
        this.parent0 = parent0;

        this.cells = cells;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(this.cells.containsKey(name)) return (SephObject)this.cells.valueAt(name);
        return this.parent0.get(name);
    }
}
