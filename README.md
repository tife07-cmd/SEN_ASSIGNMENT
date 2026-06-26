# Tife — Scientific Calculator

A native Android calculator built as an engineering tool, not a toy. The surface stays calm —
paper-white, ink-black numerals, a single ink-teal accent — while the maths underneath is dense:
a full expression engine, matrix algebra, combinatorics, and statistics.

Written in Kotlin with XML layouts, ViewBinding, and Material 3. The computation engine is pure
Kotlin with zero Android dependencies, so it is unit-tested in isolation with JUnit.

---

## Features

| Mode | What it does |
|------|--------------|
| **Basic** | Four operations with correct precedence, parentheses, and a live result preview as you type. |
| **Scientific** | Trigonometric (`sin cos tan` + inverse), hyperbolic (`sinh cosh tanh`), `ln`, `log`, `sqrt`, `exp`, powers `^`, factorial `!`, constants `π` and `e`, and a DEG/RAD toggle. |
| **Matrix** | 2×2 to 4×4 matrices. Addition, subtraction, multiplication, determinant, inverse, and transpose. |
| **Permutations & Combinations** | `nPr`, `nCr`, and `n!` computed with `BigInteger` so large factorials stay exact. |
| **Statistics** | Count, mean, median, mode, range, and both sample and population standard deviation from a list of values. |

Modes are reached through a navigation drawer (hamburger menu). The active mode survives screen
rotation and app backgrounding.

---

## Design

A deliberate, restrained visual language — the opposite of a glossy, neon calculator.

- **One accent.** Ink-teal `#0C6B5E` on a neutral paper/ink family. Nothing else colours the UI.
- **Flat.** No gradients, no glow, no elevation. Depth is a single hairline.
- **Readable surface.** A muted expression line above a large ink result, separated by a hairline.

| Token | Colour |
|-------|--------|
| Paper | `#F5F4F1` |
| Surface | `#FFFFFF` |
| Ink | `#1A1A1A` |
| Ink (muted) | `#6B6B6B` |
| Key | `#ECEBE7` |
| Accent | `#0C6B5E` |

---

## Architecture

The project separates a pure-Kotlin core from the Android UI. Fragments never compute; they call
into the engine.

```
core/
  engine/
    Token.kt          Token model and types
    Tokenizer.kt      String -> tokens (handles unary minus, functions, constants)
    ShuntingYard.kt   Infix tokens -> Reverse Polish Notation (operator precedence)
    Evaluator.kt      Evaluates RPN; DEG/RAD aware
    Calculator.kt     Facade the UI calls: evaluate("1+2*3")
    CalcException.kt  Domain errors (division by zero, bad input, ...)
  math/
    Scientific.kt     Factorial used by the ! operator
    Matrix.kt         Matrix algebra (det, inverse via adjugate, transpose, ...)
    Combinatorics.kt  nPr, nCr, factorial (BigInteger)
    Statistics.kt     mean, median, mode, range, variance, standard deviation
  format/
    NumberFormatter.kt  Trims trailing zeros; switches to scientific notation at the extremes

feature/
  common/Keypad.kt    Shared expression state; maps display symbols (x, /) to engine ASCII (*, /)
  basic/              Basic mode fragment
  scientific/         Scientific mode fragment + DEG/RAD toggle
  matrix/             Matrix grid builder + fragment
  combinatorics/      nPr / nCr / n! fragment
  statistics/         Statistics fragment

MainActivity.kt       Drawer navigation, mode persistence, lifecycle logging
```

### How an expression is evaluated

```
"1+2*3"  ->  Tokenizer  ->  ShuntingYard (RPN)  ->  Evaluator  ->  7.0  ->  NumberFormatter  ->  "7"
```

Precedence is real: `1+2*3` is `7`, `(1+2)*3` is `9`, `-3^2` is `-9` (unary minus binds looser than
the power), and `2^3^2` is `512` (power is right-associative).

A subtle UI detail: the keys display friendly symbols (`×`, `÷`, `√`) but append engine-readable
ASCII (`*`, `/`, `sqrt(`). `Keypad` translates between the two, which is why the buttons read
cleanly and the parser still works.

---

## Build and run

### Requirements

- Android Studio (latest stable) or the Android command-line tools
- JDK 17
- Android SDK Platform 37
- Gradle wrapper is included (Gradle 9.4.1); no separate Gradle install needed

### Set up the SDK location

Create a `local.properties` file in the project root pointing at your Android SDK (this file is
intentionally not committed):

```properties
sdk.dir=/Users/you/Library/Android/sdk
```

### Common commands

```bash
# Run the unit tests (no device needed)
./gradlew :app:testDebugUnitTest

# Build a debug APK -> app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:assembleDebug

# Build a release APK -> app/build/outputs/apk/release/app-release-unsigned.apk
./gradlew :app:assembleRelease

# Install on a connected device or running emulator
./gradlew :app:installDebug
```

Or open the project in Android Studio and press **Run**.

---

## Tests

The engine is covered by host-side JUnit tests under `app/src/test/`:

- `EvaluatorTest` — precedence, parentheses, unary minus, right-associative power, factorial,
  and degree-mode trigonometry.
- `MatrixTest` — determinant, that a matrix times its inverse is the identity, `nPr`/`nCr`, and
  the statistics summary figures.

```bash
./gradlew :app:testDebugUnitTest
```

---

## Android lifecycle

`MainActivity` overrides every lifecycle callback and logs it under the `Lifecycle` tag. It saves
the active mode in `onPause` (to `SharedPreferences`) and `onSaveInstanceState`, and restores it in
`onCreate`. Each calculator screen also saves its own input.

To see it during a demo: open Logcat filtered on `Lifecycle`, rotate the screen or background the
app, and watch `onPause -> onStop` then `onStart -> onResume` fire, with the same mode restored.

---

## Project details

- **Package / applicationId:** `com.tife.tifescienticcalculator`
- **Min SDK:** 24  ·  **Target / Compile SDK:** 37
- **Language:** Kotlin  ·  **UI:** XML + ViewBinding + Material 3
