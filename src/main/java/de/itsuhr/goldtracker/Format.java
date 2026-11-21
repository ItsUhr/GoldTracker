package de.itsuhr.goldtracker;

import java.util.Locale;

/** Macht aus Zahlen schöne Texte: 1250000 -> "1,25M". */
public final class Format {
	private Format() {}

	public static String geld(long v) {
		String vz = v < 0 ? "-" : "";
		long a = Math.abs(v);
		if (a >= 1_000_000_000L) return vz + kurz(a / 1e9) + "B";
		if (a >= 1_000_000L) return vz + kurz(a / 1e6) + "M";
		if (a >= 10_000L) return vz + kurz(a / 1e3) + "K";
		return vz + a;
	}

	private static String kurz(double d) {
		String s = String.format(Locale.GERMANY, d >= 100 ? "%.0f" : d >= 10 ? "%.1f" : "%.2f", d);
		if (s.contains(",")) s = s.replaceAll("0+$", "").replaceAll(",$", "");
		return s;
	}

	public static String zeit(long millis) {
		long s = Math.max(0, millis) / 1000;
		return String.format("%d:%02d:%02d", s / 3600, (s / 60) % 60, s % 60);
	}
}
