package misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//xkcd.com/2435/
//F(x1, x2,..., xn) = (arithmeticMean, geometricMean, median)
//GMMD(x1, x2, ... , xn) = F(F(...F(x1, x2, ..., xn))) until x1, x2, x3 converges
//GMMD(1, 1, 2, 3, 5) = 2.089


public class GeothmeticMeandian {
	public static void main(String[] args) {
		GMMDrecursive(Arrays.asList(1d, 1d, 2d, 3d, 5d), 3);

		//doubles aren't always a good idea:
//		System.out.println(0.04d - 0.03d);
		//but things shouldn't be too bad in this case
	}

	public static void GMMDrecursive(List<Double> list, int precision) {
		Collections.sort(list);
		System.out.println("Input: " + list);

		while(!hasConverged(list, precision)) {
			list = GMMD(list);
			System.out.println("Step: " + list);
		}

		System.out.println("Value: " + list.get(0));
	}

	private static List<Double> GMMD(List<Double> list) {
		List<Double> output = new ArrayList<>();

		output.add(arithmeticMean(list));
		output.add(geometricMean(list));
		output.add(median(list));
		Collections.sort(output);

		return output;
	}

	private static boolean hasConverged(List<Double> list, int precision) {
		if (list.size() <= 1) return true;
		else {
			for (int i = 1; i < list.size(); i++) {
				if (!precisionEquals(list.get(0), list.get(i), precision)) return false;
			}
		}
		return true;
	}

	private static double arithmeticMean(List<Double> list) {
		double total = 0;
		for (double x : list) {
			total += x;
		}
		return total / list.size();
	}

	private static boolean precisionEquals(double x1, double x2, int precision) {
		return (round(x1, precision) == round(x2, precision));
	}

	private static double round(double x, int precision) {
		if (precision < 0) throw new IllegalArgumentException();

		BigDecimal bigDec = new BigDecimal(x);
		bigDec = bigDec.setScale(precision, RoundingMode.HALF_UP);
		return bigDec.doubleValue();
	}

	private static double geometricMean(List<Double> list) {
		double total = 0;
		for (double x : list) {
			total *= x;
		}

		System.out.println(Math.pow(total, 1.0d / list.size()));	//TODO
		return Math.pow(total, 1.0d / list.size());
	}

	private static double median(List<Double> list) {
		return list.get(list.size() / 2 - 1);
	}
}
