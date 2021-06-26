package misc.eda;

/*
Maximize the number of times the strings "ghost" and "osteo" can be removed from an input string.
 */
public class MaximizeRemoval {
	public static void main(String[] args) {
		test("ghosteo");
		test("ghostmosteo");
		test("ghteo");
		test("ghghostosteoeoost");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("ost");
		}
		for (int i = 0; i < 200; i++) {
			sb.append("eo");
		}
		test(sb.toString());

		sb = new StringBuilder();


	}

	public static void test(String s) {

	}

	public static int remove(String s) {
		return 0;
	}
}
