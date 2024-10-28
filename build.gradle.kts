/*
The plugins won't be applied until you explicitly apply it later in your build script.
This allows you to control when and how the plugin's configuration is executed.
 */
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.jetbrains.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.ktlint) apply false
}