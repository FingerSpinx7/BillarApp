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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.breens.beetablescompose.BeeTablesCompose
import com.example.billarapp.R
import com.example.billarapp.domain.ProveedoresModel
import com.example.billarapp.domain.getProductosFromDataBase
import com.example.billarapp.domain.getProvedoresFromDataBase


class AnadirProd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{
            ShowContent()
        }
    }
}

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
                    containerColor = Color(0xffffffff),
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
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {/*Añadir producto*/},
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
                    .background(Color(0xFFFFCD6D))
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lista de productos",
                    fontSize = 30.sp,
                    color = Color(0xFF4A4D5D),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextFieldsAnadirProd()


            }
        }
    )
}

@Composable
private fun TextFieldsAnadirProd() {
    val proveedores = getProvedoresFromDataBase()
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableStateOf(0)
    }



    Row (verticalAlignment = Alignment.CenterVertically){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Nombre del producto")
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = "Nombre del producto", onValueChange = {})
        Spacer(modifier = Modifier.height(18.dp))

        Text(text = "Precio")
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = "Precio", onValueChange = {})


        /*Lista de proveedores*/
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = "Proveedores")
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }){

            /*Verifica que haya proveedores existentes*/
            if (proveedores.isNotEmpty()){
                Text(text = proveedores[itemPosition.value].nombre)
            }else{
                Text(text = "No hay proveedores")
            }
            Icon(Icons.Rounded.ArrowDropDown, contentDescription = "Mostrar proveedores")
            MenuProv(isDropDownExpanded,proveedores,itemPosition)
        }






        }
    }
}



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







