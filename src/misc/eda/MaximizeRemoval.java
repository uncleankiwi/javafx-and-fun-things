package misc.eda;

import java.util.ArrayList;
import java.util.List;

/*
Maximize the number of times the strings "ghost" and "osteo" can be removed from an input string.

Simplifying the problem, suppose we search the string for 'ab' and 'bc'.
Questions:
1. Is it always better to remove a larger number of string up front? A: no.
	Consider 'aaa b abab c bbb'.
	Removing the longer 'abab' -> 'aaa b c bbb' -> 'aaa bbb' (3 moves)
	Removing the shorter 'bc' -> 'aaa b aba bbb' -> 'aaa bbb' -> ... (6 moves)

2. Are there any situations in which only part of a contiguous repeating segment should be removed?
	A: probably not, unless more strings can be removed.

	Consider the above example:
	... -> 'aaa b ab c bbb' -> 'aaa b a bbb' -> ... (6 moves)
	but this is the same as removing 'bc' right from the start.

	Consider 'b a bcbc':
	Removing only one -> 'b a bc'  (3 moves)
	Removing all -> 'ba' (2 moves)
	Removing 'ab' instead -> 'b cbc' (3 moves)

	Consider 'c a bcbc', but this time 'cc' can also be removed:
	Removing all 'bc' -> 'c a' (2 moves)
	Removing one 'bc' -> 'c a bc' -> 'cc' (3 moves)
	=> Removing only part of the repeated sequence can result in more overall removals
		if more words are added.
 */
public class MaximizeRemoval {
	static final String[] WORDS = new String[] {"ghost", "osteo"};

	public static void main(String[] args) {
		test("ghosteo");				//1
		test("ghostmosteo");			//2
		test("ghteo");				//0
		test("ghghostosteoeoost");	//3
		test("");					//0

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 200; i++) {
			sb.append("eo");
		}
		test(sb.toString());			//200

		sb = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			sb.append("osteo");
		}
		for (int i = 0; i < 100; i++) {
			sb.append("ghost");
		}
		for (int i = 0; i < 40; i++) {
			sb.append("osteo");
		}
		test(sb.toString());			//180

		sb = new StringBuilder();
		for (int i = 0; i < 80; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 100; i++) {
			sb.append("gh");
		}
		for (int i = 0; i < 100; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 80; i++) {
			sb.append("eo");
		}
		test(sb.toString());			//180

		sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("eo");
		}
		for (int i = 0; i < 200; i++) {
			sb.append("ost");
		}
		test(sb.toString());			//0


	}

	private static void test(String s) {
		Path path = remove(s);
		if (s.length() > 20) {
			System.out.println(s.substring(0, 20) + " (" + path.getMoves() + " moves)");
		}
		else {
			StringBuilder sb = new StringBuilder(s);
			for (String r : path.getStrings()) {
				sb.append(" -> ")
					.append(r);
			}
			sb.append(" (")
				.append(path.getMoves())
				.append(" moves)");
			System.out.println(sb);
		}
	}

	//wrapper for recursive remove()
	public static Path remove(String s) {
		List<Path> results = remove(s, new ArrayList<Path>());
		Path bestPath;
		int greatestMoves = 0;
		for (Path path : results) {
			if (path.getMoves() > greatestMoves) {
				bestPath = path;
				greatestMoves = path.getMoves();
			}
		}
		return bestPath;
	}

	private static List<Path> remove(String s, List<Path> pathList) {
		//Given a string, if there are no removals, return.
		//For every possible removal (contiguous sequences are counted as 1),
		//do the removal(s), add to count, then call recursively on remainder.



		return pathList;

	}

	//The path that the algorithm has taken so far. Holds a list of strings, i.e.
	//the steps taken, and the number of steps taken.
	//The number of steps taken is needed because more than one removal may occur
	//during a call of remove().
	private class Path {
		private List<String> strings;
		private int moves;

		public Path() {
		}

		public void add(String string, int moves) {
			strings.add(string);
			this.moves += moves;
		}

		public int getMoves() {
			return moves;
		}

		public List<String> getStrings() {
			return strings;
		}
	}
}
