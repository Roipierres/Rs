package com.example.data.remote

import android.util.Log
import com.example.data.model.OrderEntity
import com.example.data.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

/**
 * FirestoreManager provides real-time database capabilities for storing
 * service requests and user locations using Google Cloud Firestore.
 */
object FirestoreManager {
    private const val TAG = "FirestoreManager"
    private const val COLLECTION_SERVICE_REQUESTS = "service_requests"
    private const val COLLECTION_USER_LOCATIONS = "user_locations"
    private const val COLLECTION_ORDER_CHATS = "order_chats"
    private const val COLLECTION_CALL_LOGS = "call_logs"

    private val firestore: FirebaseFirestore? by lazy {
        try {
            val db = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            db.firestoreSettings = settings
            Log.d(TAG, "Firebase Firestore successfully initialized with offline persistence")
            db
        } catch (e: Exception) {
            Log.w(TAG, "Firebase Firestore not initialized or unavailable: ${e.message}")
            null
        }
    }

    val isAvailable: Boolean
        get() = firestore != null

    /**
     * Stores or updates a service request in real-time in the `service_requests` Firestore collection.
     */
    fun saveServiceRequest(
        order: OrderEntity,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((Exception) -> Unit)? = null
    ) {
        val db = firestore ?: run {
            Log.w(TAG, "Firestore not available, skipped remote save for order ${order.orderNumber}")
            onSuccess?.invoke()
            return
        }

        val requestData = hashMapOf(
            "orderNumber" to order.orderNumber,
            "customerId" to order.customerId,
            "customerName" to order.customerName,
            "customerPhone" to order.customerPhone,
            "serviceId" to order.serviceId,
            "serviceName" to order.serviceName,
            "servicePrice" to order.servicePrice,
            "status" to order.status,
            "requestedDate" to order.requestedDate,
            "requestedTimeSlot" to order.requestedTimeSlot,
            "customerNotes" to order.customerNotes,
            "latitude" to order.latitude,
            "longitude" to order.longitude,
            "addressText" to order.addressText,
            "adminNotes" to order.adminNotes,
            "assignedWorker" to order.assignedWorker,
            "createdAt" to order.createdAt,
            "lastUpdatedTimestamp" to System.currentTimeMillis()
        )

        db.collection(COLLECTION_SERVICE_REQUESTS)
            .document(order.orderNumber)
            .set(requestData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Service request ${order.orderNumber} successfully saved to Firestore")
                onSuccess?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save service request to Firestore: ${e.message}", e)
                onFailure?.invoke(e)
            }
    }

    /**
     * Updates the status and technician assignment of a service request in real-time.
     */
    fun updateServiceRequestStatus(
        orderNumber: String,
        status: String,
        adminNotes: String? = null,
        assignedWorker: String? = null
    ) {
        val db = firestore ?: return

        val updates = mutableMapOf<String, Any>(
            "status" to status,
            "lastUpdatedTimestamp" to System.currentTimeMillis()
        )
        if (adminNotes != null) updates["adminNotes"] = adminNotes
        if (assignedWorker != null) updates["assignedWorker"] = assignedWorker

        db.collection(COLLECTION_SERVICE_REQUESTS)
            .document(orderNumber)
            .update(updates)
            .addOnSuccessListener {
                Log.d(TAG, "Order status for $orderNumber updated to $status in Firestore")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to update order status in Firestore: ${e.message}", e)
            }
    }

