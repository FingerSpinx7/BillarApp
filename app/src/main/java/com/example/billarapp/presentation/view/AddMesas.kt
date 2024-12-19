package com.example.billarapp.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.billarapp.R
import com.example.billarapp.domain.AddMesasABillar
import com.example.billarapp.domain.ProductoModel
import com.example.billarapp.ui.theme.SystemBarsColorChanger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddMesas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Variable para especificar el billar el cual se operara
        //val id_billar: Int =intent.getParcelableExtra("id_billar")!!


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
                    ShowContent(3)
                }
            }
        }

    }

}

@Composable
private fun ShowContent(id_billar:Int){
    AddMesasScreen(id_billar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMesasScreen(id_billar:Int){
    /*Variables y funciones*/
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var cantidadPool by remember { mutableStateOf("") }
    var cantidadCarambola by remember { mutableStateOf("") }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoFaltanCampos by remember { mutableStateOf(false) }
    val cantidadCarambolaInt = cantidadCarambola.toIntOrNull() ?: 0
    val cantidadPoolInt = cantidadPool.toIntOrNull() ?: 0


    if(mostrarDialogoConfirmacion){
        AlertDialog(
            onDismissRequest = {mostrarDialogoConfirmacion = false},
            title = { Text("Confirmar") },
            text = { Text("Agregar ${if (cantidadPool.isBlank()){"0"}else{cantidadPool} } mesa(s) POOL y ${if (cantidadCarambola.isBlank()){"0"}else{cantidadCarambola} } mesa(s) CARAMBOLA?") },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                        //Comprueba si el campo carambola no esta vacio o 0
                        if (cantidadCarambolaInt != 0) {
                            //iterara por cada mesa insertada
                                for (i in 1..cantidadCarambolaInt) {
                                    AddMesasABillar(
                                        id_billar = id_billar,
                                        numero_de_mesa = i,
                                        tipo = "CARAMBOLA"
                                    )

                                }

                            //Comprueba si el campo POOL no esta vacio o 0
                            if (cantidadPoolInt != 0) {
                                //iterara por cada mesa insertada
                                    for (i in cantidadCarambolaInt + 1..cantidadCarambolaInt + cantidadPoolInt) {
                                        AddMesasABillar(
                                            id_billar = id_billar,
                                            numero_de_mesa = i,
                                            tipo = "POOL"
                                        )
                                    }

                            }
                            mostrarDialogoExito = true
                            mostrarDialogoConfirmacion = false
                        } else {
                            if ((cantidadPoolInt != 0)) {
                                    for (i in 1..cantidadPoolInt) {
                                        AddMesasABillar(
                                            id_billar = id_billar,
                                            numero_de_mesa = i,
                                            tipo = "POOL"
                                        )

                                    }

                                //No se ha rellenado ningun campo
                            } else {
                                mostrarDialogoFaltanCampos = true
                                mostrarDialogoConfirmacion = false
                            }
                            mostrarDialogoExito = true
                            mostrarDialogoConfirmacion = false
                        }
                    }
                    }catch (e:Exception){
                        Log.e("dbg","Error: ${e.message}")
                    }
                }) {
                    Text("Si")
                }
            },
            dismissButton = {
                TextButton(onClick = {mostrarDialogoConfirmacion = false}) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarDialogoExito){
        AlertDialog(
            onDismissRequest = {mostrarDialogoExito=false},
            title = {Text("Resultado")},
            text = { Text("Mesas añadidas exitosamente") },
            confirmButton = {
                TextButton(onClick = {mostrarDialogoExito=false}) {
                    Text("Aceptar")
                }
            }
        )

    }



    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1D2B),
                    titleContentColor = Color(0xFFffffff),
                    navigationIconContentColor = Color(0xFFFFFFFF)
                ),
                title = {
                    Text(
                        "Añadir Mesas",
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
            BottomAppBar  (
                containerColor = Color(0xFF1A1D2B),
                contentColor = Color(0xFFFFFFFF),
                actions = {
                    //No hace nada
                },
            )
        },
        content = {innerPadding->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0E1D))
                    .padding(innerPadding)
                    .padding(18.dp)
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1D2B)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(vertical = (16.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            "CANTIDAD MESAS",
                            color = Color(0xff7FD238),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row (horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = (40.dp))){
                            Text(
                                "POOL",
                                color = Color(0xff7FD238),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 9.dp)
                            )
                        }


                        val pattern = remember { Regex("^\\d*$") } // Solo permite números enteros

                        OutlinedTextField(
                            value = cantidadPool,
                            onValueChange = { input ->
                                // Validar si el input es válido o está vacío
                                if (pattern.matches(input) || input.isEmpty()) {
                                    cantidadPool = input
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text("Cantidad", color = Color.White) },
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
                                    contentDescription = "Mesas",
                                    tint = Color(0xFF99DF5B)
                                )
                            }
                        )
                        //
                        Spacer(modifier = Modifier.height(30.dp))
                        Row (horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = (40.dp))){
                        Text(
                                "CARAMBOLA",
                                color = Color(0xff7FD238),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 9.dp)
                            )
                        }

                        OutlinedTextField(
                            value = cantidadCarambola,
                            onValueChange = { input ->
                                // Validar si el input es válido o está vacío
                                if (pattern.matches(input) || input.isEmpty()) {
                                    cantidadCarambola = input
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text("Cantidad", color = Color.White) },
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
                                    contentDescription = "Mesas",
                                    tint = Color(0xFF99DF5B)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            "Nota: Es necesario tener como al menos 1 mesa (Sea POOL o CARAMBOLA)",
                            color = Color.White,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )


                        if (cantidadCarambolaInt+cantidadPoolInt!=0){
                            Button(
                                onClick = {
                                    mostrarDialogoConfirmacion= true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xff7FD238)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Agregar",
                                    tint = Color(0xFF0B0E1D)
                                )
                            }
                        }else{
                            Button(
                                onClick = {/*nohacenada*/},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xffb2b6c1)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Agregar",
                                    tint = Color(0xFFffffff)
                                )
                            }
                        }


                    }
                }

            }

        }
    )
}

