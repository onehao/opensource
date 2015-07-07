package test;

import java.io.UnsupportedEncodingException;

public class CheckEncoding {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String UTFString = "\u4e2d\u56fd";
		String NativeString = "中国";
		
		System.out.println(UTFString.equals(NativeString));
		
		System.out.println(UTFString.length());
		System.out.println(NativeString.length());
		
		System.out.println(new String(UTFString.getBytes("GBK")));
		System.out.println(new String(NativeString.getBytes("UTF-8")));
		
		System.out.println(new String(UTFString.getBytes()).equals(new String(NativeString.getBytes())));
	}

}
