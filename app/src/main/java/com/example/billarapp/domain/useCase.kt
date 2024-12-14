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
import kotlinx.coroutines.launch





    @Composable
    fun getProductosFromDataBase(): SnapshotStateList<ProductoModel> {
        val productos = remember { mutableStateListOf<ProductoModel>() }
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
        }

        return productos
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
