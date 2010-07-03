#!/usr/bin/env ruby

PARENTS = 0..4
CELLS   = 0..30

def generate_parents(np)
  np.times.map { |num|
    "    public final SephObject parent#{num};"
  }.join("\n")
end

def generate_cells(nc)
  nc.times.map { |num|
    "    public final String selector#{num};\n" +
    "    public final SephObject cell#{num};"
  }.join("\n")
end

def generate_ctor(np, nc)
  "    public SephObject_#{np}_#{nc}(#{(["IPersistentMap meta"]+np.times.map{|num| "SephObject parent#{num}"} + nc.times.map{|num| "String selector#{num}, SephObject cell#{num}"}).join(", ")}) {\n" +
    "        super(meta);\n" +
    (np.times.map{|num| "        this.parent#{num} = parent#{num};"} + [""] +
     nc.times.map{|num| "        this.selector#{num} = selector#{num}.intern();\n        this.cell#{num} = cell#{num};"} 
     ).join("\n") +
    "\n    }"
end

def generate_ctor_cell_n(np)
  "    public SephObject_#{np}_n(#{(["IPersistentMap meta"]+np.times.map{|num| "SephObject parent#{num}"} + ["IPersistentMap cells"]).join(", ")}) {\n" +
    "        super(meta);\n" +
    (np.times.map{|num| "        this.parent#{num} = parent#{num};"} + [""] +
     ["        this.cells = cells;"]
     ).join("\n") +
    "\n    }"
end

def generate_ctor_parent_n(nc)
  "    public SephObject_n_#{nc}(#{(["IPersistentMap meta", "IPersistentVector parents"]+ nc.times.map{|num| "String selector#{num}, SephObject cell#{num}"}).join(", ")}) {\n" +
    "        super(meta);\n" +
    (["        this.parents = parents;"] + [""] +
     nc.times.map{|num| "        this.selector#{num} = selector#{num}.intern();\n        this.cell#{num} = cell#{num};"} 
     ).join("\n") +
    "\n    }"
end

def generate_ctor_n_n
  return <<JAVA
    public SephObject_n_n(IPersistentMap meta, IPersistentVector parents, IPersistentMap cells) {
        super(meta);
        this.parents = parents;
        this.cells = cells;
    }
JAVA
end

def generate_get(np, nc)
  "    public final SephObject get(String name) {\n" + 
    "        name = name.intern();\n\n" + 
    nc.times.map{|num| "        if(name == this.selector#{num}) return this.cell#{num};\n"}.join("") +
    if np == 1
      "        return this.parent0.get(name);\n"
    else
      "        SephObject result;\n" +
        np.times.map{|num| "        if((result = this.parent#{num}.get(name)) != null) return result;\n"}.join("") +
        "        return null;\n"
    end +
    "    }"
end

def generate_get_cell_n(np)
  "    public final SephObject get(String name) {\n" + 
    "        name = name.intern();\n\n" + 
    "        if(this.cells.containsKey(name)) return (SephObject)this.cells.valueAt(name);\n" +
    if np == 1
      "        return this.parent0.get(name);\n"
    else
      "        SephObject result;\n" +
        np.times.map{|num| "        if((result = this.parent#{num}.get(name)) != null) return result;\n"}.join("") +
        "        return null;\n"
    end +
    "    }"
end

def generate_get_parent_n(nc)
  "    public final SephObject get(String name) {\n" + 
    "        name = name.intern();\n\n" + 
    nc.times.map{|num| "        if(name == this.selector#{num}) return this.cell#{num};\n"}.join("") +
    "        SephObject result;\n" +
    "        for(ISeq s = this.parents.seq(); s != null; s = s.next()) {\n" + 
    "            result = ((SephObject)s.first()).get(name);\n" +
    "            if(result != null) return result;\n" +
    "        }\n" +
    "        return null;\n" +
    "    }"
end

def generate_get_n_n
  return <<JAVA
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
JAVA
end

PARENTS.each do |num_parents|
  CELLS.each do |num_cells|
    File.open("src/main/seph/lang/structure/SephObject_#{num_parents}_#{num_cells}.java", "w") do |f|
      f.write <<JAVA
/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_#{num_parents}_#{num_cells} extends SephObjectStructure {
#{generate_parents(num_parents)}

#{generate_cells(num_cells)}

#{generate_ctor(num_parents, num_cells)}

#{generate_get(num_parents, num_cells)}
}
JAVA
    end
  end
