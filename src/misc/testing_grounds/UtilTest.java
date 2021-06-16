package misc.testing_grounds;

import util.NumberToString;
import util.Padder;

public class UtilTest {
	public static void main(String[] args) {
		System.out.println(0.00004);
		System.out.println(400000000d);
		System.out.println(12341234d);
	}

	private static void testDoubleToString() {
		double d1 = 0.00004;
		double d2 = 400000000d;
		double d3 = 12341234d;
		System.out.println(d1 + " -> " + NumberToString.doubleToString(d1));
		System.out.println(d2 + " -> " + NumberToString.doubleToString(d2));
		System.out.println(d3 + " -> " + NumberToString.doubleToString(d3));
	}

	@SuppressWarnings("unused")
	private static void testUnpadder() {
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
