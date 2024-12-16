package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billarapp.R
import com.example.billarapp.domain.EditProd
import com.example.billarapp.domain.InsertProducto
import com.example.billarapp.domain.ProductoModel
import com.example.billarapp.domain.ProveedoresModel
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.domain.getProvedoresFromDataBase
import com.example.billarapp.presentation.controller.MiViewModel
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarProducto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val producto: ProductoModel=intent.getParcelableExtra("producto")!!

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
                    ProductosScreen(producto)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen(productoRecibido: ProductoModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .background(Color(0xFF0B0E1D))
        ,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = (Color(0xFF1A1D2B)),
                    titleContentColor = Color(0xFFFFFFFF),
                    navigationIconContentColor = Color(0xFFFFFFFF)
                ),
                title = {
                    Text(
                        "Editar Producto",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    val activity = (LocalContext.current as? Activity)
                    IconButton(onClick = {
                        activity?.finish()
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
                actions = {
                    //Ese boton debe llamar al menu principal
                    val activity = (LocalContext.current as? Activity)
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.Home, contentDescription = "Localized description")
                    }
                },
                containerColor = Color(0xFF1A1D2B),
                contentColor = Color.White

            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp)
            ) {
                TextFieldsAnadirProd(productoRecibido)
            }
        }
    )

}



@Composable
private fun TextFieldsAnadirProd(productoRecibido:ProductoModel) {
    val producto = remember { mutableStateOf(TextFieldValue("")) }
    val precio = remember { mutableStateOf(TextFieldValue("")) }
    var proveedor by remember { mutableStateOf(0) }
    val stock = remember{ mutableStateOf(TextFieldValue("")) }

    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center

        ) {

            /*Seccion nombre producto*/
            Text(text = "Nombre del producto", color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            NombreProductoTextField(productoRecibido,producto)
            Spacer(modifier = Modifier.height(18.dp))

            /*Seccion precio del producto*/
            Text(text = "Precio", color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            PrecioTextField(productoRecibido,precio)

            /*Seccion lista de proveedores*/
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Proveedores", color = Color.White)
            DropdownMenuProveedores(proveedorDrop = productoRecibido,
                onProveedorChanged = {nuevoProveedor ->
                    proveedor =nuevoProveedor
                })


            /*Seccion stock*/
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Inventario", color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            StockTextField(productoRecibido,stock)


            Spacer(modifier = Modifier.height(30.dp))
            ButtonSubirProducto(productoRecibido.id_producto,producto,precio,proveedor,stock)


        }
    }
}


/*Dropdown de proveedores*/
@Composable
private fun DropdownMenuProveedores(
    proveedorDrop: ProductoModel,
    onProveedorChanged: (Int) -> Unit,

) {
    val proveedores = getProvedoresFromDataBase()
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember {
        mutableStateOf(
            proveedores.indexOfFirst { it.id_proveedor==proveedorDrop.id_producto }.takeIf { it!=-1 }?:0
        ) }

    Row(

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { isDropDownExpanded.value = true }
            .background(Color(0xFF0B0E1D))
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = Color(0xFFB2B6C1),
                shape = RoundedCornerShape(12.dp)

            )
            .padding(start =12.dp)
    ) {if (proveedores.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                Icons.Rounded.AccountCircle, // Ícono de ejemplo
                contentDescription = "Proveedor",
                tint = Color(0xFF99DF5B),
                modifier = Modifier.size(30.dp).padding(end = 8.dp) // Tamaño del ícono y espaciado
            )
            Text(
                text = proveedores[itemPosition.value].nombre,
                color = Color.White
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                Icons.Rounded.AccountCircle, // Ícono para "No hay proveedores"
                contentDescription = "Advertencia",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "No hay proveedores",
                color = Color.White
            )
        }
    }
        Icon(
            Icons.Rounded.ArrowDropDown,
            contentDescription = "Mostrar proveedores",
            tint = Color.White
        )
        MenuProv(isDropDownExpanded, proveedores, itemPosition)
    }

    // Notificar el cambio de proveedor seleccionado
    if (proveedores.isNotEmpty()) {
        onProveedorChanged(proveedores[itemPosition.value].id_proveedor)
    }
}


/*Menu que saldra al dar click en proveedores*/
@Composable
private fun MenuProv(isDropDownExpanded: MutableState<Boolean>, proveedores:MutableList<ProveedoresModel>, itemPosition: MutableState<Int>){
    DropdownMenu(
        expanded = isDropDownExpanded.value,
        onDismissRequest = {
            isDropDownExpanded.value = false}
    ) {
        proveedores.forEachIndexed{
                index, nombre ->
            DropdownMenuItem(text = {
                Text(text = nombre.nombre)
            },
                onClick = {
                    isDropDownExpanded.value = false
                    itemPosition.value = index
                }
            )
        }
    }
}


