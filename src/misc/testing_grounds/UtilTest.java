package misc.testing_grounds;

import util.Fraction;

public class UtilTest {
	public static void main(String[] args) {
		Fraction f = new Fraction(2, 64);
		System.out.println(f);
		f.simplify();;
		System.out.println(f);

		Fraction g = new Fraction(3, 9);
		System.out.println(g);
		g.simplify();
		System.out.println(g);
	}
}
