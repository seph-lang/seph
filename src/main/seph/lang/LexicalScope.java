/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.Map;

import seph.lang.ast.Message;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.APersistentMap;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.RT;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class LexicalScope {
    public static class Many extends Six {
        public String[] names;
        public SephObject[] values;

        public final SephObject getValueMany(int index) {
            return values[index];
        }

        Many(LexicalScope parent, SephObject ground, Runtime runtime, String[] names) {
            super(parent, ground, runtime, names[0], names[1], names[2], names[3], names[4], names[5]);
            String[] newNames = new String[names.length-6];
            System.arraycopy(names, 5, newNames, 0, newNames.length);
            this.names = newNames;
            this.values = new SephObject[this.names.length];
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else if(index == 2) {
                    value2 = value;
                } else if(index == 3) {
                    value3 = value;
                } else if(index == 4) {
                    value4 = value;
                } else if(index == 5) {
                    value5 = value;
                } else {
                    values[index-6] = value;
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else if(index == 2) {
                    return value2;
                } else if(index == 3) {
                    return value3;
                } else if(index == 4) {
                    return value4;
                } else if(index == 5) {
                    return value5;
                } else {
                    return values[index-6];
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }
    }

    public static class Empty extends LexicalScope {
        Empty(LexicalScope parent, SephObject ground, Runtime runtime) {
            super(parent, ground, runtime);
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            assert depth != 0 : "An empty scope should never be asked to assign something";
            parent.assign(depth - 1, index, value);
        }

        @Override
        public SephObject get(int depth, int index) {
            assert depth != 0 : "An empty scope should never be asked to get something";
            return parent.get(depth - 1, index);
        }
    }

    public static class One extends LexicalScope {
        public String name0;
        public SephObject value0;

        One(LexicalScope parent, SephObject ground, Runtime runtime, String name0) {
            super(parent, ground, runtime);
            this.name0 = name0;
            this.value0 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                assert index == 0 : "A one var scope should never assign outside of that scope";
                version++;
                value0 = value;
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                assert index == 0 : "A one var scope should never assign outside of that scope";
                return value0;
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueOne() {
            return value0;
        }
    }

    public static class Two extends One {
        public String name1;
        public SephObject value1;

        Two(LexicalScope parent, SephObject ground, Runtime runtime, String name0, String name1) {
            super(parent, ground, runtime, name0);
            this.name1 = name1;
            this.value1 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else {
                    assert false : "Trying to assing index " + index + " which is not valid for a Two scope";
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else {
                    assert false : "Asked to return index " + index + " which is not valid for a Two scope";
                    return null;
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueTwo() {
            return value1;
        }
    }

    public static class Three extends Two {
        public String name2;
        public SephObject value2;

        Three(LexicalScope parent, SephObject ground, Runtime runtime, String name0, String name1, String name2) {
            super(parent, ground, runtime, name0, name1);
            this.name2 = name2;
            this.value2 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else if(index == 2) {
                    value2 = value;
                } else {
                    assert false : "Trying to assing index " + index + " which is not valid for a Three scope";
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else if(index == 2) {
                    return value2;
                } else {
                    assert false : "Asked to return index " + index + " which is not valid for a Three scope";
                    return null;
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueThree() {
            return value2;
        }
    }


    public static class Four extends Three {
        public String name3;
        public SephObject value3;

        Four(LexicalScope parent, SephObject ground, Runtime runtime, String name0, String name1, String name2, String name3) {
            super(parent, ground, runtime, name0, name1, name2);
            this.name3 = name3;
            this.value3 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else if(index == 2) {
                    value2 = value;
                } else if(index == 3) {
                    value3 = value;
                } else {
                    assert false : "Trying to assing index " + index + " which is not valid for a Four scope";
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else if(index == 2) {
                    return value2;
                } else if(index == 3) {
                    return value3;
                } else {
                    assert false : "Asked to return index " + index + " which is not valid for a Four scope";
                    return null;
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueFour() {
            return value3;
        }
    }

    public static class Five extends Four {
        public String name4;
        public SephObject value4;

        Five(LexicalScope parent, SephObject ground, Runtime runtime, String name0, String name1, String name2, String name3, String name4) {
            super(parent, ground, runtime, name0, name1, name2, name3);
            this.name4 = name4;
            this.value4 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else if(index == 2) {
                    value2 = value;
                } else if(index == 3) {
                    value3 = value;
                } else if(index == 4) {
                    value4 = value;
                } else {
                    assert false : "Trying to assing index " + index + " which is not valid for a Five scope";
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else if(index == 2) {
                    return value2;
                } else if(index == 3) {
                    return value3;
                } else if(index == 4) {
                    return value4;
                } else {
                    assert false : "Asked to return index " + index + " which is not valid for a Five scope";
                    return null;
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueFive() {
            return value4;
        }
    }


    public static class Six extends Five {
        public String name5;
        public SephObject value5;

        Six(LexicalScope parent, SephObject ground, Runtime runtime, String name0, String name1, String name2, String name3, String name4, String name5) {
            super(parent, ground, runtime, name0, name1, name2, name3, name4);
            this.name5 = name5;
            this.value5 = null;
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                if(index == 0) {
                    value0 = value;
                } else if(index == 1) {
                    value1 = value;
                } else if(index == 2) {
                    value2 = value;
                } else if(index == 3) {
                    value3 = value;
                } else if(index == 4) {
                    value4 = value;
                } else if(index == 5) {
                    value5 = value;
                } else {
                    assert false : "Trying to assing index " + index + " which is not valid for a Six scope";
                }
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                if(index == 0) {
                    return value0;
                } else if(index == 1) {
                    return value1;
                } else if(index == 2) {
                    return value2;
                } else if(index == 3) {
                    return value3;
                } else if(index == 4) {
                    return value4;
                } else if(index == 5) {
                    return value5;
                } else {
                    assert false : "Asked to return index " + index + " which is not valid for a Six scope";
                    return null;
                }
            } else {
                return parent.get(depth - 1, index);
            }
        }

        public final SephObject getValueSix() {
            return value5;
        }
    }

    public final LexicalScope parent;
    public final Runtime runtime;
    public int version = 0;
    public final SephObject ground;

    public final LexicalScope getParent() {
        return parent;
    }

    private LexicalScope(LexicalScope parent, SephObject ground, Runtime runtime) {
        assert runtime != null;

        this.parent = parent;
        this.runtime = runtime;
        this.ground = ground;
    }

    public static LexicalScope create(LexicalScope parent, SephObject ground, Runtime runtime, String[] names) {
        switch(names.length) {
        case 0:
            return new Empty(parent, ground, runtime);
        case 1:
            return new One(parent, ground, runtime, names[0]);
        case 2:
            return new Two(parent, ground, runtime, names[0], names[1]);
        case 3:
            return new Three(parent, ground, runtime, names[0], names[1], names[2]);
        case 4:
            return new Four(parent, ground, runtime, names[0], names[1], names[2], names[3]);
        case 5:
            return new Five(parent, ground, runtime, names[0], names[1], names[2], names[3], names[4]);
        case 6:
            return new Six(parent, ground, runtime, names[0], names[1], names[2], names[3], names[4], names[5]);
        default:
            return new Many(parent, ground, runtime, names);
        }
    }

    public LexicalScope newScopeWith(String[] names, SephObject newGround) {
        return create(this, newGround, runtime, names);
    }

    public abstract SephObject get(int depth, int index);
    public abstract void assign(int depth, int index, SephObject value);
}// LexicalScope
