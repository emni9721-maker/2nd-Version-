package com.example.data

object DummyData {
  val initialVaultItems = listOf(
    VaultItem(
      id = "v-1",
      title = "Cyberpunk Neo-Tokyo Skyline.raw",
      type = MediaType.IMAGE,
      mediaUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1200&q=80",
      album = "Camera",
      dateFormatted = "Today, 11:42 AM",
      originalSizeBytes = 18_450_000L, // 17.6 MB
      compressedSizeBytes = 2_450_000L,  // 2.3 MB (86% saved)
      encryptionAlgorithm = "AES-256-GCM (Hardware Keystore)",
      keyAlias = "zero_master_sec01",
      sha256Hash = "8a2f...91e3",
      isFavorite = true,
      exifData = ExifData(resolution = "4032 x 3024 (12.2 MP)", iso = "ISO 64", aperture = "f/1.8", shutterSpeed = "1/250s")
    ),
    VaultItem(
      id = "v-2",
      title = "Confidential Financial Roadmap 2027.png",
      type = MediaType.IMAGE,
      mediaUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=1200&q=80",
      album = "Secret Vault",
      dateFormatted = "Yesterday, 4:15 PM",
      originalSizeBytes = 14_200_000L,
      compressedSizeBytes = 1_850_000L, // 87% saved
      encryptionAlgorithm = "AES-256-GCM + ChaCha20",
      keyAlias = "zero_fin_vault_99",
      sha256Hash = "e3b0...4f18",
      isFavorite = true,
      exifData = ExifData(resolution = "3840 x 2160 (4K UHD)", iso = "ISO 100", aperture = "f/2.0", shutterSpeed = "1/60s")
    ),
    VaultItem(
      id = "v-3",
      title = "Hypercar Mountain Pass 4K.mp4",
      type = MediaType.VIDEO,
      mediaUrl = "https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=1200&q=80",
      album = "Camera",
      dateFormatted = "Oct 10, 2026",
      originalSizeBytes = 148_000_000L, // 141 MB
      compressedSizeBytes = 24_500_000L,  // 23.3 MB (83% saved)
      videoDurationSec = 42,
      encryptionAlgorithm = "AES-256-GCM (Hardware Keystore)",
      keyAlias = "zero_stream_video_81",
      sha256Hash = "7c1b...29a0",
      isFavorite = false,
      exifData = ExifData(resolution = "3840 x 2160 @ 60fps", iso = "ISO 200", aperture = "f/1.9", shutterSpeed = "1/120s")
    ),
    VaultItem(
      id = "v-4",
      title = "Minimalist Obsidian Architecture.jpg",
      type = MediaType.IMAGE,
      mediaUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
      album = "Wallpapers",
      dateFormatted = "Oct 08, 2026",
      originalSizeBytes = 9_600_000L,
      compressedSizeBytes = 1_400_000L,
      isFavorite = false,
      exifData = ExifData(resolution = "2560 x 1440 QHD", iso = "ISO 50", aperture = "f/2.8", shutterSpeed = "1/500s")
    ),
    VaultItem(
      id = "v-5",
      title = "Biometric Passkey Recovery Seed.png",
      type = MediaType.IMAGE,
      mediaUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
      album = "Secret Vault",
      dateFormatted = "Oct 05, 2026",
      originalSizeBytes = 6_300_000L,
      compressedSizeBytes = 720_000L,
      isFavorite = true,
      exifData = ExifData(resolution = "2048 x 2048", iso = "ISO 100", aperture = "f/2.2", shutterSpeed = "1/100s")
    ),
    VaultItem(
      id = "v-6",
      title = "Drone Flight Over Emerald Fjords.mp4",
      type = MediaType.VIDEO,
      mediaUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80",
      album = "Downloads",
      dateFormatted = "Sep 28, 2026",
      originalSizeBytes = 94_000_000L,
      compressedSizeBytes = 14_200_000L,
      videoDurationSec = 28,
      isFavorite = false
    )
  )

