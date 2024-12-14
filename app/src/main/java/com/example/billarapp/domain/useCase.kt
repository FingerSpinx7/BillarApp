package com.example.billarapp.domain
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.billarapp.data.network.supabaseBillar
import com.example.billarapp.presentation.view.productoTodo
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


suspend fun InsertProducto(proveedor:String, precio:Double, producto:String, stock:Int){
val datosParaSubir = mapOf(
        "id_proveedor" to proveedor,
        "precio" to precio,
        "det_producto" to producto,
        "Cantidad_Inv" to stock
    )
    supabaseBillar().from("Productos").insert(datosParaSubir)


}
