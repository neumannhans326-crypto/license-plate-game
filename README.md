# License Plate Game

Android app for kids to collect license plates during car trips.

## Concept

- **Goal**: Capture as many license plates as possible
- **Points**: Based on German KBA statistics (rarity = more points)
  - Normal: 1–10 points per license plate
  - Special case **BÜS** (Büsingen am Hochrhein): 100 points
- **Duplicates**: Detected but not penalized – notice: *"Name captured XX at HH:MM already"*
- **Multiple kids**: Play together in one session ("one car")

## Features

- Enter player names (1–6 kids)
- Standby screen: buttons with names + current score
- Capital letter keyboard for quick entry
- After capture: stats info + points + Wikipedia button
- Offline-capable (data in Assets + Room database)

## Tech Stack

- Kotlin + Jetpack Compose (Material3)
- Room Database (offline-first)
- CSV data in Assets folder (~80 license plates with KBA data)
- Min SDK 24, Target SDK 34

## Project Structure

```
kennzeichen-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/kennzeichen/app/
│   │   │   ├── data/database/          # Room Entities & DAOs
│   │   │   ├── data/repository/        # Business Logic & CSV Loader
│   │   │   ├── ui/
│   │   │   ├── screen/                 # Compose Screens
│   │   │   ├── component/              # UI Components
│   │   │   ├── viewmodel/              # ViewModels
│   │   ├── assets/kennzeichen.csv      # KBA Data
```

## Build

```bash
cd kennzeichen-app
./gradlew assembleDebug
```

## License

Private project – not for commercial use.