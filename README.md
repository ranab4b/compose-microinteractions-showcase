# Compose Micro-interactions Showcase

A small gallery of physics-based Compose animations: shared-element transitions, swipe-to-reveal, spring buttons, parallax scroll.

## Demos


- **Shared Element** — a record in a list expands into a full detail view using `SharedTransitionLayout` and spring-based bounds transforms.

https://github.com/user-attachments/assets/063707d3-9d5f-4f39-9cf1-1d7ad1b9775c


- **Swipe to Reveal** — drag a message to reveal archive/delete actions, driven by `Animatable` and `draggable`, settling with a spring.



https://github.com/user-attachments/assets/3f941fc1-9606-4895-ac38-f9d37ac5eb08


- **Spring Button** — a press-and-release button with bouncy scale animation and haptic feedback.


https://github.com/user-attachments/assets/5aac6a90-9f89-4b76-b812-dadfe5447be7

  
- **Parallax Scroll** — a header that moves at a different rate than the list scrolling beneath it, via `graphicsLayer`.

  

https://github.com/user-attachments/assets/eb2dacdd-1007-4cf6-b9a2-1d2cb701c0e0





## Stack
Jetpack Compose, Compose Animation APIs (`SharedTransitionLayout`, `Animatable`, `updateTransition`, `graphicsLayer`)

## Run it
```
./gradlew installDebug
```
