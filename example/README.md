# printer_controller_pr3_example

Demonstrates how to use the printer_controller_pr3 plugin.

## Getting Started

This flutter plugin is developed for accessing command methods of honeywell intermec printers from their android aar library. Grants access to the printer from pure flutter applications without opening any native android activity.

So much thanks to github.com/Phincode as i got a lot of help from his github.com/Phincode/honnywellintermecpr3 project in the development process.

## Setup

There are few steps to implement before using the methods:

### Important Note

If you want to use this on a regular android device, you should load the line print service apk first.

You can download it from (drive.google.com/file/d/1pT3PYhWltPql44V0YFpJeuZyPkY20q3Y/view?usp=sharing)

### Step 1

Add a new folder named 'libs' to your root android folder with the contents in (github.com/FatihGeylan/printer_controller_pr3/tree/main/example/android/libs)

### Step 2

Add ('include ':libs') to android/settings.gradle

### Step 3

Update your app level AndroidManifest.xml by adding (xmlns:tools="http://schemas.android.com/tools") to your manifest tag and (tools:replace="android:label") to your application tag
