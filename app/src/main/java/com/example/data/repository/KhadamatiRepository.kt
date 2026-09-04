package com.example.data.repository

import com.example.data.dao.AppSettingsDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ServiceDao
import com.example.data.dao.UserDao
import com.example.data.model.AppSettingsEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class KhadamatiRepository(
    private val userDao: UserDao,
    private val serviceDao: ServiceDao,
    private val orderDao: OrderDao,
    private val appSettingsDao: AppSettingsDao
) {
    // Services
    val allServices: Flow<List<ServiceEntity>> = serviceDao.getAllServices()
    val activeServices: Flow<List<ServiceEntity>> = serviceDao.getActiveServices()

    suspend fun getServiceById(id: Long): ServiceEntity? = serviceDao.getServiceById(id)
    suspend fun addService(service: ServiceEntity): Long = serviceDao.insertService(service)
    suspend fun updateService(service: ServiceEntity) = serviceDao.updateService(service)
    suspend fun deleteService(service: ServiceEntity) = serviceDao.deleteService(service)
    suspend fun deleteServiceById(id: Long) = serviceDao.deleteServiceById(id)

    // Orders
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    fun getCustomerOrders(customerId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersByCustomer(customerId)

    suspend fun getOrderById(id: Long): OrderEntity? = orderDao.getOrderById(id)
    suspend fun createOrder(order: OrderEntity): Long = orderDao.insertOrder(order)
    suspend fun updateOrderStatus(orderId: Long, newStatus: String) = orderDao.updateOrderStatus(orderId, newStatus)
    suspend fun updateOrder(order: OrderEntity) = orderDao.updateOrder(order)
    suspend fun deleteOrder(orderId: Long) = orderDao.deleteOrderById(orderId)

    // Users
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)
    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun getAdminUser(): UserEntity? = userDao.getAdminUser()
    suspend fun getLatestCustomer(): UserEntity? = userDao.getLatestCustomer()
    suspend fun registerUser(user: UserEntity): Long = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    // Settings
    val appSettings: Flow<AppSettingsEntity?> = appSettingsDao.getSettings()
    suspend fun updateAppSettings(settings: AppSettingsEntity) = appSettingsDao.insertOrUpdate(settings)

    // Initial Data Seeding
    suspend fun checkAndSeedDatabase() {
        // 1. Seed Settings
        val currentSettings = appSettingsDao.getSettingsSync()
        if (currentSettings == null) {
            appSettingsDao.insertOrUpdate(
                AppSettingsEntity(
                    id = 1,
                    appName = "Roi Service",
                    supportPhone = "+213555123456",
                    supportWhatsApp = "+213555123456",
                    supportEmail = "support@roiservice.dz",
                    announcementMessage = "مرحباً بكم في تطبيق Roi Service (RS) بالجزائر! خدمات صيانة منزلية فورية عبر كافة الولايات والبلديات.",
                    currency = "د.ج",
                    workingHours = "8:00 ص - 10:00 م",
                    inspectionFee = 1000.0,
                    isEmergencyActive = true
                )
            )
        } else if (currentSettings.currency != "د.ج") {
            // Automatically upgrade currency to Algerian Dinar
            appSettingsDao.insertOrUpdate(
                currentSettings.copy(
                    appName = "Roi Service",
                    currency = "د.ج",
                    inspectionFee = 1000.0,
                    supportPhone = "+213555123456",
                    supportWhatsApp = "+213555123456",
                    supportEmail = "support@roiservice.dz",
                    announcementMessage = "مرحباً بكم في تطبيق Roi Service (RS) بالجزائر! خدمات صيانة منزلية فورية عبر كافة الولايات والبلديات."
                )
            )
        }

        // 2. Seed Admin & Sample Customer in Algeria
        var admin = userDao.getAdminUser()
        if (admin == null) {
            val adminId = userDao.insertUser(
                UserEntity(
                    name = "مدير منصة Roi Service",
                    phone = "0555001122",
                    email = "admin@roiservice.dz",
                    role = "ADMIN",
                    defaultAddress = "المركز الرئيسي، ولاية الجزائر - بلدية سيدي امحمد",
                    defaultLatitude = 36.7642,
                    defaultLongitude = 3.0543
                )
            )
            admin = userDao.getUserById(adminId)
        }

        var customer = userDao.getLatestCustomer()
        if (customer == null) {
            val customerId = userDao.insertUser(
                UserEntity(
                    name = "أمين بوعلام",
                    phone = "0555123456",
                    email = "amine.boualem@gmail.com",
                    role = "CUSTOMER",
                    defaultAddress = "ولاية الجزائر، بلدية حيدرة، نهج العربي التبسي",
                    defaultLatitude = 36.7441,
                    defaultLongitude = 3.0428
                )
            )
            customer = userDao.getUserById(customerId)
        }

        // 3. Seed Services with Algerian Dinar (د.ج) prices
        val existingServices = serviceDao.getAllServices().firstOrNull() ?: emptyList()
        if (existingServices.isEmpty()) {
            val defaultServices = listOf(
                ServiceEntity(
                    title = "صيانة وتركيب السباكة",
                    category = "سباكة وصحي",
                    price = 2500.0,
                    durationEstimate = "ساعة - ساعتان",
                    description = "كشف وتصليح تسربات المياه، استبدال المحابس، تسليك المجاري وتركيب الخلاطات بأحدث المعدات.",
                    iconType = "plumbing",
                    isActive = true,
                    rating = 4.9,
                    reviewCount = 124
                ),
                ServiceEntity(
                    title = "أعمال الكهرباء والإنارة",
                    category = "كهرباء",
                    price = 2800.0,
                    durationEstimate = "ساعة ونصف",
                    description = "فحص التوصيلات الكهربائية، إصلاح الشورت، تركيب لوحات التوزيع وتركيب الثريات والسبوت لايت.",
                    iconType = "electrical",
                    isActive = true,
                    rating = 4.8,
                    reviewCount = 98
                ),
                ServiceEntity(
                    title = "صيانة وتنظيف التكييف (سبليت ومركزي)",
                    category = "تكييف وتبريد",
                    price = 3500.0,
                    durationEstimate = "ساعتان",
                    description = "غسيل فلاتر ووحدات التكييف بالضغط، تعبئة غاز الفريون الأصلي وفحص الكفاءة.",
                    iconType = "ac",
                    isActive = true,
                    rating = 5.0,
                    reviewCount = 210
                ),
                ServiceEntity(
                    title = "تنظيف شامل للمنازل والشقق",
                    category = "نظافة منزلية",
                    price = 4500.0,
                    durationEstimate = "3 - 4 ساعات",
                    description = "تنظيف عميق للأرضيات، النوافذ، المطابخ والحمامات وتلميع الرخام بمواد تعقيم آمنة.",
                    iconType = "cleaning",
                    isActive = true,
                    rating = 4.9,
                    reviewCount = 165
                ),
                ServiceEntity(
                    title = "دهانات وتشطيبات وديكور",
                    category = "دهان وديكور",
                    price = 6000.0,
                    durationEstimate = "يوم عمل",
                    description = "معالجة التشققات، طلاء الجدران بأجود أنواع الدهانات، ورق جدران وبديل الخشب والرخام.",
                    iconType = "painting",
                    isActive = true,
                    rating = 4.7,
                    reviewCount = 76
                ),
                ServiceEntity(
                    title = "نقل وتركيب الأثاث المنزلي",
                    category = "نقل وتركيب",
                    price = 8000.0,
                    durationEstimate = "حسب الكمية",
                    description = "فك وتركيب غرف النوم والمطابخ، تغليف احترافي للأثاث الحساس ونقل آمن عبر شاحنات مجهزة.",
                    iconType = "moving",
                    isActive = true,
                    rating = 4.8,
                    reviewCount = 89
                ),
                ServiceEntity(
                    title = "صيانة الحاسوب والشبكات المنزلية",
                    category = "تقنية ومعلومات",
                    price = 2500.0,
                    durationEstimate = "ساعة",
                    description = "تهيئة مودم الألياف البصرية ومقويات الواي فاي، وتمديد كوابل الشبكة، وفحص الكمبيوتر.",
                    iconType = "tech",
                    isActive = true,
                    rating = 4.9,
                    reviewCount = 54
                ),
                ServiceEntity(
                    title = "غسيل وتلميع السيارات المتنقل",
                    category = "سيارات",
                    price = 1500.0,
                    durationEstimate = "45 دقيقة",
                    description = "غسيل بخار خارجي وتنظيف داخلي بالمكنسة، تعطير وتلميع الإطارات أمام بيتك مباشرة.",
                    iconType = "car",
                    isActive = true,
                    rating = 4.8,
                    reviewCount = 143
                )
            )

            defaultServices.forEach { serviceDao.insertService(it) }
        } else {
            // Update prices if they were in old currency (< 500)
            existingServices.forEach { s ->
                if (s.price < 500.0) {
                    val updatedPrice = when {
                        s.title.contains("سباكة") -> 2500.0
                        s.title.contains("كهرباء") -> 2800.0
                        s.title.contains("تكييف") -> 3500.0
                        s.title.contains("تنظيف") -> 4500.0
                        s.title.contains("دهان") -> 6000.0
                        s.title.contains("نقل") -> 8000.0
                        s.title.contains("حاسوب") || s.title.contains("تقنية") -> 2500.0
                        s.title.contains("سيارات") || s.title.contains("غسيل") -> 1500.0
                        else -> s.price * 25.0
                    }
                    serviceDao.updateService(s.copy(price = updatedPrice))
                }
            }
        }

        // 4. Seed initial Algerian orders for testing Admin and Customer views
        val existingOrders = orderDao.getAllOrders().firstOrNull() ?: emptyList()
        if (existingOrders.isEmpty() && customer != null) {
            orderDao.insertOrder(
                OrderEntity(
                    orderNumber = "ORD-2026-101",
                    customerId = customer.id,
                    customerName = customer.name,
                    customerPhone = customer.phone,
                    serviceId = 1,
                    serviceName = "صيانة وتركيب السباكة",
                    servicePrice = 2500.0,
                    status = "NEW",
                    requestedDate = "2026-09-05",
                    requestedTimeSlot = "صباحاً (09:00 ص - 12:00 م)",
                    customerNotes = "يوجد تسريب مياه في محبس المغسلة الرئيسي بالمطبخ، الرجاء الحضور مبكراً.",
                    latitude = 36.7441,
                    longitude = 3.0428,
                    addressText = "ولاية الجزائر، بلدية حيدرة، نهج العربي التبسي، عمارة 4",
                    adminNotes = "تم إرسال إشعار أولي للفني المناوب",
                    assignedWorker = "فارس براهيمي (فني سباكة معتمد)"
                )
            )

            orderDao.insertOrder(
                OrderEntity(
                    orderNumber = "ORD-2026-102",
                    customerId = customer.id,
                    customerName = "ياسمين بن علي",
                    customerPhone = "0770987654",
                    serviceId = 3,
                    serviceName = "صيانة وتنظيف التكييف (سبليت ومركزي)",
                    servicePrice = 3500.0,
                    status = "IN_PROGRESS",
                    requestedDate = "2026-09-06",
                    requestedTimeSlot = "مساءً (04:00 م - 07:00 م)",
                    customerNotes = "المكيف يخرج هواء حار مع صوت في المروحة الخارجية.",
                    latitude = 35.7167,
                    longitude = -0.5500,
                    addressText = "ولاية وهران، بلدية بئر الجير، حي الياسمين 2",
                    adminNotes = "الفني في الطريق للعميل",
                    assignedWorker = "كريم منصوري (فني تكييف)"
                )
            )
        }
    }
}
