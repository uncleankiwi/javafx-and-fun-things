package misc.eda;

import util.Maths;

import java.util.Set;

/*
Given h number of holes in a centrifuge, return if it is possible to place t
tubes in it such that the centrifuge is balanced.

Assume the centrifuge can never be balanced if there is only 1 tube or 1 hole.
 */
public class CentrifugeBalancer {
	public static void main(String[] args) {
		System.out.println("Holes\tTubes");
		System.out.println("=============");
		test(6, 4, true);
		test(2,1, false);
		test(3,3, true);
		test(5,1, false);
		test(12,7, true);
		test(1,1, false);
		test(21,18, true);
		test(1,0, false);
		test(4,2, true);
		test(5,3, false);
		test(21,13, false);
		test(3,3, true);
		test(50,1, false);
		test(8,6, true);
		test(9,5, false);
		test(2,1, false);
		test(59,59, true);
		test(11,4, false);
		test(21,10, false);
		test(24, 13, true);

	}

	private static void test(int h, int t, boolean expectedOutput) {
		boolean actualOutput = tryPlace(h, t);
		String str = h + "\t" + t + "\t->\t" + actualOutput;
		if (actualOutput != expectedOutput) str += " (TEST FAILED)";
		System.out.println(str);
	}

	public static boolean tryPlace(int h, int t) {
		//weed out obvious answers
		if (h <= 1) return false;
		else if (t <= 1) return false;
		else if (t > h) return false;
		else if (h == t) return true;
		else {
			Set<Long> primeFactors = Maths.primeFactorsOf(h);
			//if t is equal to one of the prime factors of h, then h - t must also have t as a prime factor
			//i.e. this case is balanced.
			//	t = f, where f is a prime factor of h
			//	h = xt = xf, where x is some other multiplier
			// 	So: h - t = xf - f = f(x - 1), which has f as a prime factor.
			if (primeFactors.contains((long) t)) return true;

			//Is it possible to create t by adding together some combination (with repetition) of prime factors of h?
			//If not, this cannot be balanced.
			if (!canCreateBySumming(t, primeFactors)) return false;

			//Is it also possible to create h - t with the same method?
			//If it is, then this can be balanced.
			return canCreateBySumming(h - t, primeFactors);
		}
	}

	//returns if it is possible to create x by summing up some combination of numbers in the factors set
	private static boolean canCreateBySumming(int x, Set<Long> factors) {
		return false;
	}
}
