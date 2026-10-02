package com.example.waterconverterapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterConverterScreen() {
    // State Variable
    var inputVolume by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Liter") }
    var resultKg by remember { mutableStateOf<Double?>(null) }
    var resultGram by remember { mutableStateOf<Double?>(null) }

    // Formatter agar angka bulat tidak menampilkan desimal (contoh: 1 kg, bukan 1.000 kg)
    val formatter = DecimalFormat("#.##")

    // Fungsi Logika Kalkulasi
    fun calculateMass() {
        val volume = inputVolume.toDoubleOrNull()
        if (volume != null && volume >= 0) {
            if (selectedUnit == "Liter") {
                resultKg = volume
                resultGram = volume * 1000
            } else {
                resultGram = volume
                resultKg = volume / 1000
            }
        } else {
            resultKg = null
            resultGram = null
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💧 ", fontSize = 20.sp)
                        Text(
                            text = "Water Density Converter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- KARTU INPUT UTAMA ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Input Volume Air",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Text Field Input
                    OutlinedTextField(
                        value = inputVolume,
                        onValueChange = {
                            inputVolume = it
                            calculateMass()
                        },
                        label = { Text("Nilai Volume") },
                        placeholder = { Text("Contoh: 1.5") },
                        leadingIcon = { Text("💧", fontSize = 18.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selection Unit (Chip Selector)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pilih Satuan:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = selectedUnit == "Liter",
                                onClick = {
                                    selectedUnit = "Liter"
                                    calculateMass()
                                },
                                label = { Text("Liter (L)") },
                                shape = RoundedCornerShape(12.dp)
                            )

                            FilterChip(
                                selected = selectedUnit == "mL",
                                onClick = {
                                    selectedUnit = "mL"
                                    calculateMass()
                                },
                                label = { Text("MiliLiter (mL)") },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // --- KARTU HASIL KONVERSI ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HASIL MASSA AIR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedVisibility(
                        visible = resultKg != null,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${formatter.format(resultKg)} kg",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "Setara dengan ${formatter.format(resultGram)} Gram",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    if (resultKg == null) {
                        Text(
                            text = "Masukkan nilai volume di atas",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}