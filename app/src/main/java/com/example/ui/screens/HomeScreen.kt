package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HeritageSite
import com.example.model.PreservationAnalytics
import com.example.ui.components.Monument3DCanvas
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    featuredSites: List<HeritageSite>,
    analytics: PreservationAnalytics,
    currentLanguage: AppLanguage,
    onNavigateDirectory: () -> Unit,
    onNavigateMap: () -> Unit,
    onNavigateArchives: () -> Unit,
    onNavigateEducation: () -> Unit,
    onOpenQrScanner: () -> Unit,
    onSelectSite: (HeritageSite) -> Unit,
    onLaunchAr: (HeritageSite) -> Unit,
    modifier: Modifier = Modifier
) {
    val heroSite = featuredSites.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Grand Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(HeritageNavyDark, Obsidian, ObsidianSurface)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Tagline Pill
                    Surface(
                        color = ImperialGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.MARATHI -> "इतिहास जतन करा • वारसा अनुभवा • भविष्याला घडवा"
                                AppLanguage.HINDI -> "अतीत को संजोएं • विरासत का अनुभव करें • भविष्य को शिक्षित करें"
                                else -> "Preserve the Past • Experience the Heritage • Educate the Future"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGoldLight,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "AR Cultural Heritage\nPreservation Platform",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Explore History in Real-time Augmented Reality",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SkyArGlow,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Hero 3D Mini Interactive Preview of Raigad Fort / First Monument
                    heroSite?.let { site ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clickable { onSelectSite(site) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Monument3DCanvas(
                                    meshType = site.meshType,
                                    hotspots = site.hotspots,
                                    enableHotspotSelection = false,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Surface(
                                    color = Terracotta,
                                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = "FEATURED 3D: ${site.name}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                Button(
                                    onClick = { onLaunchAr(site) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(12.dp)
                                ) {
                                    Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Launch AR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 6 Primary Quick Action Buttons Grid (from spec)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HeroActionChip("Explore Heritage", Icons.Default.Castle, Modifier.weight(1f)) { onNavigateDirectory() }
                        HeroActionChip("Start AR Mode", Icons.Default.ViewInAr, Modifier.weight(1f)) { heroSite?.let { onLaunchAr(it) } }
                        HeroActionChip("Scan QR Code", Icons.Default.QrCodeScanner, Modifier.weight(1f)) { onOpenQrScanner() }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HeroActionChip("Heritage Map", Icons.Default.Map, Modifier.weight(1f)) { onNavigateMap() }
                        HeroActionChip("Digital Archives", Icons.Default.FolderSpecial, Modifier.weight(1f)) { onNavigateArchives() }
                        HeroActionChip("Heritage Quiz", Icons.Default.Quiz, Modifier.weight(1f)) { onNavigateEducation() }
                    }
                }
            }
        }

        // Live Preservation Telemetry Pill Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TelemetryStat("${analytics.totalSites}", "Sites Preserved", EmeraldPreservation)
                    TelemetryStat("${analytics.models3dCount}", "3D Models", ImperialGoldLight)
                    TelemetryStat("${analytics.arExperiencesCount}", "AR Visits", SkyArGlow)
                    TelemetryStat("${analytics.digitalDocuments}", "Archives", TerracottaLight)
                }
            }
        }

        // Section: Featured Heritage Sites Horizontal Carousel
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Heritage Monuments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    TextButton(onClick = onNavigateDirectory) {
                        Text("View All (${featuredSites.size}) →", color = ImperialGoldLight, fontSize = 12.sp)
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(featuredSites) { site ->
                        FeaturedMonumentCard(
                            site = site,
                            onSelect = { onSelectSite(site) },
                            onStartAr = { onLaunchAr(site) }
                        )
                    }
                }
            }
        }

        // Section: Educational Quiz Teaser Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateEducation() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = HeritageNavy
                )
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(ImperialGoldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Obsidian,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Student Heritage Exam",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Test your knowledge on forts, dynasties, and ancient temple science.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = ImperialGoldLight
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = ObsidianCard,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = ImperialGoldLight, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TelemetryStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 10.sp, color = Parchment)
    }
}

@Composable
private fun FeaturedMonumentCard(
    site: HeritageSite,
    onSelect: () -> Unit,
    onStartAr: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { onSelect() }
            .testTag("featured_card_${site.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // 3D Mini Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(ObsidianSurface)
            ) {
                Monument3DCanvas(
                    meshType = site.meshType,
                    hotspots = emptyList(),
                    enableHotspotSelection = false,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    color = ObsidianCard.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = site.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGoldLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = site.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = "${site.district}, ${site.state}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SkyArGlow
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = site.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
                    maxLines = 2,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onStartAr,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Experience in AR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
