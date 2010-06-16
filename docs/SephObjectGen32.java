public class SephObject$gen_3_2 implements SephObject {
    public final Selector name1;
    public final SephObject cell1;
    
    public final Selector name2;
    public final SephObject cell2;

    public final Selector name3;
    public final SephObject cell3;

    public final SephObject parent1;
    public final SephObject parent2;

    public SephObject$gen_3_2(Selector n1, SephObject c1, Selector n2, SephObject c2, Selector n3, SephObject c3, SephObject p1, SephObject p2) {
        this.name1 = n1;
        this.cell1 = c1;
        
        this.name2 = n2;
        this.cell2 = c2;

        this.name3 = n3;
        this.cell3 = c3;

        this.parent1 = p1;
        this.parent2 = p2;
    }

    public List<SephObject> parents() {
        return Arrays.asList(parent1, parent2);
    }
    
    public SephObject get(Selector cellName) {
        if(cellName.id == name1.id) {
            return cell1;
        } else if(cellName.id == name2.id) {
            return cell2;
        } else if(cellName.id == name3.id) {
            return cell3;
        } else {
            SephObject result = parent1.get(cellName);
            if(result == null) {
                result = parent2.get(cellName);
            }
            return result;
        }
    }
}
