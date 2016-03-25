package apache.commons.lang3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutableTriple;
/**
 * 
 * @author michael.wh
 *
 */
public class TupleTest {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("sns");
		list.add("maps");
		list.add("oss");
		
		Map<String, Integer> dic = new HashMap<String, Integer>();
		
		dic.put("c", 1);
		dic.put("u", 2);
		dic.put("d", 3);
		
		Map<Integer, String> dic2 = new HashMap<Integer, String>();
				
		dic2.put(1, "c");
		dic2.put(2, "u");
		dic2.put(3, "d");
		
		
		ImmutableTriple<List<String>, Map<String,Integer>, Map<Integer,String>> truple = ImmutableTriple.of(list, dic, dic2);
		
		System.out.println(truple);
	}
}
