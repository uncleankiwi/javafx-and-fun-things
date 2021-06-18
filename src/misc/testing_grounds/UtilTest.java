package misc.testing_grounds;

import util.Closeness;
import util.NumberToString;
import util.Padder;

public class UtilTest {
	public static void main(String[] args) {
		testCloseness();
	}

	@SuppressWarnings("unused")
	private static void testCloseness() {
		/*
		54100, 54103 -> 47
		54100, 54097 -> -47
		30, 100 -> 13
		3, 100 -> 0
		100, 197 -> 11
		100, 97 -> -27
		540333, 535222 -> -25
		 */
		System.out.println("Testing with expected results:");
		System.out.println("54100 vs 54103 -> " + Closeness.get(54100, 54103));
		System.out.println("54100 vs 54097 -> " + Closeness.get(54100, 54097));
		System.out.println("30 vs 100 -> " + Closeness.get(30, 100));
		System.out.println("3 vs 100 -> " + Closeness.get(3, 100));
		System.out.println("100 vs 197 -> " + Closeness.get(100, 197));
		System.out.println("100 vs 97 -> " + Closeness.get(100, 97));
		System.out.println("540333 vs 535222 -> " + Closeness.get(540333, 535222));

		System.out.println("\nTesting with unknown cases:");
		System.out.println("3 vs 5 -> " + Closeness.get(3, 5));
		System.out.println("35678 vs 0.00004 -> " + Closeness.get(35678, 0.00004));
		System.out.println(Math.PI + " vs 8 -> " + Closeness.get(Math.PI, 8));
		System.out.println("700000000 vs 0 -> " + Closeness.get(700000000, 0));
		System.out.println("0.005 vs 0.007 -> " + Closeness.get(0.005, 0.007));
		System.out.println("0 vs 1 -> " + Closeness.get(0, 1));
		System.out.println("10 vs 1 -> " + Closeness.get(10, 1));
		System.out.println("45723.58 vs 45753.535236 -> " + Closeness.get(45723.58, 45751.535236));

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
		double d8 = 0d;
		System.out.println(d1 + " -> " + NumberToString.doubleToString(d1));
		System.out.println(d2 + " -> " + NumberToString.doubleToString(d2));
		System.out.println(d3 + " -> " + NumberToString.doubleToString(d3));
		System.out.println(d4 + " -> " + NumberToString.doubleToString(d4));
		System.out.println(d5 + " -> " + NumberToString.doubleToString(d5));
		System.out.println(d6 + " -> " + NumberToString.doubleToString(d6));
		System.out.println(d7 + " -> " + NumberToString.doubleToString(d7));
		System.out.println(d8 + " -> " + NumberToString.doubleToString(d8));
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
