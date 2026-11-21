<img src="src/main/resources/assets/goldtracker/icon.png" width="96" align="right">

# 💰 Gold-Tracker

Eine kleine **Fabric-Client-Mod** für Minecraft **26.3**, die live anzeigt,
wie viel Geld du pro Stunde verdienst – ideal, um Farmen und Geldmethoden auf SMP-Servern zu vergleichen.

Gemacht von **ItsUhr** für YouTube.

## Funktionen
- HUD oben links: Kontostand, verdient, **Geld pro Stunde**, Messzeit, Rekord
- **Taste G** startet/stoppt eine Messung (in den Steuerungs-Einstellungen änderbar)
- Ergebnis nach dem Stoppen im Chat
- Rekord (ab 5 Minuten Messung) wird in `config/goldtracker.properties` gespeichert
- Ausgaben (Geld sinkt) werden ignoriert, gezählt werden nur Einnahmen

## Wie funktioniert das?
Die Mod liest die Zeile **„Geld“** aus der Seitenleiste (Scoreboard), die der Server sowieso an deinen
Client schickt, z. B. `Geld 5,98M`. Sie sendet nichts an den Server und automatisiert nichts –
es ist eine reine Anzeige.

> ⚠️ Frag vor der Nutzung auf einem Server, ob Client-Mods dort erlaubt sind.

## Installation
1. [Fabric Loader](https://fabricmc.net/use/) für Minecraft 26.3 installieren
2. [Fabric API](https://modrinth.com/mod/fabric-api) in den `mods`-Ordner legen
3. `goldtracker-1.0.0.jar` (unter *Releases*) in den `mods`-Ordner legen
4. Minecraft mit dem Fabric-Profil starten

## Selbst bauen
Voraussetzung: Java 25
```
gradlew build
```
Die Mod liegt danach in `build/libs/`. Tests: `gradlew test`.

## Projektaufbau
| Datei | Aufgabe |
|---|---|
| `GoldTracker.java` | Einstieg, Taste, Ablauf |
| `Hud.java` | das Panel oben links |
| `Seitenleiste.java` | liest die „Geld“-Zeile aus dem Scoreboard |
| `GeldLeser.java` | `"5,98M"` → `5980000` |
| `Rechner.java` | Verdienst pro Stunde |
| `Messung.java` | Start/Stopp, zählt Einnahmen |
| `Rekord.java` | speichert den besten Wert |
| `Format.java` | Zahlen schön anzeigen (`1,25M`) |

## Lizenz
MIT – siehe [LICENSE](LICENSE).
