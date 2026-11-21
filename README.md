# 💰 Gold-Tracker (HugoSMP)

Client-Mod (Fabric, Minecraft 26.3). Liest die Seitenleiste ("Geld 5,98M") und zeigt live,
wie viel du pro Stunde verdienst. Nur Anzeige – keine Automatik, kein Vorteil.

## ✍️ Dein Teil (fürs Video)
1. `src/main/java/de/itsuhr/goldtracker/GeldLeser.java` → `lies("5,98M")` soll `5980000` geben
2. `src/main/java/de/itsuhr/goldtracker/Rechner.java` → `proStunde(...)`

Prüfen: `gradlew test` → Bericht unter `build/reports/tests/test/index.html` (rot → grün).
Hängst du fest: Spicker in `.spicker/` (nur wenn's gar nicht geht 😉).

## Bauen & Spielen
`gradlew build` → `build/libs/goldtracker-1.0.0.jar` in `.minecraft/mods`
(dazu Fabric Loader für 26.3 + Fabric API).
Im Spiel: **G** = Messung starten/stoppen. Rekord ab 5 Min Messung.
