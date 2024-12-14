package com.example.billarapp.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.domain.ModeloProveedor
import com.example.billarapp.domain.agregarProveedorDB
import com.example.billarapp.domain.fetchProveedoresFromDatabase
import kotlinx.coroutines.launch

class AgregarProveedor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ) {
                    PantallaAgregarProveedor()
                }
            }
        }
    }
}

@Composable
fun PantallaAgregarProveedor() {
    var nombreProveedor by remember { mutableStateOf("") }
    var numeroTelefono by remember { mutableStateOf("") }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    val proveedores = remember { mutableStateListOf<ModeloProveedor>() }
    val coroutineScope = rememberCoroutineScope() // Correctamente inicializado

    // Carga inicial de proveedores al iniciar la pantalla
    LaunchedEffect(Unit) {
        try {
            Log.d("ProveedorDebug", "Iniciando carga inicial de proveedores...")
            val proveedoresDB = fetchProveedoresFromDatabase()
            proveedores.clear()
            proveedores.addAll(proveedoresDB)
            Log.d("ProveedorDebug", "Lista de proveedores actualizada: ${proveedores.size}")
        } catch (e: Exception) {
            Log.e("ProveedorDebug", "Error al cargar proveedores: ${e.message}", e)
            mensajeError = "Error al cargar proveedores: ${e.message}"
        }
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
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Añadir Proveedor",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFCD6D),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Mensaje de error
            if (mensajeError.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF4444).copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFF4444))
                ) {
                    Text(
                        text = mensajeError,
                        color = Color(0xFFFF4444),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = nombreProveedor,
                        onValueChange = { nombreProveedor = it },
                        label = { Text("Nombre del Proveedor", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFCD6D),
                            unfocusedBorderColor = Color(0xFFB2B6C1),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Persona",
                                tint = Color(0xFFFFCD6D)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = numeroTelefono,
                        onValueChange = { numeroTelefono = it },
                        label = { Text("Teléfono", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFCD6D),
                            unfocusedBorderColor = Color(0xFFB2B6C1),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                tint = Color(0xFFFFCD6D)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (nombreProveedor.isBlank() || numeroTelefono.isBlank()) {
                                mensajeError = "Todos los campos son obligatorios."
                                return@Button
                            }

                            // Lanzar una corrutina para manejar la función suspendida
                            coroutineScope.launch {
                                try {
                                    val resultado = agregarProveedorDB(nombreProveedor, numeroTelefono)

                                    if (resultado) {
                                        val nuevoProveedor = ModeloProveedor(
                                            id_proveedor = proveedores.size + 1,
                                            nombre = nombreProveedor,
                                            telefono = numeroTelefono
                                        )
                                        proveedores.add(nuevoProveedor)
                                        mostrarDialogoExito = true
                                        mensajeError = ""
                                        nombreProveedor = ""
                                        numeroTelefono = ""
                                    } else {
                                        mensajeError = "Error al añadir proveedor"
                                    }
                                } catch (e: Exception) {
                                    Log.e("ProveedorError", "Error completo: ", e)
                                    mensajeError = "Error al añadir proveedor: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFCD6D)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Añadir Proveedor",
                            color = Color(0xFF0B0E1D),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Lista de proveedores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1D2B)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Proveedores Registrados",
                        color = Color(0xFFFFCD6D),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2A2D3B))
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("ID", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Nombre", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Teléfono", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    LazyColumn {
                        items(proveedores) { proveedor ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = proveedor.id_proveedor.toString(), color = Color.White)
                                Text(text = proveedor.nombre, color = Color.White)
                                Text(text = proveedor.telefono, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoExito = false },
                title = {
                    Text(
                        "¡Éxito!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "Proveedor añadido correctamente",
                        color = Color.White
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { mostrarDialogoExito = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFFFCD6D)
                        )
                    ) {
                        Text("Aceptar")
                    }
                },
                containerColor = Color(0xFF1A1D2B),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaAgregarProveedor() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0B0E1D)
        ) {
            PantallaAgregarProveedor()
        }
    }
}
