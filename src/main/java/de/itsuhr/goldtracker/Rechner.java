package de.itsuhr.goldtracker;

/** Rechnet den Verdienst einer Messung auf eine Stunde hoch. */
public final class Rechner {
	private static final long STUNDE_MS = 3_600_000L;

	private Rechner() {}

	/**
	 * @param verdient Geld, das während der Messung dazugekommen ist
	 * @param millis   Dauer der Messung in Millisekunden
	 * @return Verdienst pro Stunde, {@code 0} wenn noch keine Zeit vergangen ist
	 */
	public static long proStunde(long verdient, long millis) {
		if (millis <= 0) return 0;
		return verdient * STUNDE_MS / millis;
	}
}
