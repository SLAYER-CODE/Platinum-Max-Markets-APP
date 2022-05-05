package Data

import androidx.annotation.Nullable
import androidx.room.*
import java.io.Serializable
import java.util.Date

@Entity(tableName = "productos",indices =  arrayOf(Index(value = arrayOf("uid"),unique = true)))
data class Producto(
    val update: Date,
    val nombre:String,
    val precioC:Double,
    val precioU:Double,
    val marca: String?,
    val detalles:String?,
    val categoria:String?,
    val stockC:Int?,
    val stockU:Int?,
    val qr:String?,
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
//    @ColumnInfo(name = "image",typeAffinity = ColumnInfo.BLOB)

):Serializable


@Entity(tableName = "imagenes",foreignKeys = arrayOf(
    ForeignKey(entity = Producto::class,parentColumns = arrayOf("uid"),childColumns = arrayOf("id_producto"),onDelete = ForeignKey.CASCADE)
))
data class ImagenesNew(
    val updateImage:Date,
    @ColumnInfo(index = true)
    val id_producto:Int,
    @ColumnInfo(name = "imageBit",typeAffinity = ColumnInfo.BLOB)
    val imageBit:ByteArray,
    @PrimaryKey(autoGenerate = true)
    val idImagen: Int=0,
):Serializable

data class listInventarioProductos(
    val uid:Int,
    val nombre:String,
    val precioC:Double,
    val precioU:Double,
    @ColumnInfo(name = "imageBit",typeAffinity = ColumnInfo.BLOB)
    val imageBit:ByteArray?,
    )

data class ClientList(
    val fecha:Date,
    val color:Int,
    val number:Int,
    @PrimaryKey(autoGenerate = true)
    val uid:Int=0,
)

data class InventarioProducts(
    @Embedded
    val producto    : Producto,
    @Relation(parentColumn = "uid", entityColumn = "id_producto")
    val imageN:List<ImagenesNew>,
)