package com.example.ui.admin

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppSettingsEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ServiceEntity
import com.example.ui.components.InteractiveMapPicker
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.getServiceIcon
import com.example.ui.components.getServiceIconBg
import com.example.ui.components.getServiceIconTint
import com.example.ui.theme.KhadamatiAmberTertiary
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiError
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import com.example.ui.viewmodel.KhadamatiViewModel
import com.example.util.LocationHelper

// 1. ADMIN DASHBOARD OVERVIEW
@Composable
fun AdminOverviewScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val allServices by viewModel.allServices.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    val newOrdersCount = allOrders.count { it.status == "NEW" }
    val inProgressCount = allOrders.count { it.status == "IN_PROGRESS" || it.status == "CONFIRMED" }
    val completedCount = allOrders.count { it.status == "COMPLETED" }
    val totalRevenue = allOrders.filter { it.status == "COMPLETED" }.sumOf { it.servicePrice }
    val currency = appSettings?.currency ?: "ر.س"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "لوحة قيادة المدير",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "متابعة الطلبات، المواقع على الخريطة، والخدمات",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                if (newOrdersCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFEBEE)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = KhadamatiError,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$newOrdersCount طلبات جديدة!",
                                color = KhadamatiError,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Metrics Grid (2x2)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "طلبات جديدة بالانتظار",
                    value = newOrdersCount.toString(),
                    subtitle = "تتطلب إجراء سريع",
                    color = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.setAdminOrderStatusFilter("NEW")
                        viewModel.setAdminTab(1)
                    }
                )
                MetricCard(
                    title = "طلبات قيد التنفيذ",
                    value = inProgressCount.toString(),
                    subtitle = "الفنيون في الميدان",
                    color = Color(0xFFF57C00),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.setAdminOrderStatusFilter("IN_PROGRESS")
                        viewModel.setAdminTab(1)
                    }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "طلبات مكتملة",
                    value = completedCount.toString(),
                    subtitle = "تم إنجازها بنجاح",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.setAdminOrderStatusFilter("COMPLETED")
                        viewModel.setAdminTab(1)
                    }
                )
                MetricCard(
                    title = "إجمالي الخدمات المعروضة",
                    value = allServices.size.toString(),
                    subtitle = "${allServices.count { it.isActive }} مفعلة حالياً",
                    color = Color(0xFF7B1FA2),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setAdminTab(2) }
                )
            }
        }

        // Revenue Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KhadamatiBluePrimary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "إجمالي الإيرادات المحققة",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${totalRevenue.toInt()} $currency",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Button(
                        onClick = { viewModel.setAdminTab(1) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "عرض كل الطلبات",
                            color = KhadamatiBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Fast Action Shortcuts
        item {
            Text(
                text = "الوصول السريع لمهام المدير",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setAdminTab(2) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(KhadamatiSecondaryTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = KhadamatiSecondaryTeal)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("إضافة خدمة جديدة للمنصة", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("أضف تسعيرة، تصنيف، ووصف لخدمة جديدة", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("←", fontSize = 16.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setAdminTab(3) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(KhadamatiAmberTertiary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = KhadamatiAmberTertiary)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("تعديل تفاصيل وإعدادات التطبيق", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("أرقام الدعم، رسالة الإعلانات، العملة، ورسوم المعاينة", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("←", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.DarkGray
            )
        }
    }
}

// 2. ADMIN ORDERS MANAGEMENT WITH MAP LOCATION (Core prompt requirement!)
@Composable
fun AdminOrdersScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val filter by viewModel.adminOrderStatusFilter.collectAsStateWithLifecycle()
    val inspectingOrder by viewModel.adminInspectingOrder.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    val filteredOrders = allOrders.filter { order ->
        when (filter) {
            "NEW" -> order.status == "NEW"
            "IN_PROGRESS" -> order.status == "IN_PROGRESS" || order.status == "CONFIRMED"
            "COMPLETED" -> order.status == "COMPLETED"
            "CANCELLED" -> order.status == "CANCELLED"
            else -> true
        }
    }

    val filterOptions = listOf(
        Pair("ALL", "الكل (${allOrders.size})"),
        Pair("NEW", "جديدة (${allOrders.count { it.status == "NEW" }})"),
        Pair("IN_PROGRESS", "جارية (${allOrders.count { it.status == "IN_PROGRESS" || it.status == "CONFIRMED" }})"),
        Pair("COMPLETED", "مكتملة (${allOrders.count { it.status == "COMPLETED" }})"),
        Pair("CANCELLED", "ملغاة (${allOrders.count { it.status == "CANCELLED" }})")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "إدارة الطلبات الواردة (${filteredOrders.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "اضغط على أي طلب لمعاينة مكانه بدقة على الخريطة والتواصل مع الزبون وتحديث الحالة",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filters Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filterOptions) { (key, title) ->
                FilterChip(
                    selected = filter == key,
                    onClick = { viewModel.setAdminOrderStatusFilter(key) },
                    label = { Text(title, fontSize = 12.sp, fontWeight = if (filter == key) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KhadamatiBluePrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لا توجد طلبات مطابقة لهذا الفلتر",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    AdminOrderCard(
                        order = order,
                        currency = appSettings?.currency ?: "ر.س",
                        onClick = { viewModel.setAdminInspectingOrder(order) }
                    )
                }
            }
        }
    }

    // Inspect Order Dialog with Live Map & Status Controls
    if (inspectingOrder != null) {
        AdminOrderInspectionDialog(
            order = inspectingOrder!!,
            currency = appSettings?.currency ?: "ر.س",
            onDismiss = { viewModel.setAdminInspectingOrder(null) },
            onUpdateStatus = { newStatus ->
                viewModel.updateOrderStatus(inspectingOrder!!.id, newStatus)
            },
            onSaveAdminNotes = { notes, worker ->
                viewModel.updateOrderAdminDetails(inspectingOrder!!.id, notes, worker)
            }
        )
    }
}

@Composable
fun AdminOrderCard(
    order: OrderEntity,
    currency: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.orderNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = KhadamatiBluePrimary
                    )
                    if (order.status == "NEW") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = "جديد",
                                color = KhadamatiError,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                OrderStatusBadge(order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.serviceName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${order.customerName} (${order.customerPhone})",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Coordinates badge & Address
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = KhadamatiSecondaryTeal, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${order.addressText} [${String.format("%.4f", order.latitude)}, ${String.format("%.4f", order.longitude)}]",
                    fontSize = 11.sp,
                    color = KhadamatiBlueDark,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.servicePrice.toInt()} $currency",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = KhadamatiBluePrimary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = KhadamatiBluePrimary.copy(alpha = 0.1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = null,
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "عرض الموقع على الخريطة",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = KhadamatiBluePrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrderInspectionDialog(
    order: OrderEntity,
    currency: String,
    onDismiss: () -> Unit,
    onUpdateStatus: (String) -> Unit,
    onSaveAdminNotes: (notes: String, worker: String) -> Unit
) {
    val context = LocalContext.current
    var adminNotes by remember { mutableStateOf(order.adminNotes) }
    var assignedWorker by remember { mutableStateOf(order.assignedWorker) }
    var isSavingNotes by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "تفاصيل الطلب: ${order.orderNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "الموعد: ${order.requestedDate} • ${order.requestedTimeSlot}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                OrderStatusBadge(order.status)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Customer Contact Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "الزبون: ${order.customerName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "الهاتف: ${order.customerPhone}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { LocationHelper.dialPhone(context, order.customerPhone) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("اتصال هاتف", fontSize = 11.sp)
                            }
                            FilledTonalButton(
                                onClick = {
                                    LocationHelper.openWhatsApp(
                                        context = context,
                                        phone = order.customerPhone,
                                        message = "السلام عليكم ${order.customerName}، معكم إدارة تطبيق خدماتي بخصوص طلبكم رقم ${order.orderNumber}"
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("واتساب", fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Service & Notes
                Text(
                    text = "الخدمة المطلوبة: ${order.serviceName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = KhadamatiBluePrimary
                )
                Text(
                    text = "القيمة: ${order.servicePrice.toInt()} $currency",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (order.customerNotes.isNotBlank()) {
                    Text(
                        text = "ملاحظات الزبون: ${order.customerNotes}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // EXACT MAP LOCATION (PROMPT REQUIREMENT)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = KhadamatiError)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "موقع الزبون بدقة على الخريطة (GPS):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = order.addressText,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "الإحداثيات: ${order.latitude} , ${order.longitude}",
                    fontSize = 11.sp,
                    color = KhadamatiBluePrimary,
                    fontWeight = FontWeight.Medium
                )

                // Visual Mini Map with live Google Maps launcher
                InteractiveMapPicker(
                    latitude = order.latitude,
                    longitude = order.longitude,
                    onLocationChanged = { _, _ -> },
                    isReadOnly = true,
                    customerLabel = order.customerName,
                    heightDp = 190
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status Update Buttons
                Text(
                    text = "تحديث حالة الطلب:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { onUpdateStatus("CONFIRMED") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("تأكيد الطلب", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onUpdateStatus("IN_PROGRESS") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("قيد التنفيذ", fontSize = 11.sp)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { onUpdateStatus("COMPLETED") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("تم الإنجاز بنجاح", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onUpdateStatus("CANCELLED") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("إلغاء الطلب", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Admin Worker & Notes Assignment
                Text(
                    text = "تكليف فني وملاحظات الإدارة:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                OutlinedTextField(
                    value = assignedWorker,
                    onValueChange = { assignedWorker = it },
                    label = { Text("اسم الفني المسؤول") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("ملاحظات داخلية للمدير") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Button(
                    onClick = {
                        onSaveAdminNotes(adminNotes, assignedWorker)
                        isSavingNotes = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBlueDark),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isSavingNotes) "تم الحفظ بنجاح" else "حفظ بيانات الفني والملاحظات")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
            ) {
                Text("إغلاق")
            }
        }
    )
}

// 3. ADMIN SERVICES MANAGEMENT (ADD, EDIT, DELETE AS USER REQUESTED!)
@Composable
fun AdminServicesScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val allServices by viewModel.allServices.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var serviceToEdit by remember { mutableStateOf<ServiceEntity?>(null) }
    var serviceToDelete by remember { mutableStateOf<ServiceEntity?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "إدارة الخدمات (${allServices.size})",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "يمكن للمدير إضافة خدمات جديدة، تعديل الأسعار، وحذفها",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("إضافة خدمة")
                    }
                }
            }

            items(allServices, key = { it.id }) { service ->
                AdminServiceItemCard(
                    service = service,
                    currency = appSettings?.currency ?: "ر.س",
                    onToggleActive = { viewModel.toggleServiceStatus(service) },
                    onEdit = { serviceToEdit = service },
                    onDelete = { serviceToDelete = service }
                )
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = KhadamatiBluePrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "إضافة خدمة جديدة")
        }
    }

    // Add Service Dialog
    if (showAddDialog) {
        ServiceFormDialog(
            title = "إضافة خدمة جديدة",
            initialService = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, cat, price, duration, desc, icon ->
                viewModel.addService(title, cat, price, duration, desc, icon)
                showAddDialog = false
            }
        )
    }

    // Edit Service Dialog
    if (serviceToEdit != null) {
        ServiceFormDialog(
            title = "تعديل الخدمة",
            initialService = serviceToEdit,
            onDismiss = { serviceToEdit = null },
            onConfirm = { title, cat, price, duration, desc, icon ->
                viewModel.updateService(
                    serviceToEdit!!.copy(
                        title = title,
                        category = cat,
                        price = price,
                        durationEstimate = duration,
                        description = desc,
                        iconType = icon
                    )
                )
                serviceToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (serviceToDelete != null) {
        AlertDialog(
            onDismissRequest = { serviceToDelete = null },
            icon = {
                Icon(Icons.Default.Delete, contentDescription = null, tint = KhadamatiError, modifier = Modifier.size(36.dp))
            },
            title = { Text("تأكيد حذف الخدمة") },
            text = {
                Text("هل أنت متأكد من رغبتك في حذف خدمة \"${serviceToDelete!!.title}\" نهائياً من المنصة؟")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteService(serviceToDelete!!.id)
                        serviceToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiError)
                ) {
                    Text("نعم، حذف الخدمة")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { serviceToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun AdminServiceItemCard(
    service: ServiceEntity,
    currency: String,
    onToggleActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (service.isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(getServiceIconBg(service.iconType)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getServiceIcon(service.iconType),
                        contentDescription = null,
                        tint = getServiceIconTint(service.iconType),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${service.category} • ${service.durationEstimate}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                // Active toggle switch
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Switch(
                        checked = service.isActive,
                        onCheckedChange = { onToggleActive() },
                        colors = SwitchDefaults.colors(checkedThumbColor = KhadamatiSuccess)
                    )
                    Text(
                        text = if (service.isActive) "مفعلة" else "معطلة",
                        fontSize = 10.sp,
                        color = if (service.isActive) KhadamatiSuccess else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.description,
                fontSize = 12.sp,
                color = Color.DarkGray,
                lineHeight = 17.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${service.price.toInt()} $currency",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = KhadamatiBluePrimary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تعديل", fontSize = 11.sp)
                    }

                    IconButton(
                        onClick = onDelete,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFFEBEE),
                            contentColor = KhadamatiError
                        ),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceFormDialog(
    title: String,
    initialService: ServiceEntity?,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        category: String,
        price: Double,
        duration: String,
        description: String,
        iconType: String
    ) -> Unit
) {
    var serviceTitle by remember { mutableStateOf(initialService?.title ?: "") }
    var serviceCategory by remember { mutableStateOf(initialService?.category ?: "سباكة وصحي") }
    var servicePrice by remember { mutableStateOf(initialService?.price?.toInt()?.toString() ?: "150") }
    var serviceDuration by remember { mutableStateOf(initialService?.durationEstimate ?: "ساعة إلى ساعتين") }
    var serviceDesc by remember { mutableStateOf(initialService?.description ?: "") }
    var selectedIcon by remember { mutableStateOf(initialService?.iconType ?: "plumbing") }

    val categories = listOf(
        "سباكة وصحي",
        "كهرباء",
        "تكييف وتبريد",
        "نظافة منزلية",
        "دهان وديكور",
        "نقل وتركيب",
        "تقنية ومعلومات",
        "سيارات"
    )

    val iconOptions = listOf(
        Pair("plumbing", "سباكة"),
        Pair("electrical", "كهرباء"),
        Pair("ac", "تكييف"),
        Pair("cleaning", "نظافة"),
        Pair("painting", "دهان"),
        Pair("moving", "نقل"),
        Pair("tech", "تقنية"),
        Pair("car", "سيارات")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = serviceTitle,
                    onValueChange = { serviceTitle = it },
                    label = { Text("اسم الخدمة") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text("التصنيف:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = serviceCategory == cat,
                            onClick = { serviceCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = servicePrice,
                        onValueChange = { servicePrice = it },
                        label = { Text("السعر") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = serviceDuration,
                        onValueChange = { serviceDuration = it },
                        label = { Text("المدة التقديرية") },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Text("أيقونة الخدمة:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(iconOptions) { (iconKey, label) ->
                        FilterChip(
                            selected = selectedIcon == iconKey,
                            onClick = { selectedIcon = iconKey },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = serviceDesc,
                    onValueChange = { serviceDesc = it },
                    label = { Text("وصف الخدمة ومميزاتها") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceNum = servicePrice.toDoubleOrNull() ?: 100.0
                    onConfirm(serviceTitle, serviceCategory, priceNum, serviceDuration, serviceDesc, selectedIcon)
                },
                enabled = serviceTitle.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
            ) {
                Text("حفظ الخدمة")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

// 4. ADMIN APP SETTINGS MANAGEMENT (Prompt: كما باقي تفاصيل التطبيق القابلة لتعديل)
@Composable
fun AdminSettingsScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    var appName by remember(appSettings) { mutableStateOf(appSettings?.appName ?: "خدماتي") }
    var supportPhone by remember(appSettings) { mutableStateOf(appSettings?.supportPhone ?: "+966501234567") }
    var supportWhatsApp by remember(appSettings) { mutableStateOf(appSettings?.supportWhatsApp ?: "+966501234567") }
    var supportEmail by remember(appSettings) { mutableStateOf(appSettings?.supportEmail ?: "support@khadamati.com") }
    var announcement by remember(appSettings) { mutableStateOf(appSettings?.announcementMessage ?: "") }
    var currency by remember(appSettings) { mutableStateOf(appSettings?.currency ?: "ر.س") }
    var workingHours by remember(appSettings) { mutableStateOf(appSettings?.workingHours ?: "8:00 ص - 11:30 م") }
    var inspectionFee by remember(appSettings) { mutableStateOf(appSettings?.inspectionFee?.toInt()?.toString() ?: "35") }

    var isSavedToast by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "تعديل تفاصيل وإعدادات التطبيق",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "يمكن للمدير تخصيص اسم التطبيق، الإعلانات للزبائن، أرقام الدعم، وساعات العمل",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "الهوية العامة والعملة",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = KhadamatiBluePrimary
                )

                OutlinedTextField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = { Text("اسم التطبيق الظاهر للزبائن") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        label = { Text("رمز العملة (مثال: ر.س، د.ج، $)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = workingHours,
                        onValueChange = { workingHours = it },
                        label = { Text("ساعات العمل") },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = inspectionFee,
                    onValueChange = { inspectionFee = it },
                    label = { Text("رسوم المعاينة الأولية") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Announcement card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "شريط الإعلانات والعروض للزبائن",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = KhadamatiBluePrimary
                )

                OutlinedTextField(
                    value = announcement,
                    onValueChange = { announcement = it },
                    label = { Text("نص الإعلان أو التنبيه في أعلى الشاشة الرئيسية") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Support channels card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "قنوات التواصل والدعم الفني",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = KhadamatiBluePrimary
                )

                OutlinedTextField(
                    value = supportPhone,
                    onValueChange = { supportPhone = it },
                    label = { Text("رقم هاتف الاتصال المباشر") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = supportWhatsApp,
                    onValueChange = { supportWhatsApp = it },
                    label = { Text("رقم الواتساب الرسمي") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = supportEmail,
                    onValueChange = { supportEmail = it },
                    label = { Text("البريد الإلكتروني للإدارة") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Save Button
        Button(
            onClick = {
                viewModel.updateAppSettings(
                    appName = appName,
                    supportPhone = supportPhone,
                    supportWhatsApp = supportWhatsApp,
                    supportEmail = supportEmail,
                    announcementMessage = announcement,
                    currency = currency,
                    workingHours = workingHours,
                    inspectionFee = inspectionFee.toDoubleOrNull() ?: 30.0
                )
                isSavedToast = true
                Toast.makeText(context, "تم حفظ تفاصيل التطبيق بنجاح!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "حفظ تفاصيل التطبيق في النظام",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isSavedToast) {
            Text(
                text = "✓ تم تحديث إعدادات وتفاصيل التطبيق بنجاح وستظهر مباشرة للزبائن",
                color = KhadamatiSuccess,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}
