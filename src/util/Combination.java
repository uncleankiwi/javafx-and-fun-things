package util;

import java.util.ArrayList;
import java.util.List;

/*
If out of n elements, k are chosen, there are
	n! / (k! * (n - k)!)
possible results.
The denominator is cancelled out first before multiplying the numerator
to minimize the chances of overflow while calculating the result.

e.g.
	get(52, 5) -> 2,598,960
	get(6, 4) -> 15

 */
public class Combination {
	public static long get(long n, long k) {
		if (k > n) return 0;

		//working out numerator and denominator factorials' components
		List<Long> numerators = new ArrayList<>();
		for (long i = Math.max(k, n - k) + 1; i <= n; i++) {
			numerators.add(i);
		}

		List<Long> denominators = new ArrayList<>();
		for (long i = 1; i <= Math.min(k, n - k); i++) {
			denominators.add(i);
		}

		//cancelling out denominator's components
		int removeIndex = -1;	//marks denominator to be removed
		while (denominators.size() > 0) {
			if (removeIndex >= 0) {
				denominators.remove(removeIndex);
				removeIndex = -1;
			}

			for (int i = 0; i < denominators.size(); i++) {
				if (removeIndex >= 0) break;

				for (int j = 0; j < numerators.size(); j++) {
					if (numerators.get(j) % denominators.get(i) == 0) {
						numerators.set(j, numerators.get(j) / denominators.get(i));
						removeIndex = i;
						break;
					}
				}
			}
		}

		//multiplying numerator components for result
		long result = 1;
		for (long x : numerators) result *= x;
		return result;
	}
}
