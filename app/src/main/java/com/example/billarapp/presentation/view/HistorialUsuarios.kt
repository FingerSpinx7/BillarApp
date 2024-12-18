package com.example.billarapp.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.domain.ProductoConsumido
import com.example.billarapp.domain.getDetallesHistorial
import com.example.billarapp.domain.getHistorialFromDatabase
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class HistorialUsuarios : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ) {
                    PantallaHistorialUsuarios()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialUsuarios() {
    // Estados de la UI
    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var termBusqueda by remember { mutableStateOf("") }
    var mostrarDetalles by remember { mutableStateOf<Int?>(null) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    val historial = getHistorialFromDatabase(
        fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )

    val historialFiltrado = remember(historial, termBusqueda) {
        if (termBusqueda.isBlank()) historial
        else historial.filter { it.cliente.contains(termBusqueda, ignoreCase = true) }
    }

    mostrarDetalles?.let { idCuenta ->
        MostrarDetallesDialog(
            idCuenta = idCuenta,
            onDismiss = { mostrarDetalles = null }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D2B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Historial Usuarios",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff7FD238),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Sección de consulta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D2B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Consultar Historial",
                        color = Color(0xff7FD238),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Campo Fecha
                        OutlinedTextField(
                            value = fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Fecha", color = Color.White) },
                            leadingIcon = {
                                IconButton(onClick = { mostrarDatePicker = true }) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = "Fecha", tint = Color(0xff99df5b))
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xff99df5b),
                                unfocusedBorderColor = Color(0xFFB2B6C1),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        // Campo Búsqueda
                        OutlinedTextField(
                            value = termBusqueda,
                            onValueChange = { termBusqueda = it },
                            label = { Text("Buscar cliente", color = Color.White) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xff99df5b))
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xff99df5b),
                                unfocusedBorderColor = Color(0xFFB2B6C1),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }

            // DatePicker
            if (mostrarDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                fechaSeleccionada = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                            mostrarDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Tabla de datos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D2B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("CLIENTE", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
                        Text("FECHA", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("TOTAL", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }
                    LazyColumn {
                        items(historialFiltrado) { registro ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(registro.cliente, color = Color.White, modifier = Modifier.weight(1.5f))
                                Text(registro.fecha_inicio, color = Color.White, modifier = Modifier.weight(1f))
                                Text("$${registro.total}", color = Color.White, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MostrarDetallesDialog(
    idCuenta: Int,
    onDismiss: () -> Unit
) {
    var productosConsumidos by remember { mutableStateOf<List<ProductoConsumido>>(emptyList()) }
    var cargandoDetalles by remember { mutableStateOf(true) }

    LaunchedEffect(idCuenta) {
        cargandoDetalles = true
        productosConsumidos = getDetallesHistorial(idCuenta)
        cargandoDetalles = false
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                "Detalles de Consumo",
                color = Color(0xff7FD238),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                if (cargandoDetalles) {
                    CircularProgressIndicator(color = Color(0xff99df5b))
                } else if (productosConsumidos.isEmpty()) {
                    Text(
                        "No hay productos consumidos para mostrar.",
                        color = Color.White
                    )
                } else {
                    LazyColumn {
                        items(productosConsumidos) { producto ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Producto: ${producto.id_producto}",
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "Cantidad: ${producto.cantidad}",
                                    color = Color.White,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cerrar")
            }
        },
        containerColor = Color(0xFF1A1D2B),
        textContentColor = Color.White
    )
}

