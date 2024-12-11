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
import kotlinx.coroutines.launch





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
