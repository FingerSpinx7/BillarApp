package com.example.billarapp.domain

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.serialization.Contextual


@kotlinx.serialization.Serializable
data class ProductoModel(
    @kotlinx.serialization.SerialName("id_producto")
    val id_producto: Int,
    @kotlinx.serialization.SerialName("id_proveedor")
    val id_proveedor: Int,
    @kotlinx.serialization.SerialName("det_producto")
    val det_producto: String,
    @kotlinx.serialization.SerialName("precio")
    val precio: Double,
    @kotlinx.serialization.SerialName("Cantidad_Inv")
    val Cantidad_Inv: Int?=0
)


@kotlinx.serialization.Serializable
data class ProductoUpload(
    val id_proveedor: Int,
    val precio: Double,
    val det_producto: String,
    val Cantidad_Inv: Int
)

@kotlinx.serialization.Serializable
data class ProveedoresModel(
    val id_proveedor: Int,
    val telefono: Long,
    val nombre: String

)