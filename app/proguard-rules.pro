
# Preserve classes used for JSON serialization and Room entities
-keep class com.kevinduran.myshop.domain.models.** { *; }
-keep class com.kevinduran.myshop.infrastructure.model.** { *; }
-keep class com.kevinduran.myshop.infrastructure.request.** { *; }
-keep class com.kevinduran.myshop.infrastructure.response.** { *; }

# Keep all classes annotated with @Keep
-keep @androidx.annotation.Keep class * { *; }

# Hilt and dependency injection generated classes
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.internal.**

# Gson reflection support
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Firebase and Google services
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Kotlin coroutines
-dontwarn kotlinx.coroutines.**

# Lottie
-keep class com.airbnb.lottie.** { *; }
