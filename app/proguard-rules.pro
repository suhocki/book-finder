# useless warnings
-dontwarn com.google.auto.**
-dontwarn com.google.common.**
-dontwarn java.nio.**
-dontwarn org.codehaus.**
-dontwarn sun.misc.**
-dontwarn com.squareup.javapoet.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.**
-dontwarn javax.tools.**
-keepattributes *Annotation*

# kotlin
-dontwarn kotlin.reflect.jvm.internal.**

# support libs
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
-keep class android.support.v7.widget.RoundRectDrawable { *; }

# moxy
-dontwarn com.arellomobile.mvp.**

# retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# di
-adaptclassstrings **

#firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.common.internal.** { *; }

# drawable animations
-keep class android.support.v7.graphics.** { *; }

# gson
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# models
-keep class app.suhocki.mybooks.domain.model.** { *; }

# eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
