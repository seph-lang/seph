/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_n extends SephObjectStructure {
    public final IPersistentVector parents;
     
    public final IPersistentMap cells;

    public SephObject_n_n(IPersistentMap meta, IPersistentVector parents, IPersistentMap cells) {
        super(meta);
        this.parents = parents;
        this.cells = cells;
    }


    public final SephObject get(String name) {
        name = name.intern();

        if(this.cells.containsKey(name)) return (SephObject)this.cells.valueAt(name);

        SephObject result;
        for(ISeq s = this.parents.seq(); s != null; s = s.next()) {
            result = ((SephObject)s.first()).get(name);
            if(result != null) return result;
        }
        return null;
    }

}
