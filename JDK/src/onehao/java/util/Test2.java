package onehao.java.util;

public class Test2 {
	
	public static void main(String[] args) {
		
		String[] stirngs={"sdf","sdfw/sdfw/dsfd"};
		boolean result = stirngs[1].contains("/");
		System.out.println(result);
		result = stirngs[1].toString().contains("/");
		System.out.println(result);
	}

}
