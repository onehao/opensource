package onehao.java.util;

import java.util.HashMap;

public class MHashMap {
	
	public static void main(String[] args) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("a", "test");
		map.put("b", "test");
		map.put("c", "test");
		map.put("d", "test");
		map.put("e", "test");
		map.put("C9", "test");
		
		//f and Aw will have the same hashcode as C9
		map.put("f", "test1");
		map.put("G", "test");
		map.put("Aw", "test");
		map.put("H", "test");
		map.put("i", "test");
		map.put("j", "test");
		
		map.get("f");
		map.get("C9");
		map.get("Aw");
		System.out.println(map);
		
		
	}

}
