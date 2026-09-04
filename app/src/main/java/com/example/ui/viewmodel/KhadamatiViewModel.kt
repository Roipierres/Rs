package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.KhadamatiDatabase
import com.example.data.model.AppSettingsEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserEntity
import com.example.data.repository.KhadamatiRepository
import com.example.util.AppNotification
import com.example.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KhadamatiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KhadamatiRepository

    init {
        val db = KhadamatiDatabase.getInstance(application)
        repository = KhadamatiRepository(
            userDao = db.userDao(),
            serviceDao = db.serviceDao(),
            orderDao = db.orderDao(),
            appSettingsDao = db.appSettingsDao()
        )
        // Seed initial data
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
            // Load initial customer & admin
            val admin = repository.getAdminUser()
            val customer = repository.getLatestCustomer()
            if (_currentUser.value == null) {
                _currentUser.value = customer ?: admin
            }
        }
    }

    // Role state: "CUSTOMER" or "ADMIN"
    private val _currentRole = MutableStateFlow("CUSTOMER")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    // Logged in user
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Navigation tab index
    private val _customerTab = MutableStateFlow(0) // 0: Services, 1: My Orders, 2: Profile
    val customerTab: StateFlow<Int> = _customerTab.asStateFlow()

    private val _adminTab = MutableStateFlow(0) // 0: Dashboard Stats, 1: Orders Inbox, 2: Services CRUD, 3: Settings
    val adminTab: StateFlow<Int> = _adminTab.asStateFlow()

    // App Settings
    val appSettings: StateFlow<AppSettingsEntity?> = repository.appSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettingsEntity()
    )

    // Services
    val allServices: StateFlow<List<ServiceEntity>> = repository.allServices.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeServices: StateFlow<List<ServiceEntity>> = repository.activeServices.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Orders
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filters for Services Screen
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Filtered Services for Customer UI
    val filteredServices: StateFlow<List<ServiceEntity>> = combine(
        activeServices,
        searchQuery,
        selectedCategory
    ) { services, query, category ->
        services.filter { service ->
            val matchesCategory = (category == "الكل" || service.category == category)
            val matchesQuery = query.isBlank() ||
                    service.title.contains(query, ignoreCase = true) ||
                    service.description.contains(query, ignoreCase = true) ||
                    service.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Service Request Sheet / Flow
    private val _selectedServiceForOrder = MutableStateFlow<ServiceEntity?>(null)
    val selectedServiceForOrder: StateFlow<ServiceEntity?> = _selectedServiceForOrder.asStateFlow()

    private val _orderSubmittedSuccess = MutableStateFlow<String?>(null)
    val orderSubmittedSuccess: StateFlow<String?> = _orderSubmittedSuccess.asStateFlow()

    // Inspecting order in Admin detail modal
    private val _adminInspectingOrder = MutableStateFlow<OrderEntity?>(null)
    val adminInspectingOrder: StateFlow<OrderEntity?> = _adminInspectingOrder.asStateFlow()

    // Order status filter in Admin Orders list ("ALL", "NEW", "IN_PROGRESS", "COMPLETED")
    private val _adminOrderStatusFilter = MutableStateFlow("ALL")
    val adminOrderStatusFilter: StateFlow<String> = _adminOrderStatusFilter.asStateFlow()

    // In-App Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                id = "notif-welcome",
                title = "مرحباً بك في Roi Service (RS) ✨",
                message = "يسعدنا خدمتك! يمكنك الآن طلب أي خدمة منزلية مع تحديد موقعك المباشر بنقرة واحدة عبر الخرائط.",
                type = "SYSTEM",
                isRead = false
            ),
            AppNotification(
                id = "notif-promo",
                title = "عرض الافتتاح الحصري 🎁",
                message = "خصم 20% على خدمات الصيانة والتكييف لهذا الأسبوع بمناسبة إطلاق تطبيق Roi Service المطور.",
                type = "PROMO",
                isRead = false
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    val unreadNotificationsCount: StateFlow<Int> = _notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    fun toggleNotificationsSheet(show: Boolean) {
        _showNotificationsSheet.value = show
        if (show) {
            markAllNotificationsAsRead()
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    fun setCustomerTab(index: Int) { _customerTab.value = index }
    fun setAdminTab(index: Int) { _adminTab.value = index }
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedCategory(cat: String) { _selectedCategory.value = cat }
    fun setAdminOrderStatusFilter(filter: String) { _adminOrderStatusFilter.value = filter }

    fun switchToAdmin() {
        viewModelScope.launch {
            val admin = repository.getAdminUser()
            if (admin != null) {
                _currentUser.value = admin
            }
            _currentRole.value = "ADMIN"
        }
    }

    fun switchToCustomer() {
        viewModelScope.launch {
            val customer = repository.getLatestCustomer()
            if (customer != null) {
                _currentUser.value = customer
            }
            _currentRole.value = "CUSTOMER"
        }
    }

    fun openServiceRequest(service: ServiceEntity) {
        _selectedServiceForOrder.value = service
    }

    fun closeServiceRequest() {
        _selectedServiceForOrder.value = null
    }

    fun clearOrderSuccessMessage() {
        _orderSubmittedSuccess.value = null
    }

    fun setAdminInspectingOrder(order: OrderEntity?) {
        _adminInspectingOrder.value = order
    }

    // Submit Order (Customer Action)
    fun submitServiceOrder(
        customerName: String,
        customerPhone: String,
        requestedDate: String,
        requestedTimeSlot: String,
        customerNotes: String,
        latitude: Double,
        longitude: Double,
        addressText: String,
        onSuccess: (String) -> Unit
    ) {
        val service = _selectedServiceForOrder.value ?: return
        val user = _currentUser.value

        viewModelScope.launch {
            val orderNum = "KHD-" + (10000..99999).random()
            val order = OrderEntity(
                orderNumber = orderNum,
                customerId = user?.id ?: 1L,
                customerName = customerName.ifBlank { user?.name ?: "عميل المنصة" },
                customerPhone = customerPhone.ifBlank { user?.phone ?: "+966500000000" },
                serviceId = service.id,
                serviceName = service.title,
                servicePrice = service.price,
                status = "NEW",
                requestedDate = requestedDate,
                requestedTimeSlot = requestedTimeSlot,
                customerNotes = customerNotes,
                latitude = latitude,
                longitude = longitude,
                addressText = addressText.ifBlank { "موقع الخريطة المحدد ($latitude, $longitude)" },
                adminNotes = "",
                assignedWorker = ""
            )

            repository.createOrder(order)
            _selectedServiceForOrder.value = null
            _orderSubmittedSuccess.value = orderNum

            // Trigger In-App Notification
            val notif = AppNotification(
                id = "notif-${System.currentTimeMillis()}",
                title = "تم تأكيد طلبك: ${service.title} 🚀",
                message = "رقم الطلب: $orderNum. وصل موقعك الجغرافي المحدد بدقة إلى لوحة إدارة Roi Service وجاري إرسال الفني.",
                type = "ORDER",
                isRead = false
            )
            _notifications.value = listOf(notif) + _notifications.value

            // Trigger External System Notification
            NotificationHelper.showSystemNotification(
                context = getApplication(),
                notificationId = (1000..9999).random(),
                title = "Roi Service - تأكيد الطلب ($orderNum)",
                message = "تم استلام طلبك لخدمة ${service.title} بنجاح وموقعك محدد بدقة على الخريطة."
            )

            onSuccess(orderNum)
        }
    }

    // Customer Registration
    fun registerCustomer(
        name: String,
        phone: String,
        email: String,
        address: String,
        latitude: Double = 24.7136,
        longitude: Double = 46.6753
    ) {
        viewModelScope.launch {
            val newUser = UserEntity(
                name = name,
                phone = phone,
                email = email,
                role = "CUSTOMER",
                defaultAddress = address,
                defaultLatitude = latitude,
                defaultLongitude = longitude
            )
            val newId = repository.registerUser(newUser)
            _currentUser.value = repository.getUserById(newId)
            _currentRole.value = "CUSTOMER"
        }
    }

    // Admin Actions: Services CRUD
    fun addService(
        title: String,
        category: String,
        price: Double,
        duration: String,
        description: String,
        iconType: String
    ) {
        viewModelScope.launch {
            val service = ServiceEntity(
                title = title,
                category = category,
                price = price,
                durationEstimate = duration,
                description = description,
                iconType = iconType,
                isActive = true
            )
            repository.addService(service)
        }
    }

    fun updateService(service: ServiceEntity) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun deleteService(serviceId: Long) {
        viewModelScope.launch {
            repository.deleteServiceById(serviceId)
        }
    }

    fun toggleServiceStatus(service: ServiceEntity) {
        viewModelScope.launch {
            repository.updateService(service.copy(isActive = !service.isActive))
        }
    }

    // Admin Actions: Order Management
    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            // Update currently inspected order if opened
            val currentInspected = _adminInspectingOrder.value
            if (currentInspected?.id == orderId) {
                _adminInspectingOrder.value = currentInspected.copy(status = newStatus)
            }

            val statusAr = when (newStatus) {
                "CONFIRMED" -> "تم تأكيد الطلب وتحديد موعد الفني"
                "IN_PROGRESS" -> "الفني في الطريق إليك وجاري تنفيذ الخدمة"
                "COMPLETED" -> "تم إنجاز الخدمة بنجاح! شكراً لتعاملك مع Roi Service"
                "CANCELLED" -> "تم إلغاء الطلب بناءً على المراجعة"
                else -> newStatus
            }

            // In-app Notification
            val notif = AppNotification(
                id = "notif-status-${System.currentTimeMillis()}",
                title = "تحديث حالة الطلب #$orderId",
                message = statusAr,
                type = "ORDER",
                isRead = false
            )
            _notifications.value = listOf(notif) + _notifications.value

            // System Notification
            NotificationHelper.showSystemNotification(
                context = getApplication(),
                notificationId = orderId.toInt(),
                title = "تحديث من Roi Service (RS)",
                message = "حالة طلبك: $statusAr"
            )
        }
    }

    fun updateOrderAdminDetails(orderId: Long, adminNotes: String, assignedWorker: String) {
        viewModelScope.launch {
            val existing = repository.getOrderById(orderId)
            if (existing != null) {
                val updated = existing.copy(
                    adminNotes = adminNotes,
                    assignedWorker = assignedWorker
                )
                repository.updateOrder(updated)
                _adminInspectingOrder.value = updated
            }
        }
    }

    // Admin Actions: Settings
    fun updateAppSettings(
        appName: String,
        supportPhone: String,
        supportWhatsApp: String,
        supportEmail: String,
        announcementMessage: String,
        currency: String,
        workingHours: String,
        inspectionFee: Double
    ) {
        viewModelScope.launch {
            val current = appSettings.value ?: AppSettingsEntity()
            val updated = current.copy(
                appName = appName,
                supportPhone = supportPhone,
                supportWhatsApp = supportWhatsApp,
                supportEmail = supportEmail,
                announcementMessage = announcementMessage,
                currency = currency,
                workingHours = workingHours,
                inspectionFee = inspectionFee
            )
            repository.updateAppSettings(updated)

            // Notify users of announcement update
            if (announcementMessage.isNotBlank()) {
                val notif = AppNotification(
                    id = "notif-announcement-${System.currentTimeMillis()}",
                    title = "إشعار من إدارة $appName",
                    message = announcementMessage,
                    type = "PROMO",
                    isRead = false
                )
                _notifications.value = listOf(notif) + _notifications.value

                NotificationHelper.showSystemNotification(
                    context = getApplication(),
                    notificationId = 999,
                    title = appName,
                    message = announcementMessage
                )
            }
        }
    }
}
