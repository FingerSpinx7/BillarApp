package com.example.billarapp.presentation.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.domain.getCuentasFromDatabase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.material.icons.filled.ArrowBack
import android.util.Log
import com.example.billarapp.domain.CuentaModel
import com.example.billarapp.ui.theme.*



class Cuentas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val idBillarSeleccionado = intent.getIntExtra("idBillarSeleccionado", -1) //Valor temporal
        setContent {
            CuentasScreen(/*idBillarSeleccionado*/)
        }
    }
}

@Composable
fun CuentasScreen(/*idBillarSeleccionado: Int*/) {
    val tableHeaders = listOf("Cuenta", "Mesa", "Abierto", "Fin", "Cliente")

    var todasLasCuentas by remember { mutableStateOf(emptyList<CuentaModel>()) }
    var fecha by remember { mutableStateOf("") }
    var busqueda by remember { mutableStateOf("") }
    var filaSeleccionada by remember { mutableStateOf<Int?>(null) }

    val idBillarSeleccionado = 1
    val context = LocalContext.current

    //Cargar BD
    LaunchedEffect(Unit) {
        todasLasCuentas = getCuentasFromDatabase()
        Log.d("CuentasDebug", "Datos cargados: ${todasLasCuentas.size} cuentas disponibles")
    }

    //Filtrar cuentas
    fun filtrarCuentas(cuentas: List<CuentaModel>): List<CuentaModel> {
        return cuentas.filter { cuenta ->
            val fechaInicio = cuenta.fecha_inicio.split("T")[0]
            cuenta.id_billar == idBillarSeleccionado && // id_billar
                    (busqueda.isBlank() || cuenta.cliente.contains(busqueda, ignoreCase = true)) &&
                    (fecha.isBlank() || fechaInicio == fecha)
        }
    }

    //Filtrar dependiendo la fecha o datos
    val cuentasFiltradas = remember(todasLasCuentas, fecha, busqueda) {
        filtrarCuentas(todasLasCuentas)
    }

    Scaffold(
        topBar = {
            // Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeSecundario)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = TextoBlanco,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Regresar",
                    color = TextoBlanco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoTarjeta)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (filaSeleccionada == null) {
                            Toast.makeText(
                                context,
                                "Por favor, selecciona un elemento de la tabla.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val cuenta = cuentasFiltradas[filaSeleccionada!!]
                            Toast.makeText(
                                context,
                                "Seleccionaste la cuenta: ${cuenta.id_cuenta}.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val tiempo = calcularTiempo(cuenta.fecha_inicio, cuenta.fecha_fin)

                            val intent = Intent(context, DetalleCuenta::class.java).apply {
                                putExtra("mesa", cuenta.Numero_mesa.toString())
                                putExtra("cuenta", cuenta.id_cuenta.toString())
                                putExtra("cliente", cuenta.cliente)
                                putExtra("tiempo", tiempo)
                            }
                            context.startActivity(intent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario)
                ) {
                    Text("Consultar Cuenta", color = TextoBlanco)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Mostrando todas las cuentas existentes.",
                            Toast.LENGTH_SHORT
                        ).show()
                        fecha = ""
                        busqueda = ""
                        filaSeleccionada = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrimario)
                ) {
                    Text("Mostrar Todo", color = TextoBlanco)
                }
            }
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(FondoTarjeta)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Consulta de Cuentas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrimario, // Texto verde
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                //Buscadores
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Fecha", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VerdePrimario)
                        Row(
                            modifier = Modifier
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar Icon",
                                tint = TextoBlanco,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        val calendar = Calendar.getInstance()
                                        DatePickerDialog(
                                            context,
                                            { _, y, m, d ->
                                                fecha = String.format("%04d-%02d-%02d", y, m + 1, d)
                                            },
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH)
                                        ).show()
                                    }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = fecha, fontSize = 16.sp, color = TextoBlanco)
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Búsqueda", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VerdePrimario)
                        Row(
                            modifier = Modifier
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = TextoBlanco,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = busqueda,
                                onValueChange = { busqueda = it },
                                textStyle = TextStyle(fontSize = 16.sp, color = TextoBlanco),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                //Encabezado tabla
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(VerdeSecundario)
                        .padding(8.dp)
                ) {
                    tableHeaders.forEach { header ->
                        Text(
                            header,
                            Modifier.weight(1f),
                            color = TextoBlanco,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                //Tabla
                LazyColumn {
                    itemsIndexed(cuentasFiltradas) { index, cuenta ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { filaSeleccionada = index }
                                .background(if (filaSeleccionada == index) VerdePrimario.copy(alpha = 0.3f) else Color.Transparent)
                                .padding(8.dp)
                        ) {
                            Text(cuenta.id_cuenta.toString(), Modifier.weight(1f), color = TextoBlanco)
                            Text(cuenta.Numero_mesa.toString(), Modifier.weight(1f), color = TextoBlanco)
                            Text(cuenta.fecha_inicio.split("T")[0], Modifier.weight(1f), color = TextoBlanco)
                            Text(cuenta.fecha_fin.split("T")[0], Modifier.weight(1f), color = TextoBlanco)
                            Text(cuenta.cliente, Modifier.weight(1f), color = TextoBlanco)
                        }
                    }
                }
            }
        }
    )
}

fun calcularTiempo(inicio: String, fin: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val inicioTime = LocalDateTime.parse(inicio, formatter)
    val finTime = LocalDateTime.parse(fin, formatter)
    val duration = Duration.between(inicioTime, finTime)
    val horas = duration.toHours()
    val minutos = duration.toMinutes() % 60
    return "$horas horas y $minutos minutos"
}
