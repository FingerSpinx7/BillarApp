package com.example.billarapp.presentation.view

import android.R.style.Theme
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.breens.beetablescompose.BeeTablesCompose
import com.example.billarapp.data.network.supabaseBillar
import com.example.billarapp.domain.ProductoModel
import com.example.billarapp.domain.getProductosFromDataBase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Productos : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShowContent()
        }

    }

}






@Preview(showBackground = true)
@Composable
fun ShowContent(){
    ProductosScreen()
}


@Composable
fun ProductosScreen() {
    /*val productos = remember { mutableStateListOf<ProductoModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val data = supabaseBillar()
                    .from("Productos")
                    .select()
                    .decodeList<ProductoModel>()
                productos.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("dbg", "Error: ${e.message}")
            }
        }
    }*/

    val productos = getProductosFromDataBase()

    val tableHeaders = listOf("Id prod.", "Id prov.", "Producto", "Precio", "Stock")

    Scaffold(
        topBar = {
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
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Acción para editar producto */ }) {
                    Text("Editar")
                }
                Button(onClick = { /* Acción para eliminar producto */ }) {
                    Text("Eliminar")
                }

                val context = LocalContext.current

                Button(onClick = {
                    val intent = Intent(context, AnadirProd::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Añadir producto")
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCD6D))
                    .padding(innerPadding)
            ) {
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

                BeeTablesCompose(data = productos, headerTableTitles = tableHeaders)
            }
        }
    )
}




