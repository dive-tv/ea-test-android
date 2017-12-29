# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontnote
-dontpreverify
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontusemixedcaseclassnames
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable, Subscribe
-renamesourcefileattribute SourceFile
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keepclassmembers enum * { *; }

-keep public class * extends android.view.View {
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributeSet);
   public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}
-keep class com.google.code.gson.** {*;}
-dontwarn com.google.code.gson.**
-keep class sdk.client.dive.** {*;}
-dontwarn sdk.client.dive.**
