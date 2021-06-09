package misc.eda;
/*
Given a number of 6-sided dice d and a desired outcome o,
	1. 	a. draw the binomial distribution of results, scaled to <= 30 characters
		b. with each row labelled with the numerical result,
		c. with an arrow indicating the row with the desired outcome.
	2. state the probability of outcome o as a fraction.

The probability of getting desired result r when rolling a number of 6-sided dice d is
	sum(0 to kmax) of (-1)^k * Combination.get(d, 6) * Combination.get(r - 6k - 1, d - 1)
		where kmax = floor((r-d)/6)

	out of a total of 6^d total possible outcomes.
 */

import util.Combination;

public class DiceBinomial {
	public static void main(String[] args) {
		draw(2, 11);
		draw(3, 11);
	}

	//draw distribution of results r when rolling d 6-sided dice
	public static void draw(int d, int desiredOutcome) {
		for (int r = d; r <= d * 6; r++) {
			int kmax = (r - d) / 6;
			long count = 0;	//the number of results of out 6^d that are r
			for (long k = 0; k <= kmax; k++) {
				count += Math.pow(-1, k) * Combination.get(d, k) * Combination.get(r - 6 * k - 1, d - 1);
			}
			String output = "result " + r + " -> " + count + "/" + Math.pow(6, d);
			if (r == desiredOutcome) output += " <-- here";
			System.out.println(output);


		}
	}
}
