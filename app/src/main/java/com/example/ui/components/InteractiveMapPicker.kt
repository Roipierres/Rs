package com.example.ui.components

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KhadamatiBlueDark
import com.example.ui.theme.KhadamatiBluePrimary
import com.example.ui.theme.KhadamatiSecondaryTeal
import com.example.util.AlgeriaLocations
import com.example.util.LocationHelper
import com.example.util.Municipality
import com.example.util.Wilaya
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveMapPicker(
    latitude: Double,
    longitude: Double,
    onLocationChanged: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    customerLabel: String = "موقع العميل في الجزائر",
    heightDp: Int = 270,
    onAddressSuggested: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }
    var isGpsLoading by remember { mutableStateOf(false) }

    // Algeria Wilaya and Baladiya State
    val allWilayas = remember { AlgeriaLocations.wilayas }
    var selectedWilaya by remember {
        mutableStateOf(
            allWilayas.minByOrNull {
                val dLat = it.latitude - latitude
                val dLng = it.longitude - longitude
                dLat * dLat + dLng * dLng
            } ?: allWilayas.first()
        )
    }

    var selectedMunicipality by remember(selectedWilaya) {
        mutableStateOf(
            selectedWilaya.municipalities.minByOrNull {
                val dLat = it.latitude - latitude
                val dLng = it.longitude - longitude
                dLat * dLat + dLng * dLng
            } ?: selectedWilaya.municipalities.first()
        )
    }

    var isWilayaDropdownOpen by remember { mutableStateOf(false) }
    var isMunicipalityDropdownOpen by remember { mutableStateOf(false) }

    // Pulse animation for location pin
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Permission launcher for real GPS
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            coroutineScope.launch {
                isGpsLoading = true
                val loc = LocationHelper.getCurrentLocation(context)
                if (loc != null) {
                    onLocationChanged(loc.latitude, loc.longitude)
                }
                isGpsLoading = false
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Algeria Hierarchical Navigation Bar: Wilayas to Municipalities (Only in picker mode)
            if (!isReadOnly) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(KhadamatiBluePrimary.copy(alpha = 0.04f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🇩🇿", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "خريطة الجزائر: اختر من الولايات إلى البلديات",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = KhadamatiBlueDark
                            )
                        }
                    }

                    // Dropdowns Row: Wilaya & Municipality
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Wilaya Selector Dropdown
                        ExposedDropdownMenuBox(
                            expanded = isWilayaDropdownOpen,
                            onExpandedChange = { isWilayaDropdownOpen = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedWilaya.nameAr,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("الولاية", fontSize = 11.sp) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWilayaDropdownOpen)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isWilayaDropdownOpen,
                                onDismissRequest = { isWilayaDropdownOpen = false }
                            ) {
                                allWilayas.forEach { wilaya ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(wilaya.nameAr, fontSize = 13.sp, fontWeight = if (wilaya.code == selectedWilaya.code) FontWeight.Bold else FontWeight.Normal)
                                                Text(wilaya.nameFr, fontSize = 11.sp, color = Color.Gray)
                                            }
                                        },
                                        onClick = {
                                            selectedWilaya = wilaya
                                            val firstMuni = wilaya.municipalities.first()
                                            selectedMunicipality = firstMuni
                                            isWilayaDropdownOpen = false
                                            // Update Coordinates and Address
                                            onLocationChanged(firstMuni.latitude, firstMuni.longitude)
                                            onAddressSuggested?.invoke("ولاية ${wilaya.nameAr}، بلدية ${firstMuni.nameAr}")
                                        },
                                        leadingIcon = if (wilaya.code == selectedWilaya.code) {
                                            { Icon(Icons.Default.Check, contentDescription = null, tint = KhadamatiBluePrimary, modifier = Modifier.size(16.dp)) }
                                        } else null
                                    )
                                }
                            }
                        }

                        // Municipality Selector Dropdown
                        ExposedDropdownMenuBox(
                            expanded = isMunicipalityDropdownOpen,
                            onExpandedChange = { isMunicipalityDropdownOpen = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedMunicipality.nameAr,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("البلدية", fontSize = 11.sp) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMunicipalityDropdownOpen)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isMunicipalityDropdownOpen,
                                onDismissRequest = { isMunicipalityDropdownOpen = false }
                            ) {
                                selectedWilaya.municipalities.forEach { muni ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(muni.nameAr, fontSize = 13.sp, fontWeight = if (muni.nameAr == selectedMunicipality.nameAr) FontWeight.Bold else FontWeight.Normal)
                                                if (muni.postalCode.isNotBlank()) {
                                                    Text(muni.postalCode, fontSize = 10.sp, color = KhadamatiBluePrimary)
                                                }
                                            }
                                        },
                                        onClick = {
                                            selectedMunicipality = muni
                                            isMunicipalityDropdownOpen = false
                                            // Update Coordinates and Address
                                            onLocationChanged(muni.latitude, muni.longitude)
                                            onAddressSuggested?.invoke("ولاية ${selectedWilaya.nameAr}، بلدية ${muni.nameAr}")
                                        },
                                        leadingIcon = if (muni.nameAr == selectedMunicipality.nameAr) {
                                            { Icon(Icons.Default.Check, contentDescription = null, tint = KhadamatiBluePrimary, modifier = Modifier.size(16.dp)) }
                                        } else null
                                    )
                                }
                            }
                        }
                    }

                    // Quick Shortcut Chips for Major Wilayas
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val keyWilayas = listOf("16", "31", "25", "09", "19", "23", "15", "13", "05", "35")
                        items(allWilayas.filter { it.code in keyWilayas }) { w ->
                            val isSelected = selectedWilaya.code == w.code
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) KhadamatiBluePrimary else Color.White,
                                border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCE2E6)) else null,
                                shadowElevation = if (isSelected) 2.dp else 0.dp,
                                modifier = Modifier.clickable {
                                    selectedWilaya = w
                                    val firstM = w.municipalities.first()
                                    selectedMunicipality = firstM
                                    onLocationChanged(firstM.latitude, firstM.longitude)
                                    onAddressSuggested?.invoke("ولاية ${w.nameAr}، بلدية ${firstM.nameAr}")
                                }
                            ) {
                                Text(
                                    text = w.nameAr.replace(Regex("^\\d+\\s*-\\s*"), ""),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else KhadamatiBlueDark,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Map Canvas Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightDp.dp)
                    .background(Color(0xFFE5ECEE))
                    .pointerInput(isReadOnly) {
                        if (!isReadOnly) {
                            detectTapGestures { tapOffset ->
                                val width = size.width
                                val height = size.height
                                val normalizedDx = (tapOffset.x - width / 2f) / (width * zoomLevel)
                                val normalizedDy = (tapOffset.y - height / 2f) / (height * zoomLevel)
                                val newLat = latitude - (normalizedDy * 0.015)
                                val newLng = longitude + (normalizedDx * 0.015)
                                onLocationChanged(
                                    (newLat * 10000.0).roundToInt() / 10000.0,
                                    (newLng * 10000.0).roundToInt() / 10000.0
                                )
                            }
                        }
                    }
                    .pointerInput(isReadOnly) {
                        if (!isReadOnly) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                zoomLevel = (zoomLevel * zoom).coerceIn(0.6f, 3.2f)
                                panOffsetX += pan.x
                                panOffsetY += pan.y
                                if (pan.x != 0f || pan.y != 0f) {
                                    val latDelta = (pan.y / 20000.0)
                                    val lngDelta = -(pan.x / 20000.0)
                                    onLocationChanged(
                                        ((latitude + latDelta) * 10000.0).roundToInt() / 10000.0,
                                        ((longitude + lngDelta) * 10000.0).roundToInt() / 10000.0
                                    )
                                }
                            }
                        }
                    }
            ) {
                // Vector Map Background Drawing (Mediterranean Sea, Coastline, Highways, City Grid)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f + panOffsetX * 0.2f
                    val cy = h / 2f + panOffsetY * 0.2f

                    // 1. Mediterranean Coastline & Sea (North top)
                    val seaColor = Color(0xFFB8D8EB)
                    val seaPath = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(w, 0f)
                        lineTo(w, cy - 140 * zoomLevel)
                        cubicTo(
                            w * 0.7f, cy - 110 * zoomLevel,
                            w * 0.35f, cy - 170 * zoomLevel,
                            0f, cy - 120 * zoomLevel
                        )
                        close()
                    }
                    drawPath(path = seaPath, color = seaColor)

                    // Coastline wave stroke
                    val coastStroke = Color(0xFF86B9D8)
                    drawPath(path = seaPath, color = coastStroke, style = Stroke(width = 3f))

                    // 2. City Blocks & Green Parks in Algeria
                    val blockColor = Color(0xFFDCE4E8)
                    val blockColor2 = Color(0xFFD2DCE1)
                    val parkColor = Color(0xFFC8E6C9)

                    // Park 1 (e.g. Hamma Botanical Garden / Parc des Sablettes vibe)
                    drawRoundRect(
                        color = parkColor,
                        topLeft = Offset(cx - 240 * zoomLevel, cy - 90 * zoomLevel),
                        size = Size(130 * zoomLevel, 90 * zoomLevel),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                    )

                    drawRect(
                        color = blockColor,
                        topLeft = Offset(cx - 300 * zoomLevel, cy + 20 * zoomLevel),
                        size = Size(160 * zoomLevel, 120 * zoomLevel)
                    )
                    drawRect(
                        color = blockColor2,
                        topLeft = Offset(cx - 90 * zoomLevel, cy - 110 * zoomLevel),
                        size = Size(170 * zoomLevel, 100 * zoomLevel)
                    )
                    drawRect(
                        color = blockColor,
                        topLeft = Offset(cx + 110 * zoomLevel, cy - 80 * zoomLevel),
                        size = Size(190 * zoomLevel, 110 * zoomLevel)
                    )
                    drawRect(
                        color = blockColor2,
                        topLeft = Offset(cx - 100 * zoomLevel, cy + 30 * zoomLevel),
                        size = Size(200 * zoomLevel, 130 * zoomLevel)
                    )
                    drawRect(
                        color = blockColor,
                        topLeft = Offset(cx + 130 * zoomLevel, cy + 50 * zoomLevel),
                        size = Size(180 * zoomLevel, 120 * zoomLevel)
                    )

                    // 3. Secondary City Avenues (White Streets)
                    val secondaryRoad = Color(0xFFFFFFFF)
                    val strokeSecondary = 13f * zoomLevel
                    drawLine(
                        color = secondaryRoad,
                        start = Offset(0f, cy - 10 * zoomLevel),
                        end = Offset(w, cy - 10 * zoomLevel),
                        strokeWidth = strokeSecondary
                    )
                    drawLine(
                        color = secondaryRoad,
                        start = Offset(0f, cy + 80 * zoomLevel),
                        end = Offset(w, cy + 80 * zoomLevel),
                        strokeWidth = strokeSecondary
                    )
                    drawLine(
                        color = secondaryRoad,
                        start = Offset(cx - 110 * zoomLevel, 0f),
                        end = Offset(cx - 110 * zoomLevel, h),
                        strokeWidth = strokeSecondary
                    )
                    drawLine(
                        color = secondaryRoad,
                        start = Offset(cx + 100 * zoomLevel, 0f),
                        end = Offset(cx + 100 * zoomLevel, h),
                        strokeWidth = strokeSecondary
                    )

                    // 4. Primary Algerian Highway (East-West Highway A1 / Route Nationale)
                    val highwayCasing = Color(0xFFCFD8DC)
                    val highwayColor = Color(0xFFFFC107) // Amber/Gold Highway
                    val strokeHighway = 22f * zoomLevel

                    val highwayPath = Path().apply {
                        moveTo(0f, cy + 40 * zoomLevel)
                        cubicTo(
                            w * 0.25f, cy + 10 * zoomLevel,
                            w * 0.65f, cy + 60 * zoomLevel,
                            w, cy + 25 * zoomLevel
                        )
                    }
                    drawPath(path = highwayPath, color = highwayCasing, style = Stroke(width = strokeHighway + 4f))
                    drawPath(path = highwayPath, color = highwayColor, style = Stroke(width = strokeHighway))

                    // Coastal Corniche Boulevard
                    val cornicheColor = Color(0xFFFFE082)
                    val cornichePath = Path().apply {
                        moveTo(0f, cy - 115 * zoomLevel)
                        cubicTo(
                            w * 0.35f, cy - 165 * zoomLevel,
                            w * 0.7f, cy - 105 * zoomLevel,
                            w, cy - 135 * zoomLevel
                        )
                    }
                    drawPath(path = cornichePath, color = Color.White, style = Stroke(width = 14f * zoomLevel))
                    drawPath(path = cornichePath, color = cornicheColor, style = Stroke(width = 10f * zoomLevel))

                    // 5. Center Radar Pulse for GPS Location
                    drawCircle(
                        color = KhadamatiBluePrimary.copy(alpha = pulseAlpha),
                        radius = 48f * pulseScale,
                        center = Offset(w / 2f, h / 2f)
                    )
                }

                // Center Pin Icon with Title Badge
                Box(
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 22.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = KhadamatiBlueDark,
                            shadowElevation = 6.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "📍", fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isReadOnly) customerLabel else "${selectedWilaya.nameAr.replace(Regex("^\\d+\\s*-\\s*"), "")} - ${selectedMunicipality.nameAr}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Pin",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(8.dp, shape = CircleShape)
                        )
                    }
                }

                // Top Left Coordinates & Algeria Badge Pill
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Text(text = "🇩🇿", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${String.format("%.4f", latitude)}° N, ${String.format("%.4f", longitude)}° E",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = KhadamatiBlueDark
                        )
                    }
                }

                // Zoom controls
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { zoomLevel = (zoomLevel * 1.25f).coerceAtMost(3.2f) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { zoomLevel = (zoomLevel / 1.25f).coerceAtLeast(0.6f) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
                    }
                }

                // Hint overlay for interactive mode
                if (!isReadOnly) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Text(
                            text = "انقر على الخريطة أو اسحب لضبط مكان بيتك أو شارعك بدقة",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Bottom Actions & Google Maps Link
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isReadOnly) {
                        // Current Location GPS Button
                        FilledTonalButton(
                            onClick = {
                                if (LocationHelper.hasLocationPermission(context)) {
                                    coroutineScope.launch {
                                        isGpsLoading = true
                                        val loc = LocationHelper.getCurrentLocation(context)
                                        if (loc != null) {
                                            onLocationChanged(loc.latitude, loc.longitude)
                                        }
                                        isGpsLoading = false
                                    }
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = KhadamatiBluePrimary.copy(alpha = 0.12f),
                                contentColor = KhadamatiBlueDark
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.GpsFixed,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isGpsLoading) "تحديد GPS..." else "موقعي الحالي بالجزائر",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Open in Google Maps Action Button
                    Button(
                        onClick = {
                            val mapLabel = if (isReadOnly) customerLabel else "ولاية ${selectedWilaya.nameAr} - بلدية ${selectedMunicipality.nameAr}"
                            LocationHelper.openInGoogleMaps(
                                context = context,
                                latitude = latitude,
                                longitude = longitude,
                                label = mapLabel
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isReadOnly) Color(0xFF2E7D32) else KhadamatiBluePrimary
                        ),
                        modifier = if (isReadOnly) Modifier.fillMaxWidth() else Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isReadOnly) "معاينة الموقع في خرائط Google" else "عرض في خرائط Google",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
