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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


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

