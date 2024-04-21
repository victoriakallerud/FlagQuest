# Description
The FlagQuest client is programmed using Kotlin. It contains API requests to the backend to retrieve information from the backend. 

# Requirements
IDE Android Studios or other IDE that can build an Android project. Alternatively, the APK file can be downloaded, an be ran according to how it is ran on your device.

# Installation
Clone project
```
git clone
```

# Running the app
1. Make sure you have up-to-date Android SDKs on your device, and note the location of these
2. Open IDE of choice
3. Open the client folder of the project
4. Change the location of your Android SDKs to the correct one by changing the sdk.dir in the local.properties file, for example:
   ```
   sdk.dir=/home/hannalunne/Android/Sdk
   ```
6. Build gradle files (method varies depending on IDE)
   - Identified issue: sometimes Android Studios doesn't detect any gradle files when project is opened. Opening the gradle files manually solved issue for us.
7. Set up Android API 33, for example "Medium Phone API UpsideDownCakePrivacySandbox"
8. Run project on emulator from IDE (in android studios, you can also plug in an Android phone and use that as an emulator) 
