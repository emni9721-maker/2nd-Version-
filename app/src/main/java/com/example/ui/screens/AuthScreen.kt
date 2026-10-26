package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseUser

/**
 * AuthScreen delegates to LoginScreen providing unified authentication.
 */
@Composable
fun AuthScreen(
  onAuthSuccess: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  LoginScreen(
    onAuthSuccess = onAuthSuccess,
    modifier = modifier
  )
}
