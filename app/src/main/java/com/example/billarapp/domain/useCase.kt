package com.example.billarapp.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


suspend fun getProductosFromDataBaseWithIdBillarFilter(id_Billar: Int): List<ProductoModel>{
    return  try {
        val response = supabaseBillar()
            .postgrest
            .from("Productos")
            .select {
                filter {
                    eq("id_billar",id_Billar)
                }
            }
        val rawJson = response.data?:"Respuesta vacia"
        val jsonArray = Json.parseToJsonElement(rawJson).jsonArray
        jsonArray.map{jsonElement ->
            val jsonObject=jsonElement.jsonObject
            ProductoModel(
                id_producto = jsonObject["id_producto"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
                id_proveedor = jsonObject["id_proveedor"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
                det_producto = jsonObject["det_producto"]?.jsonPrimitive?.content ?: "",
                precio = jsonObject["precio"]?.jsonPrimitive?.content?.toDoubleOrNull()?:0.0,
                Cantidad_Inv = jsonObject["Cantidad_Inv"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
            )

        }
    }catch (e: Exception){
        emptyList()

    }

}

suspend fun AddMesasABillar(id_billar: Int,numero_de_mesa: Int,tipo:String){
    try {
        val mesaAgregada = CuentaModelL(
            id_billar,
            numero_de_mesa,
            tipo.trim()
        )
        supabaseBillar()
            .postgrest
            .from("mesa")
            .insert(mesaAgregada)

    }catch (e:Exception){
        Log.e("dbg","Fallo en: ${e.message}")
    }
}



suspend fun InsertProductoToCuenta(id_cuenta: Int,id_producto: Int,cantidad:Int){
    try{
        val productoConsumido = mapOf(
            "id_cuenta" to id_cuenta,
            "id_producto" to id_producto,
            "cantidad" to cantidad,
        )

        supabaseBillar()
            .postgrest
            .from("productos_consumidos")
            .insert(productoConsumido)


    }catch (e:Exception){
        println(e.message)


    }
}


suspend fun getProductosFromDataBase(): List<ProductoModel>{
        return  try {
            val response = supabaseBillar()
                .postgrest
                .from("Productos")
                .select ()
            val rawJson = response.data?:"Respuesta vacia"
            val jsonArray = Json.parseToJsonElement(rawJson).jsonArray
            jsonArray.map{jsonElement ->
                val jsonObject=jsonElement.jsonObject
                ProductoModel(
                    id_producto = jsonObject["id_producto"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
                    id_proveedor = jsonObject["id_proveedor"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
                    det_producto = jsonObject["det_producto"]?.jsonPrimitive?.content ?: "",
                    precio = jsonObject["precio"]?.jsonPrimitive?.content?.toDoubleOrNull()?:0.0,
                    Cantidad_Inv = jsonObject["Cantidad_Inv"]?.jsonPrimitive?.content?.toIntOrNull()?:0,
                )

            }
        }catch (e: Exception){
            emptyList()

        }

    }

    @Composable
    fun getProvedoresFromDataBase(): SnapshotStateList<ProveedoresModel> {
        val proveedores = remember { mutableStateListOf<ProveedoresModel>() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                try {
                    val data = supabaseBillar()
                        .from("proveedor")
                        .select()
                        .decodeList<ProveedoresModel>()
                    proveedores.addAll(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("dbg", "Error: ${e.message}")
                }
            }
        }

        return proveedores
    }

suspend fun EditProd(id_producto:Int,proveedor:Int, precio:MutableState<TextFieldValue>, producto:MutableState<TextFieldValue>, stock:MutableState<TextFieldValue>){
    val datosParaEditar = ProductoModel(
        id_producto=id_producto,
        id_proveedor = proveedor,
        precio = precio.value.text.toDoubleOrNull()?:0.0,
        det_producto = producto.value.text,
        Cantidad_Inv = stock.value.text.toIntOrNull()?:0
    )
    supabaseBillar().from("Productos").update(
        {
            set("det_producto",datosParaEditar.det_producto)
            set("precio",datosParaEditar.precio)
            set("Cantidad_Inv",datosParaEditar.Cantidad_Inv)
            set("id_proveedor",datosParaEditar.id_proveedor)
        }
    ){
        filter {
            eq("id_producto",datosParaEditar.id_producto)
        }
    }
}

suspend fun InsertProducto(proveedor:Int, precio:MutableState<TextFieldValue>, producto:MutableState<TextFieldValue>, stock:MutableState<TextFieldValue>){
val datosParaSubir = ProductoUpload(
        id_proveedor = proveedor,
        precio = precio.value.text.toDoubleOrNull()?:0.0,
        det_producto = producto.value.text,
        Cantidad_Inv = stock.value.text.toIntOrNull()?:0
    )
    supabaseBillar().from("Productos").insert(datosParaSubir)
}

suspend fun eliminarProducto(producto:ProductoModel):Boolean{
    return try {
        val clienteSupabase = supabaseBillar()
        val response = clienteSupabase.postgrest
            .from(table = "Productos")
            .delete{
                filter {
                    eq(column = "id_producto",producto.id_producto)
                }
            }
        response.data!=null
    }catch (e:Exception){
        Log.e("ErrorEliminarProducto", "Error: ${e.message}")
        false
    }
}


suspend fun getCuentasFromDatabase(): List<CuentaModel> {
    return try {
        withContext(Dispatchers.IO) {
            val data = supabaseBillar()
                .from("cuenta")
                .select()
                .decodeList<CuentaModel>()
            data
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("dbg", "Error: ${e.message}")
        emptyList()
    }
}


@Composable
fun getDetalleProductosConsumidos(idCuenta: Int): SnapshotStateList<DetalleProductosConsumidosModel> {
    val detalles = remember { mutableStateListOf<DetalleProductosConsumidosModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val data = supabaseBillar()
                    .postgrest.rpc("filtrar_productos", mapOf("id_filtro" to idCuenta)) // Llama correctamente a la RPC
                    .decodeList<DetalleProductosConsumidosModel>()
                detalles.addAll(data)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error: ${e.message}")
            }
        }
    }
    return detalles
}

suspend fun fetchProveedoresFromDatabase(): List<ModeloProveedor> {
    return try {
        val response = supabaseBillar()
            .postgrest
            .from("proveedor")
            .select()
        val rawJson = response.data ?: "Respuesta vacía"
        val jsonArray = Json.parseToJsonElement(rawJson).jsonArray
        jsonArray.map { jsonElement ->
            val jsonObject = jsonElement.jsonObject
            ModeloProveedor(
                id_proveedor = jsonObject["id_proveedor"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                nombre = jsonObject["nombre"]?.jsonPrimitive?.content ?: "",
                telefono = jsonObject["telefono"]?.jsonPrimitive?.content ?: ""
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}


suspend fun agregarProveedorDB(nombre: String, telefono: String): Boolean {
    return try {
        val clienteSupabase = supabaseBillar()

        val proveedor = mapOf(
            "nombre" to nombre.trim(),
            "telefono" to telefono.trim()
        )
        Log.d("ProveedorDebug", "Enviando proveedor: $proveedor")

        clienteSupabase
            .postgrest
            .from("proveedor")
            .insert(proveedor)

        Log.d("ProveedorDebug", "Inserción exitosa")
        true
    } catch (e: Exception) {
        Log.e("ErrorAgregarProveedor", "Error detallado: ", e)
        Log.e("ErrorAgregarProveedor", "Mensaje: ${e.message}")
        false
    }
}

suspend fun eliminarProveedor(proveedor: ModeloProveedor): Boolean {
    return try {
        val clienteSupabase = supabaseBillar()

        // Verifica si el proveedor está relacionado con productos
        val productosRelacionados = clienteSupabase.postgrest
            .from("Productos")
            .select { filter { eq("id_proveedor", proveedor.id_proveedor) } }
            .decodeList<ProductoModel>()

        if (productosRelacionados.isNotEmpty()) {
            throw Exception("No se puede eliminar el proveedor porque está relacionado con productos.")
        }

        // Elimina el proveedor si no hay relaciones
        val response = clienteSupabase.postgrest
            .from(table = "proveedor")
            .delete {
                filter { eq(column = "id_proveedor", proveedor.id_proveedor) }
            }

        response.data != null
    } catch (e: Exception) {
        Log.e("ErrorEliminarProveedor", "Error detallado: ${e.message}")
        throw e // Propaga la excepción para manejarla en la UI
    }
}


//Obtención de usuarios
@Composable
fun getHistorialFromDatabase(fecha: String? = null): SnapshotStateList<HistorialModel> {
    val historial = remember { mutableStateListOf<HistorialModel>() }

    LaunchedEffect(fecha) {
        try {
            val query = supabaseBillar()
                .postgrest
                .from("cuenta_view")
                .select {
                    if (fecha != null) {
                        filter { eq("fecha_inicio", fecha) } // La comparación es directa
                    }
                }

            val data = query.decodeList<HistorialModel>()
            historial.clear()
            historial.addAll(data)

        } catch (e: Exception) {
            Log.e("ErrorHistorial", "Error al obtener historial: ${e.message}")
        }
    }

    return historial
}

suspend fun getDetallesHistorial(idCuenta: Int): List<ProductoConsumido> {
    return try {
        val response = supabaseBillar()
            .postgrest
            .from("productos_consumidos")
            .select {
                filter { eq("id_cuenta", idCuenta) }
            }
            .decodeList<ProductoConsumido>()
        response
    } catch (e: Exception) {
        Log.e("ErrorDetalles", "Error al obtener detalles: ${e.message}")
        emptyList()
    }
}

suspend fun actualizarFechaCierreCuenta(idCuenta: Int): Boolean {
    return try {
        val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        // Aquí iría la lógica para actualizar la fecha en la base de datos
        println("Fecha de cierre actualizada a $fechaActual para la cuenta $idCuenta")
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

suspend fun obtenerDetalleMesa(idCuenta: Int): DetalleMesaModel? {
    return try {
        // Simulación de obtención de datos
        DetalleMesaModel(
            id_cuenta = idCuenta,
            numero_mesa = 1,
            tipo_mesa = "Básica",
            cliente = "Juan Pérez",
            fecha_inicio = "2023-12-19 14:00:00",
            fecha_fin = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

