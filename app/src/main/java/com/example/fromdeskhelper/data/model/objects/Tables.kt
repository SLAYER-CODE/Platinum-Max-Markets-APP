package Data

import androidx.room.*
import java.io.Serializable
import java.util.Date

@Entity(tableName = "product", indices = arrayOf(Index(value = arrayOf("uid"), unique = true)))
data class Producto(
    val update: Date,
    val nombre: String,
    val precioC: Double,
    val precioNeto: Double?,
    val disconut: Double?,
//    val marca: String?,
//    val categoria: String?,
    val stockC: Int?,
    val stockC_attr: Int?,
    val stockU: Int?,
    val stockU_attr: Int?,
    val peso: Double?,
    val peso_attr: Int?,
    val stock_attr: Int?,
    val detalles: String?,

    val qr: String?,
    val available_shipment: Boolean?,
    val available_now: Boolean?,
    val available_date: Date?,

    val uniqueid: String,
    //Unique sistem
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    //Nuevos campos asignados
//    val
) : Serializable {

    @Ignore
    var ImageList: List<ImagenesProduct> = mutableListOf()

    @Ignore
    var CategorieList: List<Categories> = mutableListOf()

    @Ignore
    var BrandList: List<Brands> = mutableListOf()
}


@Entity(
    tableName = "productimage", foreignKeys = arrayOf(
        ForeignKey(
            entity = Producto::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("id_producto"),
            onDelete = ForeignKey.CASCADE
        )
    )
)

data class ImagenesProduct(
    val updateImage: Date,
    @ColumnInfo(index = true)
    var id_producto: Int,

    @ColumnInfo(name = "imageBit", typeAffinity = ColumnInfo.BLOB)
    val imageBit: ByteArray,
    @PrimaryKey(autoGenerate = true)
    val idImagen: Int = 0,
) : Serializable


@Entity(tableName = "brand", indices = arrayOf(Index(value = arrayOf("brand_id"), unique = true)))
data class Brands(
    val brand_name: String,
    @PrimaryKey(autoGenerate = true)
    val brand_id: Int = 0,
) : Serializable

@Entity(primaryKeys = ["brand_id", "uid"])
data class BrandProductRef(
    val brand_id: Long,
    val uid: Long
)

@Entity(
    tableName = "category",
    indices = arrayOf(Index(value = arrayOf("category_id"), unique = true))
)
data class Categories(
    val category_name: String,
    @PrimaryKey(autoGenerate = true)
    val category_id: Int = 0,
) : Serializable


@Entity(primaryKeys = ["category_id", "uid"])
data class CategoryProductRef(
    val category_id: Long,
    val uid: Long
)


//Creacion de una nueva tabla
data class InventarioProducts(
    @Embedded
    val producto: Producto,
    @Relation(parentColumn = "uid", entityColumn = "id_producto")
    val imageN: List<ImagenesProduct>,
)

data class listInventarioProductos(
    val update: Date,
    val nombre: String,
    val precioC: Double,
    val precioNeto: Double?,
    val disconut: Double?,
//    val marca: String?,
//    val categoria: String?,
    val stockC: Int?,
    val stockC_attr: Int?,
    val stockU: Int?,
    val stockU_attr: Int?,
    val peso: Double?,
    val peso_attr: Int?,
    val stock_attr: Int?,
    val detalles: String?,

    val qr: String?,
    val available_shipment: Boolean?,
    val available_now: Boolean?,
    val available_date: Date?,
    val uid: Int,
    @ColumnInfo(name = "imageBit", typeAffinity = ColumnInfo.BLOB)
    val imageBit: ByteArray?,
)



data class listInventarioProductosP2P(
    val uid: Int,
    val nombre: String,
    val precioC: Double?,
    val precioNeto: Double?,
    val qr: String?,
    @ColumnInfo(name = "imageBit", typeAffinity = ColumnInfo.BLOB)
    val imageBit: ByteArray?,
)

@Entity(tableName = "client", indices = arrayOf(Index(value = arrayOf("uid"), unique = true)))
data class ClientList(
    val fecha: Date,
    val name: String? = null,
    val telefone: String? = null,
    val correo: String? = null,
    val direction: String? = null,
    val color: Int,
    val number: Int,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
) : Serializable

data class ClientListGet(
    val color: Int,
    val number: Int,
    val uid: Int,
)

data class HomeListClient(
    val title: String,
    val info: String
)

data class ClientProductNew(
    val updateImage: Date,
    @ColumnInfo(index = true)
    val id_producto: Int,
    @PrimaryKey(autoGenerate = true)
    val idImagen: Int = 0,
) : Serializable

@Entity(
    tableName = "ClientProduct",
    primaryKeys = arrayOf("clientItemId", "productItemId"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ClientList::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("clientItemId")
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("productItemId")
        )
    )

)
data class ClientAndProducts(
    val clientItemId: Int,
    val productItemId: Int
)

data class ClientToProduct(
    @Embedded val ClientList: ClientList,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid",
        associateBy = Junction(
            ClientAndProducts::class,
            parentColumn = "clientItemId",
            entityColumn = "productItemId"
        )
    )
    val products: List<Producto>
)
