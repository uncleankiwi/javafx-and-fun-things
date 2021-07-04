package util;

/**
 * A stopwatch for checking run time.
 * Instantiate to start, .stop() to stop.
 */
public class Stopwatch {
	private final long timeStart;
	private final String processName;

	/**
	 * Creates and starts a stopwatch instance.
	 * @param processName the method name to display in the output.
	 */
	public Stopwatch(String processName) {
		this.timeStart = System.currentTimeMillis();
		this.processName = processName;
		System.out.println("\nStarting stopwatch for " + processName + ".");
	}

	/**
	 * Stops the stopwatch.
	 */
	public void stop() {
		long timeEnd = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder("Run time for ");
		sb.append(processName)
			.append(":")
			.append(timeEnd - timeStart)
			.append("ms.");
		System.out.println(sb);
	}
}
