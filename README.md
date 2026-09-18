# Compose Micro-interactions Showcase

A small gallery of physics-based Compose animations: shared-element transitions, swipe-to-reveal, spring buttons, parallax scroll.

## Demos
[GIF per demo, or one combined reel]

- **Shared Element** — a record in a list expands into a full detail view using `SharedTransitionLayout` and spring-based bounds transforms.
- **Swipe to Reveal** — drag a message to reveal archive/delete actions, driven by `Animatable` and `draggable`, settling with a spring.
- **Spring Button** — a press-and-release button with bouncy scale animation and haptic feedback.
- **Parallax Scroll** — a header that moves at a different rate than the list scrolling beneath it, via `graphicsLayer`.

## Stack
Jetpack Compose, Compose Animation APIs (`SharedTransitionLayout`, `Animatable`, `updateTransition`, `graphicsLayer`)

## Run it
```
./gradlew installDebug
```
