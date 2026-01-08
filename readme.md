# Wopper2 - L-System Fractal Generator

[![Build](https://github.com/dedee/wopper2/actions/workflows/build.yml/badge.svg)](https://github.com/dedee/wopper2/actions/workflows/build.yml)
[![Release](https://github.com/dedee/wopper2/actions/workflows/release.yml/badge.svg)](https://github.com/dedee/wopper2/actions/workflows/release.yml)

(C)opyright D.Pfeifle 2010-2026


![Screenshot](Screenshot1.png)

## What is Wopper2

Wopper is a Java application that generates and displays fractal graphics based on L-Systems. L-Systems are a type of formal grammar that can be used to model the development of plants, fractals, and other complex structures.

You can find more information about L-Systems on [Wikipedia](https://en.wikipedia.org/wiki/L-system).

## How to build and run

The project uses Gradle as a build system.


## L-System Syntax

The L-System rules are defined in text files with a simple syntax.

### Example

```
(1) winkel = 36
(2) R = F
(3) 0 : BASE = R++R++R++R++R
(4) 1 : R = R++R++R|R-R++R
```

1.  **winkel (angle)**: Defines the turning angle in degrees.
2.  **Constants**: Defines a constant as a sequence of commands.
3.  **Recursion level 0**: Defines the starting pattern (axiom).
4.  **Recursion level n**: Defines the replacement rules for the next recursion level.

### Commands

| Command | Description                               |
| :---: | ------------------------------------------ |
|   `F` | Draw a line forward                        |
|   `f` | Move forward without drawing               |
|   `+` | Turn right by the defined angle            |
|   `-` | Turn left by the defined angle             |
|   `|` | Turn 180 degrees                           |
|   `[` | Push the current position and angle to the stack |
|   `]` | Restore the last position and angle from the stack |

### Prerequisites

*   Java Development Kit (JDK) 17 or higher

### Build

To build the project, run the following command in the root directory of the project:

```bash
./gradlew build
```

### Run

To start the application, run the following command:

```bash
./gradlew run
```

The application will start and display a window with a graphical representation of an L-System. 
You can load different L-System files, adjust the recursion depth, 
and view the resulting fractals.

## GitHub Actions

This project includes GitHub Actions workflows for:

- **Continuous Integration**: Automatically builds and tests the project on every push and pull request
- **Release**: Automatically creates GitHub releases with compiled JAR files when you push a version tag (e.g., `v1.0.0`)


### Creating a Release

To create a release:
```bash
git tag v1.0.0
git push origin v1.0.0
```
