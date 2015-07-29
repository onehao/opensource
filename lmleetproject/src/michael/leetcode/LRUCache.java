package michael.leetcode;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Design and implement a data structure for Least Recently Used (LRU) cache. It
 * should support the following operations: get and set.
 * 
 * get(key) - Get the value (will always be positive) of the key if the key
 * exists in the cache, otherwise return -1. set(key, value) - Set or insert the
 * value if the key is not already present. When the cache reached its capacity,
 * it should invalidate the least recently used item before inserting a new
 * item.
 * 
 * @author Michael Wan.
 *
 */
public class LRUCache{

	// define a LinkedhashMap which store the key stored and used sequence.
	LinkedHashMap<Integer, Integer> map;

	private int capacity;

	public LRUCache(int capacity) {
		this.capacity = capacity;

		map = new LinkedHashMap<Integer, Integer>(capacity, 0.75F, true);
	}

	public int get(int key) {
		if (!map.containsKey(key)) {
			return -1;
		}
		return map.get(key);
	}

	public void set(int key, int value) {
		// When add a new key, remove the les
		if (map.size() == this.capacity && !map.containsKey(key)) {
			removeLeast();
		}
		map.put(key, value);
		// get to make the added key last used.
		map.get(key);
	}

	// remove the least recently used key controlled by the map.
	private void removeLeast() {
		Iterator<Integer> iterator = map.keySet().iterator();
		int last = iterator.next();
		map.remove(last);
	}

	public static void main(String[] args) {
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(
				100, 0.75F, true);
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);
		map.put(5, 5);
		map.put(6, 6);
		map.put(7, 7);
		map.put(8, 8);
		map.put(9, 9);

		// map.containsKey(4);
		// map.get(5);
		//
		// for (int key : map.keySet()) {
		// System.out.println(key);
		// }
		//
		// LRUCache cache = new LRUCache(1);
		//
		// //case1
		// cache.set(2, 1);
		// System.out.println(cache.get(2));
		// cache.set(3, 2);
		// System.out.println(cache.get(2));
		// System.out.println(cache.get(3));
		//
		// LRUCache cache2 = new LRUCache(2);
		// //case2
		// System.out.println(cache2.get(2));
		// cache2.set(2, 6);
		// System.out.println(cache2.get(1));
		// cache2.set(1, 5);
		// cache2.set(1, 2);
		// System.out.println(cache2.get(1));
		// System.out.println(cache2.get(2));

		// case3
		LRUCache cache3 = new LRUCache(2);
		cache3.set(2, 1);
		cache3.set(2, 2);
		System.out.println(cache3.get(2));
		cache3.set(1, 1);
		cache3.set(4, 1);
		System.out.println(cache3.get(2));

		// case4
		LRUCache2<Integer, Integer> cache4 = new LRUCache2<Integer, Integer>(2);
		cache4.set(2, 1);
		cache4.set(2, 2);
		System.out.println(cache4.get(2));
		cache4.set(1, 1);
		cache4.set(4, 1);
		System.out.println(cache4.get(2));
	}

}

class LRUCache2<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -2192443559249456884L;

	private int capacity;

	public LRUCache2(int capacity) {
		super(capacity, 0.075F, true);
		this.capacity = capacity;

		// map = new LinkedHashMap<Integer, Integer>(capacity, 0.75F, true);
	}
	
	public void set(K key, V value){
		super.put(key, value);
	}
	
	public V get(Object key){
		V result = super.get(key);
		return (V) (result == null ? -1 : result);
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return super.size() > this.capacity;
	}
}
