package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HeritageRepository
import com.example.model.AppLanguage
import com.example.model.HeritageSite
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

sealed class AppDestination {
    object Home : AppDestination()
    object Directory : AppDestination()
    object Map : AppDestination()
    object Education : AppDestination()
    object Archive : AppDestination()
    object Admin : AppDestination()
    object Community : AppDestination()
    object Profile : AppDestination()
    data class Detail(val site: HeritageSite) : AppDestination()
    data class Ar(val site: HeritageSite) : AppDestination()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HeritageAppRoot()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeritageAppRoot() {
    val sites by HeritageRepository.sites.collectAsState()
    val archives by HeritageRepository.archives.collectAsState()
    val submissions by HeritageRepository.submissions.collectAsState()
    val quizQuestions by HeritageRepository.quizQuestions.collectAsState()
    val savedIds by HeritageRepository.savedSiteIds.collectAsState()
    val completedArIds by HeritageRepository.completedArSiteIds.collectAsState()
    val quizScore by HeritageRepository.userQuizScore.collectAsState()
    val userRole by HeritageRepository.userRole.collectAsState()
    val currentLanguage by HeritageRepository.currentLanguage.collectAsState()

    val analytics = remember(sites, archives, submissions) {
        HeritageRepository.getAnalytics()
    }

    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }
    var backStack by remember { mutableStateOf(listOf<AppDestination>(AppDestination.Home)) }
    var showQrScannerDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    fun navigateTo(dest: AppDestination) {
        backStack = backStack + dest
        currentDestination = dest
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            val newStack = backStack.dropLast(1)
            backStack = newStack
            currentDestination = newStack.last()
        } else {
            currentDestination = AppDestination.Home
        }
    }

    BackHandler(enabled = backStack.size > 1 || currentDestination !is AppDestination.Home) {
        navigateBack()
    }

    // AR Mode takes over full-screen
    if (currentDestination is AppDestination.Ar) {
        val arSite = (currentDestination as AppDestination.Ar).site
        ArExperienceView(
            site = arSite,
            onBack = { navigateBack() }
        )
    } else {
        Scaffold(
            topBar = {
                if (currentDestination !is AppDestination.Detail) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(ImperialGoldLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Castle,
                                        contentDescription = null,
                                        tint = Obsidian,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "HeritageAR",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = when (currentLanguage) {
                                            AppLanguage.MARATHI -> "सांस्कृतिक वारसा जतन"
                                            AppLanguage.HINDI -> "सांस्कृतिक विरासत संरक्षण"
                                            else -> "Cultural Preservation"
                                        },
                                        fontSize = 10.sp,
                                        color = ImperialGoldLight
                                    )
                                }
                            }
                        },
                        actions = {
                            // QR Scanner Quick Launch Button
                            IconButton(
                                onClick = { showQrScannerDialog = true },
                                modifier = Modifier.testTag("appbar_qr_scanner_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan QR Marker",
                                    tint = SkyArGlow
                                )
                            }

                            // Language Switcher Button
                            IconButton(
                                onClick = { showLanguageDialog = true },
                                modifier = Modifier.testTag("appbar_language_btn")
                            ) {
                                Surface(
                                    color = ObsidianCard,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = currentLanguage.name.take(2),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ImperialGoldLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Obsidian,
                            titleContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                if (currentDestination !is AppDestination.Detail) {
                    NavigationBar(
                        containerColor = ObsidianSurface,
                        contentColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.navigationBarsPadding()
                    ) {
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Home,
                            onClick = { navigateTo(AppDestination.Home) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_home")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Directory,
                            onClick = { navigateTo(AppDestination.Directory) },
                            icon = { Icon(Icons.Default.Castle, contentDescription = "Sites") },
                            label = { Text("Sites", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_directory")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Map,
                            onClick = { navigateTo(AppDestination.Map) },
                            icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                            label = { Text("Map", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_map")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Education,
                            onClick = { navigateTo(AppDestination.Education) },
                            icon = { Icon(Icons.Default.School, contentDescription = "Learn") },
                            label = { Text("Learn", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_learn")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Archive,
                            onClick = { navigateTo(AppDestination.Archive) },
                            icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "Archive") },
                            label = { Text("Archive", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_archive")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Admin || currentDestination is AppDestination.Community,
                            onClick = { navigateTo(AppDestination.Admin) },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("Admin", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_admin")
                        )

                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Profile,
                            onClick = { navigateTo(AppDestination.Profile) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Obsidian,
                                selectedTextColor = ImperialGoldLight,
                                indicatorColor = ImperialGoldLight,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            ),
                            modifier = Modifier.testTag("nav_item_profile")
                        )
                    }
                }
            },
            containerColor = Obsidian
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val dest = currentDestination) {
                    is AppDestination.Home -> {
                        HomeScreen(
                            featuredSites = sites,
                            analytics = analytics,
                            currentLanguage = currentLanguage,
                            onNavigateDirectory = { navigateTo(AppDestination.Directory) },
                            onNavigateMap = { navigateTo(AppDestination.Map) },
                            onNavigateArchives = { navigateTo(AppDestination.Archive) },
                            onNavigateEducation = { navigateTo(AppDestination.Education) },
                            onOpenQrScanner = { showQrScannerDialog = true },
                            onSelectSite = { site -> navigateTo(AppDestination.Detail(site)) },
                            onLaunchAr = { site -> navigateTo(AppDestination.Ar(site)) }
                        )
                    }
                    is AppDestination.Directory -> {
                        HeritageDirectoryScreen(
                            sites = sites,
                            onSelectSite = { site -> navigateTo(AppDestination.Detail(site)) },
                            onLaunchAr = { site -> navigateTo(AppDestination.Ar(site)) }
                        )
                    }
                    is AppDestination.Map -> {
                        HeritageMapView(
                            sites = sites,
                            onSelectSite = { site -> navigateTo(AppDestination.Detail(site)) },
                            onLaunchAr = { site -> navigateTo(AppDestination.Ar(site)) }
                        )
                    }
                    is AppDestination.Education -> {
                        EducationSection(
                            questions = quizQuestions
                        )
                    }
                    is AppDestination.Archive -> {
                        DigitalArchiveView(
                            archives = archives
                        )
                    }
                    is AppDestination.Admin -> {
                        Column {
                            // Sub-navigation between Admin Dashboard & Community Portal
                            var adminTab by remember { mutableIntStateOf(0) }
                            TabRow(
                                selectedTabIndex = adminTab,
                                containerColor = ObsidianCard,
                                contentColor = ImperialGoldLight
                            ) {
                                Tab(
                                    selected = adminTab == 0,
                                    onClick = { adminTab = 0 },
                                    text = { Text("Preservation Analytics", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                                Tab(
                                    selected = adminTab == 1,
                                    onClick = { adminTab = 1 },
                                    text = { Text("Community Submissions", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                            }

                            if (adminTab == 0) {
                                AdminDashboardView(
                                    analytics = analytics,
                                    submissions = submissions,
                                    onAddSite = { newSite -> HeritageRepository.addHeritageSite(newSite) }
                                )
                            } else {
                                CommunityContributionPortal(
                                    onSubmit = { sub -> HeritageRepository.addCommunitySubmission(sub) }
                                )
                            }
                        }
                    }
                    is AppDestination.Community -> {
                        CommunityContributionPortal(
                            onSubmit = { sub -> HeritageRepository.addCommunitySubmission(sub) }
                        )
                    }
                    is AppDestination.Profile -> {
                        UserProfileView(
                            allSites = sites,
                            savedIds = savedIds,
                            completedArIds = completedArIds,
                            quizScore = quizScore,
                            currentRole = userRole,
                            currentLanguage = currentLanguage,
                            onSelectSite = { site -> navigateTo(AppDestination.Detail(site)) }
                        )
                    }
                    is AppDestination.Detail -> {
                        HeritageDetailScreen(
                            site = dest.site,
                            onBack = { navigateBack() },
                            onLaunchAr = { navigateTo(AppDestination.Ar(dest.site)) }
                        )
                    }
                    is AppDestination.Ar -> {
                        // Handled above
                    }
                }
            }
        }
    }

    // QR Marker Scanner Dialog
    if (showQrScannerDialog) {
        QrScannerDialog(
            sites = sites,
            onSiteSelected = { site ->
                navigateTo(AppDestination.Detail(site))
            },
            onDismiss = { showQrScannerDialog = false }
        )
    }

    // Multilingual Switcher Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text("Select Platform Language", fontWeight = FontWeight.Bold, color = Color.White)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.entries.forEach { lang ->
                        Card(
                            onClick = {
                                HeritageRepository.setLanguage(lang)
                                showLanguageDialog = false
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (currentLanguage == lang) ImperialGoldLight else ObsidianCard
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = lang.nativeName,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentLanguage == lang) Obsidian else Color.White
                                    )
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 11.sp,
                                        color = if (currentLanguage == lang) Obsidian else Color.LightGray
                                    )
                                }
                                if (currentLanguage == lang) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Obsidian)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close", color = Color.LightGray)
                }
            },
            containerColor = ObsidianSurface
        )
    }
}