    /**
     * Stores user GPS location and coordinates in real-time in the `user_locations` collection.
     */
    fun saveUserLocation(
        userId: Long,
        userName: String,
        userPhone: String,
        latitude: Double,
        longitude: Double,
        address: String,
        role: String = "CUSTOMER"
    ) {
        val db = firestore ?: return

        val locationData = hashMapOf(
            "userId" to userId,
            "userName" to userName,
            "userPhone" to userPhone,
            "role" to role,
            "latitude" to latitude,
            "longitude" to longitude,
            "address" to address,
            "updatedAt" to Date(),
            "timestamp" to System.currentTimeMillis()
        )

        db.collection(COLLECTION_USER_LOCATIONS)
            .document("user_$userId")
            .set(locationData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "User location for user $userId ($latitude, $longitude) synced to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to sync user location to Firestore: ${e.message}", e)
            }
    }

    /**
     * Real-time listener for incoming service requests from Firestore.
     */
    fun listenToRealtimeServiceRequests(): Flow<List<Map<String, Any>>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration = db.collection(COLLECTION_SERVICE_REQUESTS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed for service requests: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val requests = snapshot.documents.mapNotNull { doc ->
                        doc.data
                    }
                    trySend(requests)
                }
            }

        awaitClose {
            registration.remove()
        }
    }

    /**
     * Real-time listener for live user locations from Firestore.
     */
    fun listenToRealtimeUserLocations(): Flow<List<Map<String, Any>>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration = db.collection(COLLECTION_USER_LOCATIONS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed for user locations: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val locations = snapshot.documents.mapNotNull { doc ->
                        doc.data
                    }
                    trySend(locations)
                }
            }

        awaitClose {
            registration.remove()
        }
    }

    /**
     * Stores a chat message between Customer and Delivery/Technician worker in Firestore.
     */
    fun sendChatMessage(
        orderNumber: String,
        senderId: Long,
        senderName: String,
        senderRole: String,
        messageText: String,
        timestamp: Long = System.currentTimeMillis()
    ) {
        val db = firestore ?: return

        val msgData = hashMapOf(
            "orderNumber" to orderNumber,
            "senderId" to senderId,
            "senderName" to senderName,
            "senderRole" to senderRole,
            "messageText" to messageText,
            "timestamp" to timestamp
        )

        db.collection(COLLECTION_ORDER_CHATS)
            .document(orderNumber)
            .collection("messages")
            .add(msgData)
            .addOnSuccessListener {
                Log.d(TAG, "Chat message sent to Firestore for order $orderNumber")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to send chat message to Firestore: ${e.message}", e)
            }
    }

    /**
     * Listens to real-time chat messages for a specific order.
     */
    fun listenToOrderChat(orderNumber: String): Flow<List<Map<String, Any>>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration = db.collection(COLLECTION_ORDER_CHATS)
            .document(orderNumber)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Chat listen failed for $orderNumber: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { it.data }
                    trySend(messages)
                }
            }

        awaitClose {
            registration.remove()
        }
    }

    /**
     * Stores a phone or WhatsApp call log between customer and delivery worker in Firestore.
     */
    fun saveCallLog(
        orderNumber: String,
        callerName: String,
        receiverName: String,
        receiverPhone: String,
        callType: String,
        durationSeconds: Int = 0,
        status: String = "COMPLETED",
        notes: String = "",
        timestamp: Long = System.currentTimeMillis()
    ) {
        val db = firestore ?: return

        val callData = hashMapOf(
            "orderNumber" to orderNumber,
            "callerName" to callerName,
            "receiverName" to receiverName,
            "receiverPhone" to receiverPhone,
            "callType" to callType,
            "durationSeconds" to durationSeconds,
            "status" to status,
            "notes" to notes,
            "timestamp" to timestamp
        )

        db.collection(COLLECTION_CALL_LOGS)
            .document("${orderNumber}_$timestamp")
            .set(callData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Call log recorded in Firestore for order $orderNumber")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save call log in Firestore: ${e.message}", e)
            }
    }

    /**
     * Real-time listener for call logs of an order or overall.
     */
    fun listenToCallLogs(orderNumber: String? = null): Flow<List<Map<String, Any>>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val query = if (orderNumber != null) {
            db.collection(COLLECTION_CALL_LOGS).whereEqualTo("orderNumber", orderNumber)
        } else {
            db.collection(COLLECTION_CALL_LOGS)
        }

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Call log listen failed: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val calls = snapshot.documents.mapNotNull { it.data }
                trySend(calls)
            }
        }

        awaitClose {
            registration.remove()
        }
    }
}
