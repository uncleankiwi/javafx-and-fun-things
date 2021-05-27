package misc.testing_grounds;

import java.util.ArrayList;
import java.util.List;

public class PointerTest {
	public static void main(String[] args) {
		String stuff = "stuff";
		testReturn(stuff);
		System.out.println("outside method:" + stuff);

		List<String> someList1 = new ArrayList<>();
		someList1.add("stuff!");
		testListReturn(someList1);
		System.out.println(someList1);
	}

	public static void testReturn(String obj) {
		obj += "added!";
		System.out.println("in method:" + obj);
	}

	public static void testListReturn(List<String> someList) {
		someList.add("added!");
	}
}
