package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.model.AudioContent
import com.example.model.AppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AudioGuidePlayer(
    siteName: String,
    audioContent: AudioContent,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableIntStateOf(0) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var selectedLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }

    val totalDuration = audioContent.durationSeconds

    // Playback ticker
    LaunchedEffect(isPlaying, playbackSpeed) {
        while (isPlaying) {
            delay((1000 / playbackSpeed).toLong())
            if (currentSeconds < totalDuration) {
                currentSeconds += 1
            } else {
                isPlaying = false
                currentSeconds = 0
            }
        }
    }

    val activeTranscript = when (selectedLanguage) {
        AppLanguage.ENGLISH -> audioContent.englishTranscript
        AppLanguage.MARATHI -> audioContent.marathiTranscript
        AppLanguage.HINDI -> audioContent.hindiTranscript
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_guide_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ObsidianCard
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row & Language Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(ImperialGold.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = ImperialGoldLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Audio Narration Guide",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = siteName,
                            style = MaterialTheme.typography.bodySmall,
                            color = ImperialGoldLight
                        )
                    }
                }

                // Language Selector Chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    AppLanguage.entries.forEach { lang ->
                        val isLangSelected = selectedLanguage == lang
                        FilterChip(
                            selected = isLangSelected,
                            onClick = { selectedLanguage = lang },
                            label = {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isLangSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isLangSelected) Obsidian else Parchment
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ImperialGoldLight,
                                containerColor = ObsidianSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }

            // Progress Slider
            Column {
                Slider(
                    value = currentSeconds.toFloat(),
                    onValueChange = { currentSeconds = it.toInt() },
                    valueRange = 0f..totalDuration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = ImperialGoldLight,
                        activeTrackColor = ImperialGold,
                        inactiveTrackColor = ObsidianSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_seekbar")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentSeconds),
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Text(
                        text = formatTime(totalDuration),
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            // Playback Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind 10s
                IconButton(
                    onClick = { currentSeconds = (currentSeconds - 10).coerceAtLeast(0) },
                    modifier = Modifier.testTag("audio_rewind")
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind 10 seconds",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Play / Pause FAB
                FilledIconButton(
                    onClick = { isPlaying = !isPlaying },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = ImperialGoldLight,
                        contentColor = Obsidian
                    ),
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("audio_play_pause")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Forward 10s
                IconButton(
                    onClick = { currentSeconds = (currentSeconds + 10).coerceAtMost(totalDuration) },
                    modifier = Modifier.testTag("audio_forward")
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10 seconds",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Speed Switcher
                TextButton(
                    onClick = {
                        playbackSpeed = when (playbackSpeed) {
                            1.0f -> 1.25f
                            1.25f -> 1.5f
                            1.5f -> 2.0f
                            else -> 1.0f
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = ObsidianSurface)
                ) {
                    Text(
                        text = "${playbackSpeed}x",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGoldLight
                    )
                }
            }

            // Synced Live Transcript Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Subtitles,
                        contentDescription = null,
                        tint = ImperialGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Official Historic Narration Transcript (${selectedLanguage.displayName})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ImperialGoldLight
                    )
                }

                AnimatedContent(targetState = activeTranscript, label = "transcript_transition") { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Parchment,
                        lineHeight = 20.sp,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
