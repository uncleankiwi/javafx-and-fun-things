package misc.eda;

import java.util.*;

/*
Maximize the number of times the strings "ghost" and "osteo" can be removed from an input string.

Simplifying the problem, suppose we search the string for 'ab' and 'bc'.
Questions:
1. Is it always better to remove a larger number of string up front? A: no.
	Consider 'aaa b abab c bbb'.
	Removing the longer 'abab' -> 'aaa b c bbb' -> 'aaa bbb' (3 moves)
	Removing the shorter 'bc' -> 'aaa b aba bbb' -> 'aaa bbb' -> ... (6 moves)

2. Are there any situations in which only part of a contiguous repeating segment should be removed?
	A: probably not, unless more strings not in the form of 'ab' and 'bc' are added.

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

	static int differentPathsCounter = 0;	//the number of different paths

	public static void main(String[] args) {
		test("ghosteo");					//1
		test("ghostmosteo");				//2
		test("ghteo");					//0
		test("ghghostosteoeoost");		//3
		test("");						//0
		test("ghghostost");				//2
		test("ostosteoeo");				//2
		test("ghghostosteoeoostost");	//4
		test("ostostghghostosteoeo");	//4
		test("ghghostosteoeoostost");	//4
		test("ostghosteo");				//2

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
		for (int i = 0; i < 8; i++) {
			sb.append("gh");
		}
		for (int i = 0; i < 10; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 10; i++) {
			sb.append("eo");
		}
		for (int i = 0; i < 8; i++) {
			sb.append("ost");
		}
		test(sb.toString());			//18. slow when multiplied by factor of 10.

		sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 10; i++) {
			sb.append("gh");
		}
		for (int i = 0; i < 10; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 8; i++) {
			sb.append("eo");
		}
		test(sb.toString());			//18. too slow when multiplied by factor of 10.
//
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
		Path path = pickBestRemove(s);
		if (s.length() > 20) {
			System.out.println(s.substring(0, 20) + "... (" + path.getMoves() + " moves)");
		}
		else {
			StringBuilder sb = new StringBuilder();
			for (String r : path.getStrings()) {
				if (sb.length() > 0) {
					sb.append(" -> ");
				}
				sb.append(r);
			}
			sb.append(" (")
				.append(path.getMoves())
				.append(" moves)");
			System.out.println(sb);
		}
	}

	//wrapper for recursive remove()
	public static Path pickBestRemove(String s) {
		Set<Path> results = remove(s);

		System.out.println("Highest number of paths:" + differentPathsCounter + " Result size:" + results.size());
		differentPathsCounter = 0;

		Path bestPath = null;
		int greatestMoves = 0;
		for (Path path : results) {
			if (path.getMoves() > greatestMoves || bestPath == null) {
				bestPath = path;
				greatestMoves = path.getMoves();
			}
		}
		return bestPath;
	}

	private static Set<Path> remove(String s) {
		//Given a string, if there are no removals, return.
		//For every possible removal (contiguous sequences are counted as 1),
		//do the removal(s), add to count, then call recursively on remainder.

		//a b c
		//		bc
		//			c
		//			b
		//		ac
		//		ab
		Set<Path> pathsToAdd = new HashSet<>();
		int differentRemovals = 0;
		for (String searchWord : WORDS) {
			int i = 0;
			while (i < s.length()) {
				HeadSearchResult hsr = getOccurrencesAtHead(s.substring(i), searchWord);
				if (hsr.getHits() > 0) {
					differentRemovals++;
					String remainder = s.substring(0, i) + s.substring(i + hsr.getResult().length());
					Set<Path> localRemoval = remove(remainder);

					for (Path path : localRemoval) {
						path.add(s, hsr.getHits());
					}
					i += hsr.getResult().length();

					//Checking pathsToAdd to see if a Path already exists in it,
					//and (redundantly) checking to see if the new Path has a greater
					//number of moves, and replacing it if so.
					for (Path path : localRemoval) {
						Optional<Path> existingPathOptional = pathsToAdd.stream().filter(x -> x.equals(path)).findFirst();
						if (existingPathOptional.isPresent()) {
							Path existingPath = existingPathOptional.get();
							if (path.getMoves() > existingPath.getMoves()) {
								pathsToAdd.add(path);
							}
						}
						else {
							pathsToAdd.add(path);
						}
					}
//					pathsToAdd.addAll(localRemoval);

				}
				else {
					i++;
				}
			}
		}

		if (differentRemovals == 0) {
			pathsToAdd.add(new Path(s, 0));
		}

		if (pathsToAdd.size() > differentPathsCounter) {
			differentPathsCounter = pathsToAdd.size();
		}


		return pathsToAdd;

	}

	//Tries to find 1 or more contiguous searchString sequences from string s.
	//Returns: number of sequences found.
	private static HeadSearchResult getOccurrencesAtHead(String s, String searchString) {
		int found = 0;
		int lookup = 0;
		while (lookup + searchString.length() <= s.length()) {
			if (s.startsWith(searchString, lookup)) {
				lookup += searchString.length();
				found++;
			}
			else {
				break;
			}
		}
		if (found > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= found; i++) {
				sb.append(searchString);
			}
			return new HeadSearchResult(found, sb.toString());
		}
		else return new HeadSearchResult(found, null);
	}

	//Stores the result from getOccurrencesAtHead.
	//Just stores the string result and number of hits.
	private static class HeadSearchResult {
		private final int hits;
		private final String result;

		public HeadSearchResult(int hits, String result) {
			this.hits = hits;
			this.result = result;
		}

		public int getHits() {
			return hits;
		}

		public String getResult() {
			return result;
		}
	}

	//The path that the algorithm has taken so far. Holds a list of strings, i.e.
	//the steps taken, and the number of steps taken.
	//The number of steps taken is needed because more than one removal may occur
	//during a call of remove().
	//The equals() and therefore hashCode() depend on the current state of the
	//string. (i.e. the last item in the list)
	//For this problem, we *could* assume that given the same string state,
	//the number of removals is always the same, but we're not. Thus when we check
	//a Set<Path> and find that a Path is already in it, we will also check to see
	//if the new Path's number of moves is greater than the current's.
	private static class Path {
		private final LinkedList<String> strings;
		private int moves;

		public Path(String string, int moves) {
			this.strings = new LinkedList<>();
			this.strings.add(string);
			this.moves = moves;
		}

		//adds a string to the FRONT of the list
		public void add(String string, int moves) {
			strings.addFirst(string);
			this.moves += moves;
		}

		public int getMoves() {
			return moves;
		}

		public List<String> getStrings() {
			return strings;
		}

		//unused.
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Path path = (Path) o;
			return strings.getLast().equals(path.strings.getLast());
		}

		@Override
		public int hashCode() {
			return Objects.hash(strings.getLast());
		}

		@Override
		public String toString() {
			return "Path{" +
				"strings=" + strings +
				", moves=" + moves +
				'}';
		}
	}
}
