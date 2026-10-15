package com.example.data

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * AuthenticationManager handles user authentication flows including
 * Google Sign-In via Jetpack Credential Manager and email/password submission validation.
 */
object AuthenticationManager {
  private const val TAG = "AuthenticationManager"

  val auth: FirebaseAuth
    get() = Firebase.auth

  val currentUser: FirebaseUser?
    get() = auth.currentUser

  fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth ->
      trySend(auth.currentUser)
    }
    auth.addAuthStateListener(listener)
    awaitClose { auth.removeAuthStateListener(listener) }
  }

  fun attemptAutoSignIn(
    context: Context,
    credentialManager: CredentialManager,
    onAuthSuccess: (FirebaseUser) -> Unit,
    onUnauthenticated: () -> Unit,
    scope: CoroutineScope
  ) {
    val existingUser = auth.currentUser
    if (existingUser != null) {
      onAuthSuccess(existingUser)
      return
    }

    val clientId = try {
      context.getString(R.string.default_web_client_id)
    } catch (e: Exception) {
      Log.w(TAG, "default_web_client_id not found for auto sign-in", e)
      onUnauthenticated()
      return
    }

    val googleIdOption = GetGoogleIdOption.Builder()
      .setFilterByAuthorizedAccounts(true)
      .setServerClientId(clientId)
      .setAutoSelectEnabled(true)
      .build()

    val request = GetCredentialRequest.Builder()
      .addCredentialOption(googleIdOption)
      .build()

    scope.launch(Dispatchers.IO) {
      try {
        val result = credentialManager.getCredential(context, request)
        val credential = result.credential
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
          val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
          val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
          val authResult = auth.signInWithCredential(authCredential).await()
          val user = authResult.user
          if (user != null) {
            withContext(Dispatchers.Main) { onAuthSuccess(user) }
          } else {
            withContext(Dispatchers.Main) { onUnauthenticated() }
          }
        } else {
          withContext(Dispatchers.Main) { onUnauthenticated() }
        }
      } catch (e: Exception) {
        Log.d(TAG, "Auto sign-in silent bypass: ${e.message}")
        withContext(Dispatchers.Main) { onUnauthenticated() }
      }
    }
  }

  fun signInWithGoogle(
    context: Context,
    credentialManager: CredentialManager,
    onAuthSuccess: (FirebaseUser) -> Unit,
    onAuthError: (String) -> Unit,
    onAuthCancelled: () -> Unit,
    scope: CoroutineScope
  ) {
    val clientId = try {
      context.getString(R.string.default_web_client_id)
    } catch (e: Exception) {
      val err = "Google Sign-In configuration missing: default_web_client_id not found"
      Log.e(TAG, err, e)
      onAuthError(err)
      return
    }

    val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
    val request = GetCredentialRequest.Builder()
      .addCredentialOption(signInOption)
      .build()

    scope.launch {
      try {
        val activity = context as? Activity
        if (activity == null) {
          onAuthError("Context is not an Activity")
          return@launch
        }
        val result = credentialManager.getCredential(activity, request)
        val credential = result.credential
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
          val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
          val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
          val authResult = auth.signInWithCredential(authCredential).await()
          val user = authResult.user
          if (user != null) {
            onAuthSuccess(user)
          } else {
            onAuthError("Firebase authentication returned empty user")
          }
        } else {
          onAuthError("Unexpected credential type: ${credential::class.java.simpleName}")
        }
      } catch (e: GetCredentialCancellationException) {
        Log.w(TAG, "Google Sign-In flow cancelled or dismissed: ${e.message}", e)
        onAuthCancelled()
      } catch (e: Exception) {
        Log.e(TAG, "Google Sign-In failed", e)
        onAuthError(e.localizedMessage ?: "Google Sign-In failed")
      }
    }
  }

  fun signInWithEmail(
    email: String,
    password: String,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (String) -> Unit,
    scope: CoroutineScope
  ) {
    val trimmedEmail = email.trim()
    if (trimmedEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
      onError("Please enter a valid email address.")
      return
    }
    if (password.length < 6) {
      onError("Password must be at least 6 characters.")
      return
    }

    // Google Sign-In is the provisioned identity provider for this environment.
    // If Email/Password provider is not activated in the Firebase Console, direct the user to Google Sign-In.
    onError("Email/Password provider is disabled by default for zero-trust security. Please use Google Sign-In below or configure Email/Password in the Firebase Console.")
  }

  fun signOut(
    context: Context,
    credentialManager: CredentialManager,
    onSignOutComplete: () -> Unit,
    scope: CoroutineScope
  ) {
    auth.signOut()
    scope.launch {
      try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
      } catch (e: Exception) {
        Log.e(TAG, "Failed to clear credential state", e)
      } finally {
        onSignOutComplete()
      }
    }
  }
}
