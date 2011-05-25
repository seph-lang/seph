/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.SephObject;
import java.lang.invoke.*;
import java.util.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class TypeProfile {
    int count = 1;

    public int count() { 
        return count;
    }

    public int morphism() { 
        return 1; 
    }

    public TypeProfile leadCase() { 
        return this; 
    }
    
    public boolean matchAndIncrement(SephObject x) {
        if(!match(x))
            return false;
        incrementCount(1);
        return true;
    }

    public int incrementCount(int d) {
        int c1 = count + d;
        if(c1 <= 0)
            return c1 - d;
        count = c1;
        return c1;
    }

    public boolean isMonomorphic() {
        return !(this instanceof Polymorph);
    }

    public TypeProfile[] cases() {
        return new TypeProfile[]{ this };
    }

    public TypeProfile append(TypeProfile that) {
        if (that == null || that == this)  return this;
        return new Polymorph(this, that);
    }

    public abstract boolean match(SephObject x);

    public static TypeProfile forIdentity(SephObject value) {
        return new ForIdentity(value, value.identity());
    }
    public static final class ForIdentity extends TypeProfile {
        final SephObject value;
        final Object token;
        ForIdentity(SephObject value, Object token) { 
            this.value = value;
            this.token = token;
        }
        SephObject matchValue() { return this.value; }
        Object matchIdentity() { return this.token; }
        public boolean match(SephObject x) {
            return token == x.identity();
        }
    }

    static final class Polymorph extends TypeProfile {
        final ArrayList<TypeProfile> cases;
        Polymorph(TypeProfile prof) {
            count = 0;
            this.cases = new ArrayList<TypeProfile>(4);
            add(prof);
        }
        Polymorph(TypeProfile prof, TypeProfile prof2) {
            this(prof);
            add(prof2);
        }
        private void add(TypeProfile prof) {
            notPoly(prof);
            for(int pos = cases.size(); ; pos--) {
                if(pos == 0 || cases.get(pos-1).count() >= prof.count()) {
                    cases.add(pos, prof);
                    break;
                }
            }
        }

        public TypeProfile append(TypeProfile prof) {
            add(prof);
            return this;
        }

        public int morphism() {
            return cases.size();
        }

        public int count() {
          int sum = this.count;
          for(TypeProfile prof : cases) {
              sum += prof.count();
          }
          return sum;
        }

        public boolean match(SephObject x) {
            for(TypeProfile prof : cases) {
                if(prof.match(x))
                    return true;
            }
            return false;
        }

        public boolean matchAndIncrement(SephObject x) {
            TypeProfile prev = null;
            for(TypeProfile prof : cases) {
                if(prof.match(x)) {
                    int nc = prof.incrementCount(1);
                    if(prev != null && prev.count() < nc) {
                        int pos = cases.indexOf(prof);
                        for(;;) {
                            Collections.swap(cases, pos-1, pos);
                            pos--;
                            if(pos == 0 || cases.get(pos-1).count() >= nc)
                                break;
                        }
                    }
                    return true;
                }
                prev = prof;
            }
            return false;
        }

        public TypeProfile[] cases() {
            return cases.toArray(new TypeProfile[cases.size()]);
        }

        public TypeProfile leadCase() {
            return cases.get(0);
        }
        
        private static void notPoly(TypeProfile prof) {
            if(prof instanceof Polymorph)  throw new InternalError();
        }
     }
}// TypeProfile

