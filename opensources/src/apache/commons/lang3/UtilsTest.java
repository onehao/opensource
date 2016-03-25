package apache.commons.lang3;

import org.apache.commons.lang3.RandomStringUtils;

public class UtilsTest {
	public static void main(String[] args) {
		String randomString = RandomStringUtils.random(100);
		System.out.println(randomString);
		
		System.out.println(RandomStringUtils.randomAscii(100));
		
		System.out.println(RandomStringUtils.randomAlphabetic(100));
		
		System.out.println(RandomStringUtils.randomAlphanumeric(100));
		
		System.out.println(RandomStringUtils.randomNumeric(100));
	}
}
