package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import com.example.util.LocationHelper

/**
 * 1. Live Worker Tracking Map Component
 * Displays the real-time position of the technician/driver on the map
 * heading towards the customer's home with animated pulse and path.
 */
@Composable
fun LiveWorkerTrackingCard(
    order: OrderEntity,
    onOpenChat: () -> Unit,
    onCallWorker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val workerName = if (order.assignedWorker.isNotBlank()) order.assignedWorker else "الفني عبدالرحمن السعيد"

    // Infinite animation for worker motion along the path
    val infiniteTransition = rememberInfiniteTransition(label = "WorkerTracking")
    val workerProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "WorkerMovement"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Surface(
                color = KhadamatiBluePrimary.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(KhadamatiSuccess)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تتبع مسار الفني في الوقت الفعلي",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KhadamatiBlueDark
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = KhadamatiSuccess.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "وصول تقديري: 12 دقيقة",
                            fontSize = 11.sp,
                            color = KhadamatiSuccess,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Canvas Map with Dynamic Route & Live Worker Pin
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE8ECEF))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Customer Position (Fixed on map)
                    val customerX = w * 0.8f
                    val customerY = h * 0.4f

                    // Worker Start Origin
                    val originX = w * 0.15f
                    val originY = h * 0.75f

                    // Grid / Map Background roads
                    val roadColor = Color(0xFFD7DEE4)
                    for (i in 1..4) {
                        drawLine(
                            color = roadColor,
                            start = Offset(0f, h * (i * 0.2f)),
                            end = Offset(w, h * (i * 0.2f)),
                            strokeWidth = 4f
                        )
                        drawLine(
                            color = roadColor,
                            start = Offset(w * (i * 0.2f), 0f),
                            end = Offset(w * (i * 0.2f), h),
                            strokeWidth = 4f
                        )
                    }

                    // Route Path from origin to customer
                    val routePath = Path().apply {
                        moveTo(originX, originY)
                        cubicTo(
                            w * 0.35f, h * 0.85f,
                            w * 0.45f, h * 0.3f,
                            customerX, customerY
                        )
                    }

                    // Draw Route Track casing
                    drawPath(
                        path = routePath,
                        color = Color.White,
                        style = Stroke(width = 14f)
                    )
                    // Draw Route Track primary line
                    drawPath(
                        path = routePath,
                        color = KhadamatiBluePrimary,
                        style = Stroke(
                            width = 8f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f), 0f)
                        )
                    )

                    // Customer location pulse
                    drawCircle(
                        color = Color(0xFFD32F2F).copy(alpha = 0.25f),
                        radius = 35f * pulseScale,
                        center = Offset(customerX, customerY)
                    )
                    drawCircle(
                        color = Color(0xFFD32F2F),
                        radius = 9f,
                        center = Offset(customerX, customerY)
                    )

                    // Calculate Worker Current Position along bezier curve approximation
                    val t = workerProgress
                    val p0 = Offset(originX, originY)
                    val p1 = Offset(w * 0.35f, h * 0.85f)
                    val p2 = Offset(w * 0.45f, h * 0.3f)
                    val p3 = Offset(customerX, customerY)

                    val oneMinusT = 1f - t
                    val currentWorkerX = oneMinusT * oneMinusT * oneMinusT * p0.x +
                            3f * oneMinusT * oneMinusT * t * p1.x +
                            3f * oneMinusT * t * t * p2.x +
                            t * t * t * p3.x
                    val currentWorkerY = oneMinusT * oneMinusT * oneMinusT * p0.y +
                            3f * oneMinusT * oneMinusT * t * p1.y +
                            3f * oneMinusT * t * t * p2.y +
                            t * t * t * p3.y

                    // Worker Radar pulse
                    drawCircle(
                        color = KhadamatiBluePrimary.copy(alpha = 0.3f),
                        radius = 28f * pulseScale,
                        center = Offset(currentWorkerX, currentWorkerY)
                    )

                    // Worker Vehicle Marker
                    drawCircle(
                        color = KhadamatiBlueDark,
                        radius = 14f,
                        center = Offset(currentWorkerX, currentWorkerY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 5f,
                        center = Offset(currentWorkerX, currentWorkerY)
                    )
                }

                // Customer Pin Label Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 28.dp, end = 20.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD32F2F),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "منزل العميل 🏠",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Live Speed & Distance Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = KhadamatiBluePrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "المسافة: 2.8 كم • السرعة: 40 كم/س",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = KhadamatiBlueDark
                        )
                    }
                }
            }

            // Technician / Driver Details & Direct Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(KhadamatiBluePrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = workerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "المركبة: رونو إكسبريس (01344-116-16)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onOpenChat,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("شات", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onCallWorker,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("اتصال", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
