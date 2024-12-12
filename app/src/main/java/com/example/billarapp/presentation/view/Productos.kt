package com.example.billarapp.presentation.view

import android.R.style.Theme
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

class Productos : AppCompatActivity() {

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
private fun ShowContent(){
    ProductosScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen() {
    val productos = getProductosFromDataBase()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())



    val tableHeaders = listOf("Id prod.", "Id prov.", "Producto", "Precio", "Stock")

    Scaffold(
        //TopBar de la pantalla, incluyendo colores definidos, boton de regresar y titulo
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFCD6D),
                    titleContentColor = Color(0xFF0B0E1D),
                    navigationIconContentColor = Color(0xFF0B0E1D)
                ),
                title = {
                    Text(
                        "Productos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },


                navigationIcon = {
                    /*CORREGIR CLASE LLAMADA EN LA IMPLEMENTACION DE LAS DEMAS PANTALLAS*/
                    IconButton(onClick = {val intent = Intent(context, Productos::class.java)
                        context.startActivity(intent)}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },

        bottomBar = {
            BottomAppBar(
                actions = {
                    //Ese boton debe llamar al menu principal
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Localized description")
                    }

                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Localized description")
                    }

                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { val intent = Intent(context, AnadirProd::class.java)
                            context.startActivity(intent) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),

                    ) {
                        Icon(Icons.Filled.Add, "Add products")
                    }

                },

            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB7BABC))
                    .padding(innerPadding)
                    .padding(horizontal = 30.dp)
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




