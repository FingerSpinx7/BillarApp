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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.billarapp.domain.eliminarProducto
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Productos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{

            MaterialTheme{
                SystemBarsColorChanger(
                    statusBarColor = Color.Black,
                    navigationBarColor = Color.Black,
                    isLightIcons = true
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1D)
                ){
                    ShowContent()
                }
            }
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
    val productos = remember { mutableStateListOf<ProductoModel>() }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var cargando by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    val productosSeleccionados = remember { mutableStateListOf<ProductoModel>() }


    fun cargarProductos() {
        coroutineScope.launch {
            try {
                val productoDB = getProductosFromDataBase()
                productos.clear()
                productos.addAll(productoDB)
            } catch (e: Exception) {
                mensajeError = "Error al cargar proveedores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    if (cargando) {
        cargarProductos()
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar ${productosSeleccionados.size} producto(s)?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            productosSeleccionados.forEach { producto ->
                                val resultado = eliminarProducto(producto)
                                if (resultado) {
                                    productos.remove(producto)
                                }
                            }
                            productosSeleccionados.clear()
                        }
                        mostrarDialogoEliminar = false
                        mostrarDialogoExito = true
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("No")
                }
            }
        )
    }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExito = false },
            title = { Text("Productos eliminados") },
            text = { Text("Los productos seleccionados han sido eliminados exitosamente.") },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoExito = false }) {
                    Text("Aceptar")
                }
            }
        )
    }








    Scaffold(
        //TopBar de la pantalla, incluyendo colores definidos, boton de regresar y titulo
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color(0xFFffffff),
                    navigationIconContentColor = Color(0xFFFFFFFF)
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
                    IconButton(onClick = {
                        val intent = Intent(context, Productos::class.java)
                        context.startActivity(intent)
                    }) {
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
                containerColor = Color(0xFF1A1D2B),
                contentColor = Color(0xFFFFFFFF),
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
                        onClick = {
                            val intent = Intent(context, AnadirProd::class.java)
                            context.startActivity(intent)
                        },
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
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(18.dp)
            ) {
                var modoSeleccion by remember { mutableStateOf(false) }
                var textoSeleccionar by remember { mutableStateOf("Seleccionar todos") }
                val productosSeleccionados = remember { mutableStateListOf<ProductoModel>() }
                var mostrarDialogoEliminar by remember { mutableStateOf(false) }




                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón para habilitar/deshabilitar el modo selección
                    Button(
                        onClick = { modoSeleccion = !modoSeleccion },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff99df5b)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(37.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if(!modoSeleccion){
                            Text(
                                "Seleccionar",
                                color = Color(0xFF0B0E1D),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }else{
                            Icon(
                                imageVector =Icons.Default.Close,
                                contentDescription = "Cerrar modo selección",
                                tint = Color(0xFF0B0E1D)
                            )
                        }
                    }

                    // Botón de seleccionar/deseleccionar todos
                    if (modoSeleccion) {
                        Button(
                            onClick = {
                                if (productosSeleccionados.size == productos.size) {
                                    // Deseleccionar todos
                                    productosSeleccionados.clear()
                                    textoSeleccionar = "Seleccionar todos"
                                } else {
                                    // Seleccionar todos
                                    productosSeleccionados.clear()
                                    productosSeleccionados.addAll(productos)
                                    textoSeleccionar = "Deseleccionar todos"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff7FD238)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                textoSeleccionar,
                                color = Color(0xFF0B0E1D),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Botón de eliminar seleccionados
                        Button(
                            onClick = {
                                if (productosSeleccionados.isNotEmpty()) {
                                    mostrarDialogoEliminar = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff7FD238)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Eliminar seleccionados",
                                color = Color(0xFF0B0E1D),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                // Lista de productos
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
                            "Productos Registrados",
                            color = Color(0xff7FD238),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Encabezados de la tabla
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (modoSeleccion) {
                                Spacer(modifier = Modifier.width(32.dp))
                            }
                            Text("ID", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("# PROV", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("PRODUCTO", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("$", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Cant.", color = Color.White, fontWeight = FontWeight.Bold)

                        }

                        if (cargando) {
                            Text(
                                "Cargando productos...",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else if (productos.isEmpty()) {
                            Text(
                                "No hay proveedores registrados.",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            LazyColumn {
                                items(productos) { producto ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (modoSeleccion) {
                                            Checkbox(
                                                checked = productosSeleccionados.contains(producto),
                                                onCheckedChange = { isChecked ->
                                                    if (isChecked) {
                                                        productosSeleccionados.add(producto)
                                                    } else {
                                                        productosSeleccionados.remove(producto)
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Color(0xff7FD238),
                                                    uncheckedColor = Color.White
                                                )
                                            )
                                        }
                                        Text(
                                            text = producto.id_producto.toString(),
                                            color = Color.White
                                        )
                                        Text(
                                            text = producto.id_proveedor.toString(),
                                            color = Color.White
                                        )
                                        Text(text = producto.det_producto, color = Color.White)
                                        Text(text = producto.precio.toString(), color = Color.White)
                                        Text(
                                            text = producto.Cantidad_Inv.toString(),
                                            color = Color.White
                                        )

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    )
}








