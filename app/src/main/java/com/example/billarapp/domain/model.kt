package com.example.billarapp.domain


@kotlinx.serialization.Serializable
data class ProductoModel(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Float,
    val Cantidad_Inv: Int?=0
)