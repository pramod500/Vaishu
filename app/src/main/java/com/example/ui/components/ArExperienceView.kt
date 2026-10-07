package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.HeritageRepository
import com.example.model.HeritageSite
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ArExperienceView(
    site: HeritageSite,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Automatically request camera if not granted yet
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        HeritageRepository.markArCompleted(site.id)
    }

    var isSurfaceDetected by remember { mutableStateOf(false) }
    var isModelPlaced by remember { mutableStateOf(false) }
    var modelScale by remember { mutableFloatStateOf(1.0f) }
    var modelOffsetX by remember { mutableFloatStateOf(0f) }
    var modelOffsetY by remember { mutableFloatStateOf(0f) }
    var showInfoOverlay by remember { mutableStateOf(true) }
    var showSnapshotAlert by remember { mutableStateOf(false) }

    // Surface detection scan simulation
    LaunchedEffect(Unit) {
        delay(1200)
        isSurfaceDetected = true
        delay(800)
        isModelPlaced = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("ar_experience_container")
    ) {
        // 1. Camera Viewfinder or High-tech AR Simulation backdrop
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = CameraPreview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Simulated AR Pass-through Chamber
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF020617))
                        )
                    )
            ) {
                // Background grid lines simulating environmental depth
                Monument3DCanvas(
                    meshType = "fortress",
                    hotspots = emptyList(),
                    enableHotspotSelection = false,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                )
            }
        }

        // 2. Holographic Plane Detection & Placement Grid
        if (!isModelPlaced) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .border(2.dp, SkyArGlow, CircleShape)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = ImperialGoldLight,
                            strokeWidth = 3.dp,
                            modifier = Modifier.fillMaxSize()
                        )
                        Icon(
                            imageVector = Icons.Default.FilterCenterFocus,
                            contentDescription = null,
                            tint = SkyArGlow,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Surface(
                        color = ObsidianCard.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 6.dp
                    ) {
                        Text(
                            text = if (!isSurfaceDetected) "Scanning horizontal ground surface..." else "Surface detected! Anchoring 3D monument...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // 3. Placed 3D Augmented Monument in Real Space
        if (isModelPlaced) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(modelOffsetX.roundToInt(), modelOffsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            modelScale = (modelScale * zoom).coerceIn(0.4f, 2.8f)
                            modelOffsetX += pan.x
                            modelOffsetY += pan.y
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Monument3DCanvas(
                    meshType = site.meshType,
                    hotspots = site.hotspots,
                    enableHotspotSelection = true,
                    modifier = Modifier
                        .fillMaxWidth(0.92f * modelScale)
                        .fillMaxHeight(0.68f * modelScale)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
        }

        // 4. AR Header Bar
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(ObsidianCard.copy(alpha = 0.85f), CircleShape)
                    .testTag("ar_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Surface(
                color = ObsidianCard.copy(alpha = 0.85f),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SkyArGlow.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(EmeraldPreservation, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE AR: ${site.name}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(
                onClick = { showInfoOverlay = !showInfoOverlay },
                modifier = Modifier
                    .background(ObsidianCard.copy(alpha = 0.85f), CircleShape)
                    .testTag("ar_info_toggle")
            ) {
                Icon(
                    imageVector = if (showInfoOverlay) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Info",
                    tint = ImperialGoldLight
                )
            }
        }

        // 5. AR Bottom HUD (Historical Summary & Audio Narration & Camera Snapshot)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AnimatedVisibility(visible = showInfoOverlay) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ObsidianCard.copy(alpha = 0.92f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = site.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${site.period} • ${site.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ImperialGoldLight
                                )
                            }
                            AssistChip(
                                onClick = { },
                                label = { Text("3D Spatial Audio", fontSize = 10.sp, color = SkyArGlow) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = SkyArGlow,
                                        modifier = Modifier.size(12.dp)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = site.shortDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment,
                            lineHeight = 17.sp,
                            maxLines = 3
                        )
                    }
                }
            }

            // Action HUD Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Pose
                FloatingActionButton(
                    onClick = {
                        modelScale = 1.0f
                        modelOffsetX = 0f
                        modelOffsetY = 0f
                    },
                    containerColor = ObsidianCard,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Center Object")
                }

                // AR Snapshot Camera Shutter
                FloatingActionButton(
                    onClick = { showSnapshotAlert = true },
                    containerColor = Terracotta,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .testTag("ar_capture_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Capture AR Memory",
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Permission or Re-scan
                FloatingActionButton(
                    onClick = {
                        if (!hasCameraPermission) {
                            launcher.launch(Manifest.permission.CAMERA)
                        } else {
                            isModelPlaced = false
                            isSurfaceDetected = false
                        }
                    },
                    containerColor = ObsidianCard,
                    contentColor = SkyArGlow,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (!hasCameraPermission) Icons.Default.Videocam else Icons.Default.QrCodeScanner,
                        contentDescription = "Rescan Surface"
                    )
                }
            }
        }

        // Snapshot Saved Dialog
        if (showSnapshotAlert) {
            AlertDialog(
                onDismissRequest = { showSnapshotAlert = false },
                title = { Text("AR Monument Captured!", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Your augmented reality photograph with ${site.name} has been archived in your Heritage Profile gallery.")
                },
                confirmButton = {
                    Button(
                        onClick = { showSnapshotAlert = false },
                        colors = ButtonDefaults.buttonColors(containerColor = HeritageNavy)
                    ) {
                        Text("Awesome")
                    }
                }
            )
        }
    }
}
