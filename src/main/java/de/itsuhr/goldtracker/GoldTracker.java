package de.itsuhr.goldtracker;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Gold-Tracker: Client-Mod, die den Kontostand aus der Seitenleiste liest und
 * den Verdienst pro Stunde als HUD anzeigt. Taste G startet/stoppt eine Messung.
 */
public class GoldTracker implements ClientModInitializer {
	public static final String ID = "goldtracker";

	private static final Messung MESSUNG = new Messung();
	private static KeyMapping taste;
	private static int tick;
	private static String rohText = null;   // was in der Seitenleiste steht, z. B. "5,98M"
	private static long kontostand = -1;    // was GeldLeser daraus macht

	@Override
	public void onInitializeClient() {
		KeyMapping.Category kat = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ID, "main"));
		taste = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.goldtracker.toggle", InputConstants.Type.KEYBOARD, InputConstants.KEY_G, kat));

		ClientTickEvents.END_CLIENT_TICK.register(GoldTracker::tick);
		HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(ID, "hud"), (g, delta) -> zeichnen(g));
	}

	private static void tick(Minecraft mc) {
		while (taste.consumeClick()) umschalten(mc);
		if (++tick % 20 != 0) return; // 1x pro Sekunde reicht
		rohText = Seitenleiste.geldText(mc);
		kontostand = rohText == null ? -1 : GeldLeser.lies(rohText);
		MESSUNG.stand(kontostand);
	}

	private static void umschalten(Minecraft mc) {
		long jetzt = System.currentTimeMillis();
		if (!MESSUNG.laeuft()) {
			MESSUNG.start(jetzt);
			MESSUNG.stand(kontostand);
			nachricht(mc, "§6$ Gold-Tracker: §aMessung gestartet");
		} else {
			MESSUNG.stopp(jetzt);
			long ph = MESSUNG.proStunde(jetzt);
			nachricht(mc, "§6$ Gold-Tracker: §cgestoppt §7– §f+" + Format.geld(MESSUNG.verdient())
				+ " §7in " + Format.zeit(MESSUNG.dauer(jetzt)) + " §7= §e" + Format.geld(ph) + "/h");
			if (Rekord.pruefen(ph, MESSUNG.dauer(jetzt))) nachricht(mc, "§6★ NEUER REKORD: §e" + Format.geld(ph) + "/h");
		}
	}

	private static void nachricht(Minecraft mc, String text) {
		if (mc.player != null) mc.player.sendSystemMessage(Component.literal(text));
	}

	private static void zeichnen(GuiGraphicsExtractor g) {
		Minecraft mc = Minecraft.getInstance();
		Font f = mc.font;
		long jetzt = System.currentTimeMillis();
		String[] z = {
			"§6§l$ GOLD-TRACKER",
			"§7Kontostand: §f" + (kontostand >= 0 ? Format.geld(kontostand) : "§c? §8(" + (rohText == null ? "keine Seitenleiste" : rohText) + ")"),
			"§7Verdient:   §a+" + Format.geld(MESSUNG.verdient()),
			"§7Pro Stunde: §e" + Format.geld(MESSUNG.proStunde(jetzt)) + "/h",
			"§7Zeit:       §f" + Format.zeit(MESSUNG.dauer(jetzt)),
			"§7Rekord:     §6" + Format.geld(Rekord.bester()) + "/h ★",
			MESSUNG.laeuft() ? "§a● läuft §8[G] stopp" : "§8[G] Messung starten",
		};
		int x = 6, y = 6, pad = 4, breite = 0;
		for (String s : z) breite = Math.max(breite, f.width(s));
		int hoehe = z.length * (f.lineHeight + 2);
		g.fill(x - pad, y - pad, x + breite + pad, y + hoehe + pad - 2, 0xB0101418);
		g.fill(x - pad, y - pad, x - pad + 2, y + hoehe + pad - 2, 0xFFFFB300);
		for (int i = 0; i < z.length; i++) g.text(f, z[i], x + 2, y + i * (f.lineHeight + 2), 0xFFFFFFFF, true);
	}
}
