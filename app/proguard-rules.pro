# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Ensure WebView interoperability remains functional
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Protect WorkManager and SyncWorker from obfuscation
-keep class androidx.work.** { *; }
-keep class org.animalwellnessaction.app.SyncWorker { *; }

# Prevent R8 from removing vital parts of the app structure
-keep class org.animalwellnessaction.app.MainActivity { *; }
-keep class org.animalwellnessaction.app.AWAApplication { *; }
-keep class org.animalwellnessaction.app.OfflineCache { *; }
