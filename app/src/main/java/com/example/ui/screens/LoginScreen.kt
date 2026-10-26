package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ui.components.ForgotPasswordDialog
import com.example.ui.components.ZeroZLogo
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
  onAuthSuccess: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }
  val prefs = remember { context.getSharedPreferences("zero_auth_prefs", Context.MODE_PRIVATE) }

  // Auth Mode: Sign In (Default) vs Sign Up
  var isSignUpMode by remember { mutableStateOf(false) }

  // Sign In inputs (Facebook style: single box for Mobile / Email / Username + Password)
  var signInIdentifier by remember {
    mutableStateOf(prefs.getString("saved_username", null) ?: prefs.getString("saved_contact", null) ?: "")
  }
  var signInPassword by remember { mutableStateOf("") }
  var isSignInPasswordVisible by remember { mutableStateOf(false) }

  // More Options expandable
  var isMoreOptionsExpanded by remember { mutableStateOf(false) }

  // Forgot Password Dialog
  var showForgotPasswordDialog by remember { mutableStateOf(false) }

  // Sign Up fields
  var fullName by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var ageText by remember { mutableStateOf("") }
  var isPhoneSelectedForContact by remember { mutableStateOf(false) } // False = Email, True = Phone
  var contactInput by remember { mutableStateOf("") }
  var signUpPassword by remember { mutableStateOf("") }
  var isSignUpPasswordVisible by remember { mutableStateOf(false) }

  // Status & Loaders
  var isGoogleLoading by remember { mutableStateOf(false) }
  var isSubmitting by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var infoMessage by remember { mutableStateOf<String?>(null) }

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
      // 1. Header Section with uncommon "Z" logo (No lock icon!)
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp)
      ) {
        ZeroZLogo(size = 78.dp, enablePulse = true)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "ZERO",
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 2.5.sp,
          color = TextPrimary
        )

        Text(
          text = if (isSignUpMode) "First-Time Registration & Vault Creation" else "End-to-End Encrypted Cloud Storage",
          fontSize = 13.sp,
          color = CyanAccent,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(top = 2.dp)
        )

        Text(
          text = "Secure hardware-isolated storage with multi-channel authentication.",
          fontSize = 11.sp,
          color = TextSecondary,
          textAlign = TextAlign.Center,
          lineHeight = 16.sp,
          modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 2. Auth Mode Switcher (Sign In first, Sign Up second)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianSurfaceVariant)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSignUpMode) EmeraldPrimary else Color.Transparent)
            .clickable {
              isSignUpMode = false
              errorMessage = null
            }
            .padding(vertical = 10.dp)
            .testTag("tab_signin_mode"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Log In",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (!isSignUpMode) Color.Black else TextSecondary
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSignUpMode) EmeraldPrimary else Color.Transparent)
            .clickable {
              isSignUpMode = true
              errorMessage = null
            }
            .padding(vertical = 10.dp)
            .testTag("tab_signup_mode"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Create Account",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSignUpMode) Color.Black else TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Main Form Card
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
          if (!isSignUpMode) {
            // =========================================================
            // SIGN IN FORM: Mobile / Email / Username + Password
            // =========================================================
            Text(
              text = "Log In to ZERO",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Enter your mobile number, email, or username and password to log in.",
              fontSize = 12.sp,
              color = TextSecondary,
              lineHeight = 16.sp
            )

            // 1. Mobile Number OR Email OR Username (All in one single input box!)
            OutlinedTextField(
              value = signInIdentifier,
              onValueChange = {
                signInIdentifier = it
                errorMessage = null
              },
              label = { Text("Mobile number, email or username", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("Phone, email or @username", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(
                  imageVector = if (signInIdentifier.contains("@")) Icons.Default.AlternateEmail
                  else if (signInIdentifier.any { it.isDigit() }) Icons.Default.Phone
                  else Icons.Default.Person,
                  contentDescription = "Identifier",
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(18.dp)
                )
              },
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signin_identifier_input")
            )

            // 2. Password Field
            OutlinedTextField(
              value = signInPassword,
              onValueChange = {
                signInPassword = it
                errorMessage = null
              },
              label = { Text("Password", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("Enter your password", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Key,
                  contentDescription = "Password",
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(18.dp)
                )
              },
              trailingIcon = {
                IconButton(onClick = { isSignInPasswordVisible = !isSignInPasswordVisible }) {
                  Icon(
                    imageVector = if (isSignInPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                }
              },
              visualTransformation = if (isSignInPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signin_password_input")
            )

            // 3. Primary "Log In" Button
            Button(
              onClick = {
                val id = signInIdentifier.trim()
                val pass = signInPassword.trim()
                if (id.isBlank()) {
                  errorMessage = "Please enter your mobile number, email, or username."
                  return@Button
                }
                if (pass.isBlank()) {
                  errorMessage = "Please enter your password."
                  return@Button
                }

                isSubmitting = true
                errorMessage = null

                // Verify credentials against saved credentials or demo profile
                val isValid = AuthenticationManager.verifyCredentials(context, id, pass)
                if (isValid) {
                  isSubmitting = false
                  AuthenticationManager.setSessionActive(context, true, id)
                  onAuthSuccess(id)
                } else {
                  isSubmitting = false
                  errorMessage = "Incorrect identifier or password. Please verify or use Forgotten Password."
                }
              },
              enabled = !isSubmitting && !isGoogleLoading,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("signin_submit_button")
            ) {
              if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
              } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Log In", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
              }
            }

            // 4. Facebook-style "Forgotten Password?" Link
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
              horizontalArrangement = Arrangement.Center
            ) {
              Text(
                text = "Forgotten Password?",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent,
                modifier = Modifier
                  .clickable {
                    showForgotPasswordDialog = true
                    errorMessage = null
                  }
                  .padding(vertical = 4.dp, horizontal = 8.dp)
                  .testTag("forgot_password_button")
              )
            }

            // 5. Divider with "OR"
            Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianBorder)
              Text(
                text = "OR",
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
              )
              HorizontalDivider(modifier = Modifier.weight(1f), color = ObsidianBorder)
            }

            // 6. Google 1-Tap Sign In
            OutlinedButton(
              onClick = {
                isGoogleLoading = true
                errorMessage = null
                infoMessage = null
                AuthenticationManager.signInWithGoogle(
                  context = context,
                  credentialManager = credentialManager,
                  onAuthSuccess = { user ->
                    isGoogleLoading = false
                    prefs.edit().putBoolean("has_registered_account", true).apply()
                    val identifier = user.email ?: user.displayName ?: user.uid
                    AuthenticationManager.setSessionActive(context, true, identifier)
                    onAuthSuccess(identifier)
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
              enabled = !isGoogleLoading && !isSubmitting,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("google_sign_in_button")
            ) {
              if (isGoogleLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = EmeraldPrimary, strokeWidth = 2.dp)
              } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Sync, contentDescription = "Google", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Continue with Google", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
              }
            }

            // 7. Expandable "More Options" Section
            Card(
              colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceVariant),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isMoreOptionsExpanded = !isMoreOptionsExpanded }
                .testTag("more_options_card")
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                    Text("More Options", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                  }
                  Icon(
                    imageVector = if (isMoreOptionsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                }

                AnimatedVisibility(visible = isMoreOptionsExpanded) {
                  Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

                    // Option A: Forgotten Password
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showForgotPasswordDialog = true }
                        .padding(vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Icon(Icons.Default.LockReset, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                      Text("Forgotten Password & Account Recovery", fontSize = 12.sp, color = TextPrimary)
                    }

                    // Option B: Search Account
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showForgotPasswordDialog = true }
                        .padding(vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Icon(Icons.Default.Search, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                      Text("Find Your ZERO Account by Phone or Email", fontSize = 12.sp, color = TextPrimary)
                    }

                    // Option C: Create Account (Sign Up switch)
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isSignUpMode = true }
                        .padding(vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Icon(Icons.Default.PersonAdd, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                      Text("Create New Account", fontSize = 12.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          } else {
            // =========================================================
            // SIGN UP FORM (Name, Username, Age, Phone / Email, Password)
            // =========================================================
            Text(
              text = "Create Your Account",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Please enter your details to set up your encrypted vault account.",
              fontSize = 12.sp,
              color = TextSecondary
            )

            // 1. Full Name
            OutlinedTextField(
              value = fullName,
              onValueChange = {
                fullName = it
                errorMessage = null
              },
              label = { Text("Full Name", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("e.g. Tanvir Ahmed", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Name", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_name_input")
            )

            // 2. Username
            OutlinedTextField(
              value = username,
              onValueChange = {
                username = it
                errorMessage = null
              },
              label = { Text("Username", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("e.g. @tanvir_sec", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = "Username", tint = CyanAccent, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_username_input")
            )

            // 3. Age
            OutlinedTextField(
              value = ageText,
              onValueChange = {
                if (it.all { char -> char.isDigit() } && it.length <= 3) {
                  ageText = it
                  errorMessage = null
                }
              },
              label = { Text("Age", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("e.g. 24", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(imageVector = Icons.Default.Cake, contentDescription = "Age", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_age_input")
            )

            // 4. Contact Toggle (Email OR Phone number)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = "Contact Method (Choose Email or Phone)",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
              )
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(ObsidianSurfaceVariant)
                  .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isPhoneSelectedForContact) EmeraldPrimary.copy(alpha = 0.25f) else Color.Transparent)
                    .clickable { isPhoneSelectedForContact = false }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = if (!isPhoneSelectedForContact) EmeraldPrimary else TextMuted, modifier = Modifier.size(14.dp))
                    Text("Email Address", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (!isPhoneSelectedForContact) TextPrimary else TextMuted)
                  }
                }

                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isPhoneSelectedForContact) EmeraldPrimary.copy(alpha = 0.25f) else Color.Transparent)
                    .clickable { isPhoneSelectedForContact = true }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = if (isPhoneSelectedForContact) EmeraldPrimary else TextMuted, modifier = Modifier.size(14.dp))
                    Text("Phone Number", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isPhoneSelectedForContact) TextPrimary else TextMuted)
                  }
                }
              }

              OutlinedTextField(
                value = contactInput,
                onValueChange = {
                  contactInput = it
                  errorMessage = null
                },
                label = { Text(if (isPhoneSelectedForContact) "Mobile Phone Number" else "Email Address", fontSize = 12.sp, color = TextMuted) },
                placeholder = { Text(if (isPhoneSelectedForContact) "+880 1712-345678" else "user@example.com", fontSize = 12.sp, color = TextMuted) },
                leadingIcon = {
                  Icon(
                    imageVector = if (isPhoneSelectedForContact) Icons.Default.Phone else Icons.Default.Email,
                    contentDescription = "Contact",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                  )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = if (isPhoneSelectedForContact) KeyboardType.Phone else KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = EmeraldPrimary,
                  unfocusedBorderColor = ObsidianBorder
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_contact_input")
              )
            }

            // 5. Password Field
            OutlinedTextField(
              value = signUpPassword,
              onValueChange = {
                signUpPassword = it
                errorMessage = null
              },
              label = { Text("Password", fontSize = 12.sp, color = TextMuted) },
              placeholder = { Text("At least 6 characters", fontSize = 12.sp, color = TextMuted) },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Key,
                  contentDescription = "Password",
                  tint = EmeraldPrimary,
                  modifier = Modifier.size(18.dp)
                )
              },
              trailingIcon = {
                IconButton(onClick = { isSignUpPasswordVisible = !isSignUpPasswordVisible }) {
                  Icon(
                    imageVector = if (isSignUpPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                }
              },
              visualTransformation = if (isSignUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = ObsidianBorder
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_password_input")
            )

            // Submit Sign Up
            Button(
              onClick = {
                val age = ageText.toIntOrNull() ?: 0
                if (fullName.isBlank()) {
                  errorMessage = "Please enter your name."
                  return@Button
                }
                if (username.isBlank() || username == "@") {
                  errorMessage = "Please enter a username."
                  return@Button
                }
                if (age <= 0) {
                  errorMessage = "Please enter a valid age."
                  return@Button
                }
                if (contactInput.isBlank()) {
                  errorMessage = "Please enter your contact phone or email."
                  return@Button
                }
                if (signUpPassword.length < 6) {
                  errorMessage = "Password must be at least 6 characters."
                  return@Button
                }

                isSubmitting = true
                errorMessage = null
                AuthenticationManager.signUpWithDetails(
                  context = context,
                  credentialManager = credentialManager,
                  name = fullName.trim(),
                  username = username.trim(),
                  age = age,
                  contactEmailOrPhone = contactInput.trim(),
                  password = signUpPassword,
                  onSuccess = { user ->
                    isSubmitting = false
                    prefs.edit().putBoolean("has_registered_account", true).apply()
                    val identifier = user.email ?: username.trim()
                    AuthenticationManager.setSessionActive(context, true, identifier)
                    onAuthSuccess(identifier)
                  },
                  onError = { err ->
                    isSubmitting = false
                    errorMessage = err
                  },
                  onCancelled = {
                    isSubmitting = false
                  },
                  scope = scope
                )
              },
              enabled = !isSubmitting,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("signup_submit_button")
            ) {
              if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
              } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Create Account & Sign In with Google", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
              }
            }

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isSignUpMode = false }
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Already have an account? ",
                fontSize = 12.sp,
                color = TextSecondary
              )
              Text(
                text = "Log In",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
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
                Text(text = msg, color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.Medium)
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
                Text(text = msg, color = CyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Footer
      Text(
        text = "ZERO • Cryptographic E2EE Storage & Hardware Keystore",
        fontSize = 10.sp,
        color = TextMuted,
        textAlign = TextAlign.Center
      )
    }

    // Facebook-style Forgot Password Dialog
    AnimatedVisibility(
      visible = showForgotPasswordDialog,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      ForgotPasswordDialog(
        initialQuery = signInIdentifier,
        onDismiss = { showForgotPasswordDialog = false },
        onPasswordResetSuccess = { newPass, identifier ->
          showForgotPasswordDialog = false
          signInPassword = newPass
          val id = identifier.ifBlank { signInIdentifier }
          if (id.isNotBlank()) {
            signInIdentifier = id
          }
          infoMessage = "Password successfully reset! Logging you in..."
          AuthenticationManager.setSessionActive(context, true, id)
          onAuthSuccess(id)
        }
      )
    }
  }
}
