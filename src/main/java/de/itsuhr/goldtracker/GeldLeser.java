package de.itsuhr.goldtracker;

/**
 * ✍️ DEIN TEIL (im Video live schreiben!)
 *
 * Die Seitenleiste zeigt das Geld als Text, z. B. "5,98M".
 * Damit wir rechnen können, brauchen wir daraus eine echte Zahl: 5980000.
 *
 * Beispiele, die funktionieren müssen (siehe GeldLeserTest):
 *   "5,98M"  -> 5980000      (M = Million)
 *   "750K"   -> 750000       (K = Tausend, auch klein "k")
 *   "1,5B"   -> 1500000000   (B = Milliarde)
 *   "12.345" -> 12345        (Punkt = Tausender-Trennzeichen)
 *   "980"    -> 980
 *   "abc"    -> -1           (kein Geld erkannt)
 *
 * Tipps:
 *   - text.trim() entfernt Leerzeichen am Rand
 *   - letztes Zeichen holen: text.charAt(text.length() - 1)
 *   - Punkte weg, Komma in Punkt umwandeln, damit Java es als Kommazahl versteht: "5,98" -> "5.98"
 *   - Double.parseDouble("5.98") * 1_000_000 -> 5980000.0
 *   - Math.round(...) macht daraus eine ganze Zahl (long)
 *   - mit try { ... } catch (NumberFormatException e) { return -1; } fängst du Unsinn ab
 */
public final class GeldLeser {
	private GeldLeser() {}

	public static long lies(String text) {
		// TODO: Hier kommt dein Code hin!
		return -1;
	}
}
