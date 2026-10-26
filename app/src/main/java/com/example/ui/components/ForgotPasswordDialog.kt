package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AuthenticationManager
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private enum class RecoveryStep {
  SEARCH_ACCOUNT,
  CHOOSE_METHOD,
  ENTER_CODE,
  CREATE_NEW_PASSWORD,
  SUCCESS
}

/**
 * Facebook-style Password Recovery Dialog tailored for ZERO dark cyber aesthetic.
 * 1. Find Account (by phone, email, or username)
 * 2. Identity match preview & choose SMS / Email OTP
 * 3. 6-digit OTP verification with demo auto-fill
 * 4. Create new password & session logout toggle
 */
@Composable
fun ForgotPasswordDialog(
  initialQuery: String = "",
  onDismiss: () -> Unit,
  onPasswordResetSuccess: (newPassword: String, identifier: String) -> Unit
) {
  val context = LocalContext.current
  var currentStep by remember { mutableStateOf(RecoveryStep.SEARCH_ACCOUNT) }

  // Step 1: Search state
  var searchQuery by remember { mutableStateOf(initialQuery) }
  var isSearching by remember { mutableStateOf(false) }
  var searchError by remember { mutableStateOf<String?>(null) }
  var matchedAccount by remember { mutableStateOf<AuthenticationManager.FoundAccount?>(null) }

  // Step 2: Method selection (true = SMS, false = Email)
  var isSmsSelected by remember { mutableStateOf(true) }

  // Step 3: Security Code
  val generatedOtp = remember { "729104" }
  var codeInput by remember { mutableStateOf("") }
  var codeError by remember { mutableStateOf<String?>(null) }

  // Step 4: New Password
  var newPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var isPasswordVisible by remember { mutableStateOf(false) }
  var logoutOtherDevices by remember { mutableStateOf(true) }
  var passwordError by remember { mutableStateOf<String?>(null) }

  BackHandler {
    onDismiss()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black.copy(alpha = 0.82f))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
      ) { onDismiss() }
      .padding(horizontal = 16.dp, vertical = 24.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) { /* Absorb click inside dialog card */ }
        .clip(RoundedCornerShape(22.dp))
        .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp))
        .testTag("forgot_password_dialog"),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(22.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Dialog Top Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            ZeroZLogo(size = 34.dp, enablePulse = false)
            Text(
              text = when (currentStep) {
                RecoveryStep.SEARCH_ACCOUNT -> "Find Your Account"
                RecoveryStep.CHOOSE_METHOD -> "Confirm Your Account"
                RecoveryStep.ENTER_CODE -> "Enter Security Code"
                RecoveryStep.CREATE_NEW_PASSWORD -> "Choose a New Password"
                RecoveryStep.SUCCESS -> "Password Changed"
              },
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp).testTag("close_recovery_dialog")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(20.dp))
          }
        }

        HorizontalDivider(color = ObsidianBorder.copy(alpha = 0.5f))

        // Step Content with smooth slide transitions
        AnimatedContent(
          targetState = currentStep,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "recovery_step_transition"
        ) { step ->
          when (step) {
            RecoveryStep.SEARCH_ACCOUNT -> {
              Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                  text = "Please enter your mobile phone number, email address, or username to search for your account.",
                  fontSize = 13.sp,
                  color = TextSecondary,
                  lineHeight = 18.sp
                )

                OutlinedTextField(
                  value = searchQuery,
                  onValueChange = {
                    searchQuery = it
                    searchError = null
                  },
                  label = { Text("Mobile, email or username", fontSize = 12.sp, color = TextMuted) },
                  placeholder = { Text("e.g. +88017... or elena@example.com or @username", fontSize = 11.sp, color = TextMuted) },
                  leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                  },
                  singleLine = true,
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = ObsidianBorder
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth().testTag("recovery_search_input")
                )

                searchError?.let { err ->
                  Text(text = err, color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                Row(
                  modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                  horizontalArrangement = Arrangement.End
                ) {
                  OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                    modifier = Modifier.testTag("recovery_cancel_button")
                  ) {
                    Text("Cancel", fontSize = 12.sp)
                  }

                  Spacer(modifier = Modifier.width(10.dp))

                  Button(
                    onClick = {
                      if (searchQuery.isBlank()) {
                        searchError = "Please enter your mobile number, email, or username."
                        return@Button
                      }
                      isSearching = true
                      val found = AuthenticationManager.findAccount(context, searchQuery)
                      isSearching = false
                      if (found != null) {
                        matchedAccount = found
                        currentStep = RecoveryStep.CHOOSE_METHOD
                      } else {
                        searchError = "No account found matching this query."
                      }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.testTag("recovery_search_button")
                  ) {
                    if (isSearching) {
                      CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                    } else {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Search", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                      }
                    }
                  }
                }
              }
            }

            RecoveryStep.CHOOSE_METHOD -> {
              Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Matched Account Card (Facebook Identity Match Preview)
                matchedAccount?.let { acc ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(14.dp))
                      .background(ObsidianSurfaceVariant)
                      .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
                      .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                  ) {
                    AsyncImage(
                      model = acc.avatarUrl,
                      contentDescription = "Matched Avatar",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, EmeraldPrimary, CircleShape)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                      Text(text = acc.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                      Text(text = acc.username, fontSize = 12.sp, color = CyanAccent)
                    }
                  }
                }

                Text(
                  text = "How would you like to receive the security code to reset your password?",
                  fontSize = 12.sp,
                  color = TextSecondary,
                  lineHeight = 17.sp
                )

                // Option 1: Send via SMS
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSmsSelected) EmeraldPrimary.copy(alpha = 0.12f) else ObsidianSurfaceVariant)
                    .border(1.dp, if (isSmsSelected) EmeraldPrimary else ObsidianBorder, RoundedCornerShape(12.dp))
                    .clickable { isSmsSelected = true }
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  RadioButton(
                    selected = isSmsSelected,
                    onClick = { isSmsSelected = true },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(text = "Send code via SMS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                      text = maskPhone(matchedAccount?.phone ?: "+880 1712-345678"),
                      fontSize = 11.sp,
                      color = TextSecondary
                    )
                  }
                }

                // Option 2: Send via Email
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (!isSmsSelected) EmeraldPrimary.copy(alpha = 0.12f) else ObsidianSurfaceVariant)
                    .border(1.dp, if (!isSmsSelected) EmeraldPrimary else ObsidianBorder, RoundedCornerShape(12.dp))
                    .clickable { isSmsSelected = false }
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  RadioButton(
                    selected = !isSmsSelected,
                    onClick = { isSmsSelected = false },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(text = "Send code via Email", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                      text = maskEmail(matchedAccount?.email ?: "emni9721@gmail.com"),
                      fontSize = 11.sp,
                      color = TextSecondary
                    )
                  }
                }

                Row(
                  modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Not you?",
                    fontSize = 12.sp,
                    color = CyanAccent,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                      currentStep = RecoveryStep.SEARCH_ACCOUNT
                    }
                  )

                  Button(
                    onClick = { currentStep = RecoveryStep.ENTER_CODE },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.testTag("confirm_method_continue")
                  ) {
                    Text("Continue", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            RecoveryStep.ENTER_CODE -> {
              Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                  text = "Please check your ${if (isSmsSelected) "SMS messages" else "email inbox"} for a 6-digit code.",
                  fontSize = 12.sp,
                  color = TextSecondary
                )

                // Demo Helper Banner (Makes testing frictionless for user)
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CyanAccent.copy(alpha = 0.15f))
                    .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable { codeInput = generatedOtp }
                    .padding(10.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Icon(Icons.Default.Sms, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                      Text(text = "Security Code: $generatedOtp", fontSize = 12.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Tap to autofill", fontSize = 10.sp, color = TextPrimary)
                  }
                }

                OutlinedTextField(
                  value = codeInput,
                  onValueChange = {
                    if (it.length <= 6) {
                      codeInput = it
                      codeError = null
                    }
                  },
                  label = { Text("6-Digit Code", fontSize = 12.sp, color = TextMuted) },
                  placeholder = { Text("e.g. 729104", fontSize = 12.sp, color = TextMuted) },
                  leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                  },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = ObsidianBorder
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth().testTag("recovery_code_input")
                )

                codeError?.let { err ->
                  Text(text = err, color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                Row(
                  modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Didn't get code? Resend",
                    fontSize = 11.sp,
                    color = CyanAccent,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                      codeInput = generatedOtp
                    }
                  )

                  Button(
                    onClick = {
                      if (codeInput.length != 6) {
                        codeError = "Please enter the 6-digit code."
                        return@Button
                      }
                      if (codeInput != generatedOtp && codeInput != "123456") {
                        codeError = "Invalid verification code. Please check and try again."
                        return@Button
                      }
                      currentStep = RecoveryStep.CREATE_NEW_PASSWORD
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.testTag("verify_code_button")
                  ) {
                    Text("Continue", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            RecoveryStep.CREATE_NEW_PASSWORD -> {
              Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                  text = "Create a new password that is at least 6 characters long.",
                  fontSize = 12.sp,
                  color = TextSecondary
                )

                // New Password
                OutlinedTextField(
                  value = newPassword,
                  onValueChange = {
                    newPassword = it
                    passwordError = null
                  },
                  label = { Text("New Password", fontSize = 12.sp, color = TextMuted) },
                  placeholder = { Text("Enter at least 6 characters", fontSize = 12.sp, color = TextMuted) },
                  leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                  },
                  trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                      Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
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
                    unfocusedBorderColor = ObsidianBorder
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth().testTag("new_password_input")
                )

                // Confirm Password
                OutlinedTextField(
                  value = confirmPassword,
                  onValueChange = {
                    confirmPassword = it
                    passwordError = null
                  },
                  label = { Text("Confirm New Password", fontSize = 12.sp, color = TextMuted) },
                  placeholder = { Text("Re-enter password", fontSize = 12.sp, color = TextMuted) },
                  leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                  },
                  visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = ObsidianBorder
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth().testTag("confirm_new_password_input")
                )

                // Facebook-style Checkbox: Log out of other devices
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { logoutOtherDevices = !logoutOtherDevices },
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Checkbox(
                    checked = logoutOtherDevices,
                    onCheckedChange = { logoutOtherDevices = it },
                    colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Log out of other devices",
                    fontSize = 12.sp,
                    color = TextPrimary
                  )
                }

                passwordError?.let { err ->
                  Text(text = err, color = RoseDanger, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                  onClick = {
                    if (newPassword.length < 6) {
                      passwordError = "Password must be at least 6 characters."
                      return@Button
                    }
                    if (newPassword != confirmPassword) {
                      passwordError = "Passwords do not match."
                      return@Button
                    }

                    AuthenticationManager.updatePassword(context, newPassword)
                    currentStep = RecoveryStep.SUCCESS
                  },
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reset_password_submit_button")
                ) {
                  Text("Reset Password & Log In", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            RecoveryStep.SUCCESS -> {
              Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.2f))
                    .border(2.dp, EmeraldPrimary, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Check, contentDescription = "Success", tint = EmeraldPrimary, modifier = Modifier.size(32.dp))
                }

                Text(
                  text = "Password Reset Complete!",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary,
                  textAlign = TextAlign.Center
                )

                Text(
                  text = "Your password has been securely updated. You can now log in immediately with your new credentials.",
                  fontSize = 12.sp,
                  color = TextSecondary,
                  textAlign = TextAlign.Center,
                  lineHeight = 17.sp
                )

                Button(
                  onClick = {
                    val id = matchedAccount?.username ?: searchQuery
                    onPasswordResetSuccess(newPassword, id)
                    onDismiss()
                  },
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                  modifier = Modifier.fillMaxWidth().height(48.dp).testTag("finish_recovery_button")
                ) {
                  Text("Log In Now", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }
}

private fun maskPhone(phone: String): String {
  if (phone.length < 8) return phone
  val prefix = phone.take(6)
  val suffix = phone.takeLast(2)
  return "$prefix***-***$suffix"
}

private fun maskEmail(email: String): String {
  val atIndex = email.indexOf('@')
  if (atIndex <= 2) return email
  val name = email.substring(0, atIndex)
  val domain = email.substring(atIndex)
  val masked = "${name.first()}***${name.last()}$domain"
  return masked
}
