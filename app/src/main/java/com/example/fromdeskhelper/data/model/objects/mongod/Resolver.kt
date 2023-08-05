package com.example.fromdeskhelper.data.model.objects.mongod

import bolts.Bolts
import com.example.fromdeskhelper.type.BrandsInput
import com.example.fromdeskhelper.type.CategoriesInput
import com.google.gson.annotations.SerializedName
import org.bson.Document

object Resolver {
}


data class ProductsLocalImput(

    @SerializedName("product_name") val product_name: String,
    @SerializedName("price")  val price: Double,
    @SerializedName("price_neto") val price_neto: Double?,
    @SerializedName("discount")  val discount: Double?,
    @SerializedName("quantity_cantidad") val quantity_cantidad: Int?,
    @SerializedName("stockC_attr") val stockC_attr: Int?,
    @SerializedName("quantity_unity") val quantity_unity: Int?,
    @SerializedName("stockU_attr") val stockU_attr: Int?,
    @SerializedName("peso") val peso: Double?,
    @SerializedName("peso_attr") val peso_attr: Int?,
    @SerializedName("stock_attr") val stock_attr: Int?,
    @SerializedName("qr") val qr: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("available_date") val available_date: String?,
    @SerializedName("available_now") val available_now: Boolean?,
    @SerializedName("available_shipment") val available_shipment: Boolean?,
    @SerializedName("update_product") val update_product: String?,
    @SerializedName("uniqueid") val unique: String,
    @SerializedName("brands_products") public val brands_products: MutableList<BrandsInput>,
    @SerializedName("category_products")public val category_products: MutableList<CategoriesInput>,
    @SerializedName("image_realation")public val image_realation: MutableList<Document>,
)


data class ProductsModelAdapter(
    @SerializedName("product_name") val product_name: String,
    @SerializedName("price")  val price: Double,
    @SerializedName("price_neto") val price_neto: Double?,
    @SerializedName("discount")  val discount: Double?,


    @SerializedName("quantity_cantidad") val quantity_cantidad: Int?,

    @SerializedName("stockC_attr") val stockC_attr: Int?,

    @SerializedName("quantity_unity") val quantity_unity: Int?,

    @SerializedName("stockU_attr") val stockU_attr: Int?,

    @SerializedName("peso") val peso: Double?,
    @SerializedName("peso_attr") val peso_attr: Int?,

    @SerializedName("stock_attr") val stock_attr: Int?,

    @SerializedName("qr") val qr: String?,
    @SerializedName("description") val description: String?,

    @SerializedName("available_date") val available_date: String?,
    @SerializedName("available_now") val available_now: Boolean?,
    @SerializedName("available_shipment") val available_shipment: Boolean?,
    @SerializedName("update_product") val update_product: String?,

    @SerializedName("uniqueid") val unique: String,

    @SerializedName("brands_products") public val brands_products: MutableList<BrandsInput>,
    @SerializedName("category_products")public val category_products: MutableList<CategoriesInput>,
    @SerializedName("image_realation") val image_realation: MutableList<Pair<String,ByteArray>>,

    )