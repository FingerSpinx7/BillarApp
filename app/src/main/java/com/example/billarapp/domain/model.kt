package com.example.billarapp.domain

@kotlinx.serialization.Serializable
data class ProductoModel(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Float,
    val Cantidad_Inv: Int?=0
)

@kotlinx.serialization.Serializable
data class CuentaModel(
    val id_billar: Int,
    val id_cuenta: Int,
    val Numero_mesa: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val cliente: String
)

@kotlinx.serialization.Serializable
data class DetalleProductosConsumidosModel(
    val descripcion: String,
    val cantidad: Int,
    val total: Double
)