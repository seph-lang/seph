/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObjectFactory {
    public static SephObject spreadAndCreate(IPersistentMap meta, SephObject parent0, IPersistentMap keywords) {
        switch(keywords.count()) {
        case 0: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           

            return create(meta, parent0);
        }      

        case 1: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0);
        }      

        case 2: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1);
        }      

        case 3: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2);
        }      

        case 4: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3);
        }      

        case 5: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4);
        }      

        case 6: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5);
        }      

        case 7: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6);
        }      

        case 8: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7);
        }      

        case 9: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8);
        }      

        case 10: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9);
        }      

        case 11: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10);
        }      

        case 12: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11);
        }      

        case 13: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12);
        }      

        case 14: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13);
        }      

        case 15: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14);
        }      

        case 16: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15);
        }      

        case 17: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16);
        }      

        case 18: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17);
        }      

        case 19: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18);
        }      

        case 20: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19);
        }      

        case 21: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20);
        }      

        case 22: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21);
        }      

        case 23: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22);
        }      

        case 24: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23);
        }      

        case 25: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24);
        }      

        case 26: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector25 = (String)current.key();
            SephObject value25 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24, selector25, value25);
        }      

        case 27: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector25 = (String)current.key();
            SephObject value25 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector26 = (String)current.key();
            SephObject value26 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24, selector25, value25, selector26, value26);
        }      

        case 28: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector25 = (String)current.key();
            SephObject value25 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector26 = (String)current.key();
            SephObject value26 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector27 = (String)current.key();
            SephObject value27 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24, selector25, value25, selector26, value26, selector27, value27);
        }      

        case 29: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector25 = (String)current.key();
            SephObject value25 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector26 = (String)current.key();
            SephObject value26 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector27 = (String)current.key();
            SephObject value27 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector28 = (String)current.key();
            SephObject value28 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24, selector25, value25, selector26, value26, selector27, value27, selector28, value28);
        }      

        case 30: {
            ISeq seq = keywords.seq();
            MapEntry current = (MapEntry)seq.first();
           
            String selector0 = (String)current.key();
            SephObject value0 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector1 = (String)current.key();
            SephObject value1 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector2 = (String)current.key();
            SephObject value2 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector3 = (String)current.key();
            SephObject value3 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector4 = (String)current.key();
            SephObject value4 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector5 = (String)current.key();
            SephObject value5 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector6 = (String)current.key();
            SephObject value6 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector7 = (String)current.key();
            SephObject value7 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector8 = (String)current.key();
            SephObject value8 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector9 = (String)current.key();
            SephObject value9 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector10 = (String)current.key();
            SephObject value10 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector11 = (String)current.key();
            SephObject value11 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector12 = (String)current.key();
            SephObject value12 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector13 = (String)current.key();
            SephObject value13 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector14 = (String)current.key();
            SephObject value14 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector15 = (String)current.key();
            SephObject value15 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector16 = (String)current.key();
            SephObject value16 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector17 = (String)current.key();
            SephObject value17 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector18 = (String)current.key();
            SephObject value18 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector19 = (String)current.key();
            SephObject value19 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector20 = (String)current.key();
            SephObject value20 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector21 = (String)current.key();
            SephObject value21 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector22 = (String)current.key();
            SephObject value22 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector23 = (String)current.key();
            SephObject value23 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector24 = (String)current.key();
            SephObject value24 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector25 = (String)current.key();
            SephObject value25 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector26 = (String)current.key();
            SephObject value26 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector27 = (String)current.key();
            SephObject value27 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector28 = (String)current.key();
            SephObject value28 = (SephObject)current.val();
            seq = seq.next();
            current = (MapEntry)seq.first();

            String selector29 = (String)current.key();
            SephObject value29 = (SephObject)current.val();
            return create(meta, parent0, selector0, value0, selector1, value1, selector2, value2, selector3, value3, selector4, value4, selector5, value5, selector6, value6, selector7, value7, selector8, value8, selector9, value9, selector10, value10, selector11, value11, selector12, value12, selector13, value13, selector14, value14, selector15, value15, selector16, value16, selector17, value17, selector18, value18, selector19, value19, selector20, value20, selector21, value21, selector22, value22, selector23, value23, selector24, value24, selector25, value25, selector26, value26, selector27, value27, selector28, value28, selector29, value29);
        }      

        default: 
            return create(meta, parent0, keywords);
        }
    }

    public final static SephObject create(IPersistentMap meta) {
        return new SephObject_0_0(meta);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0) {
        return new SephObject_0_1(meta, selector0, cell0);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_0_2(meta, selector0, cell0, selector1, cell1);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_0_3(meta, selector0, cell0, selector1, cell1, selector2, cell2);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_0_4(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_0_5(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_0_6(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_0_7(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_0_8(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_0_9(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_0_10(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_0_11(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_0_12(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_0_13(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_0_14(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_0_15(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_0_16(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_0_17(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_0_18(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_0_19(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_0_20(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_0_21(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_0_22(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_0_23(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_0_24(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_0_25(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_0_26(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_0_27(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_0_28(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_0_29(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }

    public final static SephObject create(IPersistentMap meta, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_0_30(meta, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0) {
        return new SephObject_1_0(meta, parent0);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0) {
        return new SephObject_1_1(meta, parent0, selector0, cell0);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_1_2(meta, parent0, selector0, cell0, selector1, cell1);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_1_3(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_1_4(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_1_5(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_1_6(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_1_7(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_1_8(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_1_9(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_1_10(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_1_11(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_1_12(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_1_13(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_1_14(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_1_15(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_1_16(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_1_17(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_1_18(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_1_19(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_1_20(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_1_21(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_1_22(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_1_23(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_1_24(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_1_25(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_1_26(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_1_27(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_1_28(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_1_29(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_1_30(meta, parent0, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1) {
        return new SephObject_2_0(meta, parent0, parent1);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0) {
        return new SephObject_2_1(meta, parent0, parent1, selector0, cell0);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_2_2(meta, parent0, parent1, selector0, cell0, selector1, cell1);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_2_3(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_2_4(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_2_5(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_2_6(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_2_7(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_2_8(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_2_9(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_2_10(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_2_11(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_2_12(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_2_13(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_2_14(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_2_15(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_2_16(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_2_17(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_2_18(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_2_19(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_2_20(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_2_21(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_2_22(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_2_23(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_2_24(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_2_25(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_2_26(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_2_27(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_2_28(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_2_29(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_2_30(meta, parent0, parent1, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2) {
        return new SephObject_3_0(meta, parent0, parent1, parent2);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0) {
        return new SephObject_3_1(meta, parent0, parent1, parent2, selector0, cell0);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_3_2(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_3_3(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_3_4(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_3_5(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_3_6(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_3_7(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_3_8(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_3_9(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_3_10(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_3_11(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_3_12(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_3_13(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_3_14(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_3_15(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_3_16(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_3_17(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_3_18(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_3_19(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_3_20(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_3_21(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_3_22(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_3_23(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_3_24(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_3_25(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_3_26(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_3_27(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_3_28(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_3_29(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_3_30(meta, parent0, parent1, parent2, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3) {
        return new SephObject_4_0(meta, parent0, parent1, parent2, parent3);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0) {
        return new SephObject_4_1(meta, parent0, parent1, parent2, parent3, selector0, cell0);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_4_2(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_4_3(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_4_4(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_4_5(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_4_6(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_4_7(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_4_8(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_4_9(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_4_10(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_4_11(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_4_12(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_4_13(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_4_14(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_4_15(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_4_16(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_4_17(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_4_18(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_4_19(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_4_20(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_4_21(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_4_22(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_4_23(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_4_24(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_4_25(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_4_26(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_4_27(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_4_28(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_4_29(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }

    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_4_30(meta, parent0, parent1, parent2, parent3, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentMap cells) {
        return new SephObject_0_n(meta, cells);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, IPersistentMap cells) {
        return new SephObject_1_n(meta, parent0, cells);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, IPersistentMap cells) {
        return new SephObject_2_n(meta, parent0, parent1, cells);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, IPersistentMap cells) {
        return new SephObject_3_n(meta, parent0, parent1, parent2, cells);
    }


    public final static SephObject create(IPersistentMap meta, SephObject parent0, SephObject parent1, SephObject parent2, SephObject parent3, IPersistentMap cells) {
        return new SephObject_4_n(meta, parent0, parent1, parent2, parent3, cells);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents) {
        return new SephObject_n_0(meta, parents);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0) {
        return new SephObject_n_1(meta, parents, selector0, cell0);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1) {
        return new SephObject_n_2(meta, parents, selector0, cell0, selector1, cell1);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2) {
        return new SephObject_n_3(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3) {
        return new SephObject_n_4(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4) {
        return new SephObject_n_5(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5) {
        return new SephObject_n_6(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6) {
        return new SephObject_n_7(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7) {
        return new SephObject_n_8(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8) {
        return new SephObject_n_9(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9) {
        return new SephObject_n_10(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10) {
        return new SephObject_n_11(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11) {
        return new SephObject_n_12(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12) {
        return new SephObject_n_13(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13) {
        return new SephObject_n_14(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14) {
        return new SephObject_n_15(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15) {
        return new SephObject_n_16(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16) {
        return new SephObject_n_17(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17) {
        return new SephObject_n_18(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18) {
        return new SephObject_n_19(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19) {
        return new SephObject_n_20(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20) {
        return new SephObject_n_21(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21) {
        return new SephObject_n_22(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22) {
        return new SephObject_n_23(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23) {
        return new SephObject_n_24(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24) {
        return new SephObject_n_25(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25) {
        return new SephObject_n_26(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26) {
        return new SephObject_n_27(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27) {
        return new SephObject_n_28(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28) {
        return new SephObject_n_29(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, String selector0, SephObject cell0, String selector1, SephObject cell1, String selector2, SephObject cell2, String selector3, SephObject cell3, String selector4, SephObject cell4, String selector5, SephObject cell5, String selector6, SephObject cell6, String selector7, SephObject cell7, String selector8, SephObject cell8, String selector9, SephObject cell9, String selector10, SephObject cell10, String selector11, SephObject cell11, String selector12, SephObject cell12, String selector13, SephObject cell13, String selector14, SephObject cell14, String selector15, SephObject cell15, String selector16, SephObject cell16, String selector17, SephObject cell17, String selector18, SephObject cell18, String selector19, SephObject cell19, String selector20, SephObject cell20, String selector21, SephObject cell21, String selector22, SephObject cell22, String selector23, SephObject cell23, String selector24, SephObject cell24, String selector25, SephObject cell25, String selector26, SephObject cell26, String selector27, SephObject cell27, String selector28, SephObject cell28, String selector29, SephObject cell29) {
        return new SephObject_n_30(meta, parents, selector0, cell0, selector1, cell1, selector2, cell2, selector3, cell3, selector4, cell4, selector5, cell5, selector6, cell6, selector7, cell7, selector8, cell8, selector9, cell9, selector10, cell10, selector11, cell11, selector12, cell12, selector13, cell13, selector14, cell14, selector15, cell15, selector16, cell16, selector17, cell17, selector18, cell18, selector19, cell19, selector20, cell20, selector21, cell21, selector22, cell22, selector23, cell23, selector24, cell24, selector25, cell25, selector26, cell26, selector27, cell27, selector28, cell28, selector29, cell29);
    }


    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, IPersistentMap cells) {
        return new SephObject_n_n(meta, parents, cells);
    }

}
