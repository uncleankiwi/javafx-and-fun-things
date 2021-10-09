package misc;

import java.util.*;

/*
Classic fizz buzz problem but without the use of modulo.
 */
public class FizzBuzzNoModulo {
	public static void main(String[] args) {
		MultipleTracker tracker = new MultipleTracker();
		tracker.add(2, "Fizz");
		tracker.add(3, "Buzz");
		tracker.add(7, "Foo");
		tracker.add(12, "Bar");
		for (int i = 0; i < 200; i++) {
			System.out.println(tracker.get(i));
		}
	}

	private static class MultipleTracker {
		private final Map<Integer, List<Multiple>> multiples;

		MultipleTracker() {
			this.multiples = new HashMap<>();
		}

		String get(int n) {
			if (multiples.containsKey(n)) {
				List<Multiple> bracket = multiples.remove(n);
				Collections.sort(bracket);
				StringBuilder stringBuilder = new StringBuilder();
				for (Multiple m : bracket) {
					stringBuilder.append(m.getWord());
					int next = m.incrementAndGetNext();
					add(next, m);
				}
				return stringBuilder.toString();
			}
			else return String.valueOf(n);
		}

		void add(int next, String word) {
			Multiple multiple = new Multiple(next, word, multiples.size());
			add(next, multiple);
		}

		private void add(int next, Multiple multiple) {
			if (multiples.containsKey(next)) {
				multiples.get(next).add(multiple);
			}
			else {
				List<Multiple> bracket = new ArrayList<>();
				bracket.add(multiple);
				multiples.put(next, bracket);
			}
		}

		private static class Multiple implements Comparable<Multiple> {
			private int next;
			private final int factor;
			private final String word;
			private final int order;

			Multiple(int factor, String word, int order) {
				this.factor = factor;
				this.word = word;
				this.order = order;
				this.next = factor;
			}

			int incrementAndGetNext() {
				this.next += this.factor;
				return this.next;
			}

			String getWord() {
				return this.word;
			}

			@Override
			public int compareTo(Multiple o) {
				return Integer.compare(this.order, o.order);
			}
		}
	}
}
