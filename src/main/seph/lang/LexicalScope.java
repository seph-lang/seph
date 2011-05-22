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
    public static class Many extends LexicalScope {
        public String[] names;
        public SephObject[] values;

        Many(LexicalScope parent, Runtime runtime, String[] names) {
            super(parent, runtime);
            this.names = names;
            this.values = new SephObject[names.length];
        }

        @Override
        public void assign(int depth, int index, SephObject value) {
            if(depth == 0) {
                version++;
                values[index] = value;
            } else {
                parent.assign(depth - 1, index, value);
            }
        }

        @Override
        public SephObject get(int depth, int index) {
            if(depth == 0) {
                return values[index];
            } else {
                return parent.get(depth - 1, index);
            }
        }
    }

    public static class Empty extends LexicalScope {
        Empty(LexicalScope parent, Runtime runtime) {
            super(parent, runtime);
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

        One(LexicalScope parent, Runtime runtime, String name0) {
            super(parent, runtime);
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
    }

    public static class Two extends One {
        public String name1;
        public SephObject value1;

        Two(LexicalScope parent, Runtime runtime, String name0, String name1) {
            super(parent, runtime, name0);
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
    }

    public static class Three extends Two {
        public String name2;
        public SephObject value2;

        Three(LexicalScope parent, Runtime runtime, String name0, String name1, String name2) {
            super(parent, runtime, name0, name1);
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
    }


    public static class Four extends Three {
        public String name3;
        public SephObject value3;

        Four(LexicalScope parent, Runtime runtime, String name0, String name1, String name2, String name3) {
            super(parent, runtime, name0, name1, name2);
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
    }

    public static class Five extends Four {
        public String name4;
        public SephObject value4;

        Five(LexicalScope parent, Runtime runtime, String name0, String name1, String name2, String name3, String name4) {
            super(parent, runtime, name0, name1, name2, name3);
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
    }


    public static class Six extends Five {
        public String name5;
        public SephObject value5;

        Six(LexicalScope parent, Runtime runtime, String name0, String name1, String name2, String name3, String name4, String name5) {
            super(parent, runtime, name0, name1, name2, name3, name4);
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
    }

    public final LexicalScope parent;
    public final Runtime runtime;
    public volatile int version = 0;

    private LexicalScope(LexicalScope parent, Runtime runtime) {
        this.parent = parent;
        this.runtime = runtime;
    }

    public static LexicalScope create(LexicalScope parent, Runtime runtime, String[] names) {
        switch(names.length) {
        case 0:
            return new Empty(parent, runtime);
        case 1:
            return new One(parent, runtime, names[0]);
        case 2:
            return new Two(parent, runtime, names[0], names[1]);
        case 3:
            return new Three(parent, runtime, names[0], names[1], names[2]);
        case 4:
            return new Four(parent, runtime, names[0], names[1], names[2], names[3]);
        case 5:
            return new Five(parent, runtime, names[0], names[1], names[2], names[3], names[4]);
        case 6:
            return new Six(parent, runtime, names[0], names[1], names[2], names[3], names[4], names[5]);
        default:
            return new Many(parent, runtime, names);
        }
    }

    public LexicalScope newScopeWith(String[] names) {
        return create(this, runtime, names);
    }

    public abstract SephObject get(int depth, int index);
    public abstract void assign(int depth, int index, SephObject value);
}// LexicalScope
