package com.example.fromdeskhelper.data.model.base

import Data.*
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*


@Dao
abstract class ProductoData {


    @Query("SELECT * FROM product")
    abstract fun getAll():LiveData<List<Producto>>

    @Query("SELECT * FROM product  ORDER BY `update` DESC")
    abstract fun getAllTime():LiveData<List<Producto>>

    @Query("SELECT * FROM product WHERE uid =:id")
    abstract fun get(id:Int):LiveData<Producto>

    @Query("SELECT `update` FROM product WHERE uid=:id")
    abstract fun getTime(id:Int):LiveData<Date>


    @Query("SELECT COUNT(*) FROM product")
    abstract fun getCount():LiveData<Int>


    @Transaction
    @Query("SELECT * FROM product  ORDER BY `update` ASC")
    abstract fun getAllInventario(): List<InventarioProducts>

    @Transaction
    @Query("SELECT * FROM product WHERE uid=:id")
    abstract fun getInventarioId(id:Int):LiveData<InventarioProducts>

    @Query("SELECT * FROM product  ORDER BY `update` DESC LIMIT :limit OFFSET :offset ")
    abstract fun getAllInventarioTime(limit:Int,offset:Int):LiveData<List<InventarioProducts>>

    @Transaction
    @Query("SELECT product.uid,product.nombre,product.precioC,product.precioNeto,productimage.imageBit,qr,product.`update`,product.precioNeto,product.disconut,product.stock_attr,product.stockC,product.stockC_attr,product.stockU,product.stockU_attr,product.available_shipment,product.available_now,product.available_date  FROM product LEFT JOIN productimage  on productimage.id_producto = product.uid group by product.uid ORDER BY `update` DESC LIMIT :limit OFFSET :offset  ")
    abstract fun getByInventarioProductos(limit:Int,offset:Int): LiveData<List<listInventarioProductos>>

//    @Transaction
//    @Query("SELECT * FROM ClientProduct LEFT JOIN client on  client.uid = clientItemId LEFT JOIN productos on productos.uid = productItemId WHERE clientItemId= :user ")
//    fun getByInventarioUser(user:Int): ClientToProduct

    @Transaction
    @Query("SELECT * FROM client WHERE client.uid = :user ")
    abstract fun getByInventarioUser(user:Int): ClientToProduct


    @Transaction
    @Query("SELECT product.uid,product.nombre,product.precioC,product.precioNeto,productimage.imageBit,qr,product.`update`,product.precioNeto,product.disconut,product.stock_attr,product.stockC,product.stockC_attr,product.stockU,product.stockU_attr,product.available_shipment,product.available_now,product.available_date  FROM product LEFT JOIN productimage  on productimage.id_producto = product.uid group by product.uid ORDER BY `update` DESC")
    abstract  fun getByInventarioProductosAll(): LiveData<List<listInventarioProductos>>

    @Transaction
    @Query("SELECT product.uid,product.nombre,product.precioC,product.precioNeto,productimage.imageBit,qr  FROM product LEFT JOIN productimage  on productimage.id_producto = product.uid group by product.uid ORDER BY `update` ")
    abstract  fun getByInventarioProductosAllP2p(): LiveData<List<listInventarioProductosP2P>>


    @Transaction
    @Query("DELETE FROM ClientProduct WHERE ClientProduct.clientItemId = :user AND ClientProduct.productItemId= :product ")
    abstract fun removeByInventarioUser(user:Int,product:Int)


    @Transaction
    @Query("SELECT client.number,client.uid,client.color  FROM client ORDER BY `fecha` ")
    abstract fun getClientList(): LiveData<List<ClientListGet>>




    @Transaction
    @Query("SELECT * FROM productimage WHERE id_producto = :id")
    abstract  fun getByImagenesId(id: Int): List<ImagenesProduct>

//    Insert items
    @Insert
    abstract fun insertClient(client:ClientList):Long

    @Insert
    abstract fun insertAll(producto: Producto):Long


//    @Insert
//    fun insertProductAll(producto: InventarioProducts):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllImages(vararg imagenNew: ImagenesProduct)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProductUser(vararg relation: ClientAndProducts)

    @Insert
    abstract   fun insertImageList(ImagenesProduct: List<ImagenesProduct?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBrand(card: Brands?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBrandWithProduct(cardTagCrossRef: BrandProductRef?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategory(tag: Categories?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategoryWithProduct(cardTagCrossRef: CategoryProductRef?): Long
    fun insertProductAndImages(product:Producto):Int {
        var id = insertAll(product)
        val img: List<ImagenesProduct> = product.ImageList
        val categorie: List<Categories> = product.CategorieList
        val brand : List<Brands> = product.BrandList

        for (i in img.indices) {
//            Log.i("ROOM",.toString())
            img[i].id_producto = id.toInt()
        }

        for (i in categorie.indices){
            var categorie_id = insertCategory(categorie[i])
            insertCategoryWithProduct(CategoryProductRef(categorie_id,id))
        }

        for (i in brand.indices){
            var brand_id = insertBrand(brand[i])
            insertBrandWithProduct(BrandProductRef(brand_id,id))
        }

        insertImageList(img)
        return 1
    }

    @Update
    abstract fun update(producto: Producto)
    @Delete
    abstract fun delete(producto: Producto)
    @Delete
    abstract fun deleteImagen(NewImage: ImagenesProduct)
}