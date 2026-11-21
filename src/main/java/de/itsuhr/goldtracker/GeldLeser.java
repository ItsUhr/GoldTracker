package de.itsuhr.goldtracker;

/**
 * Wandelt den Geld-Text aus der Seitenleiste in eine Zahl um.
 *
 * <pre>
 * "5,98M"  -> 5_980_000
 * "750K"   -> 750_000
 * "1,5B"   -> 1_500_000_000
 * "12.345" -> 12_345
 * "abc"    -> -1
 * </pre>
 */
public final class GeldLeser {
	private GeldLeser() {}

	/**
	 * @param text Geldbetrag wie in der Seitenleiste, optional mit K (Tausend), M (Million) oder B (Milliarde)
	 * @return Betrag als ganze Zahl oder {@code -1}, wenn der Text kein Geldbetrag ist
	 */
	public static long lies(String text) {
		if (text == null) return -1;
		text = text.trim();
		if (text.isEmpty()) return -1;

		long faktor = 1;
		char letztes = Character.toUpperCase(text.charAt(text.length() - 1));
		if (letztes == 'K') faktor = 1_000;
		if (letztes == 'M') faktor = 1_000_000;
		if (letztes == 'B') faktor = 1_000_000_000;
		if (faktor > 1) text = text.substring(0, text.length() - 1);

		text = text.replace(".", "").replace(",", ".");

		try {
			return Math.round(Double.parseDouble(text) * faktor);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
