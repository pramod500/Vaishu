package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Hotspot
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.*

data class Point3D(val x: Float, val y: Float, val z: Float)
data class Polygon3D(
    val vertices: List<Point3D>,
    val baseColor: Color,
    val normal: Point3D
)

@Composable
fun Monument3DCanvas(
    meshType: String,
    hotspots: List<Hotspot>,
    modifier: Modifier = Modifier,
    enableHotspotSelection: Boolean = true,
    onHotspotClicked: ((Hotspot) -> Unit)? = null
) {
    var yaw by remember { mutableFloatStateOf(25f) }
    var pitch by remember { mutableFloatStateOf(-15f) }
    var zoom by remember { mutableFloatStateOf(1.0f) }
    var isAutoRotate by remember { mutableStateOf(true) }
    var isWireframe by remember { mutableStateOf(false) }
    var selectedHotspot by remember { mutableStateOf<Hotspot?>(null) }

    // Auto-rotation coroutine
    LaunchedEffect(isAutoRotate) {
        while (isAutoRotate) {
            delay(16)
            yaw = (yaw + 0.4f) % 360f
        }
    }

    val polygons = remember(meshType) { generateMonumentMesh(meshType) }

    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(HeritageNavyLight.copy(alpha = 0.85f), Obsidian),
                    radius = 800f
                )
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, gestureZoom, _ ->
                    zoom = (zoom * gestureZoom).coerceIn(0.5f, 2.5f)
                    yaw = (yaw + pan.x * 0.5f) % 360f
                    pitch = (pitch - pan.y * 0.5f).coerceIn(-75f, 75f)
                    if (pan.getDistance() > 3f) {
                        isAutoRotate = false
                    }
                }
            }
            .testTag("monument_3d_canvas")
    ) {
        // 3D Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val baseScale = min(size.width, size.height) * 0.38f * zoom

            val radYaw = Math.toRadians(yaw.toDouble()).toFloat()
            val radPitch = Math.toRadians(pitch.toDouble()).toFloat()

            val cosY = cos(radYaw)
            val sinY = sin(radYaw)
            val cosP = cos(radPitch)
            val sinP = sin(radPitch)

            fun projectPoint(p: Point3D): Triple<Float, Float, Float> {
                // Rotate around Y (Yaw)
                val x1 = p.x * cosY + p.z * sinY
                val y1 = p.y
                val z1 = -p.x * sinY + p.z * cosY

                // Rotate around X (Pitch)
                val x2 = x1
                val y2 = y1 * cosP - z1 * sinP
                val z2 = y1 * sinP + z1 * cosP

                // Isometric / Weak perspective projection
                val distance = 3.5f
                val perspective = distance / (distance + z2 * 0.4f)
                val screenX = cx + x2 * baseScale * perspective
                val screenY = cy - y2 * baseScale * perspective // invert Y for screen coords
                return Triple(screenX, screenY, z2)
            }

            // Directional sunlight vector from top-left front
            val lightDir = Point3D(0.5f, 0.8f, 0.4f)
            val lightLen = sqrt(lightDir.x * lightDir.x + lightDir.y * lightDir.y + lightDir.z * lightDir.z)
            val normLight = Point3D(lightDir.x / lightLen, lightDir.y / lightLen, lightDir.z / lightLen)

            // Sort polygons by average transformed Z depth (Painter's Algorithm)
            val projectedPolys = polygons.map { poly ->
                val projVertices = poly.vertices.map { projectPoint(it) }
                val avgZ = projVertices.map { it.third }.average().toFloat()

                // Rotate polygon normal to world coordinates for dynamic lighting
                val n = poly.normal
                val nx1 = n.x * cosY + n.z * sinY
                val ny1 = n.y
                val nz1 = -n.x * sinY + n.z * cosY
                val nx2 = nx1
                val ny2 = ny1 * cosP - nz1 * sinP
                val nz2 = ny1 * sinP + nz1 * cosP

                val dot = max(0.15f, (nx2 * normLight.x + ny2 * normLight.y + nz2 * normLight.z))
                Pair(poly to projVertices, avgZ to dot)
            }.sortedBy { it.second.first }

            // Draw 3D Polygons
            projectedPolys.forEach { (polyData, lightingData) ->
                val (poly, vertices) = polyData
                val (_, dotLight) = lightingData
                if (vertices.size >= 3) {
                    val path = Path().apply {
                        moveTo(vertices[0].first, vertices[0].second)
                        for (i in 1 until vertices.size) {
                            lineTo(vertices[i].first, vertices[i].second)
                        }
                        close()
                    }

                    if (isWireframe) {
                        drawPath(
                            path = path,
                            color = ImperialGoldLight.copy(alpha = 0.8f),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
                        )
                    } else {
                        // Apply lighting shade to baseColor
                        val shadedColor = poly.baseColor.let { c ->
                            Color(
                                red = (c.red * (0.35f + 0.65f * dotLight)).coerceIn(0f, 1f),
                                green = (c.green * (0.35f + 0.65f * dotLight)).coerceIn(0f, 1f),
                                blue = (c.blue * (0.35f + 0.65f * dotLight)).coerceIn(0f, 1f),
                                alpha = c.alpha
                            )
                        }
                        drawPath(path = path, color = shadedColor)
                        drawPath(
                            path = path,
                            color = Color.Black.copy(alpha = 0.25f),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }

            // Draw Ground Holographic Grid Lines
            val gridRadius = baseScale * 1.3f
            drawCircle(
                color = SkyArCyan.copy(alpha = 0.25f),
                radius = gridRadius,
                center = Offset(cx, cy + baseScale * 0.45f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
                )
            )
            drawCircle(
                color = ImperialGold.copy(alpha = 0.2f),
                radius = gridRadius * 0.65f,
                center = Offset(cx, cy + baseScale * 0.45f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
            )

            // Render 3D Hotspot Badges
            if (enableHotspotSelection) {
                hotspots.forEach { hs ->
                    val (hx, hy, hz) = projectPoint(Point3D(hs.x, hs.y, hs.z))
                    val isFront = hz > -0.8f // Only show if not fully occluded
                    if (isFront) {
                        // Glowing target ring
                        drawCircle(
                            color = ImperialGoldLight.copy(alpha = 0.4f),
                            radius = 16.dp.toPx(),
                            center = Offset(hx, hy)
                        )
                        drawCircle(
                            color = TerracottaLight,
                            radius = 7.dp.toPx(),
                            center = Offset(hx, hy)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = Offset(hx, hy)
                        )
                    }
                }
            }
        }

        // Overlay Controls (Rotate, Reset, Wireframe, Info Hotspot badges)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { isAutoRotate = !isAutoRotate },
                modifier = Modifier
                    .background(ObsidianCard.copy(alpha = 0.85f), CircleShape)
                    .size(40.dp)
                    .testTag("toggle_auto_rotate")
            ) {
                Icon(
                    imageVector = if (isAutoRotate) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Toggle Auto Rotation",
                    tint = ImperialGoldLight
                )
            }

            IconButton(
                onClick = { isWireframe = !isWireframe },
                modifier = Modifier
                    .background(ObsidianCard.copy(alpha = 0.85f), CircleShape)
                    .size(40.dp)
                    .testTag("toggle_wireframe")
            ) {
                Icon(
                    imageVector = Icons.Default.ViewInAr,
                    contentDescription = "Toggle Wireframe Mode",
                    tint = if (isWireframe) SkyArGlow else Color.White
                )
            }

            IconButton(
                onClick = {
                    yaw = 25f
                    pitch = -15f
                    zoom = 1.0f
                },
                modifier = Modifier
                    .background(ObsidianCard.copy(alpha = 0.85f), CircleShape)
                    .size(40.dp)
                    .testTag("reset_3d_view")
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Reset Camera View",
                    tint = Color.White
                )
            }
        }

        // Hotspots Quick Select Chips at bottom of viewer
        if (hotspots.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                hotspots.forEach { hs ->
                    val isSelected = selectedHotspot?.id == hs.id
                    AssistChip(
                        onClick = {
                            selectedHotspot = if (isSelected) null else hs
                            onHotspotClicked?.invoke(hs)
                        },
                        label = {
                            Text(
                                text = hs.title,
                                maxLines = 1,
                                fontSize = 11.sp,
                                color = if (isSelected) Obsidian else Color.White
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (isSelected) Obsidian else ImperialGoldLight
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) ImperialGoldLight else ObsidianCard.copy(alpha = 0.85f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("hotspot_chip_${hs.id}")
                    )
                }
            }
        }

        // Active Hotspot Info Card Dialog
        AnimatedVisibility(
            visible = selectedHotspot != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            selectedHotspot?.let { hs ->
                Card(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .testTag("hotspot_info_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ObsidianCard.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = hs.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            IconButton(
                                onClick = { selectedHotspot = null },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hs.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// 3D Procedural Architectural Geometry Generator
fun generateMonumentMesh(meshType: String): List<Polygon3D> {
    val list = mutableListOf<Polygon3D>()

    when (meshType) {
        "fortress" -> {
            // Hill Fortress / Bastion Ramparts
            // Base Mountain Foundation
            val rockColor = Color(0xFF6B584D)
            val wallColor = Color(0xFFB59A7A)
            val parapetColor = Color(0xFFD6C2A7)
            val gateColor = Color(0xFF8B4513)

            // Terraced base platform
            createCube(list, -0.9f, -0.7f, -0.9f, 0.9f, -0.4f, 0.9f, rockColor)
            // Main stone curtain walls
            createCube(list, -0.6f, -0.4f, -0.6f, 0.6f, 0.2f, 0.6f, wallColor)

            // Bastion towers at 4 corners
            val bastions = listOf(
                Pair(-0.65f, -0.65f),
                Pair(0.65f, -0.65f),
                Pair(-0.65f, 0.65f),
                Pair(0.65f, 0.65f)
            )
            bastions.forEach { (bx, bz) ->
                createCylinderPrism(list, bx, -0.4f, bz, 0.18f, 0.75f, 8, parapetColor)
            }

            // Central Rajsabha Throne Pavilion
            createCube(list, -0.25f, 0.2f, -0.25f, 0.25f, 0.5f, 0.25f, wallColor)
            // Pavilion Dome / Chhatri
            createPyramid(list, 0f, 0.5f, 0f, 0.35f, 0.35f, ImperialGold)

            // Grand Maha Darwaza Archway
            createCube(list, -0.12f, -0.4f, 0.6f, 0.12f, -0.1f, 0.65f, gateColor)
        }
        "temple" -> {
            // Monolithic Dravidian / Nagara Temple with Shikhara Spire
            val stoneColor = Color(0xFF9E8A78)
            val goldColor = Color(0xFFD97706)
            val sanctumColor = Color(0xFF7D6C5B)

            // Stepped Plinth (Jagati)
            createCube(list, -0.85f, -0.6f, -0.85f, 0.85f, -0.45f, 0.85f, stoneColor)
            createCube(list, -0.7f, -0.45f, -0.7f, 0.7f, -0.3f, 0.7f, stoneColor)

            // Mandapa (Pillared Hall)
            createCube(list, -0.5f, -0.3f, 0.0f, 0.5f, 0.1f, 0.6f, sanctumColor)
            // Mandapa stepped roof
            createPyramid(list, 0.0f, 0.1f, 0.3f, 0.55f, 0.25f, stoneColor)

            // Garbhagriha & Towering Vimana/Shikhara Spire
            createCube(list, -0.4f, -0.3f, -0.6f, 0.4f, 0.2f, 0.0f, sanctumColor)
            // Stepped Shikhara tiers
            createCube(list, -0.32f, 0.2f, -0.52f, 0.32f, 0.45f, -0.08f, stoneColor)
            createCube(list, -0.22f, 0.45f, -0.42f, 0.22f, 0.7f, -0.18f, stoneColor)
            // Amalaka stone disk and golden Kalasha finial
            createCube(list, -0.12f, 0.7f, -0.32f, 0.12f, 0.82f, -0.28f, goldColor)
            createPyramid(list, 0.0f, 0.82f, -0.3f, 0.12f, 0.2f, ImperialGoldLight)

            // Front Nandi Mandapa pavilion
            createCube(list, -0.18f, -0.3f, 0.65f, 0.18f, -0.05f, 0.85f, stoneColor)
        }
        "stupa" -> {
            // Sanchi Great Stupa: Hemispherical Dome, Medhi terrace, Harmika, Chhatra & 4 Toranas
            val stupaBaseColor = Color(0xFFBCAAA4)
            val domeColor = Color(0xFFD7CCC8)
            val toranaColor = Color(0xFFA1887F)
            val goldUmbrella = Color(0xFFF59E0B)

            // Circular terrace Medhi
            createCylinderPrism(list, 0f, -0.6f, 0f, 0.75f, 0.2f, 12, stupaBaseColor)
            // Hemispherical Anda Dome (modeled with stacked faceted tiers)
            createCylinderPrism(list, 0f, -0.4f, 0f, 0.65f, 0.25f, 12, domeColor)
            createCylinderPrism(list, 0f, -0.15f, 0f, 0.52f, 0.25f, 12, domeColor)
            createCylinderPrism(list, 0f, 0.1f, 0f, 0.35f, 0.2f, 12, domeColor)

            // Harmika square railing
            createCube(list, -0.15f, 0.3f, -0.15f, 0.15f, 0.45f, 0.15f, stupaBaseColor)
            // Central Yasti mast & Triple Chhatra umbrellas
            createCylinderPrism(list, 0f, 0.45f, 0f, 0.04f, 0.35f, 6, toranaColor)
            createCylinderPrism(list, 0f, 0.6f, 0f, 0.18f, 0.03f, 8, goldUmbrella)
            createCylinderPrism(list, 0f, 0.68f, 0f, 0.14f, 0.03f, 8, goldUmbrella)
            createCylinderPrism(list, 0f, 0.75f, 0f, 0.10f, 0.03f, 8, goldUmbrella)

            // North Torana ceremonial gateway
            createCube(list, -0.4f, -0.6f, 0.8f, -0.32f, 0.25f, 0.86f, toranaColor)
            createCube(list, 0.32f, -0.6f, 0.8f, 0.4f, 0.25f, 0.86f, toranaColor)
            // Triple architraves
            createCube(list, -0.5f, 0.1f, 0.81f, 0.5f, 0.15f, 0.85f, toranaColor)
            createCube(list, -0.48f, 0.18f, 0.81f, 0.48f, 0.23f, 0.85f, toranaColor)
            createCube(list, -0.45f, 0.26f, 0.81f, 0.45f, 0.31f, 0.85f, toranaColor)
        }
        "arch" -> {
            // Gateway of India: Grand Indo-Saracenic Triumphal Archway
            val basaltColor = Color(0xFFC7B198)
            val darkBasalt = Color(0xFFA69078)
            val domeColor = Color(0xFF8C7355)

            // Foundation Quay
            createCube(list, -0.8f, -0.6f, -0.5f, 0.8f, -0.45f, 0.5f, darkBasalt)

            // Left & Right Monumental Pylons
            createCube(list, -0.65f, -0.45f, -0.35f, -0.2f, 0.4f, 0.35f, basaltColor)
            createCube(list, 0.2f, -0.45f, -0.35f, 0.65f, 0.4f, 0.35f, basaltColor)

            // Arch Lintel & Upper Spandrel Wall
            createCube(list, -0.65f, 0.2f, -0.35f, 0.65f, 0.55f, 0.35f, basaltColor)

            // Corner decorative turrets / minarets (4 corners)
            val turrets = listOf(
                Pair(-0.62f, -0.32f),
                Pair(-0.62f, 0.32f),
                Pair(0.62f, -0.32f),
                Pair(0.62f, 0.32f)
            )
            turrets.forEach { (tx, tz) ->
                createCylinderPrism(list, tx, -0.45f, tz, 0.08f, 1.15f, 6, darkBasalt)
                createPyramid(list, tx, 0.7f, tz, 0.1f, 0.12f, ImperialGold)
            }

            // Central Grand Dome
            createCylinderPrism(list, 0f, 0.55f, 0f, 0.28f, 0.15f, 8, domeColor)
            createPyramid(list, 0f, 0.7f, 0f, 0.28f, 0.22f, ImperialGold)
        }
        "chariot" -> {
            // Konark / Hampi Stone Chariot
            val chariotStone = Color(0xFF9E8E7D)
            val wheelStone = Color(0xFFD4AF37)
            val darkRock = Color(0xFF6E5D4F)

            // Platform Base
            createCube(list, -0.7f, -0.45f, -0.7f, 0.7f, -0.25f, 0.7f, darkRock)

            // 4 Massive Carved Astronomical Wheels
            val wheelPositions = listOf(
                Triple(-0.65f, -0.35f, -0.45f),
                Triple(-0.65f, -0.35f, 0.45f),
                Triple(0.65f, -0.35f, -0.45f),
                Triple(0.65f, -0.35f, 0.45f)
            )
            wheelPositions.forEach { (wx, wy, wz) ->
                createCylinderPrism(list, wx, wy, wz, 0.22f, 0.06f, 10, wheelStone)
            }

            // Chariot Body Sanctuary
            createCube(list, -0.45f, -0.25f, -0.45f, 0.45f, 0.2f, 0.45f, chariotStone)
            // Stepped Chariot Vimana Spire
            createCube(list, -0.35f, 0.2f, -0.35f, 0.35f, 0.45f, 0.35f, chariotStone)
            createPyramid(list, 0f, 0.45f, 0f, 0.35f, 0.3f, wheelStone)

            // Front Elephant / Horse Steeds
            createCube(list, -0.2f, -0.45f, 0.75f, -0.05f, -0.15f, 0.95f, chariotStone)
            createCube(list, 0.05f, -0.45f, 0.75f, 0.2f, -0.15f, 0.95f, chariotStone)
        }
        else -> {
            // Ancient Rock-Cut Cave Monolith (Ajanta/Ellora)
            val caveBasalt = Color(0xFF5A4D41)
            val caveInterior = Color(0xFF2B2118)
            val pillarColor = Color(0xFF8D7B68)

            // Mountain cliff face surrounding cave
            createCube(list, -0.9f, -0.7f, -0.8f, 0.9f, 0.7f, -0.4f, caveBasalt)
            // Carved Cave Verandah Portal
            createCube(list, -0.65f, -0.5f, -0.4f, 0.65f, 0.3f, 0.1f, caveInterior)

            // Rock-cut facade pillars (4 pillars)
            listOf(-0.45f, -0.15f, 0.15f, 0.45f).forEach { px ->
                createCylinderPrism(list, px, -0.5f, 0.1f, 0.07f, 0.75f, 6, pillarColor)
            }
            // Chaitya Arch above entrance
            createPyramid(list, 0f, 0.3f, 0.0f, 0.5f, 0.3f, pillarColor)
        }
    }

    return list
}

// Geometric 3D Helpers
private fun createCube(
    list: MutableList<Polygon3D>,
    x1: Float, y1: Float, z1: Float,
    x2: Float, y2: Float, z2: Float,
    color: Color
) {
    // Front (+Z)
    list.add(
        Polygon3D(
            listOf(Point3D(x1, y1, z2), Point3D(x2, y1, z2), Point3D(x2, y2, z2), Point3D(x1, y2, z2)),
            color,
            Point3D(0f, 0f, 1f)
        )
    )
    // Back (-Z)
    list.add(
        Polygon3D(
            listOf(Point3D(x2, y1, z1), Point3D(x1, y1, z1), Point3D(x1, y2, z1), Point3D(x2, y2, z1)),
            color.copy(alpha = 0.9f),
            Point3D(0f, 0f, -1f)
        )
    )
    // Top (+Y)
    list.add(
        Polygon3D(
            listOf(Point3D(x1, y2, z2), Point3D(x2, y2, z2), Point3D(x2, y2, z1), Point3D(x1, y2, z1)),
            color,
            Point3D(0f, 1f, 0f)
        )
    )
    // Bottom (-Y)
    list.add(
        Polygon3D(
            listOf(Point3D(x1, y1, z1), Point3D(x2, y1, z1), Point3D(x2, y1, z2), Point3D(x1, y1, z2)),
            color.copy(alpha = 0.7f),
            Point3D(0f, -1f, 0f)
        )
    )
    // Left (-X)
    list.add(
        Polygon3D(
            listOf(Point3D(x1, y1, z1), Point3D(x1, y1, z2), Point3D(x1, y2, z2), Point3D(x1, y2, z1)),
            color.copy(alpha = 0.85f),
            Point3D(-1f, 0f, 0f)
        )
    )
    // Right (+X)
    list.add(
        Polygon3D(
            listOf(Point3D(x2, y1, z2), Point3D(x2, y1, z1), Point3D(x2, y2, z1), Point3D(x2, y2, z2)),
            color.copy(alpha = 0.95f),
            Point3D(1f, 0f, 0f)
        )
    )
}

private fun createPyramid(
    list: MutableList<Polygon3D>,
    cx: Float, cy: Float, cz: Float,
    halfBase: Float, height: Float,
    color: Color
) {
    val apex = Point3D(cx, cy + height, cz)
    val p1 = Point3D(cx - halfBase, cy, cz - halfBase)
    val p2 = Point3D(cx + halfBase, cy, cz - halfBase)
    val p3 = Point3D(cx + halfBase, cy, cz + halfBase)
    val p4 = Point3D(cx - halfBase, cy, cz + halfBase)

    // Base
    list.add(Polygon3D(listOf(p1, p2, p3, p4), color.copy(alpha = 0.8f), Point3D(0f, -1f, 0f)))
    // 4 Triangular Sides
    list.add(Polygon3D(listOf(p1, p2, apex), color, Point3D(0f, 0.7f, -0.7f)))
    list.add(Polygon3D(listOf(p2, p3, apex), color.copy(alpha = 0.95f), Point3D(0.7f, 0.7f, 0f)))
    list.add(Polygon3D(listOf(p3, p4, apex), color.copy(alpha = 0.85f), Point3D(0f, 0.7f, 0.7f)))
    list.add(Polygon3D(listOf(p4, p1, apex), color.copy(alpha = 0.9f), Point3D(-0.7f, 0.7f, 0f)))
}

private fun createCylinderPrism(
    list: MutableList<Polygon3D>,
    cx: Float, cy: Float, cz: Float,
    radius: Float, height: Float,
    segments: Int,
    color: Color
) {
    val topVertices = mutableListOf<Point3D>()
    val bottomVertices = mutableListOf<Point3D>()

    for (i in 0 until segments) {
        val angle = (2 * Math.PI * i / segments).toFloat()
        val x = cx + radius * cos(angle)
        val z = cz + radius * sin(angle)
        bottomVertices.add(Point3D(x, cy, z))
        topVertices.add(Point3D(x, cy + height, z))
    }

    // Top face
    list.add(Polygon3D(topVertices, color, Point3D(0f, 1f, 0f)))
    // Bottom face
    list.add(Polygon3D(bottomVertices.reversed(), color.copy(alpha = 0.7f), Point3D(0f, -1f, 0f)))

    // Side quad faces
    for (i in 0 until segments) {
        val next = (i + 1) % segments
        val p1 = bottomVertices[i]
        val p2 = bottomVertices[next]
        val p3 = topVertices[next]
        val p4 = topVertices[i]

        val midAngle = (2 * Math.PI * (i + 0.5f) / segments).toFloat()
        val normal = Point3D(cos(midAngle), 0f, sin(midAngle))
        list.add(Polygon3D(listOf(p1, p2, p3, p4), color.copy(alpha = 0.9f), normal))
    }
}
