package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public abstract class ATransientSet implements ITransientSet {
	ITransientMap impl;

	ATransientSet(ITransientMap impl) {
		this.impl = impl;
	}
	
	public int count() {
		return impl.count();
	}

	public ITransientSet conj(Object val) {
		ITransientMap m = impl.associate(val, val);
		if (m != impl) this.impl = m;
		return this;
	}

	public boolean contains(Object key) {
		return this != impl.valueAt(key, this);
	}

	public ITransientSet disjoin(Object key) throws Exception {
		ITransientMap m = impl.without(key);
		if (m != impl) this.impl = m;
		return this;
	}

	public Object get(Object key) {
		return impl.valueAt(key);
	}

	public Object invoke(Object key, Object notFound) throws Exception {
		return impl.valueAt(key, notFound);
	}

	public Object invoke(Object key) throws Exception {
		return impl.valueAt(key);	
	}
}
