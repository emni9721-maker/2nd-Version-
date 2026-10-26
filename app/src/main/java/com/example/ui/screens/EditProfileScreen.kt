package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.UserProfile
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  userProfile: UserProfile?,
  currentUser: FirebaseUser?,
  onSaveProfile: (
    name: String,
    bio: String,
    dob: String,
    location: String,
    schoolCollege: String,
    workplace: String,
    context: Context
  ) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  BackHandler {
    onDismiss()
  }

  var displayName by remember {
    mutableStateOf(userProfile?.displayName?.ifBlank { currentUser?.displayName } ?: "Elena Vance")
  }
  var bio by remember {
    mutableStateOf(userProfile?.bio ?: "Zero-knowledge private cloud curator. 100% hardware-encrypted photography & 4K cinematic reels.")
  }
  var dateOfBirth by remember {
    mutableStateOf(userProfile?.dateOfBirth ?: "15 August 2000")
  }
  var location by remember {
    mutableStateOf(userProfile?.location ?: "Dhaka, Bangladesh")
  }
  var schoolCollege by remember {
    mutableStateOf(userProfile?.schoolCollege ?: "Dhaka College / University of Dhaka")
  }
  var workplace by remember {
    mutableStateOf(userProfile?.workplace ?: "Zero Cloud Security Lab")
  }

  val avatarUrl = userProfile?.photoUrl?.ifBlank { currentUser?.photoUrl?.toString() }
    ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Edit Profile",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "প্রোফাইল তথ্য পরিবর্তন করুন",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("edit_profile_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = TextPrimary
            )
          }
        },
        actions = {
          Button(
            onClick = {
              onSaveProfile(
                displayName.trim(),
                bio.trim(),
                dateOfBirth.trim(),
                location.trim(),
                schoolCollege.trim(),
                workplace.trim(),
                context
              )
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .padding(end = 8.dp)
              .testTag("save_profile_button")
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background
        )
      )
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Profile Picture Section (Facebook style)
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Profile Picture",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Edit",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = EmeraldPrimary,
              modifier = Modifier.clickable { /* Photo picker preview */ }
            )
          }

          Box(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.BottomEnd
          ) {
            AsyncImage(
              model = avatarUrl,
              contentDescription = "Profile Photo",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .border(3.dp, EmeraldPrimary, CircleShape)
            )
            Box(
              modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary)
                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Change photo",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      // 2. Cover Photo Section (Facebook style)
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Cover Photo",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Edit",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = EmeraldPrimary,
              modifier = Modifier.clickable { /* Cover picker */ }
            )
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(
                  listOf(Color(0xFF0F1E1B), Color(0xFF0B1726), Color(0xFF1B142E))
                )
              )
              .border(1.dp, CyanAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(16.dp)
              )
              Text("Update Cover Banner", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      // 3. Details & Information Form (Name, DOB, Location, School/College, Workplace, Bio)
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "Personal Information",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "These details will appear on your public profile intro card.",
            fontSize = 11.sp,
            color = TextSecondary
          )

          HorizontalDivider(color = ObsidianBorder)

          // Name
          OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Full Name", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_name_input")
          )

          // Bio
          OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Edit, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyanAccent,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_bio_input")
          )

          // Date of Birth
          OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("e.g. 15 August 2000", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Cake, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_dob_input")
          )

          // Location
          OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location / City", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("e.g. Dhaka, Bangladesh", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyanAccent,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_location_input")
          )

          // School / College
          OutlinedTextField(
            value = schoolCollege,
            onValueChange = { schoolCollege = it },
            label = { Text("School / College / University", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("e.g. Dhaka College / University of Dhaka", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.School, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldPrimary,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_school_input")
          )

          // Workplace
          OutlinedTextField(
            value = workplace,
            onValueChange = { workplace = it },
            label = { Text("Workplace / Occupation", fontSize = 12.sp, color = TextMuted) },
            placeholder = { Text("e.g. Zero Cloud Security Lab", fontSize = 12.sp, color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Business, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyanAccent,
              unfocusedBorderColor = ObsidianBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_workplace_input")
          )
        }
      }

      // Save Button at bottom
      Button(
        onClick = {
          onSaveProfile(
            displayName.trim(),
            bio.trim(),
            dateOfBirth.trim(),
            location.trim(),
            schoolCollege.trim(),
            workplace.trim(),
            context
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("save_profile_bottom_button")
      ) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = Color.Black,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Save Profile Changes / সংরক্ষণ করুন",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
