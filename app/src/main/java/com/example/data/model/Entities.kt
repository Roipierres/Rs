package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val role: String = "CUSTOMER", // "CUSTOMER" or "ADMIN"
    val defaultAddress: String = "",
    val defaultLatitude: Double = 24.7136,
    val defaultLongitude: Double = 46.6753,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val price: Double,
    val durationEstimate: String,
    val description: String,
    val iconType: String = "build", // plumbing, electrical, ac, cleaning, painting, moving, tech, car
    val isActive: Boolean = true,
    val rating: Double = 4.9,
    val reviewCount: Int = 38,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerId: Long,
    val customerName: String,
    val customerPhone: String,
    val serviceId: Long,
    val serviceName: String,
    val servicePrice: Double,
    val status: String = "NEW", // NEW, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
    val requestedDate: String,
    val requestedTimeSlot: String,
    val customerNotes: String = "",
    val latitude: Double,
    val longitude: Double,
    val addressText: String,
    val adminNotes: String = "",
    val assignedWorker: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val appName: String = "Roi Service",
    val supportPhone: String = "+213555123456",
    val supportWhatsApp: String = "+213555123456",
    val supportEmail: String = "support@roiservice.dz",
    val announcementMessage: String = "مرحباً بكم في تطبيق Roi Service (RS) بالجزائر! احجز أفضل الفنيين المعتمدين عبر كافة الولايات والبلديات بضمان الجودة.",
    val currency: String = "د.ج",
    val workingHours: String = "8:00 ص - 10:00 م",
    val inspectionFee: Double = 1000.0,
    val isEmergencyActive: Boolean = true
)
