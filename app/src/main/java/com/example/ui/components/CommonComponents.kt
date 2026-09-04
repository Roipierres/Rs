package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KhadamatiAmberTertiary
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiError
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import com.example.util.AppNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getServiceIcon(iconType: String): ImageVector {
    return when (iconType.lowercase()) {
        "plumbing" -> Icons.Default.Handyman
        "electrical" -> Icons.Default.Bolt
        "ac" -> Icons.Default.AcUnit
        "cleaning" -> Icons.Default.CleaningServices
        "painting" -> Icons.Default.FormatPaint
        "moving" -> Icons.Default.LocalShipping
        "tech" -> Icons.Default.Computer
        "car" -> Icons.Default.DirectionsCar
        else -> Icons.Default.HomeRepairService
    }
}

fun getServiceIconBg(iconType: String): Color {
    return when (iconType.lowercase()) {
        "plumbing" -> Color(0xFFE3F2FD)
        "electrical" -> Color(0xFFFFF8E1)
        "ac" -> Color(0xFFE0F7FA)
        "cleaning" -> Color(0xFFE8F5E9)
        "painting" -> Color(0xFFF3E5F5)
        "moving" -> Color(0xFFFFF3E0)
        "tech" -> Color(0xFFEDE7F6)
        "car" -> Color(0xFFEFEBE9)
        else -> Color(0xFFE3F2FD)
    }
}

fun getServiceIconTint(iconType: String): Color {
    return when (iconType.lowercase()) {
        "plumbing" -> Color(0xFF1976D2)
        "electrical" -> Color(0xFFF57F17)
        "ac" -> Color(0xFF0097A7)
        "cleaning" -> Color(0xFF388E3C)
        "painting" -> Color(0xFF7B1FA2)
        "moving" -> Color(0xFFE65100)
        "tech" -> Color(0xFF512DA8)
        "car" -> Color(0xFF4E342E)
        else -> Color(0xFF1976D2)
    }
}

data class StatusVisual(
    val titleAr: String,
    val containerColor: Color,
    val contentColor: Color
)

fun getOrderStatusVisual(status: String): StatusVisual {
    return when (status.uppercase()) {
        "NEW" -> StatusVisual(
            titleAr = "طلب جديد",
            containerColor = Color(0xFFE3F2FD),
            contentColor = Color(0xFF0D47A1)
        )
        "CONFIRMED" -> StatusVisual(
            titleAr = "تم التأكيد",
            containerColor = Color(0xFFE0F2F1),
            contentColor = Color(0xFF004D40)
        )
        "IN_PROGRESS" -> StatusVisual(
            titleAr = "قيد التنفيذ",
            containerColor = Color(0xFFFFF3E0),
            contentColor = Color(0xFFE65100)
        )
        "COMPLETED" -> StatusVisual(
            titleAr = "مكتمل بنجاح",
            containerColor = Color(0xFFE8F5E9),
            contentColor = Color(0xFF1B5E20)
        )
        "CANCELLED" -> StatusVisual(
            titleAr = "ملغي",
            containerColor = Color(0xFFFFEBEE),
            contentColor = Color(0xFFB71C1C)
        )
        else -> StatusVisual(
            titleAr = status,
            containerColor = Color(0xFFEEEEEE),
            contentColor = Color(0xFF424242)
        )
    }
}

@Composable
fun OrderStatusBadge(status: String) {
    val visual = getOrderStatusVisual(status)
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = visual.containerColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(visual.contentColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = visual.titleAr,
                color = visual.contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhadamatiTopAppBar(
    currentRole: String,
    appName: String,
    unreadNotificationsCount: Int = 0,
    onOpenNotifications: () -> Unit = {},
    onToggleRole: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0A2342),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Advanced Animated RS Logo in TopBar
                RoiServiceCompactLogo(size = 38.dp)

                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Roi Service",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFD54F).copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "RS",
                                color = Color(0xFFFFD54F),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = if (currentRole == "ADMIN") "لوحة تحكم المدير والإدارة" else "منظومة الخدمات المنزلية الذكية",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        },
        actions = {
            // Notification Bell with unread badge
            IconButton(onClick = onOpenNotifications) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationsCount > 0) {
                            Badge(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = if (unreadNotificationsCount > 9) "9+" else unreadNotificationsCount.toString(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (unreadNotificationsCount > 0) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                        contentDescription = "الإشعارات",
                        tint = if (unreadNotificationsCount > 0) Color(0xFFFFD54F) else Color.White
                    )
                }
            }

            // Quick role switcher button
            FilledTonalButton(
                onClick = onToggleRole,
                shape = RoundedCornerShape(20.dp),
                colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color.White.copy(alpha = 0.18f),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (currentRole == "ADMIN") "الزبون" else "المدير",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

/**
 * In-App Notification Center BottomSheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppNotificationSheet(
    notifications: List<AppNotification>,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = KhadamatiBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "مركز الإشعارات والتنبيهات",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row {
                    if (notifications.isNotEmpty()) {
                        TextButton(onClick = onClearAll) {
                            Icon(Icons.Default.ClearAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("مسح الكل", fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "لا توجد إشعارات حالياً",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(notifications, key = { it.id }) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (notif.isRead) {
                                    MaterialTheme.colorScheme.surface
                                } else {
                                    KadamatiNotificationTint(notif.type)
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (!notif.isRead) Color(0xFF1976D2) else Color.LightGray
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = notif.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                                    Text(
                                        text = timeFormat.format(Date(notif.timestamp)),
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = notif.message,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

fun KadamatiNotificationTint(type: String): Color {
    return when (type) {
        "ORDER" -> Color(0xFFE3F2FD)
        "PROMO" -> Color(0xFFFFF8E1)
        else -> Color(0xFFEDE7F6)
    }
}

