package de.itsuhr.goldtracker;

/**
 * ✍️ DEIN TEIL Nr. 2
 *
 * Wie viel Geld verdienst du pro Stunde?
 *   verdient = Geld, das du in der Messung gemacht hast
 *   millis   = wie lange die Messung schon läuft (in Millisekunden)
 *
 * Eine Stunde hat 60 * 60 * 1000 = 3_600_000 Millisekunden.
 * Beispiel: 500_000 in 30 Minuten (1_800_000 ms) -> 1_000_000 pro Stunde.
 * Achtung: Wenn millis 0 ist, darfst du nicht durch 0 teilen -> dann 0 zurückgeben.
 */
public final class Rechner {
	private Rechner() {}

	public static long proStunde(long verdient, long millis) {
		// TODO: Hier kommt dein Code hin!
		return 0;
	}
}
