/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_4_20 extends SephObjectStructure {
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
    public final String selector9;
    public final SephObject cell9;
    public final String selector10;
    public final SephObject cell10;
    public final String selector11;
    public final SephObject cell11;
    public final String selector12;
    public final SephObject cell12;
    public final String selector13;
    public final SephObject cell13;
    public final String selector14;
    public final SephObject cell14;
    public final String selector15;
    public final SephObject cell15;
    public final String selector16;
    public final SephObject cell16;
    public final String selector17;
    public final SephObject cell17;
    public final String selector18;
    public final SephObject cell18;
    public final String selector19;
    public final SephObject cell19;

    public SephObject_4_20(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
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
        this.selector9 = selector9.intern();
        this.cell9 = cell9;
        this.selector10 = selector10.intern();
        this.cell10 = cell10;
        this.selector11 = selector11.intern();
        this.cell11 = cell11;
        this.selector12 = selector12.intern();
        this.cell12 = cell12;
        this.selector13 = selector13.intern();
        this.cell13 = cell13;
        this.selector14 = selector14.intern();
        this.cell14 = cell14;
        this.selector15 = selector15.intern();
        this.cell15 = cell15;
        this.selector16 = selector16.intern();
        this.cell16 = cell16;
        this.selector17 = selector17.intern();
        this.cell17 = cell17;
        this.selector18 = selector18.intern();
        this.cell18 = cell18;
        this.selector19 = selector19.intern();
        this.cell19 = cell19;
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
        if(name == this.selector9) return this.cell9;
        if(name == this.selector10) return this.cell10;
        if(name == this.selector11) return this.cell11;
        if(name == this.selector12) return this.cell12;
        if(name == this.selector13) return this.cell13;
        if(name == this.selector14) return this.cell14;
        if(name == this.selector15) return this.cell15;
        if(name == this.selector16) return this.cell16;
        if(name == this.selector17) return this.cell17;
        if(name == this.selector18) return this.cell18;
        if(name == this.selector19) return this.cell19;
        SephObject result;
        if((result = this.parent0.get(name)) != null) return result;
        if((result = this.parent1.get(name)) != null) return result;
        if((result = this.parent2.get(name)) != null) return result;
        if((result = this.parent3.get(name)) != null) return result;
        return null;
    }
}
