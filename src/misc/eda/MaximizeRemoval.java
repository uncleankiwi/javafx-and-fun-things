package misc.eda;

import java.util.ArrayList;
import java.util.List;

/*
Maximize the number of times the strings "ghost" and "osteo" can be removed from an input string.
 */
public class MaximizeRemoval {
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

	public static void test(String s) {
		List<String> result = remove(s);
		if (s.length() > 20) {
			System.out.println(s.substring(20) + " (" + (result.size() - 1) + " moves)");
		}
		else {
			StringBuilder sb = new StringBuilder(s);
			for (String r : result) {
				sb.append(" -> ")
					.append(r);
			}
			sb.append(" (")
				.append(result.size() - 1)
				.append(" moves)");
			System.out.println(sb);
		}
	}

	public static List<String> remove(String s) {
		return new ArrayList<String>();

	}
}
