package com.example.billarapp.presentation.view

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.breens.beetablescompose.BeeTablesCompose
import com.example.billarapp.R
import com.example.billarapp.domain.InsertProducto
import com.example.billarapp.domain.ProveedoresModel
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.domain.getProvedoresFromDataBase
import kotlinx.coroutines.launch


class AnadirProd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{
            ShowContent()
        }
    }
}

var productoTodo = ""
var precioTodo = 0.0
var id_proveedorTodo =0
var stockTodo =0

@Preview(showSystemUi = true)
@Preview(showBackground = true)
@Composable
private fun ShowContent(){
    ProductosScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosScreen() {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = (Color(0xFFB7BABC)),
                    titleContentColor = Color(0xFF0B0E1D),
                    navigationIconContentColor = Color(0xFF0B0E1D)
                ),
                title = {
                    Text(
                        "Añadir Productos",
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
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFCD6D))
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TextFieldsAnadirProd()


            }
        }
    )

}



@Composable
private fun TextFieldsAnadirProd() {
    var preciotmp by remember { mutableStateOf<Double?>(null) }
    preciotmp = precioTodo


    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(40.dp)){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center

    ) {
        /*Seccion nombre del producto*/
        Text(text = "Nombre del producto")
        Spacer(modifier = Modifier.height(10.dp))
        NombreProductoTextField()
        Spacer(modifier = Modifier.height(18.dp))

        /*Seccion precio del producto*/

        Text(text = "Precio")
        Spacer(modifier = Modifier.height(10.dp))
        PrecioTextField(){nuevoPrecio ->
            precioTodo= preciotmp as Double
        }

        /*Seccion lista de proveedores*/
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Proveedores")
        DropdownMenuProveedores()


        /*Seccion stock*/
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Precio")
        Spacer(modifier = Modifier.height(10.dp))
        StockTextField()

        Spacer(modifier = Modifier.height(30.dp))
        ButtonSubirProducto()


    }
    }
}



/*Dropdown de proveedores*/
@Composable
fun DropdownMenuProveedores() {
    val proveedores = getProvedoresFromDataBase()
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableStateOf(0)
    }
    val selectedProveedor = if (proveedores.isNotEmpty()) proveedores[itemPosition.value] else null


    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                isDropDownExpanded.value = true
            }
            .background(Color(0xFFFFFFFF))
            .fillMaxWidth()
            .height(50.dp)
    ){
        /*Verifica que haya proveedores existentes*/
        if (proveedores.isNotEmpty()){
            Text(text = proveedores[itemPosition.value].nombre)

        }else{
            Text(text = "No hay proveedores")
        }
        Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Mostrar proveedores")
        MenuProv(isDropDownExpanded,proveedores,itemPosition)
    }
    if (selectedProveedor != null) {
        id_proveedorTodo=selectedProveedor.id_proveedor
    }
}
/*Menu que saldra al dar click en proveedores*/
@Composable
fun MenuProv(isDropDownExpanded: MutableState<Boolean>,proveedores:MutableList<ProveedoresModel>,itemPosition: MutableState<Int>){
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
fun NombreProductoTextField() {
    var producto by remember{ mutableStateOf(TextFieldValue("")) }
    TextField(
        value = producto,
        onValueChange = {
            producto = it
        },
        leadingIcon = {Icon(Icons.Filled.Edit,"Producto")},
        label = { Text(text = "Producto") },
        placeholder = { Text(text = "Nombre del producto") },

    )
    productoTodo = productoTodo
}


/*Texfield del inventario*/
@Composable
fun StockTextField() {
    val pattern = remember { Regex("^\\d+\$") }
    var stock by remember{ mutableStateOf(TextFieldValue("")) }

    TextField(
        value = stock,
        onValueChange = {
            if (it.text.matches(pattern)|| it.text.isEmpty()) {
                stock = it
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = "Precio") },
        placeholder = { Text(text = "Precio del producto") },
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
    )
    stockTodo=stock.text.toInt()
}


/*Textfield del precio*/
@Composable
fun PrecioTextField(onValueChanged: (Double?) -> Unit) {
    val pattern = remember { Regex("^\\d*(\\.\\d{0,2})?$") } // Permite hasta 2 decimales
    var precio by remember { mutableStateOf("") }

    TextField(
        value = precio,
        onValueChange = { input ->
            // Validar que cumpla el patrón o que esté vacío
            if (pattern.matches(input) || input.isEmpty()) {
                precio = input
                onValueChanged(if (input.isNotEmpty()) input.toDouble() else null)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(text = "Cantidad") },
        placeholder = { Text(text = "Cantidad en inventario") },
        leadingIcon = { Icon(Icons.Filled.Check, contentDescription = "Precio") },
        modifier = Modifier.background(Color(0xFFFFFFFF))
    )
}



/*Botón para subir datos*/
@Composable
fun ButtonSubirProducto(){
    val coroutineScope = rememberCoroutineScope()
    Row (
        horizontalArrangement = Arrangement.Center
    ){
        ElevatedButton(onClick = { coroutineScope.launch { subirDatosDeProducto() } }) {
            Text("Añadir")
        }
    }
}


/*Logica del botón que sube el producto*/
suspend fun subirDatosDeProducto(){

    // Lógica para validar datos y subirlos
    if (productoTodo!=null&& precioTodo!=null&& productoTodo!=null&& stockTodo!=null) {
        InsertProducto(productoTodo, precioTodo, productoTodo, stockTodo)
    } else {
        println("Faltan datos por completar")
    }
}







