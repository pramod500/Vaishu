package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun UserProfileView(
    allSites: List<HeritageSite>,
    savedIds: Set<String>,
    completedArIds: Set<String>,
    quizScore: Int,
    currentRole: String,
    currentLanguage: AppLanguage,
    onSelectSite: (HeritageSite) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedSites = remember(allSites, savedIds) { allSites.filter { savedIds.contains(it.id) } }
    val arSites = remember(allSites, completedArIds) { allSites.filter { completedArIds.contains(it.id) } }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Saved Sites, 1: AR History, 2: Badges & Learning

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("user_profile_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // User Identity Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(ImperialGoldLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Obsidian,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Kirti Sabale",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = currentRole,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ImperialGoldLight,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "kirti.sabale@heritage.gov.in",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Language Preference Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("App Language:", fontSize = 12.sp, color = Parchment)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            AppLanguage.entries.forEach { lang ->
                                val isSelected = currentLanguage == lang
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { HeritageRepository.setLanguage(lang) },
                                    label = { Text(lang.displayName, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ImperialGoldLight,
                                        selectedLabelColor = Obsidian
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileStatBadge("Saved", "${savedSites.size}", Icons.Default.Bookmark, Modifier.weight(1f))
                ProfileStatBadge("AR Visited", "${arSites.size}", Icons.Default.ViewInAr, Modifier.weight(1f))
                ProfileStatBadge("Quiz Score", "$quizScore pts", Icons.Default.EmojiEvents, Modifier.weight(1f))
            }
        }

        // Subtabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ObsidianCard,
                contentColor = ImperialGoldLight
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Saved Sites (${savedSites.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("AR Passport (${arSites.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Badges", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (selectedTab == 0) {
            if (savedSites.isEmpty()) {
                item {
                    Text(
                        "No saved heritage monuments yet. Explore sites and tap the Bookmark icon to save them to your offline itinerary.",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(savedSites) { site ->
                    SavedSiteCard(site = site, onClick = { onSelectSite(site) })
                }
            }
        } else if (selectedTab == 1) {
            if (arSites.isEmpty()) {
                item {
                    Text(
                        "No AR passport stamps collected yet. Launch an Augmented Reality experience on any monument to stamp your passport!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(arSites) { site ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSite(site) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(EmeraldPreservation.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPreservation)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(site.name, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("AR Stamp Verified • ${site.location}", fontSize = 11.sp, color = SkyArGlow)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                        }
                    }
                }
            }
        } else {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    BadgeItem("Swarajya Scholar", "Explored historical Maratha fortifications and assemblies", Icons.Default.Shield, true)
                    BadgeItem("Cave Fresco Conservator", "Examined Ajanta & Ellora ancient rock-cut art in AR", Icons.Default.Palette, true)
                    BadgeItem("Astronomical Master", "Discovered solar sundial calculations at Konark", Icons.Default.WbSunny, true)
                    BadgeItem("Master of Heritage Quizzes", "Achieved high marks in National Monument examinations", Icons.Default.MilitaryTech, quizScore >= 3)
                }
            }
        }
    }
}

@Composable
private fun ProfileStatBadge(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = ImperialGoldLight, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            Text(label, fontSize = 10.sp, color = Parchment)
        }
    }
}

@Composable
private fun SavedSiteCard(site: HeritageSite, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(site.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Text(site.location, fontSize = 11.sp, color = ImperialGoldLight)
                Text(site.shortDescription, fontSize = 11.sp, color = Parchment, maxLines = 1)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun BadgeItem(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    unlocked: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (unlocked) ObsidianCard else ObsidianSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (unlocked) ImperialGold.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (unlocked) ImperialGoldLight else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = if (unlocked) Color.White else Color.Gray)
                Text(desc, fontSize = 11.sp, color = if (unlocked) Parchment else Color.Gray)
            }
        }
    }
}
