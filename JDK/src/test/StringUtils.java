package test;

public class StringUtils {
	public static String reverseString(String input){
		if(null == input || input.trim().length() <= 0 )
			return input;
		//input = String.format("%s.", input);
		String[] values = input.split(" ", -2);
		
		int length = values.length;
		StringBuilder builder = new StringBuilder(input.length());
		for(int i = length - 1 ; i > 0; i--){
			builder.append(String.format("%s ", values[i]).intern());
		}
		builder.append(values[0]);//String.format("%s%s", values[0], input.startsWith(" ") ? " ": ""));
		return builder.toString();
	}
	
	public static String reverseString2(String input){
		String result = "";
		String[] str = input.split(" ");
		for(int i = 0; i < str.length; i++){
			result += str[str.length - 1 - i] + " ";
		}
		return result;
	}
	
	private static String toString(String[] values){
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for(int i = 0; i < values.length-1; i++){
			builder.append(String.format("%s,", values[i]));
		}
		builder.append(values[values.length-1]);
		builder.append("}");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		String split = "boo:and:foobooboo";
		System.out.println(toString(split.split(":", 2)));
		System.out.println(toString(split.split(":", 5)));
		System.out.println(toString(split.split(":", -2)));
		
		System.out.println(toString(split.split("o", 6)));
		System.out.println(toString(split.split("o", -2)));
		System.out.println(toString(split.split("o", 0)));
		
		String input = "I am a student.";
		
		String result1 = reverseString(input);
		System.out.println(String.format("M: %s%s%s", "<BGN>", result1, "<EOF>"));
		
		String result2 = reverseString2(input);
		System.out.println(String.format("L: %s%s%s", "<BGN>", result2, "<EOF>"));
		
		System.out.println("-------------");
		input = " I am a student. ";
		result1 = reverseString(input);
		System.out.println(String.format("M: %s%s%s", "<BGN>", result1, "<EOF>"));
		
		result2 = reverseString2(input);
		System.out.println(String.format("L: %s%s%s", "<BGN>", result2, "<EOF>"));
		
		System.out.println("-------------");
		input = "      I     ";
		result1 = reverseString(input);
		System.out.println(String.format("M: %s%s%s", "<BGN>", result1, "<EOF>"));
		
		result2 = reverseString2(input);
		System.out.println(String.format("L: %s%s%s", "<BGN>", result2, "<EOF>"));
	}
}
