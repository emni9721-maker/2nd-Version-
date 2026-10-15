package com.example.data

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * FirebaseAuthHelper delegates to AuthenticationManager for backward compatibility.
 */
object FirebaseAuthHelper {
  val auth: FirebaseAuth
    get() = AuthenticationManager.auth

  val currentUser: FirebaseUser?
    get() = AuthenticationManager.currentUser

  fun observeAuthState(): Flow<FirebaseUser?> =
    AuthenticationManager.observeAuthState()

  fun attemptAutoSignIn(
    context: Context,
    credentialManager: CredentialManager,
    onAuthSuccess: (FirebaseUser) -> Unit,
    onUnauthenticated: () -> Unit,
    scope: CoroutineScope
  ) = AuthenticationManager.attemptAutoSignIn(
    context, credentialManager, onAuthSuccess, onUnauthenticated, scope
  )

  fun signInWithGoogle(
    context: Context,
    credentialManager: CredentialManager,
    onAuthSuccess: (FirebaseUser) -> Unit,
    onAuthError: (String) -> Unit,
    onAuthCancelled: () -> Unit,
    scope: CoroutineScope
  ) = AuthenticationManager.signInWithGoogle(
    context, credentialManager, onAuthSuccess, onAuthError, onAuthCancelled, scope
  )

  fun signOut(
    context: Context,
    credentialManager: CredentialManager,
    onSignOutComplete: () -> Unit,
    scope: CoroutineScope
  ) = AuthenticationManager.signOut(
    context, credentialManager, onSignOutComplete, scope
  )
}
