# IDT take-home assignment

## Overview

The app is made up of two screen:

- `HomeScreen` allows specifying the table's size via rows (max 1000) and columns (max 6) fields
- `TableScreen` displays the table with cells filled with random data; single click changes the
  color of the cell, while a double click enables users to edit the cell's content

The app is targeted towards tablets, should use Jetpack Compose and be modularized.

## Requirements & Setup

- JDK 17 and up
- Android Studio Quail 4 | 2026.1.4 Patch 1 and up

## Architecture

The app is split into the following modules:

- `:data` represents the data layer and includes various repositories; `CellRepository` is backed by
  an SQLite database
- `:domain` represents the domain layer and includes various use cases that mostly delegate to the
  `:data` layer
- `:ui` represents the UI layer and includes navigation, Compose screens and ViewModels; it depends
  on the `:domain` module
- `:design` represents a tiny design system built on the new Compose Styles API
- `:app-android` modules wires everything up and is home to the DI graph

The project also contains `build-logic` included build that provides several convention plugins.
This is a template that I use for most new projects these days.

## Key Decisions

- Kotlin Multiplatform throughout the project instead of plain Android - while marginally more
  involved at the initial stages (`build-logic` saves quite a bit of time on setup) it offers a
  speedier target (`jvm`) that plays nicely with most hosted CIs (e.g. GitHub Actions)
- SQLite database was chosen as the underlying data source for `CellRepository` as it allows to both
  persist the data on disk (so the app plays well with process death) and provides a very natural
  API for the kind of interactions the requirement calls for
- The `cell` table includes `sessionId` to enable proper handling of process death and restoration
- The `cell` tables stores each cell flat (e.g. `sessionId`, `row` and `column`), which scales
  fairly well for differently sized table at the expense of table row count
- The `cell` table stores both the `content` and the `checked` state (again, process death)
- `HomeScreen` doesn't feature a dedicated `HomeViewModel` in charge of input validation - instead,
  this task is solely handled by `AllowedRangeInputTransformation` that drops values outside
  allowed range for either rows or columns; this approach dismisses invalid states entirely and
  simplifies UX somewhat
- A `Dialog` (via Navigation 3) is used for cell editing as an alternative to in-place approach,
  greatly simplifying things like focus management in a large dataset; this also make it a bit more
  obvious where the user was before a process death as the `Dialog` will be remembered and
  displayed (including the uncommited edits)
- An application-wide `CoroutineScope` is used for some cleanup tasks, like deleting the data from
  the table; it's a rough equivalent to `GlobalScope`

## Known Limitations / Next Steps

- It's possible that the generated session is not be removed in `TableViewModel.onCleared()` since
  it may fire before the `PopulateTableUseCase` had a chance to complete; this is handled on
  subsequent app starts where we delete anything remaining in the `cell` table
- The dataset size isn't particularly large and can be safely loaded into memory; if the
  requirements change and call for even more rows/columns then a Paging 3 integration should be
  considered
- Some of the modules (particularly `:data`) would benefit from an -api/-impl split; at the moment
  they include both the public interface and actual implementation, which shouldn't be something
  visible by the likes of `:domain` and `:ui`

## Testing

Most of the modules include some form of automated testing accessible via `./gradlew allTests`. Note
that the UI tests found in `:ui` run strictly on `jvm` target - this was mostly done to avoid
setting up emulators on the CI. They also tend to run so much faster.

## Tooling

There are several tools used in the project to ensure code quality:

- The code is formatted using [kempt](https://github.com/ZacSweers/kempt) (ktfmt with Google style)
- Detekt (albeit still an alpha version) with Compose-specific rule set is used for static analysis;
  it runs as part of `./gradlew check`
- A simple GitHub Actions workflow is also present

Project is predominantly built on Kotlin Multiplatform and to demonstrate this an additional
desktop target (`:app-desktop`) is available for consumption.

SQLite is backed by [SQLDelight](https://github.com/sqldelight/sqldelight), but isn't strictly a
requirement. A more common approach would be to use Room 3 with a largely similar outcome.

DI is provided via [Metro](https://github.com/ZacSweers/metro). It was chosen due to a familiar
API (very Dagger-like), easy Kotlin Multiplatform support and blazing fast compilation (thanks to
being implemented as a Kotlin Compiler Plugin). Koin or kotlin-inject would work just as nice here,
though.
