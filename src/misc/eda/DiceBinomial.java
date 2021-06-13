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
import util.Fraction;
import util.Padder;

import java.util.ArrayList;
import java.util.List;

public class DiceBinomial {
	static final int OUTCOME_WIDTH = 2;	//graph axis, measured in tabs
	static final int GRAPH_WIDTH = 30;		//graph's width, measured in spaces
	static final int TAB_SIZE = 4;			//number of spaces to a tabs

	public static void main(String[] args) {
		draw(1, 11);
		draw(2, 11);
		draw(3, 11);
		draw(4, 11);
	}

	//draw distribution of results r when rolling d 6-sided dice
	public static void draw(int d, int desiredOutcome) {
		List<Integer> frequencies = new ArrayList<>();
		List<Integer> outcomes = new ArrayList<>();
		int highestFrequency = 0;
		int desiredFrequency = 0;

		for (int r = d; r <= d * 6L; r++) {
			outcomes.add(r);

			int kmax = (r - d) / 6;
			int frequency = 0;	//the number of results of out 6^d that are r
			for (long k = 0; k <= kmax; k++) {
				frequency += Math.pow(-1, k) * Combination.get(d, k) * Combination.get(r - 6 * k - 1, d - 1);
			}
			frequencies.add(frequency);

			if (highestFrequency < frequency) highestFrequency = frequency;
			if (r == desiredOutcome) desiredFrequency = frequency;
		}

		//scaling every frequency such that max width of a bar is 30
		//but no matter how low a frequency is, it never gets scaled to 0
		//unless the frequency was already 0.
		List<Integer> scaledFrequencies = new ArrayList<>();
		double scaleMultiplier = 1;
		if (highestFrequency > GRAPH_WIDTH) {
			scaleMultiplier = (double) GRAPH_WIDTH / highestFrequency;
		}
		for (long frequency : frequencies) {
			if (frequency == 0) scaledFrequencies.add(0);
			else {
				int scaledFrequency = Math.max((int) (frequency * scaleMultiplier), 1);
				scaledFrequencies.add(scaledFrequency);
			}

		}

		//printing
		//f		*****	<--
		System.out.println("Outcomes of rolling " + d + " 6-sided dice:");
		System.out.println(Padder.padRightTabs("Outcome", OUTCOME_WIDTH) + "Frequency");
		for (int i = 0; i < outcomes.size(); i++) {
			String line =
				Padder.padRightTabs(String.valueOf(outcomes.get(i)), OUTCOME_WIDTH) +
				Padder.padRightTabs(
					Padder.bar('*', scaledFrequencies.get(i)), GRAPH_WIDTH / TAB_SIZE + 1);

			if (outcomes.get(i) == desiredOutcome) {
				line += "<--";
			}
			System.out.println(line);
		}
		Fraction desiredOutcomeProbability = new Fraction(desiredFrequency, (long) Math.pow(d, 6));
		desiredOutcomeProbability.simplify();
		System.out.println("Probability of desired outcome " + desiredOutcome + ": " + desiredOutcomeProbability + "\n");
	}
}
