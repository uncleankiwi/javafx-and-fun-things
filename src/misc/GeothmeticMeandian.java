package misc;

import java.util.List;

//xkcd.com/2435/
//F(x1, x2,..., xn) = (arithmeticMean, geometricMean, median)
//GMMD(x1, x2, ... , xn) = F(F(...F(x1, x2, ..., xn))) until x1, x2, x3 converges


public class GeothmeticMeandian {
	public static void main(String[] args) {

	}

	public static double

	public static List<Double> GMMD(List<Double> list) {
		List<Double> output =
	}

	public static double arithmeticMean(List<Double> list) {
		double total = 0;
		for (double x : list) {
			total += x;
		}
		return total / list.size();
	}

	public static double geometricMean(List<Double> list) {
		double total = 0;
		for (double x : list) {
			total *= x;
		}
		return Math.pow(total, 1.0d / list.size());
	}
}
