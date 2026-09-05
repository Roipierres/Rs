package com.example.util

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 5. Gemini AI Home Assistant Service
 * Provides intelligent instant troubleshooting, safety advice, and automated service recommendations.
 * Uses the modern supported model 'gemini-3.5-flash'.
 */
object AiHomeAssistantService {

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    suspend fun diagnoseIssue(
        userProblem: String,
        category: String = ""
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // If no API key configured, provide high quality intelligent fallback offline diagnosis
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getExpertRuleBasedDiagnosis(userProblem, category)
        }

        try {
            val systemPrompt = """
                أنت 'المساعد الذكي لمنصة Roi Service (RS)' في الجزائر المتخصص في الصيانة وخدمات المنازل.
                مهمتك:
                1. تقديم إرشادات أمان فورية وسريعة لحماية المستخدم وأفراد العائلة (مثل إغلاق المحبس أو قاطع الكهرباء).
                2. تشخيص المشكلة بدقة واقتراح سبب العطل المحتمل.
                3. التوصية بالخدمة المناسبة من خدمات Roi Service (سباكة، كهرباء، تكييف، دهانات، صيانة أجهزة).
                4. الرد بلغة عربية سلسة مع لمسة تشجيعية، واستخدام نقاط واضحة ومختصرة.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemPrompt\n\nوصف المشكلة من العميل:\n$userProblem")
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResp = JSONObject(responseBody)
                val candidates = jsonResp.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "")
                        if (text.isNotBlank()) {
                            return@withContext text
                        }
                    }
                }
            }

            // Fallback if API returned empty
            getExpertRuleBasedDiagnosis(userProblem, category)
        } catch (e: Exception) {
            getExpertRuleBasedDiagnosis(userProblem, category)
        }
    }

    private fun getExpertRuleBasedDiagnosis(problem: String, category: String): String {
        val lower = problem.lowercase()
        return when {
            lower.contains("تسرب") || lower.contains("ماء") || lower.contains("حنفية") || lower.contains("مرحاض") || lower.contains("أنبوب") -> {
                """
                🔧 **تشخيص ذكي من Roi Service (سباكة ومياه):**
                
                ⚠️ **إجراء أمان فوري وضروري:**
                1. أغلق المحبس الرئيسي للماء فوراً لتفادي تضرر الأرضيات والجدران.
                2. تجنب تشغيل الأجهزة الكهربائية القريبة من مكان التسرب.
                
                🔍 **السبب المحتمل:**
                غالباً تآكل في حشوة (Joint) الحنفية أو تشقق في أنبوب التغذية المرن.
                
                ✅ **الخدمة الموصى بها في Roi Service:**
                خدمة **"إصلاح تسربات المياه والسباكة"** - متوفر فني مجهز بقطع غيار أصلية جاهز للانطلاق إليك.
                """.trimIndent()
            }

            lower.contains("كهرباء") || lower.contains("شرار") || lower.contains("قاطع") || lower.contains("مقبس") || lower.contains("disjoncteur") -> {
                """
                ⚡ **تشخيص ذكي من Roi Service (كهرباء وطاقة):**
                
                ⚠️ **إجراء أمان فوري حاسم:**
                1. لا تلمس المفتاح أو السلك بيدك العارية أو بأيدٍ مبتلة نهائياً.
                2. افصل القاطع الفرعي (Disjoncteur) المخصص للغرفة من اللوحة الرئيسية.
                
                🔍 **السبب المحتمل:**
                حمل زائد (Court-circuit) أو تلامس أسلاك داخل العلبة الجدارية.
                
                ✅ **الخدمة الموصى بها في Roi Service:**
                خدمة **"صيانة وتركيب التمديدات الكهربائية"** مع فحص سلامة الشبكة بجهاز القياس.
                """.trimIndent()
            }

            lower.contains("مكيف") || lower.contains("تبريد") || lower.contains("climatiseur") || lower.contains("غاز") -> {
                """
                ❄️ **تشخيص ذكي من Roi Service (تكييف وتبريد):**
                
                ⚠️ **إجراء أمان واقتصادي:**
                1. أوقف تشغيل المكيف مؤقتاً لتجنب احتراق الضاغط (Compresseur).
                2. تأكد من إزالة العوالق الظاهرة من الفلاتر الداخلية.
                
                🔍 **السبب المحتمل:**
                نقص في شحنة غاز الفريون (R410A / R32) أو انسداد فلاتر الهواء الداخلية.
                
                ✅ **الخدمة الموصى بها في Roi Service:**
                خدمة **"تنظيف وشحن غاز المكيفات"** لضمان برودة مثالية مع كشف التسريب.
                """.trimIndent()
            }

            else -> {
                """
                ✨ **تشخيص مستشار Roi Service الذكي:**
                
                شكراً لتوضيح المشكلة.
                
                💡 **نصائح أولية:**
                • تأكد من سلامة المحيط وعزل مصدر العطل إن وُجد.
                • التقط صوراً واضحة للعطل لتسليمها للفني فور وصوله.
                
                🚀 **خطوتك التالية:**
                يمكنك تأكيد طلب الخدمة مباشرة عبر الخريطة، وسيتواصل معك فني Roi Service المتخصص خلال دقائق مع ضمان فحص معتمد.
                """.trimIndent()
            }
        }
    }
}
