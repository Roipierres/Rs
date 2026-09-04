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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.ui.viewmodel.KhadamatiViewModel

@Composable
fun MainAppScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
    val adminTab by viewModel.adminTab.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()

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
                currentRole = currentRole,
                appName = appName,
                unreadNotificationsCount = unreadCount,
                onOpenNotifications = {
                    viewModel.toggleNotificationsSheet(true)
                },
                onToggleRole = {
                    if (currentRole == "ADMIN") {
                        viewModel.switchToCustomer()
                    } else {
                        viewModel.switchToAdmin()
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = KhadamatiBluePrimary,
                tonalElevation = 8.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                if (currentRole == "CUSTOMER") {
                    // Customer Bottom Navigation
                    NavigationBarItem(
                        selected = customerTab == 0,
                        onClick = { viewModel.setCustomerTab(0) },
                        icon = { Icon(Icons.Default.HomeRepairService, contentDescription = "الخدمات") },
                        label = { Text("الخدمات", fontSize = 11.sp, fontWeight = if (customerTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                    NavigationBarItem(
                        selected = customerTab == 1,
                        onClick = { viewModel.setCustomerTab(1) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (allOrders.isNotEmpty()) {
                                        Badge(containerColor = KhadamatiBluePrimary) {
                                            Text(allOrders.size.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = "طلباتي")
                            }
                        },
                        label = { Text("طلباتي", fontSize = 11.sp, fontWeight = if (customerTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                    NavigationBarItem(
                        selected = customerTab == 2,
                        onClick = { viewModel.setCustomerTab(2) },
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "حسابي") },
                        label = { Text("حسابي", fontSize = 11.sp, fontWeight = if (customerTab == 2) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                } else {
                    // Admin Bottom Navigation
                    NavigationBarItem(
                        selected = adminTab == 0,
                        onClick = { viewModel.setAdminTab(0) },
                        icon = { Icon(Icons.Default.Assessment, contentDescription = "الرئيسية") },
                        label = { Text("الرئيسية", fontSize = 11.sp, fontWeight = if (adminTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                    NavigationBarItem(
                        selected = adminTab == 1,
                        onClick = { viewModel.setAdminTab(1) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (pendingOrdersCount > 0) {
                                        Badge(containerColor = Color(0xFFD32F2F)) {
                                            Text(pendingOrdersCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Inbox, contentDescription = "الطلبات")
                            }
                        },
                        label = { Text("الطلبات", fontSize = 11.sp, fontWeight = if (adminTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                    NavigationBarItem(
                        selected = adminTab == 2,
                        onClick = { viewModel.setAdminTab(2) },
                        icon = { Icon(Icons.Default.Build, contentDescription = "الخدمات") },
                        label = { Text("الخدمات", fontSize = 11.sp, fontWeight = if (adminTab == 2) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                    NavigationBarItem(
                        selected = adminTab == 3,
                        onClick = { viewModel.setAdminTab(3) },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "الإعدادات") },
                        label = { Text("الإعدادات", fontSize = 11.sp, fontWeight = if (adminTab == 3) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KhadamatiBlueDark,
                            indicatorColor = KhadamatiBlueContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Smooth Screen Fade and Scale Transition
            AnimatedContent(
                targetState = Pair(currentRole, if (currentRole == "CUSTOMER") customerTab else adminTab),
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
