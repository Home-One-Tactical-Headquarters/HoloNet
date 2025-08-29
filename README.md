# HoloNet

## Project Goal

HoloNet is a Kotlin Multiplatform desktop application that reimagines the popular MagicMirror concept using Kotlin and Compose. Built with Compose Multiplatform for the desktop, HoloNet aims to provide a sleek, customizable smart mirror interface that can display various information modules on your screen.

### Key Features

- **Kotlin Multiplatform**: Leverages Kotlin's multiplatform capabilities for shared business logic
- **Compose Desktop**: Modern declarative UI framework for native desktop performance
- **Plugin System**: Utilizes PF4J (Plugin Framework for Java) for a robust, extensible module architecture
- **Modular Design**: Easy-to-develop plugins for weather, calendar, news, system monitoring, and more

### Architecture

The project is structured as a Kotlin Multiplatform application targeting desktop platforms, with a plugin-based architecture that allows developers to create custom modules without modifying the core application.

### Why HoloNet?

Unlike traditional MagicMirror implementations that rely on web technologies, HoloNet provides:
- Native desktop performance
- Type-safe plugin development with Kotlin
- Modern UI with Compose
- Cross-platform compatibility through Kotlin Multiplatform

## Project Structure

* `/composeApp` contains the shared code across Compose Multiplatform applications
  - `commonMain` - Common code for all targets
  - Platform-specific folders for target-specific implementations

## Getting Started

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)