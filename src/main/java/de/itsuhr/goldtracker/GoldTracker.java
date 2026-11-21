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
		Hud.zeichnen(g, kontostand, rohText, MESSUNG);
	}
}
