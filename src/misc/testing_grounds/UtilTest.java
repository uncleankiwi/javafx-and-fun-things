package misc.testing_grounds;

import util.Padder;

public class UtilTest {
	public static void main(String[] args) {
		String s1 = "...34";
		String s2 = ".34";
		String s3 = "..sdf..";
		String s4 = "sdfsdf";
		String s5 = "...";
		String s6 = "";
		System.out.println(Padder.unpadLeft(s1, '.'));
		System.out.println(Padder.unpadLeft(s2, '.'));
		System.out.println(Padder.unpadLeft(s3, '.'));
		System.out.println(Padder.unpadLeft(s4, '.'));
		System.out.println(Padder.unpadLeft(s5, '.'));
		System.out.println(Padder.unpadLeft(s6, '.'));
	}
}
