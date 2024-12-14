package com.example.billarapp.domain


@kotlinx.serialization.Serializable
data class ProductoModel(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Double,
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