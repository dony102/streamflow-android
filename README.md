# StreamFlow Android App

Android client for StreamFlow - connects to your StreamFlow server running on your PC/laptop.

## 🚀 How to Build APK

### Option 1: Using GitHub Actions (Recommended - No Setup Required!)

1. **Create a GitHub repository**
   - Go to [github.com](https://github.com) and create a new repository
   - Name it `streamflow-android` or anything you like

2. **Upload this folder**
   - Upload all files from this `streamflow-android` folder to your repository
   - Make sure to include the `.github` folder!

3. **Wait for build**
   - Go to the "Actions" tab in your repository
   - The build will start automatically
   - Wait ~5 minutes for it to complete

4. **Download APK**
   - Click on the completed workflow run
   - Scroll down to "Artifacts"
   - Download `StreamFlow-Debug` or `StreamFlow-Release`
   - Extract and install the APK on your Android phone!

### Option 2: Using Android Studio (Manual)

1. Install [Android Studio](https://developer.android.com/studio)
2. Open this folder as a project
3. Click Build → Build Bundle(s) / APK(s) → Build APK(s)
4. Find the APK in `app/build/outputs/apk/`

## 📱 How to Use the App

1. **Start StreamFlow on your PC/Laptop**
   - Run the StreamFlow desktop app (`.exe` file)
   - Note the IP address shown (e.g., `192.168.1.5`)

2. **Connect from Android**
   - Open the StreamFlow app on your phone
   - Enter your PC's IP address (e.g., `192.168.1.5`)
   - Enter port `7575` (default)
   - Tap "Connect"

3. **Requirements**
   - Phone and PC must be on the **same WiFi network**
   - StreamFlow server must be running on PC

## 📁 Project Structure

```
streamflow-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/streamflow/app/
│   │   │   └── MainActivity.java      # Main app logic
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml
│   │   │   └── values/                 # strings, colors, themes
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .github/workflows/build.yml         # Auto-build APK
├── build.gradle
├── settings.gradle
└── gradle/wrapper/
```

## ⚙️ Features

- 🌐 **WebView-based** - Full StreamFlow interface in app form
- 💾 **Saves server IP** - No need to re-enter every time
- 🌙 **Dark theme** - Matches StreamFlow desktop design
- 📱 **Fullscreen mode** - No status/navigation bars
- 🔄 **Auto-reconnect prompt** - Easy retry on connection failure

## 📝 License

MIT License - Same as StreamFlow main project.
