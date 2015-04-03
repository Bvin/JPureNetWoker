package cn.bvin.library.net;

import java.util.HashMap;
import java.util.Map;

public class MapParam {

	private Map<String, Object> params;

	public MapParam() {
		this.params = new HashMap<String, Object>();
	}

	public MapParam(Map<String, Object> params) {
		this();
		this.params.putAll(params);
	}
	
	public void put(String key, Object value) {
		this.params.put(key, value);
	}
	
	public Object  get(Object key) {
		return this.params.get(key);
	}
	
	public Map<String, Object> get() {
		return this.params;
	}
	
	public boolean contains(String key){
		return this.params.containsKey(key);
	}
	
	public void clear() {
		this.params.clear();
	}
}
