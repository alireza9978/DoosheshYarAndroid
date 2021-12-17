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
-renamesourcefileattribute SourceFile

#-injars      bin/
#-injars      bin/resources.ap_
#-libraryjars      libs/commons-math3-3.6.1.jar
#-libraryjars      libs/aa-poi-ooxml-schemas-3.10-reduced-more-0.1.5.jar
#-libraryjars      libs/aa-poi-3.10-min-0.1.5.jar
#-outjars     bin/application.apk
-libraryjars D:/idea/SDK/temp/platforms/android-31/android.jar
#/usr/local/android-sdk/platforms/android-28/android.jar

-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-keep class javax.** { *; }
-keep class org.apache.** { *; }
-keep class twitter4j.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
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

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}


-keep class org.openxmlformats.** { *; }
-keep class schemaorg_apache_xmlbeans.* { *; }
-keep class schemasMicrosoftComOfficeExcel.* { *; }
-keep class schemasMicrosoftComOfficeOffice.* { *; }
-keep class schemasMicrosoftComOfficePowerpoint.* { *; }
-keep class schemasMicrosoftComOfficeWord.* { *; }
-keep class schemasMicrosoftComVml.* { *; }
-keep class aavax.xml.* { *; }
-keep class org.apache.poi.* { *; }
-keep class org.apache.xmlbeans.* { *; }
-keep class org.dom4j.* { *; }
-keep class org.w3c.dom.* { *; }
-keep class repackage.* { *; }
-keep class schemaorg_apache_xmlbeans.* { *; }