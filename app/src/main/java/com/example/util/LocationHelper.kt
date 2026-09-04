package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

data class Coordinates(
    val latitude: Double,
    val longitude: Double,
    val title: String = ""
)

object LocationHelper {

    // Default reference coordinates (Algeria - Algiers Hydra)
    val DEFAULT_COORDINATES = Coordinates(36.7441, 3.0428, "ولاية الجزائر، بلدية حيدرة")

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Coordinates? {
        if (!hasLocationPermission(context)) return null

        return suspendCancellableCoroutine { continuation ->
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            Coordinates(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                title = "موقعي الحالي عبر GPS"
                            )
                        )
                    } else {
                        // Fallback to last known location
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                            if (lastLoc != null) {
                                continuation.resume(
                                    Coordinates(
                                        latitude = lastLoc.latitude,
                                        longitude = lastLoc.longitude,
                                        title = "آخر موقع معروف"
                                    )
                                )
                            } else {
                                continuation.resume(null)
                            }
                        }.addOnFailureListener {
                            continuation.resume(null)
                        }
                    }
                }.addOnFailureListener {
                    continuation.resume(null)
                }
            } catch (e: Exception) {
                continuation.resume(null)
            }
        }
    }

    fun openInGoogleMaps(
        context: Context,
        latitude: Double,
        longitude: Double,
        label: String = "موقع العميل"
    ) {
        val encodedLabel = Uri.encode(label)
        val geoUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($encodedLabel)")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        try {
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                // Fallback to generic maps URL
                val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, browserUri)
                context.startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
            val fallbackIntent = Intent(Intent.ACTION_VIEW, browserUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(fallbackIntent)
            } catch (ex: Exception) {
                Toast.makeText(context, "تعذر فتح الخرائط: $latitude, $longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun dialPhone(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر إجراء الاتصال", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phone: String, message: String = "") {
        try {
            val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
            val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تطبيق واتساب غير متوفر", Toast.LENGTH_SHORT).show()
        }
    }
}
