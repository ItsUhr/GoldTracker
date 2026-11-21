package de.itsuhr.goldtracker;

/**
 * Eine Messung: Start mit Taste G, Stopp mit Taste G.
 * Zählt nur Zuwächse – wenn du etwas kaufst (Geld sinkt), wird das ignoriert.
 */
public final class Messung {
	private boolean laeuft;
	private long startZeit;
	private long dauerBeiStopp;
	private long letzterStand = -1;
	private long verdient;

	public boolean laeuft() { return laeuft; }

	public void start(long jetzt) {
		laeuft = true;
		startZeit = jetzt;
		dauerBeiStopp = 0;
		verdient = 0;
		letzterStand = -1;
	}

	public void stopp(long jetzt) {
		dauerBeiStopp = dauer(jetzt);
		laeuft = false;
	}

	/** Neuer Kontostand aus der Seitenleiste (-1 = nicht erkannt). */
	public void stand(long geld) {
		if (!laeuft || geld < 0) return;
		if (letzterStand >= 0 && geld > letzterStand) verdient += geld - letzterStand;
		letzterStand = geld;
	}

	public long dauer(long jetzt) { return laeuft ? jetzt - startZeit : dauerBeiStopp; }
	public long verdient() { return verdient; }
	public long proStunde(long jetzt) { return Rechner.proStunde(verdient, dauer(jetzt)); }
}
