/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_0_n extends SephObjectStructure {

     
    public final IPersistentMap cells;

    public SephObject_0_n(IPersistentMap meta, IPersistentMap cells) {
        super(meta);

        this.cells = cells;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(this.cells.containsKey(name)) return (SephObject)this.cells.valueAt(name);
        SephObject result;
        return null;
    }
}
