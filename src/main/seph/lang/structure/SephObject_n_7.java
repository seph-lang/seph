/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_7 extends SephObjectStructure {
    public final IPersistentVector parents;

    public final String selector0;
    public final SephObject cell0;
    public final String selector1;
    public final SephObject cell1;
    public final String selector2;
    public final SephObject cell2;
    public final String selector3;
    public final SephObject cell3;
    public final String selector4;
    public final SephObject cell4;
    public final String selector5;
    public final SephObject cell5;
    public final String selector6;
    public final SephObject cell6;

    public SephObject_n_7(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        super(meta);
        this.parents = parents;

        this.selector0 = selector0.intern();
        this.cell0 = cell0;
        this.selector1 = selector1.intern();
        this.cell1 = cell1;
        this.selector2 = selector2.intern();
        this.cell2 = cell2;
        this.selector3 = selector3.intern();
        this.cell3 = cell3;
        this.selector4 = selector4.intern();
        this.cell4 = cell4;
        this.selector5 = selector5.intern();
        this.cell5 = cell5;
        this.selector6 = selector6.intern();
        this.cell6 = cell6;
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        if(name == this.selector1) return this.cell1;
        if(name == this.selector2) return this.cell2;
        if(name == this.selector3) return this.cell3;
        if(name == this.selector4) return this.cell4;
        if(name == this.selector5) return this.cell5;
        if(name == this.selector6) return this.cell6;
        SephObject result;
        for(ISeq s = this.parents.seq(); s != null; s = s.next()) {
            result = ((SephObject)s.first()).get(name);
            if(result != null) return result;
        }
        return null;
    }
}
