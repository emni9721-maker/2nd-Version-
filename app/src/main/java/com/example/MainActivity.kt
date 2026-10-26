package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.components.CloudStorageViewDialog
import com.example.ui.components.KotlinHubDialog
import com.example.ui.components.MediaDetailDialog
import com.example.ui.components.MessengerSheet
import com.example.ui.components.StoryViewerModal
import com.example.ui.components.UploadModal
import com.example.ui.components.ZeroBottomNav
import com.example.ui.components.ZeroTopBar
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PostsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VaultScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseDanger
import com.example.ui.theme.ZEROTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: MainViewModel = viewModel()
      val context = LocalContext.current

      LaunchedEffect(Unit) {
        viewModel.checkInitialSession(context)
        viewModel.initFirebase(context)
      }

      val isAmoledMode by viewModel.isAmoledMode.collectAsState()

      ZEROTheme(isAmoledMode = isAmoledMode) {
        ZeroAppContent(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ZeroAppContent(viewModel: MainViewModel) {
  val context = LocalContext.current
  val isSessionActive by viewModel.isSessionActive.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val isAmoledMode by viewModel.isAmoledMode.collectAsState()

  // Auth Gate: Persistent session check - user remains logged in until explicit logout in Settings
  if (currentUser == null && !isSessionActive) {
    LoginScreen(
      onAuthSuccess = { identifier ->
        viewModel.onSessionLogin(context, identifier)
      }
    )
    return
  }

  // Vault states
  val userProfile by viewModel.userProfile.collectAsState()
  val isVaultUnlocked by viewModel.isVaultUnlocked.collectAsState()
  val isDecoyVault by viewModel.isDecoyVault.collectAsState()
  val pinInput by viewModel.pinInput.collectAsState()
  val pinError by viewModel.pinError.collectAsState()
  val selectedVaultAlbum by viewModel.selectedVaultAlbum.collectAsState()
  val vaultItems by viewModel.vaultItems.collectAsState()
  val selectedMediaDetail by viewModel.selectedMediaDetail.collectAsState()

  // Social & Reels states
  val posts by viewModel.posts.collectAsState()
  val stories by viewModel.stories.collectAsState()
  val activeStory by viewModel.activeStory.collectAsState()
  val reels by viewModel.reels.collectAsState()
  val activeReelIndex by viewModel.activeReelIndex.collectAsState()
  val isReelsCommentsOpen by viewModel.isReelsCommentOpen.collectAsState()

  // Messenger states
  val conversations by viewModel.conversations.collectAsState()
  val activeConversation by viewModel.activeConversation.collectAsState()
  val isMessengerOpen by viewModel.isMessengerOpen.collectAsState()

  // Storage & Cloud
  val storageCategories by viewModel.storageCategories.collectAsState()
  val cloudPlans by viewModel.cloudPlans.collectAsState()
  val snapshots by viewModel.snapshots.collectAsState()

  // Google Photos-style Automatic Backup
  val isAutoBackupEnabled by viewModel.isAutoBackupEnabled.collectAsState()
  val backupStatus by viewModel.backupStatus.collectAsState()
  val lastBackupTime by viewModel.lastBackupTime.collectAsState()
  val backupQuality by viewModel.backupQuality.collectAsState()
  val backupWifiOnly by viewModel.backupWifiOnly.collectAsState()
  val backupFolders by viewModel.backupFolders.collectAsState()
  val isBackingUp by viewModel.isBackingUp.collectAsState()
  val backupProgress by viewModel.backupProgress.collectAsState()

  // Modals & Storage View
  val isUploadModalOpen by viewModel.isUploadModalOpen.collectAsState()
  val uploadDraft by viewModel.uploadDraft.collectAsState()
  val isKotlinHubOpen by viewModel.isKotlinHubOpen.collectAsState()
  val toastNotification by viewModel.toastNotification.collectAsState()
  var isCloudStorageViewOpen by remember { mutableStateOf(false) }
  var isEditProfileOpen by remember { mutableStateOf(false) }

  // Notifications state
  val notifications by viewModel.notifications.collectAsState()
  val selectedLanguage by viewModel.selectedLanguage.collectAsState()

  val unreadMessagesCount = conversations.sumOf { it.unreadCount }

  // System Back Navigation Handling
  BackHandler(enabled = currentTab != AppTab.FEED) {
    if (currentTab == AppTab.VAULT) {
      viewModel.setTab(AppTab.SETTINGS)
    } else {
      viewModel.setTab(AppTab.FEED)
    }
  }

  // Toast Banner Auto Dismiss
  LaunchedEffect(toastNotification?.id) {
    if (toastNotification != null) {
      delay(3000)
      viewModel.clearToast()
    }
  }

  Scaffold(
    topBar = {
      if (currentTab != AppTab.REELS && currentTab != AppTab.VAULT && currentTab != AppTab.CHAT) {
        ZeroTopBar(
          onOpenSettings = { viewModel.setTab(AppTab.SETTINGS) },
          onBackToFeed = { viewModel.setTab(AppTab.FEED) },
          showBackButton = currentTab != AppTab.FEED
        )
      }
    },
    bottomBar = {
      if (currentTab == AppTab.FEED) {
        ZeroBottomNav(
          currentTab = currentTab,
          onSelectTab = { viewModel.setTab(it) },
          unreadMessagesCount = unreadMessagesCount,
          unreadNotificationsCount = notifications.count { !it.isRead }
        )
      }
    },
    modifier = Modifier
      .fillMaxSize()
      .testTag("zero_main_scaffold")
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        AppTab.FEED -> {
          HomeScreen(
            posts = posts,
            stories = stories,
            onOpenStory = { viewModel.openStory(it) },
            onToggleLike = { viewModel.togglePostLike(it) },
            onAddComment = { postId, text -> viewModel.addCommentToPost(postId, text) },
            onOpenUpload = { viewModel.openUploadModal() }
          )
        }
        AppTab.POSTS -> {
          PostsScreen(
            posts = posts,
            onToggleLike = { viewModel.togglePostLike(it) },
            onAddComment = { postId, text -> viewModel.addCommentToPost(postId, text) },
            onBackToFeed = { viewModel.setTab(AppTab.FEED) }
          )
        }
        AppTab.CHAT -> {
          ChatScreen(
            conversations = conversations,
            activeConversation = activeConversation,
            onSelectConversation = { viewModel.selectConversation(it) },
            onSendMessage = { convId, text -> viewModel.sendMessage(convId, text) },
            onSendFileMessage = { convId, fileName, fileSize, ext ->
              viewModel.sendFileMessage(convId, fileName, fileSize, ext)
            },
            onBackToFeed = { viewModel.setTab(AppTab.FEED) }
          )
        }
        AppTab.NOTIFICATIONS -> {
          NotificationsScreen(
            notifications = notifications,
            onBackToFeed = { viewModel.setTab(AppTab.FEED) },
            onClearAll = { viewModel.clearAllNotifications() }
          )
        }
        AppTab.VAULT -> {
          VaultScreen(
            isUnlocked = isVaultUnlocked,
            isDecoyVault = isDecoyVault,
            pinInput = pinInput,
            pinError = pinError,
            selectedAlbum = selectedVaultAlbum,
            vaultItems = vaultItems,
            onPinDigit = { viewModel.onPinDigit(it) },
            onPinDelete = { viewModel.onPinDelete() },
            onUnlockBiometric = { viewModel.unlockWithBiometrics() },
            onLockVault = { viewModel.lockVault() },
            onSelectAlbum = { viewModel.setVaultAlbum(it) },
            onOpenDetail = { viewModel.openMediaDetail(it) },
            onRestoreMedia = { viewModel.restoreMedia(it) },
            onPermanentlyDelete = { viewModel.permanentlyDeleteMedia(it) },
            onBackToSettings = { viewModel.setTab(AppTab.SETTINGS) }
          )
        }
        AppTab.REELS -> {
          ReelsScreen(
            reels = reels,
            activeReelIndex = activeReelIndex,
            isCommentsOpen = isReelsCommentsOpen,
            onSetActiveReel = { viewModel.setActiveReel(it) },
            onToggleLike = { viewModel.toggleReelLike(it) },
            onOpenComments = { viewModel.openReelsComments() },
            onCloseComments = { viewModel.closeReelsComments() },
            onAddComment = { reelId, text -> viewModel.addCommentToReel(reelId, text) },
            onBackToFeed = { viewModel.setTab(AppTab.FEED) }
          )
        }
        AppTab.PROFILE -> {
          ProfileScreen(
            posts = posts,
            vaultItems = vaultItems,
            reels = reels,
            currentUser = currentUser,
            userProfile = userProfile,
            onOpenEditProfile = { isEditProfileOpen = true },
            onOpenReel = { reelIndex ->
              viewModel.setActiveReel(reelIndex)
              viewModel.setTab(AppTab.REELS)
            },
            onOpenUpload = { viewModel.openUploadModal() },
            onBackToFeed = { viewModel.setTab(AppTab.FEED) }
          )
        }
        AppTab.SETTINGS -> {
          SettingsScreen(
            isAmoledMode = isAmoledMode,
            storageCategories = storageCategories,
            cloudPlans = cloudPlans,
            snapshots = snapshots,
            isAutoBackupEnabled = isAutoBackupEnabled,
            backupStatus = backupStatus,
            lastBackupTime = lastBackupTime,
            backupQuality = backupQuality,
            backupWifiOnly = backupWifiOnly,
            backupFolders = backupFolders,
            isBackingUp = isBackingUp,
            backupProgress = backupProgress,
            onToggleAutoBackup = { viewModel.toggleAutoBackup() },
            onTriggerBackupNow = { viewModel.triggerBackupNow() },
            onSetBackupQuality = { viewModel.setBackupQuality(it) },
            onToggleBackupWifiOnly = { viewModel.toggleBackupWifiOnly() },
            onToggleBackupFolder = { viewModel.toggleBackupFolder(it) },
            onOpenCloudStorageView = { isCloudStorageViewOpen = true },
            onOpenVault = { viewModel.setTab(AppTab.VAULT) },
            onToggleAmoled = { viewModel.toggleAmoledMode() },
            onSelectPlan = { viewModel.selectCloudPlan(it) },
            onCreateSnapshot = { viewModel.createSnapshot() },
            onRestoreSnapshot = { viewModel.restoreSnapshot(it) },
            onOpenKotlinHub = { viewModel.openKotlinHub() },
            onBackToFeed = { viewModel.setTab(AppTab.FEED) },
            currentLanguage = selectedLanguage,
            onSelectLanguage = { viewModel.setLanguage(it) },
            userPhone = userProfile?.phoneNumber ?: "+880 1712-345678",
            userPrimaryEmail = userProfile?.email?.ifBlank { currentUser?.email } ?: "emni9721@gmail.com",
            userSecondaryEmail = userProfile?.secondaryEmail ?: "recovery@zero.io",
            onUpdatePhone = { viewModel.updatePhoneNumber(it) },
            onRemovePhone = { viewModel.removePhoneNumber() },
            onUpdatePrimaryEmail = { viewModel.updatePrimaryEmail(it) },
            onUpdateSecondaryEmail = { viewModel.updateRecoveryEmail(it) },
            onRemoveSecondaryEmail = { viewModel.removeRecoveryEmail() },
            onChangePassword = { cur, new, cb -> viewModel.changeMasterPassword(cur, new, cb) },
            onSignOut = { viewModel.signOut(context) }
          )
        }
      }

      // In-App Toast Notification Banner
      AnimatedVisibility(
        visible = toastNotification != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 16.dp, start = 16.dp, end = 16.dp)
      ) {
        toastNotification?.let { toast ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (toast.isError) RoseDanger else EmeraldPrimary)
              .padding(horizontal = 16.dp, vertical = 10.dp)
          ) {
            Text(
              text = toast.message,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
          }
        }
      }

      // Dialog: Fullscreen Media Detail Viewer
      selectedMediaDetail?.let { item ->
        MediaDetailDialog(
          item = item,
          onDismiss = { viewModel.closeMediaDetail() },
          onToggleFavorite = { viewModel.toggleFavorite(item.id) },
          onSoftDelete = { viewModel.softDeleteMedia(item.id) },
          onShareToFeed = { viewModel.shareVaultItemToFeed(item) }
        )
      }

      // Dialog: Smart Upload Modal
      if (isUploadModalOpen) {
        UploadModal(
          draft = uploadDraft,
          onUpdateDraft = { viewModel.updateUploadDraft(it) },
          onSubmit = { viewModel.submitUpload() },
          onDismiss = { viewModel.closeUploadModal() }
        )
      }

      // Dialog: Ephemeral Story Viewer
      activeStory?.let { story ->
        StoryViewerModal(
          story = story,
          onDismiss = { viewModel.closeStory() }
        )
      }

      // Sheet: Encrypted Direct Messenger
      if (isMessengerOpen) {
        MessengerSheet(
          conversations = conversations,
          activeConversation = activeConversation,
          onSelectConversation = { viewModel.selectConversation(it) },
          onSendMessage = { convId, text -> viewModel.sendMessage(convId, text) },
          onDismiss = { viewModel.closeMessenger() }
        )
      }

      // Dialog: Kotlin Code Hub & CI/CD Inspector
      if (isKotlinHubOpen) {
        KotlinHubDialog(
          onDismiss = { viewModel.closeKotlinHub() }
        )
      }

      // Dialog: Cloud Storage View (Automatic Backup items)
      if (isCloudStorageViewOpen) {
        CloudStorageViewDialog(
          onDismiss = { isCloudStorageViewOpen = false }
        )
      }

      // Screen / Modal: Facebook-style Edit Profile
      if (isEditProfileOpen) {
        EditProfileScreen(
          userProfile = userProfile,
          currentUser = currentUser,
          onSaveProfile = { name, bio, dob, loc, school, work, ctx ->
            viewModel.updateFullUserProfile(name, bio, dob, loc, school, work, ctx)
          },
          onDismiss = { isEditProfileOpen = false }
        )
      }
    }
  }
}
