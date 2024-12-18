package com.example.billarapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class ProductoModel(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Float,
    val Cantidad_Inv: Int?=0
)

@kotlinx.serialization.Serializable
data class ModeloProveedor(
    @kotlinx.serialization.SerialName("id_proveedor")
    val id_proveedor: Int = 0,
    @kotlinx.serialization.SerialName("nombre")
    val nombre: String,
    @kotlinx.serialization.SerialName("telefono")
    val telefono: String
)

@Serializable
data class HistorialModel(
    @SerialName("id_cuenta")
    val id_cuenta: Long,
    @SerialName("cliente")
    val cliente: String,
    @SerialName("fecha_inicio")
    val fecha_inicio: String,
    @SerialName("fecha_fin")
    val fecha_fin: String?,
    @SerialName("productos")
    val productos: List<ProductoConsumido>? = null,
    @SerialName("total")
    val total: Double = 0.0
)

@Serializable
data class ProductoConsumido(
    @SerialName("id_producto")
    val id_producto: Int,
    @SerialName("cantidad")
    val cantidad: Int
)