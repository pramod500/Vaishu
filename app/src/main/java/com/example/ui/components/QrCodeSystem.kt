package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HeritageSite
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun QrCodeCard(
    site: HeritageSite,
    onLaunchArFromQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showActionNotice by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("qr_code_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Official AR Marker QR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HeritageNavy
                    )
                    Text(
                        text = "Scan on-site at monument to launch AR model",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Surface(
                    color = ImperialGoldGlow,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "VERIFIED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AncientBronze,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Procedural QR Code Visual Canvas with Center Heritage Seal
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(2.dp, ParchmentBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                ProceduralQrCanvas(seed = site.qrIdentifier.hashCode())

                // Center Heritage Crest Logo
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(HeritageNavy, CircleShape)
                        .border(2.dp, ImperialGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = null,
                        tint = ImperialGoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = "ID: ${site.qrIdentifier}",
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeritageNavy
            )

            // QR Actions Row: Print, Download, Share, Test Launch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { showActionNotice = "QR Code image downloaded to device gallery." },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("download_qr_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Download", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { showActionNotice = "Printing formatted placard for ${site.name}." },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("print_qr_button")
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Print", fontSize = 12.sp)
                }

                Button(
                    onClick = onLaunchArFromQr,
                    colors = ButtonDefaults.buttonColors(containerColor = HeritageNavy),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("test_qr_ar_launch")
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Simulate Scan", fontSize = 12.sp)
                }
            }

            showActionNotice?.let { msg ->
                Text(
                    text = msg,
                    color = EmeraldPreservation,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun QrScannerDialog(
    sites: List<HeritageSite>,
    onSiteSelected: (HeritageSite) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = ImperialGoldLight,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("QR Marker Scanner", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Point camera at any official heritage placard QR marker, or select a site marker below to instantly test the flow:",
                    fontSize = 13.sp,
                    color = Parchment
                )

                // Simulated active viewfinder reticle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(ObsidianCard, RoundedCornerShape(12.dp))
                        .border(1.5.dp, SkyArGlow, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CropFree,
                        contentDescription = null,
                        tint = SkyArGlow,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "SCANNING FOR HERITAGE MARKERS...",
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
                    )
                }

                Text(
                    text = "Detected Physical Monuments:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = ImperialGoldLight
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(sites) { s ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSiteSelected(s)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(s.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(s.qrIdentifier, fontSize = 11.sp, color = SkyArGlow)
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ImperialGoldLight, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.LightGray)
            }
        },
        containerColor = ObsidianSurface
    )
}

// Procedural 2D QR Pattern Canvas
@Composable
fun ProceduralQrCanvas(seed: Int, modifier: Modifier = Modifier.fillMaxSize()) {
    Canvas(modifier = modifier) {
        val gridSize = 21
        val cellSize = size.width / gridSize
        val rand = Random(seed)

        // Draw Finder Patterns at 3 corners (Top-Left, Top-Right, Bottom-Left)
        drawFinderPattern(0f, 0f, cellSize)
        drawFinderPattern((gridSize - 7) * cellSize, 0f, cellSize)
        drawFinderPattern(0f, (gridSize - 7) * cellSize, cellSize)

        // Fill data matrix
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val inFinder1 = row < 7 && col < 7
                val inFinder2 = row < 7 && col >= gridSize - 7
                val inFinder3 = row >= gridSize - 7 && col < 7
                val inCenter = row in 8..12 && col in 8..12

                if (!inFinder1 && !inFinder2 && !inFinder3 && !inCenter) {
                    if (rand.nextBoolean()) {
                        drawRect(
                            color = HeritageNavy,
                            topLeft = Offset(col * cellSize, row * cellSize),
                            size = Size(cellSize * 0.9f, cellSize * 0.9f)
                        )
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFinderPattern(x: Float, y: Float, cellSize: Float) {
    // 7x7 outer square
    drawRect(
        color = HeritageNavy,
        topLeft = Offset(x, y),
        size = Size(cellSize * 7, cellSize * 7)
    )
    // 5x5 white inner square
    drawRect(
        color = Color.White,
        topLeft = Offset(x + cellSize, y + cellSize),
        size = Size(cellSize * 5, cellSize * 5)
    )
    // 3x3 solid core
    drawRect(
        color = HeritageNavy,
        topLeft = Offset(x + cellSize * 2, y + cellSize * 2),
        size = Size(cellSize * 3, cellSize * 3)
    )
}
