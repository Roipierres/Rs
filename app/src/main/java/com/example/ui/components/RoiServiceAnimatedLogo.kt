package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KhadamatiAmberTertiary
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBlueLight
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import kotlin.math.cos
import kotlin.math.sin

/**
 * Compact animated RS Logo for TopAppBar
 */
@Composable
fun RoiServiceCompactLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CompactLogoAnimation")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Canvas Hexagon / Shield Ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.toPx()
            val h = size.toPx()
            val strokeWidth = 2.5f.dp.toPx()

            val gradientBrush = Brush.sweepGradient(
                listOf(
                    Color(0xFFFFD54F).copy(alpha = glowAlpha),
                    Color(0xFF00E5FF).copy(alpha = glowAlpha),
                    Color(0xFF2979FF).copy(alpha = glowAlpha),
                    Color(0xFFFFD54F).copy(alpha = glowAlpha)
                )
            )

            // Rounded Shield/Hexagon path
            val path = Path().apply {
                moveTo(w * 0.5f, h * 0.05f)
                lineTo(w * 0.92f, h * 0.28f)
                lineTo(w * 0.92f, h * 0.72f)
                lineTo(w * 0.5f, h * 0.95f)
                lineTo(w * 0.08f, h * 0.72f)
                lineTo(w * 0.08f, h * 0.28f)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF002171)),
                    center = Offset(w * 0.5f, h * 0.5f)
                )
            )

            drawPath(
                path = path,
                brush = gradientBrush,
                style = Stroke(width = strokeWidth)
            )
        }

        // Lettermark "RS"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "R",
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.42f).sp,
                color = Color(0xFFFFD54F),
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-1).sp
            )
            Text(
                text = "S",
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.42f).sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-1).sp
            )
        }
    }
}

/**
 * Hero Animated Logo with Crown / Diamond accent and "Roi Service" display typography
 */
@Composable
fun RoiServiceHeroBadge(
    modifier: Modifier = Modifier,
    onLogoClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeroLogoAnimation")

    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HaloRotation"
    )

    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingScale"
    )

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerOffset"
    )

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable(
                enabled = onLogoClick != null,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onLogoClick?.invoke() },
        shape = RoundedCornerShape(22.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0A2342),
                            Color(0xFF0F3A66),
                            Color(0xFF071C35)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated Crest Container
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .scale(breathingScale),
                    contentAlignment = Alignment.Center
                ) {
                    // Rotating Glow Aura
                    Canvas(
                        modifier = Modifier
                            .size(70.dp)
                            .rotate(haloRotation)
                    ) {
                        val stroke = 3.dp.toPx()
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFFFFD54F),
                                    Color(0xFF00E5FF),
                                    Color(0xFF2979FF),
                                    Color(0xFFFFD54F)
                                )
                            ),
                            style = Stroke(width = stroke)
                        )
                    }

                    // Inner Hexagon Shield
                    Canvas(modifier = Modifier.size(54.dp)) {
                        val w = size.width
                        val h = size.height

                        val shieldPath = Path().apply {
                            moveTo(w * 0.5f, 0f)
                            lineTo(w, h * 0.25f)
                            lineTo(w, h * 0.75f)
                            lineTo(w * 0.5f, h)
                            lineTo(0f, h * 0.75f)
                            lineTo(0f, h * 0.25f)
                            close()
                        }

                        drawPath(
                            path = shieldPath,
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1565C0),
                                    Color(0xFF0D47A1),
                                    Color(0xFF002171)
                                )
                            )
                        )
                    }

                    // Crown and RS Monogram
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(13.dp)
                        )
                        Row {
                            Text(
                                text = "R",
                                fontWeight = FontWeight.Black,
                                fontSize = 21.sp,
                                color = Color(0xFFFFD54F),
                                letterSpacing = (-1).sp
                            )
                            Text(
                                text = "S",
                                fontWeight = FontWeight.Black,
                                fontSize = 21.sp,
                                color = Color.White,
                                letterSpacing = (-1).sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Brand Title & Slogan
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Roi Service",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFFD54F).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "RS",
                                color = Color(0xFFFFD54F),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "خدمات منزلية احترافية سريعة ومضمونة",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF00E5FF).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "⚡ حجز فوري عبر الخريطة",
                                color = Color(0xFF80DEEA),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF66BB6A).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "✓ ضمان جودة معتمد",
                                color = Color(0xFFA5D6A7),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
