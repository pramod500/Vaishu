package com.example.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArchiveItem
import com.example.ui.theme.*

@Composable
fun DigitalArchiveView(
    archives: List<ArchiveItem>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("All") }
    var selectedItemForDetail by remember { mutableStateOf<ArchiveItem?>(null) }

    val types = remember(archives) { listOf("All") + archives.map { it.type }.distinct() }

    val filteredArchives = remember(archives, searchQuery, selectedTypeFilter) {
        archives.filter { item ->
            (selectedTypeFilter == "All" || item.type == selectedTypeFilter) &&
            (searchQuery.isEmpty() || item.title.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("digital_archive_view")
    ) {
        // Search & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search manuscripts, maps, inscriptions...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ImperialGoldLight) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("archive_search_field"),
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

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(types) { t ->
                    val isSelected = selectedTypeFilter == t
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTypeFilter = t },
                        label = { Text(t, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ImperialGoldLight,
                            selectedLabelColor = Obsidian
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Archive List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredArchives) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedItemForDetail = item }
                        .testTag("archive_item_${item.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(HeritageNavyLight, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (item.type) {
                                    "Historical Photograph" -> Icons.Default.PhotoCamera
                                    "Old Cartographic Map" -> Icons.Default.Map
                                    "Rock Inscription" -> Icons.Default.Gesture
                                    "Oral Ballad / Tradition" -> Icons.Default.Mic
                                    else -> Icons.Default.Article
                                },
                                contentDescription = null,
                                tint = ImperialGoldLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = ObsidianSurface,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = item.type,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SkyArGlow,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = item.yearOrEra,
                                    fontSize = 11.sp,
                                    color = ImperialGoldLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Parchment,
                                maxLines = 2,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Code: ${item.archiveCode}",
                                fontSize = 10.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }

        // Detail Inspector Modal Dialog
        selectedItemForDetail?.let { item ->
            AlertDialog(
                onDismissRequest = { selectedItemForDetail = null },
                title = {
                    Text(item.title, fontWeight = FontWeight.Bold, color = Color.White)
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(color = ObsidianCard, shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = "Archive Code: ${item.archiveCode}",
                                fontSize = 11.sp,
                                color = SkyArGlow,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Parchment,
                            lineHeight = 20.sp
                        )

                        Divider(color = ObsidianCard)

                        Text("Preservation Condition: ${item.preservationCondition}", fontSize = 12.sp, color = EmeraldPreservation, fontWeight = FontWeight.SemiBold)
                        Text("Repository / In-Situ: ${item.location}", fontSize = 12.sp, color = ImperialGoldLight)
                        Text("Historical Era: ${item.yearOrEra}", fontSize = 12.sp, color = Color.LightGray)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedItemForDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = HeritageNavy)
                    ) {
                        Text("Close")
                    }
                },
                containerColor = ObsidianSurface
            )
        }
    }
}
