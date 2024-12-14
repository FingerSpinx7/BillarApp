package com.example.billarapp.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.billarapp.data.network.supabaseBillar
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

@Composable
fun getProductosFromDataBase(): SnapshotStateList<ProductoModel> {
    val productos = remember { mutableStateListOf<ProductoModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val data = supabaseBillar()
                    .postgrest
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

suspend fun fetchProveedoresFromDatabase(): List<ModeloProveedor> {
    return try {
        Log.d("ProveedorDebug", "Iniciando carga de proveedores")
        val data = supabaseBillar()
            .postgrest
            .from("proveedor")
            .select()
            .decodeList<ModeloProveedor>()

        Log.d("ProveedorDebug", "Datos recibidos: $data")
        data
    } catch (e: Exception) {
        Log.e("ProveedorDebug", "Error al cargar proveedores", e)
        emptyList()
    }
}

// Función de utilidad para insertar proveedores
suspend fun agregarProveedorDB(nombre: String, telefono: String): Boolean {
    return try {
        val clienteSupabase = supabaseBillar()

        // Usar un Map en lugar de un objeto ModeloProveedor
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