/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_1_5 extends SephObjectStructure {
    public final SephObject parent0;

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

    public SephObject_1_5(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        super(meta);
        this.parent0 = parent0;

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
    }

    public final SephObject get(String name) {
        name = name.intern();

        if(name == this.selector0) return this.cell0;
        if(name == this.selector1) return this.cell1;
        if(name == this.selector2) return this.cell2;
        if(name == this.selector3) return this.cell3;
        if(name == this.selector4) return this.cell4;
        return this.parent0.get(name);
    }
}
