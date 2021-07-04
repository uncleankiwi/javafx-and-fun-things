package util;

/**
 * A stopwatch for checking run time.
 * Instantiate to start, .stop() to stop.
 */
public class Stopwatch {
	private final long timeStart;

	/**
	 * Creates and starts a stopwatch instance.
	 */
	public Stopwatch() {
		this.timeStart = System.currentTimeMillis();
	}

	/**
	 * Stops the stopwatch.
	 * @param processName the method name to display in the output.
	 */
	public void stop(String processName) {
		long timeEnd = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder("Run time for ");
		sb.append(processName)
			.append(":")
			.append(timeEnd - timeStart)
			.append("ms.");
	}
}
