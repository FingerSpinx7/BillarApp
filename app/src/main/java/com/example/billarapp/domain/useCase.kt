package com.example.billarapp.domain
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


    @Composable
    public fun getProductosFromDataBase(): SnapshotStateList<ProductoModel> {
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
