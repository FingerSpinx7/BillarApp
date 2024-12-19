package com.example.billarapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
):Parcelable


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

@kotlinx.serialization.Serializable
data class CuentaModelL(
    @SerialName("id_billar")
    val id_billar: Int,
    @SerialName("Numero_de_mesa")
    val Numero_de_mesa: Int,
    @SerialName("tipo_mesa")
    val tipo_mesa: String

)

@kotlinx.serialization.Serializable
data class CuentaModelUpload(
    @SerialName("id_billar")
    val id_billar: Int,
    @SerialName("cliente")
    val cliente: String,
    @SerialName("Numero_mesa")
    val Numero_mesa: Int


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