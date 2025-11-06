package io.github.alelk.pws

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.alelk.pws.core.navigation.SharedScreens

@Composable
fun App() {
  MaterialTheme {
    val root = rememberScreen(SharedScreens.Books)
    Navigator(root)
  }
}