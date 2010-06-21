package seph.lang.persistent;

import java.util.Map;

import seph.lang.persistent.PersistentHashMap.INode;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
abstract class ATransientMap implements ITransientMap {
	abstract void ensureEditable();
	abstract ITransientMap doAssoc(Object key, Object val);
	abstract ITransientMap doWithout(Object key);
	abstract Object doValAt(Object key, Object notFound);
	abstract int doCount();
	abstract PersistentMap doPersistent();

	public ITransientMap conj(Object o) {
		ensureEditable();
		if(o instanceof Map.Entry) {
			Map.Entry e = (Map.Entry) o;
			return associate(e.getKey(), e.getValue());
        } else if(o instanceof IPersistentVector) {
			IPersistentVector v = (IPersistentVector) o;
			if(v.count() != 2)
				throw new IllegalArgumentException("Vector arg to map conj must be a pair");
			return associate(v.at(0), v.at(1));
        }
		
		ITransientMap ret = this;
		for(ISeq es = RT.seq(o); es != null; es = es.next()) {
			Map.Entry e = (Map.Entry) es.first();
			ret = ret.associate(e.getKey(), e.getValue());
        }
		return ret;
	}

	public final Object valueAt(Object key) {
		return valueAt(key, null);
	}

	public final ITransientMap associate(Object key, Object val) {
		ensureEditable();
		return doAssoc(key, val);
	}

	public final ITransientMap without(Object key) {
		ensureEditable();
		return doWithout(key);
	}

	public final PersistentMap persistent() {
		ensureEditable();
		return doPersistent();
	}

	public final Object valueAt(Object key, Object notFound) {
		ensureEditable();
		return doValAt(key, notFound);
	}

	public final int count() {
		ensureEditable();
		return doCount();
	}
}
