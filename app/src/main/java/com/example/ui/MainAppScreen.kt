package com.example.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.admin.AdminOrdersScreen
import com.example.ui.admin.AdminOverviewScreen
import com.example.ui.admin.AdminServicesScreen
import com.example.ui.admin.AdminSettingsScreen
import com.example.ui.components.InAppNotificationSheet
import com.example.ui.components.KhadamatiTopAppBar
import com.example.ui.customer.CustomerHomeScreen
import com.example.ui.customer.CustomerOrdersScreen
import com.example.ui.customer.CustomerProfileScreen
import com.example.ui.theme.KhadamatiBlueContainer
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiNeonAmber
import com.example.ui.theme.KhadamatiNeonCyan
import com.example.ui.viewmodel.KhadamatiViewModel

@Composable
fun MainAppScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
    val adminTab by viewModel.adminTab.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()

    val effectiveRole = if (isAdminAuthenticated && currentRole == "ADMIN") "ADMIN" else "CUSTOMER"
    val pendingOrdersCount = allOrders.count { it.status == "NEW" }
    val appName = appSettings?.appName ?: "Roi Service"

    // Request Notification permission on Android 13+ (Tiramisu)
    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission granted or denied handled gracefully */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            KhadamatiTopAppBar(
                currentRole = effectiveRole,
                appName = appName,
                unreadNotificationsCount = unreadCount,
                onOpenNotifications = {
                    viewModel.toggleNotificationsSheet(true)
                },
                onToggleRole = {
                    if (effectiveRole == "ADMIN") {
                        viewModel.logoutAdmin()
                    } else {
                        viewModel.switchToAdmin()
                    }
                }
            )
        },
        bottomBar = {
            CompactModernBottomBar(
                effectiveRole = effectiveRole,
                customerTab = customerTab,
                adminTab = adminTab,
                allOrdersCount = allOrders.size,
                pendingOrdersCount = pendingOrdersCount,
                onSelectCustomerTab = { viewModel.setCustomerTab(it) },
                onSelectAdminTab = { viewModel.setAdminTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Smooth Screen Fade and Scale Transition
            AnimatedContent(
                targetState = Pair(effectiveRole, if (effectiveRole == "CUSTOMER") customerTab else adminTab),
                transitionSpec = {
                    (fadeIn(animationSpec = tween(320, easing = FastOutSlowInEasing)) +
                     scaleIn(initialScale = 0.96f, animationSpec = tween(320, easing = FastOutSlowInEasing)))
                        .togetherWith(
                            fadeOut(animationSpec = tween(240, easing = FastOutSlowInEasing)) +
                            scaleOut(targetScale = 1.02f, animationSpec = tween(240, easing = FastOutSlowInEasing))
                        )
                },
                label = "ScreenTransition"
            ) { (role, tab) ->
                if (role == "CUSTOMER") {
                    when (tab) {
                        0 -> CustomerHomeScreen(viewModel = viewModel)
                        1 -> CustomerOrdersScreen(viewModel = viewModel)
                        else -> CustomerProfileScreen(viewModel = viewModel)
                    }
                } else {
                    when (tab) {
                        0 -> AdminOverviewScreen(viewModel = viewModel)
                        1 -> AdminOrdersScreen(viewModel = viewModel)
                        2 -> AdminServicesScreen(viewModel = viewModel)
                        else -> AdminSettingsScreen(viewModel = viewModel)
                    }
                }
            }

            // In-App Notification Center Sheet
            if (showNotificationsSheet) {
                InAppNotificationSheet(
                    notifications = notifications,
                    onDismiss = { viewModel.toggleNotificationsSheet(false) },
                    onClearAll = { viewModel.clearNotifications() }
                )
            }
        }
    }
}

/**
 * Sleek, ultra-compact bottom navigation bar with glowing top accent line
 * and prominent, high-contrast professional icons.
 */
@Composable
private fun CompactModernBottomBar(
    effectiveRole: String,
    customerTab: Int,
    adminTab: Int,
    allOrdersCount: Int,
    pendingOrdersCount: Int,
    onSelectCustomerTab: (Int) -> Unit,
    onSelectAdminTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val surfaceColor = if (isDark) Color(0xFF0F172A) else Color.White

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = surfaceColor,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Luminous glowing neon top hairline accent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                KhadamatiNeonCyan.copy(alpha = 0.45f),
                                KhadamatiNeonAmber.copy(alpha = 0.65f),
                                KhadamatiBluePrimary.copy(alpha = 0.45f)
                            )
                        )
                    )
            )

            // Compact 52dp items row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (effectiveRole == "CUSTOMER") {
                    CompactBottomNavItem(
                        icon = Icons.Default.Category,
                        label = "الخدمات",
                        selected = customerTab == 0,
                        onClick = { onSelectCustomerTab(0) }
                    )
                    CompactBottomNavItem(
                        icon = Icons.Default.ReceiptLong,
                        label = "طلباتي",
                        selected = customerTab == 1,
                        badgeCount = allOrdersCount,
                        badgeColor = KhadamatiBluePrimary,
                        onClick = { onSelectCustomerTab(1) }
                    )
                    CompactBottomNavItem(
                        icon = Icons.Default.AccountCircle,
                        label = "حسابي",
                        selected = customerTab == 2,
                        onClick = { onSelectCustomerTab(2) }
                    )
                } else {
                    CompactBottomNavItem(
                        icon = Icons.Default.Assessment,
                        label = "الرئيسية",
                        selected = adminTab == 0,
                        onClick = { onSelectAdminTab(0) }
                    )
                    CompactBottomNavItem(
                        icon = Icons.Default.Inbox,
                        label = "الطلبات",
                        selected = adminTab == 1,
                        badgeCount = pendingOrdersCount,
                        badgeColor = Color(0xFFD32F2F),
                        onClick = { onSelectAdminTab(1) }
                    )
                    CompactBottomNavItem(
                        icon = Icons.Default.Build,
                        label = "الخدمات",
                        selected = adminTab == 2,
                        onClick = { onSelectAdminTab(2) }
                    )
                    CompactBottomNavItem(
                        icon = Icons.Default.Tune,
                        label = "الإعدادات",
                        selected = adminTab == 3,
                        onClick = { onSelectAdminTab(3) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactBottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    badgeColor: Color = KhadamatiBluePrimary,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val activeColor = if (isDark) KhadamatiNeonCyan else KhadamatiBluePrimary
    val inactiveColor = if (isDark) Color.White.copy(alpha = 0.65f) else Color(0xFF475569)
    val activeBgColor = if (isDark) KhadamatiNeonCyan.copy(alpha = 0.16f) else KhadamatiBlueContainer.copy(alpha = 0.7f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with optional badge and active highlight pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (selected) activeBgColor else Color.Transparent)
                .padding(horizontal = 12.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (badgeCount > 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = badgeColor,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selected) activeColor else inactiveColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) activeColor else inactiveColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Crisp readable label
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (selected) activeColor else inactiveColor,
            maxLines = 1
        )

        // Micro active glowing indicator line
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(width = 12.dp, height = 2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(activeColor)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}
