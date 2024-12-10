package com.example.billarapp.presentation.view

import android.R.style.Theme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Productos : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShowContent()
        }
    }


}
@Serializable
data class Producto(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Double,
    val cantidad_Inv: Int
)

@Preview(showBackground = true)
@Composable
fun ShowContent(){
    ProductosScreen(supabaseBillar())
}


@Composable
fun ProductosScreen(supabaseBillar: SupabaseClient) {
    val productos = remember { mutableStateListOf<Producto>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // Decodificación de datos desde Supabase
                val data = supabaseBillar
                    .from("Productos")
                    .select()
                    .decodeList<Producto>() // Decodificar directamente en objetos Producto
                productos.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace() // Manejar errores si ocurren
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCD6D))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFFB7BABC)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* Acción para volver atrás */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.width(70.dp)
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Productos",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B0E1D),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lista de productos",
            fontSize = 30.sp,
            color = Color(0xFF4A4D5D),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabla de productos
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = producto.id_producto.toString(), fontSize = 16.sp)
                    Text(text = producto.det_producto, fontSize = 16.sp)
                    Text(text = producto.precio.toString(), fontSize = 16.sp)
                    Text(text = producto.cantidad_Inv.toString(), fontSize = 16.sp)

                    Row {
                        Button(
                            onClick = { /* Acción para editar producto */ },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Editar")
                        }

                        Button(
                            onClick = { /* Acción para eliminar producto */ }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Añadir producto
        Button(
            onClick = { /* Acción para redirigir a añadir producto */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Añadir producto")
        }
    }
}



