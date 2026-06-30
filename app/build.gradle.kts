import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

/**
 * Reads a property from local.properties, returning [defaultValue] if the file
 * or property is absent.  Logs a warning at configuration time when the value is
 * empty so developers know the key is missing before they hit a runtime error.
 */
fun getLocalProperty(key: String, defaultValue: String = ""): String {
    val localPropertiesFile = rootProject.file("local.properties")
    if (!localPropertiesFile.exists()) {
        logger.warn("local.properties not found — $key will be empty. " +
                "Copy local.properties.example to local.properties and add your key.")
        return defaultValue
    }
    val props = Properties().apply { load(localPropertiesFile.inputStream()) }
    val value = props.getProperty(key, defaultValue)
    if (value.isBlank()) {
        logger.warn("$key is empty in local.properties — Gemini API calls will fail. " +
                "See local.properties.example for setup instructions.")
    }
    return value
}

android {
    namespace = "com.gersseba.garden"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.gersseba.garden"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                "\"${getLocalProperty("GEMINI_API_KEY")}\""
            )
        }
        release {
            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                "\"${getLocalProperty("GEMINI_API_KEY")}\""
            )
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}