  val initialStories = listOf(
    Story("s-1", "Elena Vance", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80", "2h ago"),
    Story("s-2", "Kaelen Chen", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80", "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?auto=format&fit=crop&w=800&q=80", "4h ago"),
    Story("s-3", "Aria Thorne", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80", "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=800&q=80", "7h ago"),
    Story("s-4", "Devin Zero", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=800&q=80", "12h ago")
  )

  val initialPosts = listOf(
    SocialPost(
      id = "p-1",
      authorName = "Elena Vance",
      authorHandle = "@elena_sec",
      authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
      timestamp = "20 min ago",
      caption = "Just synchronized 128GB of encrypted studio footage straight into my ZERO hardware vault. Zero cloud leaks, zero third-party snooping. Pure obsidian bliss. 🔐⚡️",
      tags = listOf("#ZEROvault", "#EncryptedCloud", "#Android15"),
      mediaUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1200&q=80",
      likesCount = 342,
      isLiked = true,
      commentsCount = 28,
      comments = listOf(
        Comment("c-1", "Marcus Thorne", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80", "The compression pipeline is wild. Saved 80% with no visible artifacting!", "14m ago"),
        Comment("c-2", "Aria", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80", "Is that the new AMOLED pure black mode? Looks gorgeous.", "5m ago")
      ),
      sharesCount = 19
    ),
    SocialPost(
      id = "p-2",
      authorName = "Nova Studio Collective",
      authorHandle = "@novadesign",
      authorAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80",
      timestamp = "2 hours ago",
      caption = "Testing the vertical 4K reels workflow. Seamless hardware decoding and AES-256 integrity tags embedded on every frame! 🎬✨",
      tags = listOf("#MotionDesign", "#Reels4K", "#ObsidianUI"),
      mediaUrl = "https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=1200&q=80",
      isVideo = true,
      likesCount = 1_289,
      isLiked = false,
      commentsCount = 94,
      comments = listOf(
        Comment("c-3", "Liam Ray", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80", "Incredible framerate on mobile!", "1h ago")
      ),
      sharesCount = 67
    )
  )

  val initialReels = listOf(
    Reel(
      id = "r-1",
      creatorName = "CyberPulse Studios",
      creatorHandle = "@cyberpulse",
      creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
      description = "Night cruise through Neo-Shinjuku. Shot in 4K HDR, compressed and encrypted locally in ZERO vault! 🌃🚀",
      audioTrack = "Synthwave Odyssey • Resonance 2026",
      videoThumbnail = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=800&q=80",
      likesCount = 4820,
      isLiked = true,
      commentsCount = 312,
      sharesCount = 890,
      comments = listOf(
        Comment("rc-1", "Kai", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80", "The color grading is insane!", "2h ago"),
        Comment("rc-2", "Zara", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80", "Listening to this on repeat 🔥", "1h ago")
      )
    ),
    Reel(
      id = "r-2",
      creatorName = "Aria Digital",
      creatorHandle = "@ariadigital",
      creatorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
      description = "Fastest way to lock your private files with biometric face unlock and hardware master key. 🛡️🔐",
      audioTrack = "Zero Beats • Encrypted Frequency",
      videoThumbnail = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
      likesCount = 2950,
      isLiked = false,
      commentsCount = 145,
      sharesCount = 420
    ),
    Reel(
      id = "r-3",
      creatorName = "Vance Speed Lab",
      creatorHandle = "@vancespeed",
      creatorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
      description = "Full throttle through the coastal cliffs. 4K 60fps stabilized recording. 🏎️💨",
      audioTrack = "Apex Twin • Turbocharger Mix",
      videoThumbnail = "https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=800&q=80",
      likesCount = 8120,
      isLiked = true,
      commentsCount = 680,
      sharesCount = 1240
    )
  )

  val initialConversations = listOf(
    Conversation(
      id = "conv-1",
      participantName = "Elena Vance",
      participantHandle = "@elena_sec",
      participantAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
      isOnline = true,
      lastMessage = "Did you get the encrypted photo from the vault?",
      lastMessageTime = "12:04 PM",
      unreadCount = 2,
      messages = listOf(
        ChatMessage("m-1", "Hey! Have you configured the new Keystore AES-256 master key?", "11:58 AM", isMine = false),
        ChatMessage("m-2", "Yes! Just upgraded. The StrongBox secure enclave integration is super fast.", "12:01 PM", isMine = true),
        ChatMessage("m-3", "Awesome! Did you get the encrypted photo from the vault?", "12:04 PM", isMine = false)
      )
    ),
    Conversation(
      id = "conv-2",
      participantName = "Nova Security Bot",
      participantHandle = "@zerobot",
      participantAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=300&q=80",
      isOnline = true,
      lastMessage = "Automated AES snapshot #84 created successfully.",
      lastMessageTime = "10:15 AM",
      unreadCount = 0,
      messages = listOf(
        ChatMessage("m-4", "System report: Daily database snapshot completed. Integrity checksum verified (SHA-256: 8f4e2c...b19d).", "10:15 AM", isMine = false)
      )
    )
  )

  val storageCategories = listOf(
    StorageCategory("Photos (Encrypted RAW & JPG)", 8.4, 0.45f),
    StorageCategory("4K / High-Bitrate Videos", 6.8, 0.37f),
    StorageCategory("Encrypted Documents & Passkeys", 2.1, 0.11f),
    StorageCategory("System Backup Snapshots", 1.1, 0.07f)
  )

  val cloudPlans = listOf(
    CloudPlan(
      id = "tier-starter",
      name = "Starter Vault",
      capacityGb = 50,
      priceMonthly = "Free",
      isCurrent = true,
      badge = "Current Active",
      features = listOf("50 GB Encrypted Storage", "Hardware AES-256 GCM", "Biometric Lock", "Standard Support")
    ),
    CloudPlan(
      id = "tier-pro",
      name = "Pro Vault",
      capacityGb = 2000,
      priceMonthly = "$4.99/mo",
      isCurrent = false,
      badge = "Most Popular",
      features = listOf("2 TB (2,000 GB) Vault", "Zero-Knowledge Encryption", "4K Reels Streaming", "Instant Recovery Snapshots", "Priority CDN Sync")
    ),
    CloudPlan(
      id = "tier-studio",
      name = "Studio Master",
      capacityGb = 10000,
      priceMonthly = "$12.99/mo",
      isCurrent = false,
      features = listOf("10 TB Studio Storage", "Multi-Seat Shared Vaults", "Lossless RAW Archiving", "Custom Decoy Vaults", "Dedicated HSM Enclave")
    ),
    CloudPlan(
      id = "tier-enterprise",
      name = "Infinite Enterprise",
      capacityGb = 100000,
      priceMonthly = "$24.99/mo",
      isCurrent = false,
      badge = "Unlimited Power",
      features = listOf("100 TB Massive Cloud", "Cold Storage Offline Mirrors", "Enterprise Compliance Audit", "24/7 VIP White-Glove Support")
    )
  )

  val initialSnapshots = listOf(
    BackupSnapshot("snap-1", "Full Vault Snapshot #84", "Oct 12, 2026 10:15 AM", 18.4, "8f4e2c7a...901e"),
    BackupSnapshot("snap-2", "Pre-Upgrade Snapshot #83", "Oct 09, 2026 08:30 PM", 16.2, "d41d8cd9...388a"),
    BackupSnapshot("snap-3", "Biometric Seed Backup #82", "Oct 01, 2026 02:11 PM", 14.8, "e2fc714c...a90b")
  )
}
