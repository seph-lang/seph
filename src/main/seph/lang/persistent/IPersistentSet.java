package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IPersistentSet extends IPersistentCollection, Counted {
	public IPersistentSet disjoin(Object key) throws Exception;
	public boolean contains(Object key);
	public Object get(Object key);
}
