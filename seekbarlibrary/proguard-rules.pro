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

-ignorewarnings

-keep class com.zlm.libs.widget.CustomSeekBar
-keepclassmembers class com.zlm.libs.widget.CustomSeekBar {
   public *;
}
-keep class com.zlm.libs.widget.MusicSeekBar
-keepclassmembers class com.zlm.libs.widget.MusicSeekBar {
   public *;
}

-keep public interface class com.zlm.libs.widget.CustomSeekBar$OnChangeListener{ *; }
-keep public interface class com.zlm.libs.widget.MusicSeekBar$OnMusicListener{ *; }