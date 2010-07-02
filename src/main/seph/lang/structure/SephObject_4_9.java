/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_4_9 extends SephObjectStructure {
    public final SephObject parent0;
    public final SephObject parent1;
    public final SephObject parent2;
    public final SephObject parent3;

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
    public final String selector7;
    public final SephObject cell7;
    public final String selector8;
    public final SephObject cell8;

    public SephObject_4_9(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        super(meta);
        this.parent0 = parent0;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.parent3 = parent3;

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
        this.selector7 = selector7.intern();
        this.cell7 = cell7;
        this.selector8 = selector8.intern();
        this.cell8 = cell8;
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
        if(name == this.selector7) return this.cell7;
        if(name == this.selector8) return this.cell8;
        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        if((result = this.parent2.get(name)) != null) return result;
        if((result = this.parent3.get(name)) != null) return result;
        return null;
    }
}
