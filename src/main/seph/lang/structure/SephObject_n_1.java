/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_1 extends SephObjectStructure {
    public final IPersistentVector parents;

    public final String selector0;
    public final SephObject cell0;

    public SephObject_n_1(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0) {
        super(meta);
        this.parents = parents;

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        SephObject result;
        for(ISeq s = this.parents.seq(); s != null; s = s.next()) {
            result = ((SephObject)s.first()).get(name);
            if(result != null) return result;
        }
        return null;
    }
}
