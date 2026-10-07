package com.example.ui.components

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HeritageRepository
import com.example.model.QuizQuestion
import com.example.ui.theme.*

@Composable
fun EducationSection(
    questions: List<QuizQuestion>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Interactive Quiz, 1: Architectural Glossary, 2: Conservation Lessons

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("education_section")
    ) {
        // Tab Switcher
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = ObsidianCard,
            contentColor = ImperialGoldLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Heritage Quiz", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Architecture", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Conservation", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        when (selectedTab) {
            0 -> QuizPlayView(questions = questions)
            1 -> ArchitecturalGlossaryView()
            2 -> ConservationLessonsView()
        }
    }
}

@Composable
fun QuizPlayView(questions: List<QuizQuestion>) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions.getOrNull(currentIndex)

    if (isQuizCompleted || currentQ == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(ImperialGold.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = ImperialGoldLight,
                    modifier = Modifier.size(54.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Quiz Master Challenge Completed!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You scored $score out of ${questions.size} points (${(score * 100) / questions.size}%)",
                style = MaterialTheme.typography.bodyLarge,
                color = SkyArGlow
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    HeritageRepository.recordQuizScore(score)
                    currentIndex = 0
                    selectedOptionIndex = null
                    isSubmitted = false
                    isQuizCompleted = false
                    score = 0
                },
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("restart_quiz_button")
            ) {
                Text("Retake Heritage Quiz", fontWeight = FontWeight.Bold)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        fontWeight = FontWeight.Bold,
                        color = ImperialGoldLight,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Current Score: $score",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ImperialGold,
                    trackColor = ObsidianSurface
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Surface(
                            color = HeritageNavy,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = currentQ.siteName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SkyArGlow,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentQ.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            items(currentQ.options.indices.toList()) { idx ->
                val optionText = currentQ.options[idx]
                val isSelected = selectedOptionIndex == idx
                val isCorrect = idx == currentQ.correctIndex

                val borderColor = when {
                    isSubmitted && isCorrect -> EmeraldPreservation
                    isSubmitted && isSelected && !isCorrect -> Color.Red
                    isSelected -> ImperialGoldLight
                    else -> ObsidianCard
                }

                val backgroundColor = when {
                    isSubmitted && isCorrect -> EmeraldPreservation.copy(alpha = 0.2f)
                    isSubmitted && isSelected && !isCorrect -> Color.Red.copy(alpha = 0.2f)
                    isSelected -> ImperialGold.copy(alpha = 0.15f)
                    else -> ObsidianCard
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSubmitted) { selectedOptionIndex = idx }
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    if (isSelected) ImperialGoldLight else ObsidianSurface,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + idx).toString(),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Obsidian else Color.White,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Explanation & Next Action
            item {
                AnimatedVisibility(visible = isSubmitted) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ObsidianSurface, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (selectedOptionIndex == currentQ.correctIndex) "✓ Correct Answer!" else "✗ Incorrect",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedOptionIndex == currentQ.correctIndex) EmeraldPreservation else Color(0xFFFF6B6B),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Parchment,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!isSubmitted) {
                        Button(
                            onClick = {
                                if (selectedOptionIndex != null) {
                                    isSubmitted = true
                                    if (selectedOptionIndex == currentQ.correctIndex) {
                                        score += 1
                                    }
                                }
                            },
                            enabled = selectedOptionIndex != null,
                            colors = ButtonDefaults.buttonColors(containerColor = ImperialGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("submit_answer_button")
                        ) {
                            Text("Submit Answer", fontWeight = FontWeight.Bold, color = Obsidian)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentIndex + 1 < questions.size) {
                                    currentIndex += 1
                                    selectedOptionIndex = null
                                    isSubmitted = false
                                } else {
                                    HeritageRepository.recordQuizScore(score)
                                    isQuizCompleted = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("next_question_button")
                        ) {
                            Text(if (currentIndex + 1 < questions.size) "Next Question →" else "See Results ★", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArchitecturalGlossaryView() {
    val terms = listOf(
        Pair("Chaitya Griha", "A rock-cut or structural Buddhist sanctuary hall featuring a barrel-vaulted ceiling and a stupa placed at the apse for congregational circumambulation."),
        Pair("Shikhara / Vimana", "The soaring pyramidal or curvilinear spire rising majestically above the inner sanctum (Garbhagriha) of a Hindu temple representing Mount Meru."),
        Pair("Torana Gateway", "A ceremonial freestanding stone gateway composed of two upright pillars supporting two or three horizontal architraves, often carved with celestial narratives."),
        Pair("Bastion (Buruj)", "A protruding defensive semicircular or polygonal stone tower projecting outward from the fort curtain wall to provide defensive crossfire."),
        Pair("Jali Latticework", "Perforated stone screens with intricate geometric or floral arabesque piercings that filter sunlight, cool breezes, and create dramatic shadow patterns."),
        Pair("Mandapa", "Pillared public assembly hall preceding the sanctum sanctorum in temple architecture, used for devotional music, sacred dances, and religious discourses.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(terms) { (term, def) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(term, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ImperialGoldLight)
                    Spacer(Modifier.height(6.dp))
                    Text(def, style = MaterialTheme.typography.bodyMedium, color = Parchment, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ConservationLessonsView() {
    val principles = listOf(
        Triple("Minimum Intervention", "Preserve as much original authentic historic fabric as possible. Any restorative repair must strictly respect the original artisans' materials and structural integrity.", Icons.Default.Handyman),
        Triple("Reversibility of Repairs", "New materials applied in structural stabilization should be capable of being removed in the future without damaging the ancient substrate.", Icons.Default.Undo),
        Triple("LiDAR & 3D AR Documentation", "Before initiating physical restoration, complete non-destructive 3D laser photogrammetry scanning must be created to freeze digital geometry for eternity.", Icons.Default.ViewInAr),
        Triple("Respect for Indigenous Science", "Traditional mortars such as lime, jaggery, and bel fruit extracts offer superior breathability compared to modern Portland cement which traps moisture.", Icons.Default.NaturePeople)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(principles) { (title, desc, icon) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(EmeraldPreservation.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = EmeraldPreservation, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(4.dp))
                        Text(desc, style = MaterialTheme.typography.bodySmall, color = Parchment, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}
