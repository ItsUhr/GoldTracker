package de.itsuhr.goldtracker;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

/**
 * Liest die Seitenleiste (Scoreboard rechts), die der Server sowieso an deinen Client schickt.
 * Sucht die Zeile mit "Geld" und gibt den Text dahinter zurück, z. B. "5,98M".
 */
public final class Seitenleiste {
	private Seitenleiste() {}

	public static final String SCHLUESSELWORT = "Geld";

	/** @return Text hinter "Geld" oder null, wenn keine Seitenleiste / keine Geld-Zeile da ist. */
	public static String geldText(Minecraft mc) {
		if (mc.level == null || mc.player == null) return null;
		Scoreboard sb = mc.level.getScoreboard();
		Objective obj = sb.getDisplayObjective(DisplaySlot.SIDEBAR);
		if (obj == null) return null;
		for (PlayerScoreEntry e : sb.listPlayerScores(obj)) {
			if (e.isHidden()) continue;
			Component name = e.display() != null ? e.display()
				: PlayerTeam.formatNameForTeam(sb.getPlayersTeam(e.owner()), e.ownerName());
			String treffer = nachWort(name.getString());
			if (treffer != null) return treffer;
		}
		return null;
	}

	/** "$ Geld 5,98M" -> "5,98M" */
	static String nachWort(String zeile) {
		int i = zeile.indexOf(SCHLUESSELWORT);
		if (i < 0) return null;
		String rest = zeile.substring(i + SCHLUESSELWORT.length()).replace(":", "").trim();
		int leer = rest.indexOf(' ');
		return leer > 0 ? rest.substring(0, leer) : rest;
	}
}
