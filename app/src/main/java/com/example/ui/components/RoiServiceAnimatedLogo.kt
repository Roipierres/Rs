package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.KhadamatiNeonAmber
import com.example.ui.theme.KhadamatiNeonCyan
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-craft Animated Brand Logo with glowing neon rings, orbiting particle sparks,
 * radial aura glow, and metallic shimmer sweep across the actual brand logo.
 */
@Composable
fun RoiServiceBrandAnimatedLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    showParticles: Boolean = true,
    showShimmer: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RoiLogoAnimation")

    // 1. Clockwise outer ring rotation
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OuterRingRotation"
    )

    // 2. Counter-clockwise inner orbit rotation
    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "InnerRingRotation"
    )

    // 3. Gentle breathing scale of the aura & logo
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingScale"
    )

    // 4. Pulsing radial glow alpha
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    // 5. Metallic light shimmer sweep
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1.2f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerSweep"
    )

    val containerSize = size + 24.dp
    val cornerRadius = size * 0.26f

    Box(
        modifier = modifier
            .size(containerSize)
            .clickable(
                enabled = onClick != null,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        // LAYER 1: Dynamic Background Radial Aura (Pulsing Glow)
        Canvas(modifier = Modifier.size(containerSize)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val maxRadius = this.size.minDimension / 2f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD54F).copy(alpha = glowAlpha * 0.45f),
                        Color(0xFF00E5FF).copy(alpha = glowAlpha * 0.28f),
                        Color(0xFF1976D2).copy(alpha = glowAlpha * 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius * breathingScale
                ),
                radius = maxRadius * breathingScale,
                center = center
            )
        }

        // LAYER 2: Rotating Outer Neon Halo Ring (Clockwise)
        Canvas(
            modifier = Modifier
                .size(size + 14.dp)
                .rotate(outerRotation)
        ) {
            val strokeWidth = 2.4.dp.toPx()
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFFD54F), // Gold yellow
                        Color(0xFF00E5FF), // Neon cyan
                        Color(0xFFFFA000), // Amber
                        Color(0xFF2979FF), // Royal blue
                        Color(0xFFFFD54F)
                    )
                ),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
                )
            )
        }

        // LAYER 3: Counter-rotating Inner Accent Ring
        Canvas(
            modifier = Modifier
                .size(size + 6.dp)
                .rotate(innerRotation)
        ) {
            val strokeWidth = 1.8.dp.toPx()
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF00E5FF).copy(alpha = 0.9f),
                        Color(0xFFFFD54F).copy(alpha = 0.9f),
                        Color(0xFF00E5FF).copy(alpha = 0.9f)
                    )
                ),
                style = Stroke(width = strokeWidth)
            )
        }

        // LAYER 4: Orbiting Luminous Particles (Sparks)
        if (showParticles) {
            Canvas(modifier = Modifier.size(containerSize)) {
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                val orbitRadius = (this.size.minDimension / 2f) - 6.dp.toPx()
                val particleCount = 4

                for (i in 0 until particleCount) {
                    val angleOffset = (i * (2 * PI / particleCount)).toFloat()
                    val currentAngle = Math.toRadians(outerRotation.toDouble()).toFloat() + angleOffset
                    val x = center.x + orbitRadius * cos(currentAngle)
                    val y = center.y + orbitRadius * sin(currentAngle)

                    val particleColor = if (i % 2 == 0) Color(0xFFFFD54F) else Color(0xFF00E5FF)

                    // Outer particle glow
                    drawCircle(
                        color = particleColor.copy(alpha = glowAlpha * 0.8f),
                        radius = 4.2.dp.toPx(),
                        center = Offset(x, y)
                    )
                    // Inner bright core
                    drawCircle(
                        color = Color.White,
                        radius = 2.0.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }

        // LAYER 5: THE ACTUAL BRAND LOGO IMAGE
        Box(
            modifier = Modifier
                .size(size)
                .scale(breathingScale)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.Black)
                .border(
                    width = 1.8.dp,
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFD54F), Color(0xFF00E5FF), Color(0xFFFFA000))
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_roi_service_brand_logo),
                contentDescription = "شعار التطبيق الرسمي Roi Service",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // LAYER 6: Animated Metallic Light Sweep across the logo
            if (showShimmer) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = this.size.width
                    val h = this.size.height
                    val currentX = w * shimmerOffset

                    val shimmerBrush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFFFD54F).copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.45f),
                            Color(0xFF00E5FF).copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        start = Offset(currentX - w * 0.4f, 0f),
                        end = Offset(currentX + w * 0.4f, h)
                    )

                    drawRect(brush = shimmerBrush)
                }
            }
        }
    }
}

/**
 * Compact animated RS Logo for TopAppBar with glowing orbiting aura
 */
@Composable
fun RoiServiceCompactLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    RoiServiceBrandAnimatedLogo(
        modifier = modifier,
        size = size,
        showParticles = true,
        showShimmer = true
    )
}

/**
 * Hero Animated Logo Badge with dynamic lighting, brand display typography,
 * and service highlight badges.
 */
@Composable
fun RoiServiceHeroBadge(
    modifier: Modifier = Modifier,
    onLogoClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeroBadgeAnimation")

    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HaloRotation"
    )

    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BorderGlowAlpha"
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
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.4.dp,
            brush = Brush.sweepGradient(
                listOf(
                    Color(0xFFFFD54F).copy(alpha = borderGlowAlpha),
                    Color(0xFF00E5FF).copy(alpha = borderGlowAlpha),
                    Color(0xFF2979FF).copy(alpha = borderGlowAlpha),
                    Color(0xFFFFD54F).copy(alpha = borderGlowAlpha)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF061528),
                            Color(0xFF0A2342),
                            Color(0xFF071C35)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // THE ACTUAL ANIMATED BRAND LOGO (R SERVICE)
                RoiServiceBrandAnimatedLogo(
                    size = 68.dp,
                    showParticles = true,
                    showShimmer = true
                )

                Spacer(modifier = Modifier.width(14.dp))

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
                            color = Color(0xFFFFD54F).copy(alpha = 0.22f)
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF00E5FF).copy(alpha = 0.16f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFF00E5FF).copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "⚡ حجز فوري",
                                    color = Color(0xFF80DEEA),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF66BB6A).copy(alpha = 0.16f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color(0xFF66BB6A).copy(alpha = 0.4f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "✓ ضمان معتمد",
                                    color = Color(0xFFA5D6A7),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
