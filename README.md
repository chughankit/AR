Holographic AI Companion (Android)

Overview
- Android ARCore app skeleton that places a 3D avatar in AR and chats via voice using a pluggable AI backend. Uses community Sceneform for AR UX, Android SpeechRecognizer for STT, and TextToSpeech for TTS. Ships with a local echo AI for offline development.

What’s Included
- AR: Tap-to-place avatar in `MainActivity` and `AvatarController`.
- Voice: Listen and speak via `VoiceManager`.
- AI: `AiService` interface with `LocalEchoAiService` stub.
- Assets: Put your GLB model at `app/src/main/assets/models/companion.glb`.

Project Structure
- settings.gradle / build.gradle — Gradle setup
- app/build.gradle — Android config and deps (ARCore + Sceneform)
- app/src/main/AndroidManifest.xml — permissions and activity
- app/src/main/java/com/example/holoai/
  - MainActivity.kt — UI + glue logic
  - HoloAiApp.kt — Application class
  - ar/AvatarController.kt — avatar placement and simple emote
  - ai/AiService.kt, ai/LocalEchoAiService.kt — AI abstraction and stub
  - voice/VoiceManager.kt — STT and TTS

Prerequisites
- Android Studio Hedgehog/Koala+, Android SDK 34, JDK 17
- ARCore-capable Android device

Setup
1) Open this folder in Android Studio and let Gradle sync.
2) Add a GLB model named `companion.glb` to `app/src/main/assets/models/`.
   - Tip: Keep the model small (<10MB) and with baked animations if you have them.
3) Build and run on a physical ARCore device.
4) Grant camera and microphone permissions when prompted.

Using the App
- Tap “Place Companion” then tap a detected plane to place the avatar.
- Press “Talk” to speak. The local echo AI will respond and TTS will speak it.

Integrate a Real AI Backend
- Create `RemoteAiService` implementing `AiService` and call your provider (OpenAI, Azure, etc.). Replace usage in `MainActivity`.
- Add your API key handling (e.g., `local.properties` or encrypted storage) and network permissions (already included).

Object Awareness (Next Steps)
- For object/text recognition, integrate ML Kit Object Detection and Text Recognition, and combine with ARCore camera frames. Start with on-device models for latency and privacy.

Lip Sync & Emotes (Next Steps)
- Drive blendshapes from phoneme timings (TTS APIs with visemes) or amplitude. Sceneform exposes Filament materials to control morph targets if present in the GLB.

Navigation & Gestures (Next Steps)
- Use anchors and pathfinding on detected planes to simulate walking, or add simple directional pointing using billboarded arrows.

Notes
- Dependencies reference common, recent versions. If Gradle has trouble resolving Sceneform, bump to the latest `com.gorisse.thomas.sceneform:sceneform` and ensure `mavenCentral()` is enabled.

License
- This template is provided as-is for you to build on.

