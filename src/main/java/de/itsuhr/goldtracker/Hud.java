package de.itsuhr.goldtracker;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix3x2fStack;

/** Zeichnet das Gold-Tracker-Panel oben links. */
public final class Hud {
	private Hud() {}

	private static final int BREITE = 150;
	private static final int ZEILE = 13;

	private static final int HINTERGRUND = 0xE6101520;
	private static final int RAHMEN = 0xFF7A5A14;
	private static final int GOLD_HELL = 0xFFFFD45A;
	private static final int GOLD_DUNKEL = 0xFFE08A00;
	private static final int TITEL = 0xFF2A1800;
	private static final int LABEL = 0xFF8E97AA;
	private static final int WEISS = 0xFFF2F4F8;
	private static final int GRUEN = 0xFF5CFF7A;
	private static final int GELB = 0xFFFFD54A;
	private static final int ORANGE = 0xFFFFA62B;
	private static final int ROT = 0xFFFF5C5C;
	private static final int TRENNER = 0xFF262D3D;

	private static final ItemStack BARREN = new ItemStack(Items.GOLD_INGOT);
	private static final ItemStack SMARAGD = new ItemStack(Items.EMERALD);
	private static final ItemStack SONNE = new ItemStack(Items.SUNFLOWER);
	private static final ItemStack UHR = new ItemStack(Items.CLOCK);
	private static final ItemStack STERN = new ItemStack(Items.NETHER_STAR);

	public static void zeichnen(GuiGraphicsExtractor g, long kontostand, String rohText, Messung m) {
		Font f = Minecraft.getInstance().font;
		long jetzt = System.currentTimeMillis();
		long proStunde = m.proStunde(jetzt);
		long rekord = Rekord.bester();

		int x = 6, y = 6;
		int kopf = 18;
		int zeilen = 5;
		int balken = rekord > 0 ? 12 : 0;
		int fuss = 16;
		int hoehe = kopf + 4 + zeilen * ZEILE + balken + fuss;

		// Panel + Rahmen
		g.fill(x, y, x + BREITE, y + hoehe, HINTERGRUND);
		g.outline(x - 1, y - 1, BREITE + 2, hoehe + 2, RAHMEN);

		// Kopf mit Gold-Verlauf
		g.fillGradient(x, y, x + BREITE, y + kopf, GOLD_HELL, GOLD_DUNKEL);
		item(g, BARREN, x + 3, y + 1, 1f);
		Component titel = Component.literal("GOLD-TRACKER").withStyle(ChatFormatting.BOLD);
		g.text(f, titel, x + (BREITE - f.width(titel)) / 2 + 6, y + 5, TITEL, false);

		// Werte
		int zy = y + kopf + 4;
		String konto = kontostand >= 0 ? Format.geld(kontostand) : (rohText == null ? "keine Leiste" : "? " + rohText);
		zeile(g, f, SMARAGD, "Kontostand", konto, kontostand >= 0 ? WEISS : ROT, x, zy);
		zeile(g, f, BARREN, "Verdient", "+" + Format.geld(m.verdient()), GRUEN, x, zy += ZEILE);
		zeile(g, f, SONNE, "Pro Stunde", Format.geld(proStunde) + "/h", GELB, x, zy += ZEILE);
		zeile(g, f, UHR, "Zeit", Format.zeit(m.dauer(jetzt)), WEISS, x, zy += ZEILE);
		zeile(g, f, STERN, "Rekord", Format.geld(rekord) + "/h", ORANGE, x, zy += ZEILE);
		zy += ZEILE;

		// Fortschritt zum Rekord
		if (rekord > 0) {
			int bx = x + 6, bw = BREITE - 12, by = zy + 2;
			float anteil = Math.min(1f, proStunde / (float) rekord);
			g.fill(bx, by, bx + bw, by + 4, TRENNER);
			g.fillGradient(bx, by, bx + Math.round(bw * anteil), by + 4, GOLD_HELL, GOLD_DUNKEL);
			String prozent = Math.round(anteil * 100) + "% vom Rekord";
			klein(g, f, prozent, bx + bw - Math.round(f.width(prozent) * 0.75f), by + 5, LABEL);
			zy += balken;
		}

		// Fußzeile: Status
		g.fill(x + 4, zy + 2, x + BREITE - 4, zy + 3, TRENNER);
		int fy = zy + 6;
		if (m.laeuft()) {
			boolean an = (jetzt / 500) % 2 == 0;
			g.fill(x + 7, fy + 2, x + 11, fy + 6, an ? GRUEN : 0xFF2E7A3E);
			g.text(f, Component.literal("LIVE").withStyle(ChatFormatting.BOLD), x + 15, fy, GRUEN, true);
			taste(g, f, "G", "Stopp", x + BREITE - 6, fy);
		} else {
			g.fill(x + 7, fy + 2, x + 11, fy + 6, LABEL);
			g.text(f, "BEREIT", x + 15, fy, LABEL, true);
			taste(g, f, "G", "Start", x + BREITE - 6, fy);
		}
	}

	private static void zeile(GuiGraphicsExtractor g, Font f, ItemStack icon, String label, String wert, int farbe, int x, int y) {
		item(g, icon, x + 5, y, 0.75f);
		g.text(f, label, x + 19, y + 2, LABEL, true);
		g.text(f, wert, x + BREITE - 6 - f.width(wert), y + 2, farbe, true);
	}

	/** Tasten-Hinweis rechtsbündig: [G] Stopp */
	private static void taste(GuiGraphicsExtractor g, Font f, String taste, String text, int rechts, int y) {
		int tw = f.width(text);
		int kx = rechts - tw - 4 - f.width(taste) - 6;
		g.fill(kx, y - 2, kx + f.width(taste) + 6, y + 9, 0xFF2A3142);
		g.outline(kx, y - 2, f.width(taste) + 6, 11, 0xFF4A5368);
		g.text(f, taste, kx + 3, y, WEISS, false);
		g.text(f, text, rechts - tw, y, LABEL, true);
	}

	private static void item(GuiGraphicsExtractor g, ItemStack stack, int x, int y, float skala) {
		Matrix3x2fStack p = g.pose();
		p.pushMatrix();
		p.translate(x, y);
		p.scale(skala, skala);
		g.item(stack, 0, 0);
		p.popMatrix();
	}

	private static void klein(GuiGraphicsExtractor g, Font f, String text, int x, int y, int farbe) {
		Matrix3x2fStack p = g.pose();
		p.pushMatrix();
		p.translate(x, y);
		p.scale(0.75f, 0.75f);
		g.text(f, text, 0, 0, farbe, false);
		p.popMatrix();
	}
}
