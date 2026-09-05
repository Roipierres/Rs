package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.UserEntity
import com.example.ui.components.AiHomeAssistantDialog
import com.example.ui.components.InteractiveMapPicker
import com.example.ui.components.LiveWorkerTrackingCard
import com.example.ui.components.OrderChatAndCallSheet
import com.example.ui.components.OrderInvoiceDialog
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.RoiServiceHeroBadge
import com.example.ui.components.getServiceIcon
import com.example.ui.components.getServiceIconBg
import com.example.ui.components.getServiceIconTint
import com.example.ui.theme.KhadamatiAmberTertiary
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import com.example.ui.viewmodel.KhadamatiViewModel
import com.example.util.LocationHelper

@Composable
fun CustomerHomeScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val services by viewModel.filteredServices.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val selectedServiceForOrder by viewModel.selectedServiceForOrder.collectAsStateWithLifecycle()
    val orderSubmittedSuccess by viewModel.orderSubmittedSuccess.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var showAiAssistantDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        "الكل",
        "سباكة وصحي",
        "كهرباء",
        "تكييف وتبريد",
        "نظافة منزلية",
        "دهان وديكور",
        "نقل وتركيب",
        "تقنية ومعلومات",
        "سيارات"
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. ADVANCED ANIMATED HERO RS LOGO (Roi Service)
            item {
                RoiServiceHeroBadge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            // 2. Announcement Banner from Admin Settings
            item {
                appSettings?.announcementMessage?.takeIf { it.isNotBlank() }?.let { announcement ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = KhadamatiBluePrimary.copy(alpha = 0.08f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(KhadamatiBluePrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = KhadamatiBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = announcement,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Gemini AI Home Diagnosis Assistant Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { showAiAssistantDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = KhadamatiBlueDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(KhadamatiAmberTertiary.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = KhadamatiAmberTertiary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "المساعد الذكي للأعطال",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = KhadamatiAmberTertiary
                                    ) {
                                        Text(
                                            text = "Gemini AI",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = KhadamatiBlueDark,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "تشخيص فوري للأعطال المنزلية وإرشادات أمان لبيتك",
                                    fontSize = 11.sp,
                                    color = Color.LightGray
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = { showAiAssistantDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("فحص عطل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("ابحث عن خدمة (سباكة، تكييف، تنظيف...)") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = KhadamatiBluePrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "مسح")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Categories Row
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(cat) },
                            label = { Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KhadamatiBluePrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            // Header for Available Services
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الخدمات المتاحة (${services.size})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "سعر ثابت وضمان جودة",
                        fontSize = 12.sp,
                        color = KhadamatiSecondaryTeal,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Services Cards List
            if (services.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد خدمات مطابقة لبحثك حالياً",
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                items(services, key = { it.id }) { service ->
                    CustomerServiceCard(
                        service = service,
                        currency = appSettings?.currency ?: "ر.س",
                        onRequestClick = {
                            viewModel.openServiceRequest(service)
                        }
                    )
                }
            }
        }

        // Service Request Modal Sheet
        if (selectedServiceForOrder != null) {
            ServiceRequestBottomSheet(
                service = selectedServiceForOrder!!,
                currentUser = currentUser,
                currency = appSettings?.currency ?: "ر.س",
                onDismiss = { viewModel.closeServiceRequest() },
                onSubmit = { name, phone, date, time, notes, lat, lng, address ->
                    viewModel.submitServiceOrder(
                        customerName = name,
                        customerPhone = phone,
                        requestedDate = date,
                        requestedTimeSlot = time,
                        customerNotes = notes,
                        latitude = lat,
                        longitude = lng,
                        addressText = address,
                        onSuccess = {
                            // Dialog shown via orderSubmittedSuccess state
                        }
                    )
                }
            )
        }

        // Order Submitted Success Alert
        if (orderSubmittedSuccess != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearOrderSuccessMessage() },
                icon = {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = KhadamatiSuccess,
                        modifier = Modifier.size(54.dp)
                    )
                },
                title = {
                    Text(
                        text = "تم إرسال طلبك بنجاح!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column {
                        Text("رقم الطلب الخاص بك:")
                        Text(
                            text = orderSubmittedSuccess ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = KhadamatiBluePrimary,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                        Text(
                            "وصلت تفاصيل طلبك مع موقعك المحدد بدقة عبر الخرائط إلى لوحة تحكم الإدارة. سيتم التواصل معك مباشرة لتأكيد موعد وصول الفني.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearOrderSuccessMessage()
                            viewModel.setCustomerTab(1) // Move to My Orders
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                    ) {
                        Text("متابعة طلباتي")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { viewModel.clearOrderSuccessMessage() }) {
                        Text("إغلاق")
                    }
                }
            )
        }

        // Gemini AI Home Assistant Diagnosis Dialog
        if (showAiAssistantDialog) {
            AiHomeAssistantDialog(
                onSelectServiceCategory = { category ->
                    viewModel.setSelectedCategory(category)
                    showAiAssistantDialog = false
                },
                onDismiss = { showAiAssistantDialog = false }
            )
        }
    }
}

@Composable
fun CustomerServiceCard(
    service: ServiceEntity,
    currency: String,
    onRequestClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .clickable { onRequestClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Feature Tag Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF00E5FF).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "⚡ حجز مباشر وموقع دقيق",
                        color = Color(0xFF00838F),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF4CAF50).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "🛡️ ضمان خدمة معتمد",
                        color = Color(0xFF2E7D32),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upgraded Icon Container
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(getServiceIconBg(service.iconType)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getServiceIcon(service.iconType),
                        contentDescription = null,
                        tint = getServiceIconTint(service.iconType),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = KhadamatiBluePrimary.copy(alpha = 0.09f)
                        ) {
                            Text(
                                text = service.category,
                                color = KhadamatiBlueDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${service.rating} (${service.reviewCount})",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Price Badge
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${service.price.toInt()} $currency",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = KhadamatiBluePrimary
                    )
                    Text(
                        text = "سعر الخدمة",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = service.durationEstimate,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onRequestClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "طلب سريع عبر الخريطة",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestBottomSheet(
    service: ServiceEntity,
    currentUser: UserEntity?,
    currency: String,
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        phone: String,
        date: String,
        time: String,
        notes: String,
        lat: Double,
        lng: Double,
        address: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var customerName by remember { mutableStateOf(currentUser?.name ?: "") }
    var customerPhone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var requestedDate by remember { mutableStateOf("غداً") }
    var requestedTimeSlot by remember { mutableStateOf("صباحاً (09:00 ص - 12:00 م)") }
    var customerNotes by remember { mutableStateOf("") }
    var attachedPhotosCount by remember { mutableIntStateOf(0) }
    var attachedVideosCount by remember { mutableIntStateOf(0) }

    // Exact Map Location state (Algeria)
    var selectedLat by remember { mutableDoubleStateOf(currentUser?.defaultLatitude ?: 36.7441) }
    var selectedLng by remember { mutableDoubleStateOf(currentUser?.defaultLongitude ?: 3.0428) }
    var addressText by remember { mutableStateOf(currentUser?.defaultAddress ?: "ولاية الجزائر، بلدية حيدرة") }

    val dateOptions = listOf("اليوم (طلب عاجل)", "غداً", "بعد غد", "تحديد تاريخ آخر")
    val timeOptions = listOf(
        "صباحاً (09:00 ص - 12:00 م)",
        "ظهراً (01:00 م - 04:00 م)",
        "مساءً (05:00 م - 09:00 م)"
    )
    val popularLocations = listOf(
        Triple("الجزائر - حيدرة", 36.7441, 3.0428),
        Triple("الجزائر - سيدي امحمد", 36.7642, 3.0543),
        Triple("وهران - بئر الجير", 35.7167, -0.5500),
        Triple("قسنطينة - الخروب", 36.2628, 6.6942),
        Triple("البليدة - بوفاريك", 36.5700, 2.9100),
        Triple("سطيف - العلمة", 36.1500, 5.6800),
        Triple("عنابة - البوني", 36.8500, 7.7333),
        Triple("تلمسان - منصورة", 34.8667, -1.3333)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "طلب خدمة: ${service.title}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "السعر المقدر: ${service.price.toInt()} $currency",
                        fontSize = 13.sp,
                        color = KhadamatiBluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إلغاء")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Customer Name & Phone
            Text(
                text = "بيانات التواصل",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("الاسم الكامل للزبون") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("رقم هاتف الجوال للتواصل") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Preferred Date
            Text(
                text = "الموعد المفضل للخدمة",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dateOptions) { opt ->
                    FilterChip(
                        selected = requestedDate == opt,
                        onClick = { requestedDate = opt },
                        label = { Text(opt, fontSize = 12.sp) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(timeOptions) { slot ->
                    FilterChip(
                        selected = requestedTimeSlot == slot,
                        onClick = { requestedTimeSlot = slot },
                        label = { Text(slot, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Problem description
            Text(
                text = "وصف المشكلة / متطلبات الخدمة",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customerNotes,
                onValueChange = { customerNotes = it },
                placeholder = { Text("اكتب تفاصيل ما تحتاجه أو سبب العطل بدقة لمساعدة الفني...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Photo/Video Diagnosis (معاينة وتشخيص العطل بالصور والفيديو)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "تشخيص بصري للعطل (صور / فيديو مباشر)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "يساعد الفني على إحضار قطع الغيار والمعدات المناسبة مسبقاً",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                attachedPhotosCount++
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (attachedPhotosCount > 0) "صور ($attachedPhotosCount)" else "إرفاق صور",
                                fontSize = 11.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                attachedVideosCount++
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (attachedVideosCount > 0) "فيديو ($attachedVideosCount)" else "فيديو العطل",
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (attachedPhotosCount > 0 || attachedVideosCount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = KhadamatiSecondaryTeal.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = KhadamatiSecondaryTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "تم تجهيز المرفقات ($attachedPhotosCount صورة، $attachedVideosCount فيديو) لإرسالها مع الطلب للفني",
                                    fontSize = 10.sp,
                                    color = KhadamatiSecondaryTeal,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exact Location Section via Interactive Map (Core requirement!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = KhadamatiBluePrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "تحديد موقعك بدقة عبر الخريطة (GPS)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "سيصل هذا الموقع المحدد إلى لوحة تحكم الإدارة لتوجه الفني إليك مباشرة",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Preset location buttons
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(popularLocations) { (name, lat, lng) ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = KhadamatiBluePrimary.copy(alpha = 0.08f),
                        modifier = Modifier.clickable {
                            selectedLat = lat
                            selectedLng = lng
                            val parts = name.split(" - ")
                            addressText = if (parts.size == 2) "ولاية ${parts[0]}، بلدية ${parts[1]}" else "ولاية $name"
                        }
                    ) {
                        Text(
                            text = name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = KhadamatiBlueDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Map Component with Algeria Wilayas to Municipalities selector
            InteractiveMapPicker(
                latitude = selectedLat,
                longitude = selectedLng,
                onLocationChanged = { lat, lng ->
                    selectedLat = lat
                    selectedLng = lng
                },
                customerLabel = "موقع بيتك بالجزائر",
                heightDp = 260,
                onAddressSuggested = { suggestedAddress ->
                    addressText = suggestedAddress
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Detailed Address / Street text field
            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                label = { Text("تفاصيل العنوان الإضافية (اسم الشارع، رقم العمارة، الشقة)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    val finalNotes = buildString {
                        append(customerNotes)
                        if (attachedPhotosCount > 0 || attachedVideosCount > 0) {
                            if (customerNotes.isNotBlank()) append("\n")
                            append("📎 [مرفقات التشخيص: $attachedPhotosCount صورة، $attachedVideosCount فيديو]")
                        }
                    }
                    onSubmit(
                        customerName,
                        customerPhone,
                        requestedDate,
                        requestedTimeSlot,
                        finalNotes,
                        selectedLat,
                        selectedLng,
                        addressText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تأكيد وإرسال الطلب للإدارة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomerOrdersScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var selectedOrderForDetail by remember { mutableStateOf<OrderEntity?>(null) }
    var activeChatOrder by remember { mutableStateOf<OrderEntity?>(null) }
    var selectedOrderForTracking by remember { mutableStateOf<OrderEntity?>(null) }
    var selectedOrderForInvoice by remember { mutableStateOf<OrderEntity?>(null) }

    // Filter customer orders (or show all recent for demo test convenience)
    val customerOrders = allOrders

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "طلباتي السابقة والنشطة (${customerOrders.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "يمكنك متابعة حالة الطلب وموقع الفني وتفاصيل التنفيذ",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (customerOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.HomeRepairService,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لم تقم بأي طلب خدمة حتى الآن",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.setCustomerTab(0) },
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                    ) {
                        Text("استعراض الخدمات المتاحة")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(customerOrders, key = { it.id }) { order ->
                    CustomerOrderCard(
                        order = order,
                        currency = appSettings?.currency ?: "د.ج",
                        onCardClick = { selectedOrderForDetail = order },
                        onOpenChat = { activeChatOrder = order },
                        onOpenTracking = { selectedOrderForTracking = order },
                        onOpenInvoice = { selectedOrderForInvoice = order }
                    )
                }
            }
        }
    }

    // Chat and Call Log Sheet between Customer and Worker
    if (activeChatOrder != null) {
        OrderChatAndCallSheet(
            order = activeChatOrder!!,
            viewModel = viewModel,
            currentUserRole = "CUSTOMER",
            onDismiss = { activeChatOrder = null }
        )
    }

    // 1. Live Worker Tracking Map Dialog
    if (selectedOrderForTracking != null) {
        AlertDialog(
            onDismissRequest = { selectedOrderForTracking = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تتبع مباشر لموقع الفني",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = { selectedOrderForTracking = null }) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }
            },
            text = {
                LiveWorkerTrackingCard(
                    order = selectedOrderForTracking!!,
                    onOpenChat = {
                        val o = selectedOrderForTracking!!
                        selectedOrderForTracking = null
                        activeChatOrder = o
                    },
                    onCallWorker = {
                        val phone = appSettings?.supportPhone ?: "+213555123456"
                        LocationHelper.dialPhone(context, phone)
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = { selectedOrderForTracking = null },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                ) {
                    Text("إغلاق التتبع")
                }
            }
        )
    }

    // 2. Electronic Invoice & QR Dialog
    if (selectedOrderForInvoice != null) {
        OrderInvoiceDialog(
            order = selectedOrderForInvoice!!,
            currency = appSettings?.currency ?: "د.ج",
            onDismiss = { selectedOrderForInvoice = null }
        )
    }

    // Customer Order Detail Dialog
    if (selectedOrderForDetail != null) {
        val order = selectedOrderForDetail!!
        AlertDialog(
            onDismissRequest = { selectedOrderForDetail = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.orderNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    OrderStatusBadge(order.status)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = order.serviceName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = KhadamatiBluePrimary
                    )
                    Text(
                        text = "التكلفة: ${order.servicePrice.toInt()} ${appSettings?.currency ?: "د.ج"}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "الموعد: ${order.requestedDate} - ${order.requestedTimeSlot}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    if (order.customerNotes.isNotBlank()) {
                        Text(
                            text = "ملاحظاتك: ${order.customerNotes}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    if (order.assignedWorker.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = KhadamatiSecondaryTeal.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "الفني المسؤول: ${order.assignedWorker}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = KhadamatiSecondaryTeal,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "موقع الخدمة المحدد:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = order.addressText,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    // Mini map showing the exact spot
                    InteractiveMapPicker(
                        latitude = order.latitude,
                        longitude = order.longitude,
                        onLocationChanged = { _, _ -> },
                        isReadOnly = true,
                        customerLabel = "موقع طلبك",
                        heightDp = 180
                    )
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (order.status == "ASSIGNED" || order.status == "IN_PROGRESS") {
                        FilledTonalButton(
                            onClick = {
                                selectedOrderForTracking = order
                                selectedOrderForDetail = null
                            }
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تتبع")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            selectedOrderForInvoice = order
                            selectedOrderForDetail = null
                        }
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("الفاتورة")
                    }

                    Button(
                        onClick = {
                            activeChatOrder = order
                            selectedOrderForDetail = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("شات")
                    }
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedOrderForDetail = null }) {
                    Text("إغلاق")
                }
            }
        )
    }
}

@Composable
fun CustomerOrderCard(
    order: OrderEntity,
    currency: String,
    onCardClick: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenTracking: () -> Unit,
    onOpenInvoice: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
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
                Text(
                    text = order.orderNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = KhadamatiBluePrimary
                )
                OrderStatusBadge(order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.serviceName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${order.requestedDate} • ${order.requestedTimeSlot}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = KhadamatiSecondaryTeal, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.addressText,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.servicePrice.toInt()} $currency",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = KhadamatiBlueDark
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // 1. Live Tracking Map Button
                    if (order.status == "ASSIGNED" || order.status == "IN_PROGRESS") {
                        FilledTonalButton(
                            onClick = onOpenTracking,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("تتبع الفني", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // 2. Electronic Invoice Button
                    IconButton(
                        onClick = onOpenInvoice,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Receipt,
                            contentDescription = "الفاتورة الإلكترونية",
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = onOpenChat,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("شات", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onCardClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("التفاصيل", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerProfileScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf(currentUser?.name ?: "أمين بوعلام") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "0555123456") }
    var email by remember { mutableStateOf(currentUser?.email ?: "amine.boualem@gmail.com") }
    var address by remember { mutableStateOf(currentUser?.defaultAddress ?: "ولاية الجزائر، بلدية حيدرة") }
    var isSaved by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Avatar Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(KhadamatiBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = currentUser?.name ?: "حساب الزبون",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "زبون مسجل في منصة خدماتي",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "بيانات الحساب والعناوين",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الهاتف") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("البريد الإلكتروني") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("العنوان الافتراضي") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.registerCustomer(name, phone, email, address)
                        isSaved = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("حفظ التعديلات")
                }

                if (isSaved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ تم حفظ البيانات بنجاح",
                        color = KhadamatiSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Switch to Admin Control Panel button
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KhadamatiAmberTertiary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = KhadamatiAmberTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "صلاحيات الإدارة والمدير",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = KhadamatiAmberTertiary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "بصفتك مدير النظام، يمكنك الانتقال إلى لوحة تحكم الإدارة لمتابعة الطلبات الواردة على الخريطة وإضافة وتعديل وحذف الخدمات وتعديل تفاصيل المنصة.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.switchToAdmin() },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBlueDark),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("الدخول إلى لوحة تحكم المدير")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
