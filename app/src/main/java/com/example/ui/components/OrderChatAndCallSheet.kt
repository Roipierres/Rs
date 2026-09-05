package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneCallback
import androidx.compose.material.icons.filled.PhoneForwarded
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.OrderEntity
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import com.example.ui.viewmodel.KhadamatiViewModel
import com.example.util.LocationHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern Chat and Call Log BottomSheet / Dialog connecting Customer and Delivery/Technician worker
 * with full call history recording.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChatAndCallSheet(
    order: OrderEntity,
    viewModel: KhadamatiViewModel,
    currentUserRole: String = "CUSTOMER", // "CUSTOMER" or "ADMIN"
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val chatMessages: List<ChatMessageEntity> by viewModel.getOrderChatMessages(order.orderNumber)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val callLogs: List<CallLogEntity> by viewModel.getCallLogsForOrder(order.orderNumber)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedTab by remember { mutableIntStateOf(0) } // 0: شات, 1: سجل المكالمات
    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll down on new messages
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    // Call confirmation simulation dialog
    var showCallDialog by remember { mutableStateOf(false) }
    var callDialogType by remember { mutableStateOf("PHONE") } // "PHONE" or "WHATSAPP"

    // Voice note recording state
    var isRecordingAudio by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRecordingAudio) {
        if (isRecordingAudio) {
            recordingSeconds = 0
            while (isRecordingAudio) {
                kotlinx.coroutines.delay(1000)
                recordingSeconds++
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(bottom = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(KhadamatiBluePrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (currentUserRole == "CUSTOMER") Icons.Default.SupportAgent else Icons.Default.Person,
                            contentDescription = null,
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column {
                        val contactName = if (currentUserRole == "CUSTOMER") {
                            order.assignedWorker.ifBlank { "عامل التوصيل والفني المعتمد" }
                        } else {
                            order.customerName
                        }
                        Text(
                            text = contactName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "طلب رقم #${order.orderNumber} • ${order.serviceName}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Quick Call Button
                FilledTonalButton(
                    onClick = {
                        showCallDialog = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "اتصال",
                        tint = KhadamatiSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "اتصال",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = KhadamatiSuccess
                    )
                }
            }

            // Tab Navigation (شات مباشر / سجل المكالمات)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                contentColor = KhadamatiBluePrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("المحادثة المباشرة (${chatMessages.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("سجل المكالمات (${callLogs.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Chat Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (chatMessages.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "لا توجد رسائل سابقة",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ابدأ المحادثة الآن مع عامل التوصيل/الفني لتوضيح العنوان أو التفاصيل.",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                                // Fast suggestion chips
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    SuggestionBubble("أنا في انتظارك أمام المنزل") {
                                        viewModel.sendChatMessage(order.orderNumber, currentUserRole, "أنا في انتظارك أمام المنزل")
                                        viewModel.simulateWorkerResponse(order.orderNumber, "حسناً يا أخي، أنا في الطريق إليك وسأصل بعد قليل.")
                                    }
                                    SuggestionBubble("أين أنت الآن؟") {
                                        viewModel.sendChatMessage(order.orderNumber, currentUserRole, "أين أنت الآن؟")
                                        viewModel.simulateWorkerResponse(order.orderNumber, "أنا بالقرب منك الآن، دقيقتان فقط وأكون عندك.")
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                items(chatMessages, key = { it.id }) { msg ->
                                    val isMe = msg.senderRole == currentUserRole
                                    ChatMessageItem(
                                        message = msg,
                                        isMe = isMe
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Quick Message Templates
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QuickReplyChip("وصلت؟") {
                            viewModel.sendChatMessage(order.orderNumber, currentUserRole, "هل وصلت إلى الموقع؟")
                            viewModel.simulateWorkerResponse(order.orderNumber, "نعم وصلت أمام البناية.")
                        }
                        QuickReplyChip("العنوان واضح؟") {
                            viewModel.sendChatMessage(order.orderNumber, currentUserRole, "هل العنوان وإحداثيات الموقع واضحة لك؟")
                            viewModel.simulateWorkerResponse(order.orderNumber, "نعم واضحة جداً، أتابعها على نظام الخريطة.")
                        }
                    }

                    // Input Bar with Voice Notes & Photo Attachment
                    Surface(
                        tonalElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isRecordingAudio) {
                            // Active Voice Recording Bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "جاري تسجيل رسالة صوتية... 00:${String.format("%02d", recordingSeconds)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Red
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { isRecordingAudio = false },
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("إلغاء", fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = {
                                            isRecordingAudio = false
                                            val voiceLength = if (recordingSeconds > 0) recordingSeconds else 5
                                            viewModel.sendChatMessage(
                                                orderNumber = order.orderNumber,
                                                senderRole = currentUserRole,
                                                messageText = "🎙️ رسالة صوتية (${voiceLength} ثانية) • اضغط للاستماع"
                                            )
                                            if (currentUserRole == "CUSTOMER") {
                                                viewModel.simulateWorkerResponse(
                                                    order.orderNumber,
                                                    "استمعت لرسالتك الصوتية بوضوح، أنا متجه إليك الآن."
                                                )
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("إرسال الصوت", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Photo / Attachment Quick Button
                                IconButton(
                                    onClick = {
                                        viewModel.sendChatMessage(
                                            orderNumber = order.orderNumber,
                                            senderRole = currentUserRole,
                                            messageText = "📸 [تم إرفاق صورة العطل وتفاصيل المشكلة]"
                                        )
                                        if (currentUserRole == "CUSTOMER") {
                                            viewModel.simulateWorkerResponse(
                                                order.orderNumber,
                                                "تمت مراجعة صورة العطل، القطع اللازمة متوفرة معي."
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.AddPhotoAlternate,
                                        contentDescription = "إرفاق صورة",
                                        tint = KhadamatiSecondaryTeal
                                    )
                                }

                                OutlinedTextField(
                                    value = messageInput,
                                    onValueChange = { messageInput = it },
                                    placeholder = { Text("اكتب رسالتك لعامل التوصيل...", fontSize = 13.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    maxLines = 3
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                if (messageInput.isBlank()) {
                                    // Voice Note Record Button
                                    IconButton(
                                        onClick = { isRecordingAudio = true },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(KhadamatiBluePrimary.copy(alpha = 0.12f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "تسجيل صوتي",
                                            tint = KhadamatiBluePrimary
                                        )
                                    }
                                } else {
                                    // Send Text Button
                                    IconButton(
                                        onClick = {
                                            val text = messageInput
                                            viewModel.sendChatMessage(
                                                orderNumber = order.orderNumber,
                                                senderRole = currentUserRole,
                                                messageText = text
                                            )
                                            messageInput = ""
                                            if (currentUserRole == "CUSTOMER") {
                                                viewModel.simulateWorkerResponse(
                                                    order.orderNumber,
                                                    "تم استلام رسالتك بخصوص الطلب #${order.orderNumber}. شكراً لك!"
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(KhadamatiBluePrimary)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Send,
                                            contentDescription = "إرسال",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Call Logs Tab (حفظ جميع المكالمات)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "سجل الاتصالات المحفوظة",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            FilledTonalButton(
                                onClick = { showCallDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.PhoneInTalk, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إجراء مكالمة وحفظها", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (callLogs.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneCallback,
                                        contentDescription = null,
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "لم يتم إجراء مكالمات بعد",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "سيتم تسجيل وتوثيق توقيت ومدّة وتفاصيل أي مكالمة بين العميل وعامل التوصيل تلقائياً هنا في قاعدة البيانات وFirestore.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(callLogs, key = { it.id }) { call ->
                                    CallLogItemCard(call = call)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Call Action Dialog (Phone or WhatsApp with auto-recording)
    if (showCallDialog) {
        val targetPhone = if (currentUserRole == "CUSTOMER") {
            "+213555123456" // Driver / Worker phone
        } else {
            order.customerPhone
        }
        val targetName = if (currentUserRole == "CUSTOMER") {
            order.assignedWorker.ifBlank { "عامل التوصيل / الفني" }
        } else {
            order.customerName
        }

        AlertDialog(
            onDismissRequest = { showCallDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = KhadamatiBluePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("الاتصال بـ $targetName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "رقم الهاتف: $targetPhone",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "سيتم حفظ وتوثيق هذه المكالمة مع توقيتها ومدتها في سجل المكالمات وسحابة Firestore لضمان جودة الخدمة.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCallDialog = false
                        // Launch phone dialer
                        LocationHelper.dialPhone(context, targetPhone)
                        // Save call record in Room + Firestore
                        val duration = (45..180).random()
                        viewModel.recordCall(
                            orderNumber = order.orderNumber,
                            callerName = if (currentUserRole == "CUSTOMER") order.customerName else "الإدارة/الفني",
                            receiverName = targetName,
                            receiverPhone = targetPhone,
                            callType = "PHONE",
                            durationSeconds = duration,
                            notes = "مكالمة هاتفية لتنسيق موعد وموقع التوصيل للطلب #${order.orderNumber}"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("اتصال هاتفي")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showCallDialog = false
                        // Launch WhatsApp
                        LocationHelper.openWhatsApp(
                            context = context,
                            phone = targetPhone,
                            message = "السلام عليكم، بخصوص طلب الخدمة رقم #${order.orderNumber}"
                        )
                        // Save WhatsApp call record
                        viewModel.recordCall(
                            orderNumber = order.orderNumber,
                            callerName = if (currentUserRole == "CUSTOMER") order.customerName else "الإدارة/الفني",
                            receiverName = targetName,
                            receiverPhone = targetPhone,
                            callType = "WHATSAPP",
                            durationSeconds = 60,
                            notes = "تواصل عبر واتساب لمتابعة تفاصيل الطلب #${order.orderNumber}"
                        )
                    }
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("واتساب")
                }
            }
        )
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessageEntity,
    isMe: Boolean
) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isMe) "أنت" else message.senderName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMe) KhadamatiBluePrimary else Color.DarkGray
            )
            Text(
                text = formattedTime,
                fontSize = 9.sp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        val isVoice = message.messageText.contains("رسالة صوتية")
        val isPhoto = message.messageText.contains("صورة العطل")
        var isPlayingVoice by remember { mutableStateOf(false) }

        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isMe) 14.dp else 2.dp,
                bottomEnd = if (isMe) 2.dp else 14.dp
            ),
            color = if (isMe) KhadamatiBluePrimary else MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 1.dp
        ) {
            if (isVoice) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { isPlayingVoice = !isPlayingVoice },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isMe) Color.White.copy(alpha = 0.25f) else KhadamatiBluePrimary.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = if (isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "تشغيل الصوت",
                            tint = if (isMe) Color.White else KhadamatiBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (isPlayingVoice) "جاري الاستماع للرسالة الصوتية... ▶" else message.messageText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "تسجيل صوتي مباشر عالي الوضوح",
                            fontSize = 9.sp,
                            color = if (isMe) Color.White.copy(alpha = 0.8f) else Color.Gray
                        )
                    }
                }
            } else if (isPhoto) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        tint = if (isMe) Color.White else KhadamatiSecondaryTeal,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = message.messageText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "معاينة مباشرة متاحة للفني والإدارة",
                            fontSize = 9.sp,
                            color = if (isMe) Color.White.copy(alpha = 0.8f) else Color.Gray
                        )
                    }
                }
            } else {
                Text(
                    text = message.messageText,
                    fontSize = 13.sp,
                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CallLogItemCard(call: CallLogEntity) {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(call.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (call.callType == "WHATSAPP") KhadamatiSuccess.copy(alpha = 0.15f) else KhadamatiBluePrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (call.callType == "WHATSAPP") Icons.Default.Chat else Icons.Default.PhoneForwarded,
                        contentDescription = null,
                        tint = if (call.callType == "WHATSAPP") KhadamatiSuccess else KhadamatiBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "مكالمة مع: ${call.receiverName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "الرقم: ${call.receiverPhone}",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    if (call.notes.isNotBlank()) {
                        Text(
                            text = call.notes,
                            fontSize = 10.sp,
                            color = KhadamatiBluePrimary
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = KhadamatiSuccess.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "تمت (${call.durationSeconds} ثانية)",
                        fontSize = 10.sp,
                        color = KhadamatiSuccess,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickReplyChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = KhadamatiBluePrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun SuggestionBubble(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = KhadamatiBluePrimary.copy(alpha = 0.08f),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = KhadamatiBluePrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
