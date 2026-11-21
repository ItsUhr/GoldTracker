package de.itsuhr.goldtracker;

import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Speichert deinen besten Wert pro Stunde in config/goldtracker.properties. */
public final class Rekord {
	private static final Path DATEI = FabricLoader.getInstance().getConfigDir().resolve("goldtracker.properties");
	/** Erst ab 5 Minuten Messung zählt ein Rekord (sonst ist der Wert zu zufällig). */
	public static final long MIN_DAUER = 5 * 60 * 1000;
	private static long bester = laden();

	private Rekord() {}

	public static long bester() { return bester; }

	/** @return true, wenn es ein neuer Rekord ist */
	public static boolean pruefen(long proStunde, long dauer) {
		if (dauer < MIN_DAUER || proStunde <= bester) return false;
		bester = proStunde;
		Properties p = new Properties();
		p.setProperty("rekordProStunde", Long.toString(bester));
		try (Writer w = Files.newBufferedWriter(DATEI)) { p.store(w, "Gold-Tracker"); } catch (Exception ignored) {}
		return true;
	}

	private static long laden() {
		if (!Files.exists(DATEI)) return 0;
		Properties p = new Properties();
		try (Reader r = Files.newBufferedReader(DATEI)) {
			p.load(r);
			return Long.parseLong(p.getProperty("rekordProStunde", "0"));
		} catch (Exception e) { return 0; }
	}
}