end

PARENTS.each do |num_parents|
  File.open("src/main/seph/lang/structure/SephObject_#{num_parents}_n.java", "w") do |f|
    f.write <<JAVA
/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_#{num_parents}_n extends SephObjectStructure {
#{generate_parents(num_parents)}
     
    public final IPersistentMap cells;

#{generate_ctor_cell_n(num_parents)}

#{generate_get_cell_n(num_parents)}
}
JAVA
  end
end

CELLS.each do |num_cells|
  File.open("src/main/seph/lang/structure/SephObject_n_#{num_cells}.java", "w") do |f|
    f.write <<JAVA
/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_#{num_cells} extends SephObjectStructure {
    public final IPersistentVector parents;

#{generate_cells(num_cells)}

#{generate_ctor_parent_n(num_cells)}

#{generate_get_parent_n(num_cells)}
}
JAVA
  end
end

  File.open("src/main/seph/lang/structure/SephObject_n_n.java", "w") do |f|
    f.write <<JAVA
/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObject_n_n extends SephObjectStructure {
    public final IPersistentVector parents;
     
    public final IPersistentMap cells;

#{generate_ctor_n_n}

#{generate_get_n_n}
}
JAVA
  end

def all_factories
  p1 = PARENTS.map do |num_parents|
    CELLS.map do |num_cells|
      <<JAVA
    public final static SephObject create(#{(["IPersistentMap meta"] + num_parents.times.map{|np| "SephObject parent#{np}"} + num_cells.times.map{|nc| "String selector#{nc}, SephObject cell#{nc}"}).join(", ")}) {
        return new SephObject_#{num_parents}_#{num_cells}(#{(["meta"] + num_parents.times.map{|np| "parent#{np}"} + num_cells.times.map{|nc| "selector#{nc}, cell#{nc}"}).join(", ")});
    }
JAVA
    end.join("\n")
  end.join("\n\n")

  p2 = PARENTS.map do |num_parents|
    <<JAVA
    public final static SephObject create(#{(["IPersistentMap meta"] + num_parents.times.map{|np| "SephObject parent#{np}"} + ["IPersistentMap cells"]).join(", ")}) {
        return new SephObject_#{num_parents}_n(#{(["meta"] + num_parents.times.map{|np| "parent#{np}"} + ["cells"]).join(", ")});
    }
JAVA
  end.join("\n\n")

  p3 = CELLS.map do |num_cells|
    <<JAVA
    public final static SephObject create(#{(["IPersistentMap meta"] + ["IPersistentVector parents"] + num_cells.times.map{|nc| "String selector#{nc}, SephObject cell#{nc}"}).join(", ")}) {
        return new SephObject_n_#{num_cells}(#{(["meta"] + ["parents"] + num_cells.times.map{|nc| "selector#{nc}, cell#{nc}"}).join(", ")});
    }
JAVA
  end.join("\n\n")

  p4 = <<JAVA
    public final static SephObject create(IPersistentMap meta, IPersistentVector parents, IPersistentMap cells) {
        return new SephObject_n_n(meta, parents, cells);
    }
JAVA

  [p1, p2, p3, p4].join("\n\n")
end

def cell_count_cases
  spaces = "            "
  CELLS.map do |num|
    <<JAVA
        case #{num}: {
#{spaces}ISeq seq = keywords.seq();
#{spaces}MapEntry current = (MapEntry)seq.first();
           
#{num.times.map{|cc| "#{spaces}String selector#{cc} = (String)current.key();\n#{spaces}SephObject value#{cc} = (SephObject)current.val();#{if (num-1) != cc; "\n#{spaces}seq = seq.next();\n#{spaces}current = (MapEntry)seq.first();"; end}"}.join("\n\n")}
#{spaces}return create(meta, #{(["parent0"] + num.times.map{|cc| "selector#{cc}, value#{cc}"}).join(", ")});
        }      
JAVA
  end.join("\n")
end

File.open("src/main/seph/lang/structure/SephObjectFactory.java", "w") do |f|
  f.write <<JAVA
/* THIS FILE IS GENERATED. DO NOT EDIT */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;

public class SephObjectFactory {
    public static SephObject spreadAndCreate(IPersistentMap meta, SephObject parent0, IPersistentMap keywords) {
        switch(keywords.count()) {
#{cell_count_cases}
        default: 
            return create(meta, parent0, keywords);
        }
    }

#{all_factories}
}
JAVA
end
