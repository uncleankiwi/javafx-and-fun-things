package misc.testing_grounds;

import util.NumberToString;
import util.Padder;

public class UtilTest {
	public static void main(String[] args) {
		testDoubleToString();
	}

	@SuppressWarnings("unused")
	private static void testDoubleToString() {
		double d1 = 0.00004;		//adds a 0 to the right, but acceptable
		double d2 = 400000000d;
		double d3 = 12341234d;
		double d4 = 0.000041;
		double d5 = 43210000d;
		double d6 = 43.214321;
		double d7 = 12341234.5;
		System.out.println(d1 + " -> " + NumberToString.doubleToString(d1));
		System.out.println(d2 + " -> " + NumberToString.doubleToString(d2));
		System.out.println(d3 + " -> " + NumberToString.doubleToString(d3));
		System.out.println(d4 + " -> " + NumberToString.doubleToString(d4));
		System.out.println(d5 + " -> " + NumberToString.doubleToString(d5));
		System.out.println(d6 + " -> " + NumberToString.doubleToString(d6));
		System.out.println(d7 + " -> " + NumberToString.doubleToString(d7));
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
