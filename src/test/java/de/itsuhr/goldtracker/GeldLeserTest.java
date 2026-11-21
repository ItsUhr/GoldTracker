package de.itsuhr.goldtracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests für {@link GeldLeser}, {@link Rechner} und {@link Seitenleiste}. Ausführen mit {@code gradlew test}. */
class GeldLeserTest {
	@Test void millionen() { assertEquals(5_980_000L, GeldLeser.lies("5,98M")); }
	@Test void tausend() { assertEquals(750_000L, GeldLeser.lies("750K")); }
	@Test void tausendKlein() { assertEquals(12_500L, GeldLeser.lies("12,5k")); }
	@Test void milliarden() { assertEquals(1_500_000_000L, GeldLeser.lies("1,5B")); }
	@Test void mitPunkt() { assertEquals(12_345L, GeldLeser.lies("12.345")); }
	@Test void normaleZahl() { assertEquals(980L, GeldLeser.lies("980")); }
	@Test void leerzeichen() { assertEquals(2_000_000L, GeldLeser.lies("  2M ")); }
	@Test void unsinn() { assertEquals(-1L, GeldLeser.lies("abc")); }
	@Test void leer() { assertEquals(-1L, GeldLeser.lies("")); }

	@Test void proStunde() { assertEquals(1_000_000L, Rechner.proStunde(500_000, 1_800_000)); }
	@Test void proStundeNull() { assertEquals(0L, Rechner.proStunde(500_000, 0)); }

	@Test void seitenleisteZeile() { assertEquals("5,98M", Seitenleiste.nachWort("$ Geld 5,98M")); }
}
