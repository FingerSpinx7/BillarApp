package com.example.billarapp.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billarapp.R
import com.example.billarapp.domain.CuentaModel
import com.example.billarapp.domain.InsertProductoToCuenta
import com.example.billarapp.domain.ProductoModel
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProductoToCuenta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val cuentaId: Int =intent.getParcelableExtra("cuentaId")!!
        enableEdgeToEdge()

        setContent(){
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
                    ShowContent(2)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowContent(cuentaId:Int){
    val context = LocalContext.current
    val productos = remember { mutableStateListOf<ProductoModel>() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    var modoSeleccion by remember { mutableStateOf(true) }
    var productoSeleccionado = remember { mutableStateOf<ProductoModel?>(null) }
    var cantidad by remember { mutableStateOf("") }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoFaltanCampos by remember { mutableStateOf(false) }
    var mensajeDialogoExito by remember { mutableStateOf("") }

    if(mostrarDialogoConfirmacion){

        productoSeleccionado.value?.let {
            MostrarDialogoConfirmacion(
                producto = it.det_producto,
                onConfirmar = {
                    mostrarDialogoConfirmacion = false
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            InsertProductoToCuenta(
                                id_cuenta = cuentaId,
                                id_producto = productoSeleccionado.value!!.id_producto,
                                cantidad = cantidad.toInt(),
                            )
                            mensajeDialogoExito = "Producto añadido con exito"
                        }catch (e:Exception){
                            mensajeDialogoExito = "Error al subir el producto: ${e.message}"
                        }
                        mostrarDialogoExito = true
                    }
                },
                onCancelar = {mostrarDialogoConfirmacion=false}
            )
        }
    }

    if (mostrarDialogoExito){
        MostarDialogoExito(
            mensaje = mensajeDialogoExito,
            onDismiss = {mostrarDialogoExito = false},
            context = context
        )
    }

    if(mostrarDialogoFaltanCampos){
        MostrarDialogoFaltanCampos { mostrarDialogoFaltanCampos = false }
    }

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



    Scaffold(
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

                },


                )
        },
        content = {
                innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(18.dp)
            ){



                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0B0E1D))
                ){
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        /*Lista te productos*/
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(20.dp),
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
                                        "No hay productos registrados.",
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
                                                        checked = productoSeleccionado.value == producto, // Verifica si este producto es el seleccionado
                                                        onCheckedChange = { isChecked ->
                                                            productoSeleccionado.value = if (isChecked) producto else null // Actualiza el producto seleccionado
                                                        },
                                                        colors = CheckboxDefaults.colors(
                                                            checkedColor = Color(0xff7FD238),
                                                            uncheckedColor = Color.White
                                                        )
                                                    )
                                                }

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
                        //
                        //Formulario
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1A1D2B)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically // Centrar ambos componentes
                            ) {
                                val pattern = remember { Regex("^\\d*$") } // Solo permite números enteros

                                // TextField
                                OutlinedTextField(
                                    value = cantidad,
                                    onValueChange = { input ->
                                        if (pattern.matches(input)) {
                                            cantidad = input
                                        }
                                    },
                                    label = { Text("Cant.", color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp), // Altura uniforme

                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xff99df5b),
                                        unfocusedBorderColor = Color(0xffb2b6c1),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.AddCircle,
                                            contentDescription = "Añadir",
                                            tint = Color(0xff99df5b)
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // Botón Agregar
                                Button(
                                    onClick = {
                                        if (!productoSeleccionado.equals(null) && cantidad.isNotBlank()) {
                                            mostrarDialogoConfirmacion = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (productoSeleccionado.equals(null) || cantidad.isBlank())
                                            Color(0xFFB2B6C1)
                                        else
                                            Color(0xff7FD238),
                                        contentColor = if (productoSeleccionado.equals(null) || cantidad.isBlank())
                                            Color.White
                                        else
                                            Color.Black
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp), // Altura uniforme
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (productoSeleccionado.equals(null) || cantidad.isBlank())
                                            Icons.Filled.Edit
                                        else
                                            Icons.Filled.Add,
                                        contentDescription = "Editar"
                                    )
                                }
                            }
                        }

                    }

                }


            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MostrarDialogoConfirmacion(
    producto: String,
    onConfirmar: ()->Unit,
    onCancelar:()->Unit
){

    AlertDialog(
        onDismissRequest = {onCancelar()},
        title = { Text("Confirmar") },
        text = { Text("¿Está seguro con registrar productos a la cuenta?") },
        confirmButton = {
            TextButton(onClick = {onConfirmar()}) {
                Text("Si")
            }
        },
        dismissButton = {
            TextButton(onClick = {onCancelar()}) {
                Text("Cancelar")
            }
        }
    )
}

//Dialogo Faltan campos por llenar
@Composable
private fun MostrarDialogoFaltanCampos(onDismiss: ()->Unit){
    AlertDialog(
        onDismissRequest = {onDismiss()},
        title = { Text("Error") },
        text = {Text("Por favor, completa todos los campos antes de continuar")},
        confirmButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Aceptar")
            }
        }
    )
}

//Dialogo Datos subidos exitosamente
@Composable
private fun MostarDialogoExito(mensaje: String, onDismiss: () -> Unit, context: Context){
    AlertDialog(
        onDismissRequest = {onDismiss()},
        title = {Text("Resultado")},
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                //Nav to Productos
                val intent = Intent (context,Productos::class.java)
                context.startActivity(intent)
            }) {
                Text("Aceptar")
            }
        }
    )
}
