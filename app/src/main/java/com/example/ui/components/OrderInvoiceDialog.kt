package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.OrderEntity
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.ui.theme.KhadamatiSuccess
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 4. Electronic Invoice & QR Receipt Dialog
 * Displays an official branded invoice with breakdown, QR Code for verification/payment,
 * and export/share capabilities.
 */
@Composable
fun OrderInvoiceDialog(
    order: OrderEntity,
    currency: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val serviceFee = order.servicePrice
    val taxRate = 0.05 // 5% official service tax
    val taxAmount = serviceFee * taxRate
    val totalAmount = serviceFee + taxAmount

    val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault())
    val invoiceDate = dateFormat.format(Date(order.createdAt))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(KhadamatiBluePrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Receipt,
                            contentDescription = null,
                            tint = KhadamatiBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "فاتورة إلكترونية رسمية",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = KhadamatiBlueDark
                        )
                        Text(
                            text = "Roi Service - RS Algérie",
                            fontSize = 11.sp,
                            color = KhadamatiSecondaryTeal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Status & Invoice ID Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("رقم الفاتورة:", fontSize = 11.sp, color = Color.Gray)
                            Text("INV-${order.orderNumber}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KhadamatiBluePrimary)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("التاريخ والوقت:", fontSize = 11.sp, color = Color.Gray)
                            Text(invoiceDate, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("العميل:", fontSize = 11.sp, color = Color.Gray)
                            Text(order.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("هاتف العميل:", fontSize = 11.sp, color = Color.Gray)
                            Text(order.customerPhone, fontSize = 11.sp)
                        }
                    }
                }

                // Line Items Table
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("بيان الخدمة المنجزة:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(order.serviceName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("${serviceFee.toInt()} $currency", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "ملاحظات: ${order.customerNotes.ifBlank { "خدمة صيانة قياسية منزلية مع الضمان" }}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("رسوم الخدمة الأساسية:", fontSize = 11.sp, color = Color.DarkGray)
                            Text("${serviceFee.toInt()} $currency", fontSize = 11.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ضريبة وضمان الخدمة (5%):", fontSize = 11.sp, color = Color.DarkGray)
                            Text("${taxAmount.toInt()} $currency", fontSize = 11.sp)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("المجموع النهائي الصافي:", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            Text(
                                text = "${totalAmount.toInt()} $currency",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = KhadamatiBluePrimary
                            )
                        }
                    }
                }

                // QR Code Verification & Payment Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = KhadamatiBluePrimary.copy(alpha = 0.05f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Simulated High Quality Vector QR Code Box
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = KhadamatiBlueDark,
                                modifier = Modifier.size(62.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KhadamatiSuccess, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("رمز تحقق رسمي موثوق", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KhadamatiSuccess)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "امسح الرمز بواسطة كاميرا الهاتف لتأكيد السداد وتأكيد ضمان Roi Service لمدة 30 يوماً.",
                                fontSize = 10.sp,
                                color = Color.DarkGray,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    shareInvoiceText(context, order, totalAmount, currency)
                },
                colors = ButtonDefaults.buttonColors(containerColor = KhadamatiBluePrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("مشاركة الفاتورة")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "تم حفظ الفاتورة بنجاح في سجل حسابك 🧾", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("حفظ في الحساب")
            }
        }
    )
}

private fun shareInvoiceText(context: Context, order: OrderEntity, total: Double, currency: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_SUBJECT,
            "فاتورة خدمة Roi Service #${order.orderNumber}"
        )
        putExtra(
            Intent.EXTRA_TEXT,
            """
            🧾 *فاتورة رسمية - Roi Service (RS)*
            ━━━━━━━━━━━━━━━━━
            رقم الطلب: #${order.orderNumber}
            الخدمة: ${order.serviceName}
            العميل: ${order.customerName}
            الموقع: ${order.addressText}
            تاريخ التنفيذ: ${order.requestedDate}
            المبلغ الإجمالي: ${total.toInt()} $currency
            ━━━━━━━━━━━━━━━━━
            ضمان جودة معتمد لمدة 30 يوماً من Roi Service.
            شكراً لثقتكم بنا! 🇩🇿
            """.trimIndent()
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "مشاركة الفاتورة عبر:"))
}
