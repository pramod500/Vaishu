package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HeritageRepository
import com.example.model.HeritageSite
import com.example.model.Hotspot
import com.example.ui.components.AudioGuidePlayer
import com.example.ui.components.Monument3DCanvas
import com.example.ui.components.QrCodeCard
import com.example.ui.theme.*

@Composable
fun HeritageDetailScreen(
    site: HeritageSite,
    onBack: () -> Unit,
    onLaunchAr: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val savedSiteIds by HeritageRepository.savedSiteIds.collectAsState()
    val isSaved = savedSiteIds.contains(site.id)

    var selectedSectionTab by remember { mutableIntStateOf(0) } // 0: 3D & History, 1: Audio Guide, 2: Timeline & Facts, 3: Official QR

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Obsidian)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = site.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row {
                    IconButton(
                        onClick = { HeritageRepository.toggleSaveSite(site.id) },
                        modifier = Modifier.testTag("detail_save_toggle")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save Site",
                            tint = if (isSaved) ImperialGoldLight else Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Discover ${site.name} in Augmented Reality on the National Cultural Heritage Platform! Historic period: ${site.period}. Location: ${site.location}."
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Heritage Site"))
                        },
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = ObsidianSurface,
                modifier = Modifier.navigationBarsPadding(),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Interactive Mode", fontSize = 11.sp, color = SkyArGlow)
                        Text("3D Mesh & Hotspots Active", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = onLaunchAr,
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("detail_start_ar_btn")
                    ) {
                        Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Start AR Experience", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        },
        containerColor = Obsidian,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Interactive 3D Model Hero Canvas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                ) {
                    Monument3DCanvas(
                        meshType = site.meshType,
                        hotspots = site.hotspots,
                        enableHotspotSelection = true,
                        modifier = Modifier.fillMaxSize()
                    )

                    // AR Badge on top left of 3D canvas
                    Surface(
                        color = Terracotta,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ViewInAr, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("3D Digital Twin", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Site Title & Meta
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = site.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${site.nativeName} • ${site.location}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ImperialGoldLight
                            )
                        }

                        Surface(
                            color = ObsidianCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPreservation)
                        ) {
                            Text(
                                text = "${site.preservationScore}% PRESERVED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPreservation,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Subtabs
                    TabRow(
                        selectedTabIndex = selectedSectionTab,
                        containerColor = ObsidianCard,
                        contentColor = ImperialGoldLight,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedSectionTab == 0,
                            onClick = { selectedSectionTab = 0 },
                            text = { Text("History", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedSectionTab == 1,
                            onClick = { selectedSectionTab = 1 },
                            text = { Text("Audio Guide", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedSectionTab == 2,
                            onClick = { selectedSectionTab = 2 },
                            text = { Text("Timeline", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedSectionTab == 3,
                            onClick = { selectedSectionTab = 3 },
                            text = { Text("QR Marker", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Tab Content
            when (selectedSectionTab) {
                0 -> {
                    // History & Architecture
                    item {
                        DetailSectionCard(title = "Historical Overview", icon = Icons.Default.MenuBook) {
                            Text(
                                text = site.historyDetail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Parchment,
                                lineHeight = 21.sp
                            )
                        }
                    }

                    item {
                        DetailSectionCard(title = "Architectural Engineering", icon = Icons.Default.Architecture) {
                            Text(
                                text = site.architectureDetail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Parchment,
                                lineHeight = 21.sp
                            )
                        }
                    }

                    item {
                        DetailSectionCard(title = "Cultural & Heritage Significance", icon = Icons.Default.Public) {
                            Text(
                                text = site.culturalImportance,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Parchment,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }
                1 -> {
                    // Audio Guide Player
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            AudioGuidePlayer(
                                siteName = site.name,
                                audioContent = site.audioContent
                            )
                        }
                    }
                }
                2 -> {
                    // Timeline & Key Facts
                    item {
                        DetailSectionCard(title = "Historical Timeline", icon = Icons.Default.Timeline) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                site.timelineEvents.forEach { ev ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Surface(
                                            color = ImperialGoldLight,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = ev.year,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Obsidian,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(ev.era, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SkyArGlow)
                                            Text(ev.event, style = MaterialTheme.typography.bodySmall, color = Parchment)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        DetailSectionCard(title = "Crucial Preservation Facts", icon = Icons.Default.Lightbulb) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                site.importantFacts.forEach { fact ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text("•", color = ImperialGoldLight, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(fact, style = MaterialTheme.typography.bodyMedium, color = Parchment)
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Official QR Marker Placard
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            QrCodeCard(
                                site = site,
                                onLaunchArFromQr = onLaunchAr
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(icon, contentDescription = null, tint = ImperialGoldLight, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            content()
        }
    }
}
