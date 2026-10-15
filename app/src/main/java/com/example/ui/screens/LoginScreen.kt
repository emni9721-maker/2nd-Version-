package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.AuthenticationManager
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
  onAuthSuccess: (FirebaseUser) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var isPasswordVisible by remember { mutableStateOf(false) }
  var isGoogleLoading by remember { mutableStateOf(false) }
  var isEmailLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var infoMessage by remember { mutableStateOf<String?>(null) }

  val pulseTransition = rememberInfiniteTransition(label = "shieldPulse")
  val pulseScale by pulseTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Header Section
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
      ) {
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                listOf(EmeraldPrimary.copy(alpha = 0.25f), Color.Transparent)
              )
            )
            .border(2.dp, EmeraldPrimary, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Zero Shield",
            tint = EmeraldPrimary,
            modifier = Modifier
              .size(38.dp)
              .scale(pulseScale)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "ZERO",
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 2.5.sp,
          color = TextPrimary
        )

        Text(
          text = "Encrypted Cloud Vault & Social Network",
          fontSize = 13.sp,
          color = CyanAccent,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 2.dp)
        )

        Text(
          text = "Hardware-isolated AES-256 GCM storage with cross-platform synchronization.",
          fontSize = 11.sp,
          color = TextSecondary,
          textAlign = TextAlign.Center,
          lineHeight = 16.sp,
          modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Main Login Card
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "Sign In to Your Account",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )

          // 1. Google Sign-In Button (Primary Zero-Trust Provider)
          Button(
            onClick = {
              isGoogleLoading = true
              errorMessage = null
              infoMessage = null
              AuthenticationManager.signInWithGoogle(
                context = context,
                credentialManager = credentialManager,
                onAuthSuccess = { user ->
                  isGoogleLoading = false
                  onAuthSuccess(user)
                },
                onAuthError = { err ->
                  isGoogleLoading = false
                  errorMessage = err
                },
                onAuthCancelled = {
                  isGoogleLoading = false
                },
                scope = scope
              )
            },
            enabled = !isGoogleLoading && !isEmailLoading,
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldPrimary,
              disabledContainerColor = EmeraldDark.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("google_sign_in_button")
          ) {
            if (isGoogleLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.Black,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Connecting Google...",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            } else {
              Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Google Sign In",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Sign in with Google",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }

          // Divider
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            HorizontalDivider(
              modifier = Modifier.weight(1f),
              color = ObsidianBorder
            )
            Text(
              text = "or continue with email",
              fontSize = 11.sp,
              color = TextMuted,
              modifier = Modifier.padding(horizontal = 10.dp)
            )
            HorizontalDivider(
              modifier = Modifier.weight(1f),
              color = ObsidianBorder
            )
          }

          // 2. Email Field
          OutlinedTextField(
            value = email,
            onValueChange = {
              email = it
              errorMessage = null
            },
            label = { Text("Email Address", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("user@example.com", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                tint = if (email.isNotBlank()) EmeraldPrimary else TextMuted,
                modifier = Modifier.size(18.dp)
              )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_email_input")
          )

          // 3. Password Field
          OutlinedTextField(
            value = password,
            onValueChange = {
              password = it
              errorMessage = null
            },
            label = { Text("Master Password", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("••••••••", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password",
                tint = if (password.isNotBlank()) EmeraldPrimary else TextMuted,
                modifier = Modifier.size(18.dp)
              )
            },
            trailingIcon = {
              IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                  imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                  tint = TextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_password_input")
          )

          // Forgot Password Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Text(
              text = "Forgot password?",
              fontSize = 11.sp,
              color = CyanAccent,
              fontWeight = FontWeight.Medium,
              modifier = Modifier
                .clickable {
                  infoMessage = "Password resets are managed via your authenticated Google security account."
                }
                .testTag("forgot_password_button")
            )
          }

          // 4. Submit Email/Password Login
          Button(
            onClick = {
              isEmailLoading = true
              errorMessage = null
              infoMessage = null
              AuthenticationManager.signInWithEmail(
                email = email,
                password = password,
                onSuccess = { user ->
                  isEmailLoading = false
                  onAuthSuccess(user)
                },
                onError = { err ->
                  isEmailLoading = false
                  errorMessage = err
                },
                scope = scope
              )
            },
            enabled = !isGoogleLoading && !isEmailLoading,
            colors = ButtonDefaults.buttonColors(
              containerColor = ObsidianSurfaceVariant,
              contentColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("login_submit_button")
          ) {
            if (isEmailLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = TextPrimary,
                strokeWidth = 2.dp
              )
            } else {
              Text(
                text = "Log In with Email",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Error and Info banners
          AnimatedVisibility(visible = errorMessage != null) {
            errorMessage?.let { msg ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(RoseDanger.copy(alpha = 0.15f))
                  .border(1.dp, RoseDanger.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(10.dp)
              ) {
                Text(
                  text = msg,
                  color = RoseDanger,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  lineHeight = 15.sp
                )
              }
            }
          }

          AnimatedVisibility(visible = infoMessage != null) {
            infoMessage?.let { msg ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(CyanAccent.copy(alpha = 0.15f))
                  .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(10.dp)
              ) {
                Text(
                  text = msg,
                  color = CyanAccent,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  lineHeight = 15.sp
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Footer
      Text(
        text = "ZERO Vault v1.0 • E2EE Hardware Isolated StrongBox",
        fontSize = 10.sp,
        color = TextMuted,
        textAlign = TextAlign.Center
      )
    }
  }
}
