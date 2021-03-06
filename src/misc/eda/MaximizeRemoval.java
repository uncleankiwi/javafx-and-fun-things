package misc.eda;

import util.Stopwatch;

import java.util.*;

/*
Maximize the number of times the strings "ghost" and "osteo" can be removed from an input string.

Simplifying the problem, suppose we search the string for 'ab' and 'bc'.
Questions:
1. Is it always better to remove a larger number of string up front? A: no.
	Consider 'aa abab c bbb'.
	Removing the longer 'abab' -> 'aa c bbb' -> 'aa bbb' (2 moves)
	Removing the shorter 'bc' -> 'aa aba bbb' -> 'aa bb' -> ... (5 moves)

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

There are four algorithms here. The slowRemove() is recursive and branches every time it hits a possible
removal.

fastPruningRemove() isn't recursive, and always check to see if an equivalent path already exists before
adding it (i.e. unlike slowRemove, it does memoization of results to reduce duplication of work).

fastPruningRemove() also has a prune option, which instructs it to ignore all different paths except for
the top 5 performers, making it run even faster. This produces WRONG results in some cases, but it's
fast. This functionality has been kept for posterity.

biasedRemove() isn't recursive, and starts off with 2 paths (to be precise, there are the same number of paths
as the number of words), each biased for a different word. Each of these biased path will produce a biased
path when they remove the word they're biased for, and an unbiased path when they remove a word they're not
biased for. At every step, the code only retains one biased path per word (i.e. 2) plus one unbiased path.

memoRemove() is recursive and very similar to slowRemove(), except it uses memoization to avoid strings that
have already been handled by another Path. It is also able to run the slow tests. However, it still runs too
slow, taking as much time as fastPruningRemove().

Results:
slowRemove()
	fast tests						210ms
	slow tests						freezes...

fastPruningRemove()
	fast tests, without pruning		90ms
	slow tests, without pruning		1802ms

	fast tests, with pruning		30ms, WRONG RESULTS
	slow tests, with pruning		150ms, WRONG RESULTS

biasedRemove()
	fast tests						80ms
	slow tests						130ms

memoRemove()
	fast tests						150ms
	slow tests						1730ms
 */
public class MaximizeRemoval {
	private static List<String> fastTests;
	private static List<String> slowTests;

	static final String[] WORDS = new String[] {"ghost", "osteo"};

	public static void main(String[] args) {
		init();
		slowRemoveFastTests();
		fastRemoveFastTests(false);
		fastRemoveSlowTests(false);
		fastRemoveFastTests(true);
		fastRemoveSlowTests(true);
		biasedRemoveFastTests();
		biasedRemoveSlowTests();
		memoRemoveFastTests();
		memoRemoveSlowTests();
	}

	private static void init() {
		fastTests = new ArrayList<>();
		fastTests.add("ghosteo");					//1
		fastTests.add("ghostmosteo");				//2
		fastTests.add("ghteo");					//0
		fastTests.add("ghghostosteoeoost");		//3
		fastTests.add("");						//0
		fastTests.add("ghghostost");				//2
		fastTests.add("ostosteoeo");				//2
		fastTests.add("ghghostosteoeoostost");	//4
		fastTests.add("ostostghghostosteoeo");	//4
		fastTests.add("ghghostosteoeoostost");	//4
		fastTests.add("ostghosteo");				//2

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 200; i++) {
			sb.append("eo");
		}
		fastTests.add(sb.toString());			//200

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
		fastTests.add(sb.toString());			//180

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
		fastTests.add(sb.toString());			//18. slow when multiplied by factor of 10.

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
		fastTests.add(sb.toString());			//18. too slow when multiplied by factor of 10.

		sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("eo");
		}
		for (int i = 0; i < 200; i++) {
			sb.append("ost");
		}
		fastTests.add(sb.toString());			//0

		slowTests = new ArrayList<>();
		sb = new StringBuilder();
		for (int i = 0; i < 80; i++) {
			sb.append("gh");
		}
		for (int i = 0; i < 100; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 100; i++) {
			sb.append("eo");
		}
		for (int i = 0; i < 80; i++) {
			sb.append("ost");
		}
		slowTests.add(sb.toString());			//18. slow when multiplied by factor of 10.

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
		slowTests.add(sb.toString());			//18. too slow when multiplied by factor of 10.
	}

	@SuppressWarnings("unused")
	private static void slowRemoveFastTests() {
		Stopwatch sw = new Stopwatch("slowRemoveFastTests");
		fastTests.stream()
			.map(MaximizeRemoval::slowRemove)
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void fastRemoveFastTests(@SuppressWarnings("SameParameterValue") boolean prune) {
		String pruneString = (prune) ? "with prune" : "without prune";
		Stopwatch sw = new Stopwatch("fastRemoveFastTests " + pruneString);
		fastTests.stream()
			.map(string -> fastRemove(string, prune))
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void fastRemoveSlowTests(@SuppressWarnings("SameParameterValue") boolean prune) {
		String pruneString = (prune) ? "with prune" : "without prune";
		Stopwatch sw = new Stopwatch("fastRemoveSlowTests " + pruneString);
		slowTests.stream()
			.map(string -> fastRemove(string, prune))
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void biasedRemoveFastTests() {
		Stopwatch sw = new Stopwatch("biasedRemoveFastTests");
		fastTests.stream()
			.map(MaximizeRemoval::biasedRemove)
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void biasedRemoveSlowTests() {
		Stopwatch sw = new Stopwatch("biasedRemoveSlowTests");
		slowTests.stream()
			.map(MaximizeRemoval::biasedRemove)
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void memoRemoveFastTests() {
		Stopwatch sw = new Stopwatch("memoRemoveFastTests");
		fastTests.stream()
			.map(MaximizeRemoval::memoRemove)
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	@SuppressWarnings("unused")
	private static void memoRemoveSlowTests() {
		Stopwatch sw = new Stopwatch("memoRemoveSlowTests");
		slowTests.stream()
			.map(MaximizeRemoval::memoRemove)
			.map(MaximizeRemoval::pickBestRemove)
			.forEach(MaximizeRemoval::printResult);
		sw.stop();
	}

	private static void printResult(Path path) {
		if (path == null) {
			System.out.println("No best path exists.");
			return;
		}

		String s = path.getStrings().get(0);
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

	public static Path pickBestRemove(Set<Path> results) {
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

	//basically pickBestRemove() but also compares one other path out of the given set
	public static Path pickBestRemove(Set<Path> results, Path comparator) {
		if (comparator == null) {
			return pickBestRemove(results);
		}
		else {
			Set<Path> set = new HashSet<>(results);
			set.add(comparator);
			return pickBestRemove(set);
		}
	}

	//wrapper for version 4 of remove().
	private static Set<Path> memoRemove(String s) {
		return memoRemove(s, new HashSet<>());
	}

	/*
	Fourth version of remove(). Recursive. Uses memoization; a path goes down a route only
	if it hasn't been taken already.
	 */
	private static Set<Path> memoRemove(String s, Set<String> memo) {
		Set<Path> pathsToAdd = new HashSet<>();
		int differentRemovals = 0;
		for (String searchWord : WORDS) {
			int i = 0;
			while (i < s.length()) {
				HeadSearchResult hsr = getOccurrencesAtHead(s.substring(i), searchWord);
				if (hsr.getHits() > 0) {

					String remainder = s.substring(0, i) + s.substring(i + hsr.getResult().length());

					if (!memo.contains(remainder)) {
						memo.add(remainder);
						differentRemovals++;

						Set<Path> localRemoval = memoRemove(remainder, memo);

						for (Path path : localRemoval) {
							path.addFirst(s, hsr.getHits());
						}
						i += hsr.getResult().length();

						//Replace path in set if this new path has a greater number of moves.
						for (Path path : localRemoval) {
							replacePathIfGreater(path, pathsToAdd);
						}
					}
					else {
						i++;
					}
				}
				else {
					i++;
				}
			}
		}
		if (differentRemovals == 0) {
			pathsToAdd.add(new Path(s, 0));
		}
		return pathsToAdd;

	}

	/*Third version of remove(). Non-recursive.
	At the start, it creates one path per word in the remove list, then assigns the word as the path's bias.
	There are also one pendingPath per word; whenever a biased path removes the word it's biased for,
	it results in a biased path.
	When a biased path removes a word it's not biased for, or when an unbiased path removes anything,
	it results in an unbiased path.
	When the biased path cannot remove its word, it retains the bias, and all results are deemed biased.
	The best biased path for every word and best unbiased path are picked every loop, then it repeats.
	All paths get moved into the donePaths pool when they are done.
	i.e.:
		No removals -> done
		Biased path -> biased (and maybe unbiased) 	or biased (if biased is empty)
		Unbiased path -> unbiased
	 */
	private static Set<Path> biasedRemove(String s) {
		Map<String, Path> activePaths = new HashMap<>();
		Map<String, Path> pendingPaths = new HashMap<>();
		Set<Path> donePaths = new HashSet<>();

		for (String searchWord : WORDS) {
			activePaths.put(searchWord, new Path(s, 0, searchWord));
		}

		while (activePaths.size() != 0) {
			for (Iterator<Map.Entry<String, Path>> iterator = activePaths.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, Path> entry = iterator.next();
				Path currentActivePath = entry.getValue();
				BiasedRemoveResult biasedRemoveResult = biasedRemoveStep(currentActivePath);
				if (biasedRemoveResult.donePath != null) {
					replacePathIfGreater(currentActivePath, donePaths);
					iterator.remove();
				}
				else {
					//try to replace the unbiased and biased activePaths if they are better
					Path bestUnbiasedPath = pickBestRemove(biasedRemoveResult.pendingUnbiasedPaths, activePaths.get(null));
					if (bestUnbiasedPath != null) {
						pendingPaths.put(null, pickBestRemove(biasedRemoveResult.pendingUnbiasedPaths, activePaths.get(null)));
					}
					if (biasedRemoveResult.bias != null) {
						pendingPaths.put(
							biasedRemoveResult.bias,
							pickBestRemove(biasedRemoveResult.pendingBiasedPaths, activePaths.get(biasedRemoveResult.bias)));
					}
				}
			}
			activePaths = pendingPaths;
			pendingPaths = new HashMap<>();
		}

		return donePaths;
	}

	private static BiasedRemoveResult biasedRemoveStep(Path path) {
		Set<Path> pendingBiasedPaths = new HashSet<>();
		Set<Path> pendingUnbiasedPath = new HashSet<>();
		Path donePath = null;
		int differentRemovals = 0;
		for (String searchWord : WORDS) {
			int i = 0;
			String currentActivePathString = path.getStrings().get(path.getStrings().size() - 1);
			while (i < currentActivePathString.length()) {
				HeadSearchResult hsr = getOccurrencesAtHead(currentActivePathString.substring(i), searchWord);
				if (hsr.getHits() > 0) {
					differentRemovals++;
					String remainder = currentActivePathString.substring(0, i) + currentActivePathString.substring(i + hsr.getResult().length());

					Path branch = new Path(path);
					branch.addLast(remainder, hsr.getHits());

					if (path.isBias(searchWord)) pendingBiasedPaths.add(branch);
					else pendingUnbiasedPath.add(branch);

					i += hsr.getResult().length();
				}
				else {
					i++;
				}
			}
		}
		if (differentRemovals == 0) {
			donePath = path;
		}
		else {
			if (pendingBiasedPaths.size() == 0 && path.bias != null) {
				//biased paths are empty even though given path was biased, so make all unbiased paths biased
				pendingBiasedPaths.addAll(pendingUnbiasedPath);
				pendingUnbiasedPath.clear();
			}

			//setting each path with their proper biases
			for (Path p : pendingBiasedPaths) {
				p.bias = path.bias;
			}
		}
		return new BiasedRemoveResult(pendingBiasedPaths, pendingUnbiasedPath, donePath, path.bias);
	}

	private static class BiasedRemoveResult {
		final Set<Path> pendingBiasedPaths;
		final Set<Path> pendingUnbiasedPaths;
		final Path donePath;
		final String bias;

		public BiasedRemoveResult(Set<Path> pendingBiasedPaths, Set<Path> pendingUnbiasedPaths, Path donePath, String bias) {
			this.pendingBiasedPaths = pendingBiasedPaths;
			this.pendingUnbiasedPaths = pendingUnbiasedPaths;
			this.donePath = donePath;
			this.bias = bias;
		}
	}


	//Second version of remove(). Non-recursive.
	//When prune is set to true, it will attempt to always maintain 5 of the top-performing paths,
	//discarding any other paths.
	//Every path produced is checked against the pool it is going into to see if there is an
	//equivalent path in it already.
	//Gives wrong results in some cases though.
	private static Set<Path> fastRemove(String s, boolean prune) {
		Set<Path> activePaths = new HashSet<>();	//paths to step in current loop
		Set<Path> pendingPaths = new HashSet<>();	//paths to step next loop
		Set<Path> donePaths = new HashSet<>();		//paths that are done stepping
		activePaths.add(new Path(s, 0));

		while (activePaths.size() != 0) {
			//Step each path in activePaths. If a path has reached a dead end, remove it and put it in donePaths.
			//Otherwise, put it in pendingPaths. At the end, replace activePaths with pendingPaths
			for (Iterator<Path> iterator = activePaths.iterator(); iterator.hasNext();) {
				Path currentActivePath = iterator.next();
				FastRemoveResult fastRemoveResult = fastRemoveStep(currentActivePath);
				if (fastRemoveResult.donePath != null) {
					replacePathIfGreater(currentActivePath, donePaths);
					iterator.remove();
				}
				else {
					for (Path pendingPath : fastRemoveResult.pendingPaths) {
						if (prune) replacePathAndPruneWorst(pendingPath, pendingPaths);
						else replacePathIfGreater(pendingPath, pendingPaths);
					}
				}
			}
			activePaths = pendingPaths;
			pendingPaths = new HashSet<>();
		}


		return donePaths;
	}

	//returns all possible paths from a given path. Used by fastRemove
	private static FastRemoveResult fastRemoveStep(Path path) {
		Set<Path> pendingPaths = new HashSet<>();
		Path donePath = null;
		int differentRemovals = 0;
		for (String searchWord : WORDS) {
			int i = 0;
			String currentActivePathString = path.getStrings().get(path.getStrings().size() - 1);
			while (i < currentActivePathString.length()) {
				HeadSearchResult hsr = getOccurrencesAtHead(currentActivePathString.substring(i), searchWord);
				if (hsr.getHits() > 0) {
					differentRemovals++;
					String remainder = currentActivePathString.substring(0, i) + currentActivePathString.substring(i + hsr.getResult().length());

					Path branch = new Path(path);
					branch.addLast(remainder, hsr.getHits());

					pendingPaths.add(branch);

					i += hsr.getResult().length();
				}
				else {
					i++;
				}
			}
		}
		if (differentRemovals == 0) {
			donePath = path;
		}
		return new FastRemoveResult(donePath, pendingPaths);
	}

	//
	private static class FastRemoveResult {
		final Path donePath;
		final Set<Path> pendingPaths;

		public FastRemoveResult(Path donePath, Set<Path> pendingPaths) {
			this.donePath = donePath;
			this.pendingPaths = pendingPaths;
		}
	}

	//First version of remove(). Recursive.
	private static Set<Path> slowRemove(String s) {
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
					Set<Path> localRemoval = slowRemove(remainder);

					for (Path path : localRemoval) {
						path.addFirst(s, hsr.getHits());
					}
					i += hsr.getResult().length();

					//Replace path in set if this new path has a greater number of moves.
					for (Path path : localRemoval) {
						replacePathIfGreater(path, pathsToAdd);
					}
				}
				else {
					i++;
				}
			}
		}
		if (differentRemovals == 0) {
			pathsToAdd.add(new Path(s, 0));
		}
		return pathsToAdd;

	}

	//Checks a Set to see if a Path already exists in it.
	//If it doesn't adds it.
	//If it does, checks if new Path has a greater number of moves,
	//and if so, replaces it.
	private static void replacePathIfGreater(Path path, Set<Path> set) {
		Optional<Path> existingPathOptional = set.stream().filter(x -> x.equals(path)).findFirst();
		if (existingPathOptional.isPresent()) {
			Path existingPath = existingPathOptional.get();
			if (path.getMoves() > existingPath.getMoves()) {
				set.add(path);
			}
		}
		else {
			set.add(path);
		}
	}

	//Checks a Set to see if a Path already exists in it.
	//If it doesn't, adds it if there are fewer than 5 paths.
	//If there are more than 5 paths, check if it outperforms the worst path. If so, replace it.
	//If it does, checks if new Path has a greater number of moves,
	//and if so, replaces it.
	private static void replacePathAndPruneWorst(Path path, Set<Path> set) {
		final int MAX_SET_SIZE = 5;
		Optional<Path> existingPathOptional = set.stream().filter(x -> x.equals(path)).findFirst();
		if (existingPathOptional.isPresent()) {
			Path existingPath = existingPathOptional.get();
			if (path.getMoves() > existingPath.getMoves()) {
				set.add(path);
			}
		}
		else {
			if (set.size() < MAX_SET_SIZE) {
				set.add(path);
			}
			else {
				int worstMoves = Integer.MAX_VALUE;
				Path worstPath = null;
				for (Path setPath : set) {
					if (setPath.getMoves() < worstMoves) {
						worstMoves = setPath.getMoves();
						worstPath = setPath;
					}
				}
				if (worstMoves < path.getMoves()) {
					set.remove(worstPath);
					set.add(path);
				}
			}
		}
	}

	//Checks a Set to see if a Path already exists in it.
	//If it doesn't, adds it if there are fewer than 5 paths.
	//If there are more than 5 paths, check if it is BETTER than the BEST path. If so, replace it.
	//(Better, not worse, since the idea here is that better paths early on lead to worse paths later.)
	//(That idea is probably false.)
	//If it does exist, checks if new Path has a greater number of moves,
	//and if so, replaces it.
	@SuppressWarnings("unused")
	private static void replacePathAndPruneBest(Path path, Set<Path> set) {
		final int MAX_SET_SIZE = 5;
		Optional<Path> existingPathOptional = set.stream().filter(x -> x.equals(path)).findFirst();
		if (existingPathOptional.isPresent()) {
			Path existingPath = existingPathOptional.get();
			if (path.getMoves() > existingPath.getMoves()) {
				set.add(path);
			}
		}
		else {
			if (set.size() < MAX_SET_SIZE) {
				set.add(path);
			}
			else {
				int bestMoves = 0;
				Path bestPath = null;
				for (Path setPath : set) {
					if (setPath.getMoves() > bestMoves) {
						bestMoves = setPath.getMoves();
						bestPath = setPath;
					}
				}
				if (bestMoves > path.getMoves()) {
					set.remove(bestPath);
					set.add(path);
				}
			}
		}
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
		String bias = null;

		public Path(String string, int moves, String bias) {
			this(string, moves);
			this.bias = bias;
		}

		public Path(String string, int moves) {
			this.strings = new LinkedList<>();
			this.strings.add(string);
			this.moves = moves;
		}

		public boolean isBias(String o) {
			if (this.bias == null) return false;
			else return this.bias.equals(o);
		}

		//for cloning
		private Path(Path originalPath) {
			this.strings = new LinkedList<>(originalPath.getStrings());
			this.moves = originalPath.getMoves();
			this.bias = null;
		}

		//adds a string to the FRONT of the list
		public void addFirst(String string, int moves) {
			strings.addFirst(string);
			this.moves += moves;
		}

		public void addLast(String string, int moves) {
			strings.addLast(string);
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
