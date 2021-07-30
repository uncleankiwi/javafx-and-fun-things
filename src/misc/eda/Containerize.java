package misc.eda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Given a list of items of varying sizes, this does two different things:
	1. tries to fit all items into n groups of fixed size s
	2. tries to split the items into n groups of equal size
 */
public class Containerize {
	public static void main(String[] args) {
		List<Integer> list1 = toList(1,2,3,4,5);
		System.out.println(fit(list1, 2, 5));
		System.out.println(split(list1, 3));
	}

	//tries to fit the given items into containerCount containers of containerSize.
	//returns null if it can't be fit.
	public static List<List<Integer>> fit(List<Integer> items, int containerSize, int containerCount) {
		ParseResult parseResult = new ParseResult(items);
		if (parseResult.totalSize > containerSize * containerCount) return null;
		else {

		}
		return null; //todo
	}

	//splits the given items into containerCount groups of equal size (totalSize/containerCount)
	//returns null if it can't be split.
	public static List<List<Integer>> split(List<Integer> items, int containerCount) {
		ParseResult parseResult = new ParseResult(items);
		if (parseResult.totalSize % containerCount != 0) return null;
		else {

		}
		return null; //todo
	}

	private static class ParseResult {
		final int totalSize;
		final Map<Integer,Integer> tally;

		ParseResult(List<Integer> items) {
			Map<Integer,Integer> tally = new HashMap<>();
			int totalSize = 0;
			for (int item : items) {
				totalSize += item;
				addToTally(tally, item);
			}
			this.totalSize = totalSize;
			this.tally = tally;
		}
	}

	private static void addToTally(Map<Integer, Integer> tally, int item) {
		if (tally.containsKey(item)) {
			tally.put(item, tally.get(item) + 1);
		}
		else {
			tally.put(item, 1);
		}
	}

	private static List<Integer> toList(int... k) {
		List<Integer> list = new ArrayList<>();
		for (int i : k) {
			list.add(i);
		}
		return list;
	}
}
