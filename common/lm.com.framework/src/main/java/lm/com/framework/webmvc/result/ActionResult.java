package lm.com.framework.webmvc.result;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author mrluo735
 *
 */
public class ActionResult implements Map<String, Object>, Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8191797595000946827L;

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	private final Map<String, Object> map;

	public ActionResult() {
		this(DEFAULT_INITIAL_CAPACITY, false);
	}

	public ActionResult(Map<String, Object> map) {
		this.map = map;
	}

	public ActionResult(boolean ordered) {
		this(DEFAULT_INITIAL_CAPACITY, ordered);
	}

	public ActionResult(int initialCapacity) {
		this(initialCapacity, false);
	}

	public ActionResult(int initialCapacity, boolean ordered) {
		if (ordered) {
			map = new LinkedHashMap<String, Object>(initialCapacity);
		} else {
			map = new HashMap<String, Object>(initialCapacity);
		}
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	public void clear() {
		map.clear();
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public Object clone() {
		return new ActionResult(new HashMap<String, Object>(map));
	}

	public boolean equals(Object obj) {
		return this.map.equals(obj);
	}

	public int hashCode() {
		return this.map.hashCode();
	}
}
