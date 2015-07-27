package test;

import java.util.Arrays;

public class CheckString {
	public static void main(String[] args) {
		String hello = "Hello", lo = "lo";
		String a = new String("hello");
		String b = new String("hello");
		String c = "hello";
		String d = "hello";
		System.out.print((hello == "Hello") + " ");
		System.out.print((Other.hello == hello) + " ");
		System.out.print((test.other.Other.hello == hello) + " ");
		System.out.print((hello == ("Hel"+"lo")) + " ");
		System.out.print((hello == ("Hel"+lo)) + " ");
		System.out.println(hello == ("Hel"+lo).intern());
		
		System.out.println("--------------");
		System.out.println(a == b);
		System.out.println(c == d);
		System.out.println(a == c);
		System.out.println(a.intern() == c);
		System.out.println(a.intern() == b.intern());
		System.out.println(hello == String.format("Hel%s", lo));
		
		System.out.println(String.format("%s %s! this is number: %d", "Hello", "World", 10));
		}
}
class Other { static String hello = "Hello"; }
