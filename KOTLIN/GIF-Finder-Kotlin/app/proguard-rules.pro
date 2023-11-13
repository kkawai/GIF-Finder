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

-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient
-dontwarn kotlinx.parcelize.Parcelize
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

-keep class com.giphy.** { *; }
-keep interface com.giphy.** { *; }

-keep class com.fasterxml.** { *; }
-keep interface com.fasterxml.** { *; }

-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }

-keep class com.jakewharton.** { *; }
-keep interface com.jakewharton.** { *; }

-keep class com.github.bumptech.** { *; }
-keep interface com.github.bumptech.** { *; }

