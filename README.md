# Kennzeichen-Spiel

Android-App für Kinder zum Sammeln von Kfz-Kennzeichen während Autofahrten.

## Konzept

- **Ziel**: Möglichst viele Kennzeichen erfassen
- **Punkte**: Basierend auf KBA-Statistik (Seltenheit = mehr Punkte)
  - Normal: 1–10 Punkte pro Kennzeichen
  - Sonderfall **BÜS** (Büsingen am Hochrhein): 100 Punkte
- **Doppelte**: Werden erkannt, aber nicht abgestraft – Hinweis: *"Name hat um HH:MM bereits XX erfasst"*
- **Mehrere Kinder**: Spielen gemeinsam in einer Session ("ein Auto")

## Features

- Spieler-Namen eingeben (1–6 Kinder)
- Standby-Screen: Buttons mit Namen + aktueller Punktzahl
- Großbuchstaben-Tastatur zur schnellen Eingabe
- Nach Erfassung: Statistik-Info + Punkte + Wikipedia-Button
- Offline-fähig (Daten in Assets + Room-Datenbank)

## Tech Stack

- Kotlin + Jetpack Compose (Material3)
- Room Database (offline-first)
- CSV-Datenbasis im Assets-Ordner (~80 Kennzeichen mit KBA-Daten)
- Min SDK 24, Target SDK 34

## Projektstruktur

```
kennzeichen-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/kennzeichen/app/
│   │   │   ├── data/database/          # Room Entities & DAOs
│   │   │   ├── data/repository/        # Business Logic & CSV-Loader
│   │   │   ├── ui/
│   │   │   ├── screen/                 # Compose Screens
│   │   │   ├── component/              # UI Components
│   │   │   ├── viewmodel/              # ViewModels
│   │   ├── assets/kennzeichen.csv      # KBA-Daten
```

## Build

```bash
cd kennzeichen-app
./gradlew assembleDebug
```

## Lizenz

Privates Projekt – nicht für kommerzielle Nutzung.