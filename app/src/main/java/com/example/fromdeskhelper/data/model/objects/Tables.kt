package Data

import androidx.annotation.Nullable
import androidx.room.*
import com.google.android.gms.common.api.Api
import java.io.Serializable
import java.util.Date

@Entity(tableName = "productos", indices = arrayOf(Index(value = arrayOf("uid"), unique = true)))
data class Producto(
    val update: Date,
    val nombre: String,
    val precioC: Double,
    val precioU: Double,
    val marca: String?,
    val detalles: String?,
    val categoria: String?,
    val stockC: Int?,
    val stockU: Int?,
    val qr: String?,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
//    @ColumnInfo(name = "image",typeAffinity = ColumnInfo.BLOB)

) : Serializable


@Entity(
    tableName = "imagenes", foreignKeys = arrayOf(
        ForeignKey(
            entity = Producto::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("id_producto"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ImagenesNew(
    val updateImage: Date,
    @ColumnInfo(index = true)
    val id_producto: Int,
    @ColumnInfo(name = "imageBit", typeAffinity = ColumnInfo.BLOB)
    val imageBit: ByteArray,
    @PrimaryKey(autoGenerate = true)
    val idImagen: Int = 0,
) : Serializable

data class listInventarioProductos(
    val uid: Int,
    val nombre: String,
    val precioC: Double,
    val precioU: Double,
    val qr: String?,
    @ColumnInfo(name = "imageBit", typeAffinity = ColumnInfo.BLOB)
    val imageBit: ByteArray?,
)


data class InventarioProducts(
    @Embedded
    val producto: Producto,
    @Relation(parentColumn = "uid", entityColumn = "id_producto")
    val imageN: List<ImagenesNew>,
)

@Entity(tableName = "client", indices = arrayOf(Index(value = arrayOf("uid"), unique = true)))
data class ClientList(
    val fecha: Date,
    val name: String?=null,
    val telefone: String?=null,
    val correo: String?=null,
    val direction: String?=null,
    val color: Int,
    val number: Int,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
):Serializable

data class ClientListGet(
    val color: Int,
    val number: Int,
    val uid: Int ,
)

@Entity(
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

data class ClientNameProduct(
    @Embedded val ClientList: ClientList,
    @Relation(
        parentColumn = "productItemId",
        entityColumn = "products",
        associateBy = Junction(ClientAndProducts::class)
    )
    val products: List<Producto>
)
