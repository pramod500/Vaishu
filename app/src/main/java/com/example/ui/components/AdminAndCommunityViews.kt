package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun AdminDashboardView(
    analytics: PreservationAnalytics,
    submissions: List<CommunitySubmission>,
    onAddSite: (HeritageSite) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddSiteDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("admin_dashboard"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "National Preservation Command",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Real-time Heritage Digitization & AR Telemetry",
                        style = MaterialTheme.typography.bodySmall,
                        color = ImperialGoldLight
                    )
                }

                Button(
                    onClick = { showAddSiteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_heritage_site_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Site", fontSize = 12.sp)
                }
            }
        }

        // Metrics Grid (2x2 Cards)
        item {
            Text(
                text = "Preservation Core Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SkyArGlow
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Digitized Sites",
                        value = "${analytics.totalSites}",
                        sub = "${analytics.arEnabledSites} AR Enabled",
                        icon = Icons.Default.Castle,
                        color = ImperialGoldLight,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "3D Models Live",
                        value = "${analytics.models3dCount}",
                        sub = "Interactive Shaders",
                        icon = Icons.Default.ViewInAr,
                        color = SkyArGlow,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "AR Experiences",
                        value = "${analytics.arExperiencesCount}",
                        sub = "+340 this week",
                        icon = Icons.Default.CameraAlt,
                        color = TerracottaLight,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Preservation Rate",
                        value = "${analytics.activePreservationRate}%",
                        sub = "ASI Benchmark Met",
                        icon = Icons.Default.Verified,
                        color = EmeraldPreservation,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Community Submissions Moderation Queue
        item {
            Text(
                text = "Community Contribution Moderation Queue",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(submissions) { sub ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(sub.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("By ${sub.contributorName} (${sub.contributorRole}) • ${sub.date}", fontSize = 11.sp, color = Color.LightGray)
                        }

                        Surface(
                            color = when (sub.status) {
                                "Approved & Archived", "Verified & Preserved" -> EmeraldPreservation.copy(alpha = 0.2f)
                                else -> ImperialGold.copy(alpha = 0.2f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = sub.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (sub.status) {
                                    "Approved & Archived", "Verified & Preserved" -> EmeraldPreservation
                                    else -> ImperialGoldLight
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(sub.description, style = MaterialTheme.typography.bodySmall, color = Parchment, lineHeight = 16.sp)
                    Spacer(Modifier.height(12.dp))

                    if (sub.status == "Pending Review") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { HeritageRepository.updateSubmissionStatus(sub.id, "Approved & Archived") },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPreservation),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Approve & Archive", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { HeritageRepository.updateSubmissionStatus(sub.id, "Needs Field Verification") },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Request Verifications", fontSize = 11.sp, color = Parchment)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddSiteDialog) {
        AddHeritageSiteDialog(
            onDismiss = { showAddSiteDialog = false },
            onSave = { newSite ->
                onAddSite(newSite)
                showAddSiteDialog = false
            }
        )
    }
}

@Composable
fun CommunityContributionPortal(
    onSubmit: (CommunitySubmission) -> Unit,
    modifier: Modifier = Modifier
) {
    var contributorName by remember { mutableStateOf("") }
    var contributorRole by remember { mutableStateOf("Student Researcher") }
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Forts & Defenses") }
    var location by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("Medieval Era") }
    var description by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("community_contribution_form"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Citizen Heritage Preservation Portal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Contribute local monuments, oral ballads, folk memories, and unarchived ruins.",
                style = MaterialTheme.typography.bodySmall,
                color = ImperialGoldLight
            )
        }

        if (isSubmitted) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPreservation, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Submission Transmitted Successfully!", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "Thank you for preserving cultural heritage. Your submission has been forwarded to the Archaeological Review Board.",
                            fontSize = 12.sp,
                            color = Parchment,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                isSubmitted = false
                                title = ""
                                location = ""
                                description = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HeritageNavy)
                        ) {
                            Text("Submit Another Heritage Item")
                        }
                    }
                }
            }
        } else {
            item {
                OutlinedTextField(
                    value = contributorName,
                    onValueChange = { contributorName = it },
                    label = { Text("Your Name / Researcher ID") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Heritage Title / Monument Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Geographic Location (Village / District / Coordinates)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Historical Significance / Architectural Details / Oral Legend") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Button(
                    onClick = {
                        val sub = CommunitySubmission(
                            id = "sub-${System.currentTimeMillis()}",
                            contributorName = contributorName.ifEmpty { "Anonymous Citizen Scholar" },
                            contributorRole = contributorRole,
                            title = title.ifEmpty { "Uncatalogued Heritage Site" },
                            category = category,
                            location = location.ifEmpty { "Maharashtra, India" },
                            period = period,
                            description = description.ifEmpty { "Historical documentation submitted for archival inspection." },
                            date = "Just now",
                            status = "Pending Review"
                        )
                        onSubmit(sub)
                        isSubmitted = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_community_form"),
                    colors = ButtonDefaults.buttonColors(containerColor = ImperialGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Submit for Academic Verification", fontWeight = FontWeight.Bold, color = Obsidian)
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = Parchment)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(sub, fontSize = 10.sp, color = color)
        }
    }
}

@Composable
fun AddHeritageSiteDialog(
    onDismiss: () -> Unit,
    onSave: (HeritageSite) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Forts") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Digitize New Heritage Site", fontWeight = FontWeight.Bold, color = Color.White) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Monument Name") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location (District, State)") })
                OutlinedTextField(value = period, onValueChange = { period = it }, label = { Text("Historical Period / Era") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Short Description") }, maxLines = 3)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val id = "site-${System.currentTimeMillis()}"
                    val site = HeritageSite(
                        id = id,
                        name = name.ifEmpty { "New Heritage Monument" },
                        nativeName = name,
                        location = location.ifEmpty { "India" },
                        district = "Pune",
                        state = "Maharashtra",
                        latitude = 18.5204,
                        longitude = 73.8567,
                        period = period.ifEmpty { "17th Century" },
                        century = "Historic",
                        category = category,
                        monumentType = "Preserved Heritage",
                        shortDescription = description.ifEmpty { "Digitally catalogued heritage structure." },
                        historyDetail = "Historic chronicle archived by preservation authorities.",
                        architectureDetail = "Stone architectural masonry with seismic resistant foundations.",
                        culturalImportance = "Inscribed for educational and public tourism preservation.",
                        importantFacts = listOf("Digitally preserved using photogrammetry and AR."),
                        timelineEvents = listOf(TimelineEvent("Historic", "Founding and construction", "Origin")),
                        preservationStatus = "Under Review & Preservation",
                        preservationScore = 90,
                        imageUrl = "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?auto=format&fit=crop&w=1200&q=80",
                        meshType = "fortress",
                        hotspots = listOf(Hotspot("h1", "Main Portal", "Historic entranceway", 0f, 0f, 0.4f)),
                        audioContent = AudioContent("Welcome to this newly catalogued heritage site.", "या नवीन वारसा स्थळात आपले स्वागत आहे.", "इस नए सांस्कृतिक विरासत स्थल में आपका स्वागत है."),
                        qrIdentifier = "HERITAGE-AR-CUSTOM-${System.currentTimeMillis() % 1000}"
                    )
                    onSave(site)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
            ) {
                Text("Save to Live Database")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.LightGray) }
        },
        containerColor = ObsidianSurface
    )
}
