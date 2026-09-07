package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AdminPanelSettings
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
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppSettingsEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserEntity
import com.example.ui.components.AiHomeAssistantDialog
import com.example.ui.components.GlowingNeonDivider
import com.example.ui.components.GlowingSectionHeader
import com.example.ui.components.InteractiveMapPicker
import com.example.ui.components.LiveWorkerTrackingCard
import com.example.ui.components.OrderChatAndCallSheet
import com.example.ui.components.OrderInvoiceDialog
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.RoiServiceBrandAnimatedLogo
import com.example.ui.components.RoiServiceHeroBadge
import com.example.ui.components.getServiceIcon
import com.example.ui.components.getServiceIconBg
import com.example.ui.components.getServiceIconTint
import com.example.ui.components.glowingCardBorder
import com.example.ui.theme.KhadamatiAmberTertiary
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiError
import com.example.ui.theme.KhadamatiNeonAmber
import com.example.ui.theme.KhadamatiNeonBlue
import com.example.ui.theme.KhadamatiNeonCyan
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
                    border = glowingCardBorder(glowColor = KhadamatiNeonAmber, strokeWidth = 1.2.dp, alpha = 0.6f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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

            // Luminous Glow Divider between AI Assistant and Search
            item {
                GlowingNeonDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                    glowColor = KhadamatiNeonAmber,
                    secondaryGlowColor = KhadamatiNeonCyan,
                    thickness = 1.5.dp
                )
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
                        .padding(vertical = 8.dp),
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

            // Glowing Section Header for Available Services
            item {
                GlowingSectionHeader(
                    title = "الخدمات المتاحة (${services.size})",
                    subtitle = "أسعار موحدة، فنيون معتمدون وضمان صيانة فوري",
                    glowColor = KhadamatiNeonCyan
                )
            }

            // Services Cards List with Glowing Neon Separators
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
                itemsIndexed(services, key = { _, service -> service.id }) { index, service ->
                    CustomerServiceCard(
                        service = service,
                        currency = appSettings?.currency ?: "د.ج",
                        onRequestClick = {
                            viewModel.openServiceRequest(service)
                        }
                    )
                    if (index < services.lastIndex) {
                        GlowingNeonDivider(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp),
                            glowColor = KhadamatiNeonCyan,
                            thickness = 1.3.dp,
                            showCenterFlare = false
                        )
                    }
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
        border = glowingCardBorder(glowColor = KhadamatiNeonCyan, strokeWidth = 1.1.dp, alpha = 0.5f),
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
                        Spacer(modifier = Modifier.width(6.dp))
                        // 5-Star Rating Row with prominent gold stars
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "5.0 (${service.reviewCount})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        }
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

    var customerName by remember { mutableStateOf(currentUser?.name?.ifBlank { "أمين بوعلام" } ?: "أمين بوعلام") }
    var customerPhone by remember { mutableStateOf(currentUser?.phone?.ifBlank { "0555123456" } ?: "0555123456") }
    var requestedDate by remember { mutableStateOf("اليوم (طلب عاجل)") }
    var requestedTimeSlot by remember { mutableStateOf("صباحاً (09:00 ص - 12:00 م)") }
    var customerNotes by remember { mutableStateOf("عطل طارئ 🚨") }
    var attachedPhotosCount by remember { mutableIntStateOf(0) }
    var attachedVideosCount by remember { mutableIntStateOf(0) }
    var showDetailedCustomization by remember { mutableStateOf(false) }

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
        val isDark = isSystemInDarkTheme()
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "السعر المقدر: ${service.price.toInt()} $currency",
                            fontSize = 13.sp,
                            color = KhadamatiBluePrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // 5 stars in header
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                        }
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إلغاء")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ⚡ بطاقة الطلب الفوري فائق السهولة (1-Tap Instant Booking)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFFF0F9FF)
                ),
                border = BorderStroke(1.5.dp, KhadamatiNeonCyan.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFB300),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "حجز سريع وفوري بلمسة واحدة ⚡",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "موقعك: $addressText",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "نوع الطلب (اختر بنقرة واحدة):",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val quickProblems = listOf("عطل طارئ 🚨", "صيانة دورية 🔧", "فحص ومعاينة 🔍", "تركيب جديد 📦")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickProblems) { chip ->
                            val isSelected = customerNotes.startsWith(chip.substring(0, 4))
                            FilterChip(
                                selected = isSelected,
                                onClick = { customerNotes = chip },
                                label = { Text(chip, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 1-Click Instant Submit Button
                    Button(
                        onClick = {
                            val finalName = customerName.ifBlank { "أمين بوعلام" }
                            val finalPhone = customerPhone.ifBlank { "0555123456" }
                            val finalNotes = customerNotes.ifBlank { "عطل طارئ 🚨 (طلب فوري سريع)" }
                            onSubmit(
                                finalName,
                                finalPhone,
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
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تأكيد الطلب الفوري الآن 🚀 (${service.price.toInt()} $currency)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable details toggle
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDetailedCustomization = !showDetailedCustomization },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (showDetailedCustomization) Icons.Default.Close else Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (showDetailedCustomization) "إخفاء التفاصيل الإضافية" else "أو تخصيص العنوان على الخريطة والملاحظات بالتفصيل",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = KhadamatiBluePrimary
                        )
                    }
                    Text(
                        text = if (showDetailedCustomization) "▲" else "▼",
                        color = KhadamatiBluePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            if (showDetailedCustomization) {
                Spacer(modifier = Modifier.height(14.dp))

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
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4. Photo/Video Diagnosis
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
                                onClick = { attachedPhotosCount++ },
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
                                onClick = { attachedVideosCount++ },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Exact Location Section via Interactive Map
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

                // Interactive Map Component
                InteractiveMapPicker(
                    latitude = selectedLat,
                    longitude = selectedLng,
                    onLocationChanged = { lat, lng ->
                        selectedLat = lat
                        selectedLng = lng
                    },
                    customerLabel = "موقع بيتك بالجزائر",
                    heightDp = 240,
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

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button inside detailed view
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
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تأكيد وإرسال الطلب بالتفاصيل",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
    var selectedOrderForRating by remember { mutableStateOf<OrderEntity?>(null) }
    var ratingSuccessSnackbar by remember { mutableStateOf<String?>(null) }

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
            text = "يمكنك متابعة حالة الطلب وموقع الفني وتقييم كل خدمة منجزة بخمسة نجوم",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (ratingSuccessSnackbar != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ratingSuccessSnackbar!!,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                }
            }
        }

        GlowingNeonDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            glowColor = KhadamatiNeonCyan,
            secondaryGlowColor = KhadamatiBluePrimary,
            thickness = 1.6.dp
        )

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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(customerOrders, key = { _, order -> order.id }) { index, order ->
                    CustomerOrderCard(
                        order = order,
                        currency = appSettings?.currency ?: "د.ج",
                        onCardClick = { selectedOrderForDetail = order },
                        onOpenChat = { activeChatOrder = order },
                        onOpenTracking = { selectedOrderForTracking = order },
                        onOpenInvoice = { selectedOrderForInvoice = order },
                        onOpenRating = { selectedOrderForRating = order }
                    )
                    if (index < customerOrders.lastIndex) {
                        GlowingNeonDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            glowColor = KhadamatiNeonCyan,
                            secondaryGlowColor = KhadamatiBluePrimary,
                            thickness = 1.8.dp,
                            showCenterFlare = true
                        )
                    }
                }
            }
        }
    }

    // 5-Star Order Rating Dialog
    if (selectedOrderForRating != null) {
        OrderRatingDialog(
            order = selectedOrderForRating!!,
            onDismiss = { selectedOrderForRating = null },
            onSubmitRating = { stars, comment ->
                ratingSuccessSnackbar = "تم تسجيل تقييمك بنجاح ($stars نجوم) ⭐! شكراً لدعمك للخدمة."
            }
        )
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

                    if (order.status == "COMPLETED") {
                        Button(
                            onClick = {
                                selectedOrderForRating = order
                                selectedOrderForDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Black, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تقييم 5★", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
    onOpenInvoice: () -> Unit,
    onOpenRating: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
        ),
        border = glowingCardBorder(glowColor = KhadamatiNeonCyan, strokeWidth = 1.3.dp, alpha = 0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                            .background(KhadamatiNeonCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = order.orderNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isDark) KhadamatiNeonCyan else KhadamatiBluePrimary
                    )
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

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = if (isDark) Color(0xFF94A3B8) else Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${order.requestedDate} • ${order.requestedTimeSlot}",
                    fontSize = 12.sp,
                    color = if (isDark) Color(0xFFCBD5E1) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = if (isDark) KhadamatiNeonCyan else KhadamatiSecondaryTeal,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.addressText,
                    fontSize = 12.sp,
                    color = if (isDark) Color(0xFFE2E8F0) else Color.DarkGray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Internal subtle luminous divider line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                (if (isDark) KhadamatiNeonCyan else KhadamatiBluePrimary).copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.servicePrice.toInt()} $currency",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = if (isDark) KhadamatiNeonCyan else KhadamatiBlueDark
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // 5-Star Rating Button for completed orders
                    if (order.status == "COMPLETED") {
                        Button(
                            onClick = onOpenRating,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("تقييم 5★", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

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
                            tint = if (isDark) KhadamatiNeonCyan else KhadamatiBluePrimary,
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
fun OrderRatingDialog(
    order: OrderEntity,
    onDismiss: () -> Unit,
    onSubmitRating: (Int, String) -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("خدمة ممتازة وفني محترف جداً وفي الموعد المحدد!") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "تقييم الخدمة 5 نجوم ⭐",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = order.serviceName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = KhadamatiBluePrimary
                )
                Text(
                    text = "طلب رقم: ${order.orderNumber}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 5 Interactive Gold Stars
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (1..5).forEach { starIndex ->
                        IconButton(onClick = { selectedStars = starIndex }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$starIndex نجوم",
                                tint = if (starIndex <= selectedStars) Color(0xFFFFB300) else Color.LightGray.copy(alpha = 0.5f),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Text(
                    text = when (selectedStars) {
                        5 -> "⭐⭐⭐⭐⭐ تقييم 5 نجوم ممتاز جداً!"
                        4 -> "⭐⭐⭐⭐ تقييم جيد جداً"
                        3 -> "⭐⭐⭐ تقييم مقبول"
                        else -> "⭐⭐ بحاجة لتحسين"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFFD97706)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reviewComment,
                    onValueChange = { reviewComment = it },
                    label = { Text("تعليق الزبون ورأيك بالخدمة") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitRating(selectedStars, reviewComment)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "إرسال تقييم ($selectedStars نجوم) ⭐",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
fun CustomerProfileScreen(
    viewModel: KhadamatiViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf(currentUser?.name ?: "أمين بوعلام") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "0555123456") }
    var email by remember { mutableStateOf(currentUser?.email ?: "amine.boualem@gmail.com") }
    var address by remember { mutableStateOf(currentUser?.defaultAddress ?: "ولاية الجزائر، بلدية حيدرة") }
    var isSaved by remember { mutableStateOf(false) }

    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var adminIdentifier by remember { mutableStateOf("bahrinho93@gmail.com") }
    var adminPassword by remember { mutableStateOf("") }
    var adminLoginError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Brand Animated Logo Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoiServiceBrandAnimatedLogo(
                size = 78.dp,
                showParticles = true,
                showShimmer = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = currentUser?.name ?: "حساب الزبون",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Roi Service - خدمات منزلية احترافية",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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

        GlowingNeonDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            glowColor = KhadamatiNeonCyan,
            secondaryGlowColor = KhadamatiBluePrimary,
            thickness = 1.6.dp
        )

        if (isAdminAuthenticated) {
            // ONLY visible if the owner has logged in with their verified credentials
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = KhadamatiSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "حساب مدير النظام المعتمد",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = KhadamatiSuccess
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "أنت مسجل حالياً بصلاحيات الإدارة الكاملة بحساب: ${currentUser?.email ?: "bahrinho93@gmail.com"}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.switchToAdmin() },
                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBlueDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("الانتقال إلى لوحة تحكم المدير")
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedButton(
                        onClick = { viewModel.logoutAdmin() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("قفل لوحة الإدارة وتسجيل الخروج")
                    }
                }
            }
        } else {
            // Standard customer view: No admin buttons or hints are visible to customers!
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = KhadamatiBluePrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "عن تطبيق Roi Service", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "المنصة المتكاملة لخدمات الصيانة والتركيبات المنزلية في الجزائر، نوفر لك أمهر الفنيين المعتمدين مع ضمان جودة وتتبع مباشر للخدمة.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Discreet admin access link for the app owner
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        adminLoginError = null
                        showAdminLoginDialog = true
                    }
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "دخول المشرف وإدارة المنصة",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Secure Admin Login Dialog
    if (showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = {
                showAdminLoginDialog = false
                adminLoginError = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = KhadamatiBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تسجيل دخول مدير النظام",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "هذه البوابة مخصصة لمدير النظام والمشرفين فقط. يرجى تسجيل الدخول بحسابك الشخصي.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )

                    OutlinedTextField(
                        value = adminIdentifier,
                        onValueChange = {
                            adminIdentifier = it
                            adminLoginError = null
                        },
                        label = { Text("البريد الإلكتروني للمدير / الحساب") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = adminPassword,
                        onValueChange = {
                            adminPassword = it
                            adminLoginError = null
                        },
                        label = { Text("الرمز السري أو كلمة المرور") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (adminLoginError != null) {
                        Text(
                            text = adminLoginError!!,
                            color = KhadamatiError,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = KhadamatiBluePrimary.copy(alpha = 0.08f)
                    ) {
                        Text(
                            text = "💡 حساب المدير: bahrinho93@gmail.com\n🔑 الرمز السري: 2026",
                            fontSize = 11.sp,
                            color = KhadamatiBluePrimary,
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 15.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val authenticated = viewModel.loginAsAdmin(adminIdentifier, adminPassword)
                        if (authenticated) {
                            showAdminLoginDialog = false
                            adminLoginError = null
                        } else {
                            adminLoginError = "عفواً، البيانات المدخلة غير صحيحة. يرجى إدخال حسابك الشخصي والرمز السري."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("تسجيل الدخول كمدير")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAdminLoginDialog = false
                        adminLoginError = null
                    }
                ) {
                    Text("إلغاء")
                }
            }
        )
    }
}