/*TextField encargada de almacenar el producto*/
@Composable
private fun NombreProductoTextField(producto_default:ProductoModel,producto_ingresado:MutableState<TextFieldValue>) {
    if (producto_ingresado.value.text.isEmpty()) {
        producto_ingresado.value = TextFieldValue(producto_default.det_producto)
    }

    OutlinedTextField(
        value = producto_ingresado.value,
        onValueChange = { nuevoTexto ->
            producto_ingresado.value = nuevoTexto
        },
        label = { Text(text = "Producto", color = Color.White) },
        placeholder = { Text(text = "Nombre del producto") },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff99df5b),
            unfocusedBorderColor = Color(0xFFB2B6C1),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )

    )
}


/*Texfield del inventario*/
@Composable
private fun StockTextField(producto_default: ProductoModel,stockRecibido:MutableState<TextFieldValue>) {
    val pattern = remember { Regex("^\\d*$") } // Solo permite números enteros
    // Inicializa el valor de precio_ingresado con el precio por defecto si está vacío
    if (stockRecibido.value.text.isEmpty()) {
        stockRecibido.value = TextFieldValue(producto_default.Cantidad_Inv.toString())
    }

    OutlinedTextField(
        value = stockRecibido.value,
        onValueChange = { input ->
            // Validar que cumpla el patrón o que esté vacío
            if (pattern.matches(input.text) || input.text.isEmpty()) {
                stockRecibido.value = input
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text("Cantidad en Inventario", color = Color.White) },
        placeholder = { Text(text = "Ingresa cantidad") },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff99df5b),
            unfocusedBorderColor = Color(0xFFB2B6C1),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        leadingIcon = {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "Inventario",
                tint = Color(0xFF99DF5B)
            )
        }
    )
}


/*Textfield del precio*/
@Composable
private fun PrecioTextField(producto_default: ProductoModel,precio_ingresado:MutableState<TextFieldValue>) {
    val pattern = remember { Regex("^\\d*(\\.\\d{0,2})?$") } // Permite hasta 2 decimales
    // Inicializa el valor de precio_ingresado con el precio por defecto si está vacío
    if (precio_ingresado.value.text.isEmpty()) {
        precio_ingresado.value = TextFieldValue(producto_default.precio.toString())
    }

    OutlinedTextField(
        value = precio_ingresado.value,
        onValueChange = { input ->
            // Validar que cumpla el patrón o que esté vacío
            if (pattern.matches(input.text) || input.text.isEmpty()) {
                precio_ingresado.value = input
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = "Precio", color =  Color.White) },
        placeholder = { Text(text = "Precio") },
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Precio del producto",
                tint = Color(0xFF99DF5B)
            ) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff99df5b),
            unfocusedBorderColor = Color(0xFFB2B6C1),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}


/*Boton que sube el producto*/
@Composable
private fun ButtonSubirProducto(prod_seleccionado:Int,producto: MutableState<TextFieldValue>, precio: MutableState<TextFieldValue>, proveedor: Int, stock: MutableState<TextFieldValue>) {
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoFaltanCampos by remember { mutableStateOf(false) }
    var mensajeDialogoExito by remember { mutableStateOf("") }
    val context = LocalContext.current
    val id_prod= prod_seleccionado

    ElevatedButton(onClick = {
        // Valida los datos antes de enviarlos
        if (producto.value.text.isNotBlank() && precio.value.text.isNotBlank() && proveedor!=0 && stock.value.text.isNotBlank()) {
            mostrarDialogoConfirmacion = true

        } else {
            mostrarDialogoFaltanCampos= true
        }
    },
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color(0xFF99DF5B)
            )

    ) {
        Text("Añadir Producto", color = Color.Black)
    }

    if(mostrarDialogoConfirmacion){
        MostrarDialogoConfirmacion(
            producto = producto.value.text,
            onConfirmar = {
                mostrarDialogoConfirmacion = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        EditProd(
                            id_producto= id_prod,
                            proveedor = proveedor,
                            precio = precio,
                            producto = producto,
                            stock = stock
                        )
                        mensajeDialogoExito = "Producto modificado con exito"
                    }catch (e:Exception){
                        mensajeDialogoExito = "Error al modificar el producto: ${e.message}"
                    }
                    mostrarDialogoExito = true
                }
            },
            onCancelar = {mostrarDialogoConfirmacion=false}
        )
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
        text = { Text("¿Deseas modificar el producto $producto?") },
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
        text = { Text("Por favor, completa todos los campos antes de continuar") },
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
        title = { Text("Resultado") },
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


