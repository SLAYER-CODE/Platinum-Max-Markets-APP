package com.example.fromdeskhelper.data.model.objects.mongod

import com.google.gson.annotations.SerializedName
import org.bson.Document

object Resolver {
}


data class ProductsLocalImput(
    @SerializedName("brands_products") public val brands_products: List<String>,
    @SerializedName("category_products")public val category_products: List<String>,
    @SerializedName("description")public val description: String?,
    @SerializedName("image_realation")public val image_realation: MutableList<Document>,
    @SerializedName("old_price")public val old_price: Double?,
    @SerializedName("price_cantidad")public val price_cantidad: Double?,
    @SerializedName("price_unity")public val price_unity: Double?,
    @SerializedName("product_name")public val product_name: String,
    @SerializedName("qr")public val qr: String?,
    @SerializedName("quantity_cantidad")public val quantity_cantidad: Int?,
    @SerializedName("quantity_unity")public val quantity_unity: Int?,
    @SerializedName("update_product")public val update_product: String?,
)


data class ProductsModelAdapter(
    @SerializedName("brands_products") val brands_products: List<String>,
    @SerializedName("category_products")  val category_products: List<String>,
    @SerializedName("description") val description: String?,
    @SerializedName("image_realation") val image_realation: MutableList<Pair<String,ByteArray>>,
    @SerializedName("old_price") val old_price: Double?,
    @SerializedName("price_cantidad")  val price_cantidad: Double?,
    @SerializedName("price_unity")  val price_unity: Double?,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("qr") val qr: String?,
    @SerializedName("quantity_cantidad") val quantity_cantidad: Int?,
    @SerializedName("quantity_unity") val quantity_unity: Int?,
    @SerializedName("update_product") val update_product: String?,
)