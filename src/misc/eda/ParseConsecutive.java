package misc.eda;

import java.util.ArrayList;
import java.util.List;

/*
	Given a string, return a list of consecutive numbers it contains.
 */
public class ParseConsecutive {
	public static void main(String[] args) {
		test("12345");
		test("567");
		test("111213");
		test("91011");
		test("17461747174817491750");
		test("827183");
		test("0");
		test("777777");
		test("");
		test("982a");
		test("gee");
	}

	private static void test(String str) {
		String output;
		try {
			output = parse(str).toString();
		}
		catch (Exception e) {
			output = e.getMessage();
		}
		System.out.println(str + " -> " + output);
	}

	public static List<Long> parse(String str) {
		if (str.length() == 0) throw new RuntimeException("String is empty");

		for (int length = 1; length < str.length() / 2; length++) {
			List<Long> results = new ArrayList<>();
			long startingNumber;
			try {
				startingNumber = Long.parseLong(str.substring(0, length));
			}
			catch (NumberFormatException e) {
				throw new RuntimeException("String cannot contain non-numerical characters");
			}
			results.add(startingNumber);
			results = parse(str.substring(length), results);
			if (results != null) return results;
		}

		throw new RuntimeException("String does not contain consecutive numbers");
	}

	private static List<Long> parse(String str, List<Long> results) {
		long next = results.get(results.size() - 1) + 1;
		String nextStr = String.valueOf(next);
		try {
			String strToCompare = str.substring(0, nextStr.length());
			if (strToCompare.equals(nextStr)) {
				//next consecutive number is at the head of the string
				results.add(next);

				//recursion or return
				if (nextStr.equals(str)) {
					return results;
				}
				else {
					return parse(str.substring(nextStr.length()), results);
				}
			}
			else {
				//consecutive number isn't at head of string
				return null;
			}
		}
		catch (Exception e) {
			//string is too short to possibly contain the next consecutive number
			return null;
		}
	}
}
