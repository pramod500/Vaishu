package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HeritageSite
import com.example.ui.theme.*

@Composable
fun HeritageMapView(
    sites: List<HeritageSite>,
    onSelectSite: (HeritageSite) -> Unit,
    onLaunchAr: (HeritageSite) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var selectedStateFilter by remember { mutableStateOf("All") }
    var selectedSiteForPreview by remember { mutableStateOf<HeritageSite?>(sites.firstOrNull()) }

    val categories = remember(sites) { listOf("All") + sites.map { it.category }.distinct() }
    val states = remember(sites) { listOf("All") + sites.map { it.state }.distinct() }

    val filteredSites = remember(sites, selectedCategoryFilter, selectedStateFilter) {
        sites.filter { site ->
            (selectedCategoryFilter == "All" || site.category == selectedCategoryFilter) &&
            (selectedStateFilter == "All" || site.state == selectedStateFilter)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("heritage_map_screen")
    ) {
        // Filter Chips Row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HeritageNavy,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // State Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(states) { st ->
                    val isSelected = selectedStateFilter == st
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStateFilter = st },
                        label = { Text(st, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ImperialGoldLight,
                            selectedLabelColor = Obsidian
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Interactive Vector Map Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Obsidian, RoundedCornerShape(24.dp))
        ) {
            // Coordinate bounds for Indian subcontinent projection
            // Lat: ~8 to ~36, Long: ~68 to ~98
            val minLat = 10.0
            val maxLat = 28.0
            val minLong = 70.0
            val maxLong = 88.0

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(filteredSites) {
                        detectTapGestures { tapOffset ->
                            val width = size.width
                            val height = size.height

                            // Find closest marker within touch radius
                            val tappedSite = filteredSites.minByOrNull { site ->
                                val normX = ((site.longitude - minLong) / (maxLong - minLong)).toFloat()
                                val normY = (1f - ((site.latitude - minLat) / (maxLat - minLat)).toFloat())
                                val px = normX * width * 0.8f + width * 0.1f
                                val py = normY * height * 0.8f + height * 0.1f
                                val dx = tapOffset.x - px
                                val dy = tapOffset.y - py
                                dx * dx + dy * dy
                            }
                            if (tappedSite != null) {
                                selectedSiteForPreview = tappedSite
                            }
                        }
                    }
            ) {
                val w = size.width
                val h = size.height

                // Draw decorative cartographic latitude/longitude grid
                val gridColor = HeritageNavyLight.copy(alpha = 0.35f)
                for (i in 1..5) {
                    val gy = h * (i / 6f)
                    drawLine(gridColor, Offset(0f, gy), Offset(w, gy), strokeWidth = 1f)
                    val gx = w * (i / 6f)
                    drawLine(gridColor, Offset(gx, 0f), Offset(gx, h), strokeWidth = 1f)
                }

                // Draw artistic peninsula coastline contours
                val coastPath = Path().apply {
                    moveTo(w * 0.22f, h * 0.2f)
                    lineTo(w * 0.25f, h * 0.5f)
                    lineTo(w * 0.35f, h * 0.85f)
                    lineTo(w * 0.5f, h * 0.95f) // Southern tip
                    lineTo(w * 0.65f, h * 0.82f)
                    lineTo(w * 0.82f, h * 0.52f)
                    lineTo(w * 0.75f, h * 0.2f)
                }
                drawPath(
                    coastPath,
                    color = SkyArCyan.copy(alpha = 0.25f),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Draw markers for filtered heritage sites
                filteredSites.forEach { site ->
                    val normX = ((site.longitude - minLong) / (maxLong - minLong)).toFloat().coerceIn(0f, 1f)
                    val normY = (1f - ((site.latitude - minLat) / (maxLat - minLat)).toFloat()).coerceIn(0f, 1f)
                    val px = normX * w * 0.8f + w * 0.1f
                    val py = normY * h * 0.8f + h * 0.1f

                    val isSelected = selectedSiteForPreview?.id == site.id

                    // Marker outer halo
                    drawCircle(
                        color = if (isSelected) ImperialGoldLight else SkyArGlow.copy(alpha = 0.5f),
                        radius = if (isSelected) 18.dp.toPx() else 10.dp.toPx(),
                        center = Offset(px, py)
                    )

                    // Marker pin head
                    drawCircle(
                        color = if (isSelected) TerracottaLight else ImperialGold,
                        radius = if (isSelected) 10.dp.toPx() else 6.dp.toPx(),
                        center = Offset(px, py)
                    )

                    // Pin core center
                    drawCircle(
                        color = Color.White,
                        radius = if (isSelected) 4.dp.toPx() else 2.5.dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }

            // Map legend badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                color = ObsidianCard.copy(alpha = 0.85f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).background(TerracottaLight, CircleShape))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "${filteredSites.size} Heritage Sites Mapped",
                        fontSize = 11.sp,
                        color = Parchment,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Bottom Selected Site Preview Card
        selectedSiteForPreview?.let { site ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSelectSite(site) }
                    .testTag("map_site_preview_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = site.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                color = ImperialGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    site.category,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGoldLight,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "${site.district}, ${site.state} • ${site.period}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SkyArGlow
                        )

                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = site.shortDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment,
                            maxLines = 2,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Launch AR shortcut button
                    FilledIconButton(
                        onClick = { onLaunchAr(site) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Terracotta,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.ViewInAr, contentDescription = "Launch AR Experience")
                    }
                }
            }
        }
    }
}
