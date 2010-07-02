/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_0 extends SephObjectStructure {
    public final IPersistentVector parents;



    public SephObject_n_0(IPersistentMap meta, IPersistentVector parents) {
        super(meta);
        this.parents = parents;

    }

    public final SephObject get(String name) {
        name = name.intern();

        SephObject result;
        for(ISeq s = this.parents.seq(); s != null; s = s.next()) {
            result = ((SephObject)s.first()).get(name);
            if(result != null) return result;
        }
        return null;
    }
}
