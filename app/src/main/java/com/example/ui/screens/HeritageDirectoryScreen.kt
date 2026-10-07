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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HeritageSite
import com.example.ui.theme.*

@Composable
fun HeritageDirectoryScreen(
    sites: List<HeritageSite>,
    onSelectSite: (HeritageSite) -> Unit,
    onLaunchAr: (HeritageSite) -> Unit,
    initialCategory: String = "All",
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedState by remember { mutableStateOf("All") }
    var onlyArFilter by remember { mutableStateOf(false) }

    val categories = remember(sites) {
        listOf("All", "Forts", "Temples", "Caves", "Palaces", "Historical Buildings", "Archaeological Sites")
    }

    val states = remember(sites) {
        listOf("All") + sites.map { it.state }.distinct()
    }

    val filteredSites = remember(sites, searchQuery, selectedCategory, selectedState, onlyArFilter) {
        sites.filter { s ->
            val matchesQuery = searchQuery.isEmpty() ||
                s.name.contains(searchQuery, ignoreCase = true) ||
                s.location.contains(searchQuery, ignoreCase = true) ||
                s.district.contains(searchQuery, ignoreCase = true) ||
                s.period.contains(searchQuery, ignoreCase = true) ||
                s.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || s.category.equals(selectedCategory, ignoreCase = true)
            val matchesState = selectedState == "All" || s.state.equals(selectedState, ignoreCase = true)
            val matchesAr = !onlyArFilter || s.isArAvailable

            matchesQuery && matchesCategory && matchesState && matchesAr
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("heritage_directory_screen")
    ) {
        // Search & Filters Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Raigad, Ajanta, forts, dynasties...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ImperialGoldLight) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ImperialGoldLight,
                    unfocusedBorderColor = ObsidianCard,
                    focusedContainerColor = ObsidianCard,
                    unfocusedContainerColor = ObsidianCard,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Category Horizontal Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HeritageNavy,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Secondary Toggles: State & AR Only
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(states) { st ->
                        val isSelected = selectedState == st
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedState = st },
                            label = { Text(st, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ImperialGoldLight,
                                selectedLabelColor = Obsidian
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    selected = onlyArFilter,
                    onClick = { onlyArFilter = !onlyArFilter },
                    label = { Text("AR Only", fontSize = 10.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(12.dp))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Terracotta,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(28.dp)
                )
            }
        }

        // Monument List
        if (filteredSites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(54.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No heritage monuments match your search.", color = Color.LightGray, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = {
                        searchQuery = ""
                        selectedCategory = "All"
                        selectedState = "All"
                        onlyArFilter = false
                    }) {
                        Text("Reset All Filters", color = ImperialGoldLight)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredSites) { site ->
                    HeritageSiteCard(
                        site = site,
                        onViewDetails = { onSelectSite(site) },
                        onLaunchAr = { onLaunchAr(site) }
                    )
                }
            }
        }
    }
}

@Composable
fun HeritageSiteCard(
    site: HeritageSite,
    onViewDetails: () -> Unit,
    onLaunchAr: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() }
            .testTag("heritage_card_${site.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Name & Native Name + Preservation Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = site.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${site.nativeName} • ${site.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SkyArGlow
                    )
                }

                Surface(
                    color = EmeraldPreservation.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPreservation, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${site.preservationScore}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPreservation
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = site.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = Parchment,
                lineHeight = 17.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Badges Row: Category, AR Available, 3D Available, Audio Available
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeritageBadge(label = site.category, color = ImperialGoldLight)
                if (site.isArAvailable) {
                    HeritageBadge(label = "AR Live", color = SkyArGlow)
                }
                if (site.is3dAvailable) {
                    HeritageBadge(label = "3D Mesh", color = ImperialGold)
                }
                if (site.isAudioAvailable) {
                    HeritageBadge(label = "Audio", color = EmeraldPreservation)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Parchment)
                ) {
                    Text("View Full History", fontSize = 12.sp)
                }

                Button(
                    onClick = onLaunchAr,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    modifier = Modifier.testTag("launch_ar_${site.id}")
                ) {
                    Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Start AR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HeritageBadge(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}
