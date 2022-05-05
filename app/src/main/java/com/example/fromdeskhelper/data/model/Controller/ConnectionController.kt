package com.example.fromdeskhelper.data.model.Controller

import Data.Producto
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import com.example.fromdeskhelper.data.model.CarritoUserProductos
import com.example.fromdeskhelper.data.model.Productos
import com.example.fromdeskhelper.data.model.User
//import com.example.finalproductos.data.model.CarritoUserProductos
//import com.example.finalproductos.data.model.Productos
//import com.example.finalproductos.data.model.User

import com.example.fromdeskhelper.ui.View.activity.MainActivity
import java.sql.*
import java.sql.Connection
import java.util.Date

class ConnectionController {
    var conexion : Connection?=null;
    fun verifysession(cox:Connection?,user:String,baseContext: Context): HashMap<Intent, String>{
        val newComprobation : CallableStatement = cox!!.prepareCall("{? = call dbo.InitComprobationAcces(?)}")
        newComprobation.setString(2,user)
//                    println(tonkentask.result.token.toString().split("."))
//                    println(tonkentask.result.signInProvider)
//                    println(tonkentask.result.signInSecondFactor)
        newComprobation.registerOutParameter(1, Types.INTEGER)
        newComprobation.execute()
        val new = newComprobation.getInt(1)
        if(new == 1){
            //return hashMapOf<Intent,String>(Intent(baseContext,MainCliente::class.java) to "Bienvenido!")
            return hashMapOf<Intent,String>(Intent(baseContext,MainActivity::class.java) to "Bienvenido!")
        }else if(new==2){
            //return hashMapOf<Intent,String>(Intent(baseContext,VendedorActivity::class.java) to "")
            return hashMapOf<Intent,String>(Intent(baseContext,MainActivity::class.java) to "")
        }else{
            return hashMapOf<Intent,String>(Intent(baseContext,MainActivity::class.java) to "Inicie session nuevamente!")
        }
    }

    fun activity(baseContext: Context):Intent{
        return Intent(baseContext,MainActivity::class.java)
    }

    fun productos(cox:Connection?):List<Producto>{
        val proc: ArrayList<Producto> = ArrayList()
        try {
            val stat: Statement = cox!!.createStatement()
            val rs: ResultSet = stat.executeQuery("SELECT * FROM Productos")

            while (rs.next()) {
                var image= rs.getBytes("imagen")
                var secondimage = rs.getString("imagenSecond")
                var id = rs.getInt("idProducto")
                var precio =  rs.getInt("precio").toDouble()
                var nombre = rs.getString("nombre")
                var descripcion = rs.getString("descripcion")
                println("$id $precio $nombre $descripcion")
                if(image == null){
                    image = byteArrayOf()
                }
                if(secondimage == null){
                    secondimage = ""
                }

                proc.add(
                    Producto(
                        uid = id,
                        nombre = nombre,
                        precioC = precio,
                        detalles = descripcion,
                        categoria = null,
                        marca = null,
                        precioU = precio.toDouble(),
                        qr = null,
                        stockC = null,
                        stockU = null,
                        update = Date()
//                        image,
//                        secondimage
                    )
                )
            }
        }catch (e:SQLException){
            println("Ubo un error "+e.errorCode+e.toString())
        }
        return proc;
    }
    fun tokenIdUser(cox: Connection?,token: String):User{
        val callP:PreparedStatement = cox!!.prepareCall("{call getTokenUserid(?)}")
        callP.setString(1,token)
        val rs:ResultSet = callP.executeQuery()
        rs.next()
        var dni = rs.getString("dni")
        var tarjeta = rs.getString("tarjeta")
        if(tarjeta==null){
            tarjeta=""
        }
        if(dni == null){
            dni = ""
        }

        val User = User(rs.getInt("id"),rs.getString("token"),rs.getDate("uenter"),tarjeta,rs.getInt("typeUser"),dni)
        return User
    }



    fun addCarritoUser(cox:Connection?,token: String,idproducto:Int){
        val statement:PreparedStatement = cox!!.prepareStatement("INSERT INTO Carritos VALUES(?,?,?)")
        statement.setInt(1,idproducto)
        statement.setDate(2,java.sql.Date.valueOf(java.time.LocalDate.now().toString()))
        statement.setInt(3,tokenIdUser(cox,token).id)
        statement.executeUpdate()
    }

    fun insertProducto(cox:Connection?,precio:Int,nombre:String,descripcion:String,imagen:ByteArray?,imagenScond:String?){
        val statement:PreparedStatement = cox!!.prepareStatement("INSERT INTO Productos VALUES(?,?,?,?,?)")
        statement.setInt(1,precio)
        statement.setString(2,nombre)
        statement.setString(3,descripcion)
        if(imagen==null){
            statement.setNull(4,Types.NULL)
        }else {
            statement.setBytes(4, imagen)
        }
        if(imagenScond == null) {
            statement.setNull(5,Types.NULL)
        }else{
            statement.setString(5, imagenScond)
        }
        statement.executeUpdate()
    }

    fun deleteCarritoUser(cox:Connection?,token:String,idproducto: Int){
        val statement:PreparedStatement = cox!!.prepareStatement("DELETE FROM Carritos WHERE Carritos.idCProducto = ?")
        statement.setInt(1,idproducto)
        statement.executeUpdate()
    }

    fun carritoUser(cox:Connection?,token:String):List<CarritoUserProductos>{
        val cup:ArrayList<CarritoUserProductos> = ArrayList()
        try{
            val callP:PreparedStatement = cox!!.prepareCall("{call getUserCarrito(?)}")
            callP.setString(1,token)
            val rs:ResultSet = callP.executeQuery()
            while (rs.next()){
                var image= rs.getBytes("imagen")
                var secondimage = rs.getString("imagenSecond")
                var id = rs.getInt("idProducto")
                var precio =  rs.getInt("precio")
                var nombre = rs.getString("nombre")
                var descripcion = rs.getString("descripcion")
                if(image == null){
                    image = byteArrayOf()
                }
                if(secondimage == null){
                    secondimage = ""
                }

                var tarjeta = rs.getString("tarjeta")
                if(tarjeta == null){
                    tarjeta = ""
                }
                var dni = rs.getString("dni")
                if(dni == null){
                    dni=""
                }
                cup.add(CarritoUserProductos(rs.getInt("idCProducto"),rs.getDate("fecha"),
                    User(rs.getInt("myuser"),rs.getString("token"),rs.getDate("uenter"),tarjeta,rs.getInt("typeUser"),dni),
                    Productos(
                        id,
                        precio,
                        nombre,
                        descripcion,
                        image,
                        secondimage
                    )
                ))
            }
        }catch (e:SQLException){
            println("Ubo un error "+e.errorCode+e.toString())
        }
        return cup
    }

    fun Conn(): Connection?{
        try{
            val  policy : StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)


            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
//            conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.13:1433;instance=MYINSTACIA;databaseName=Cafeteria:user=slayer;password=tiopazhc;integratedSecurity=true")
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.13:3000;instance=MYINSTACIA;databaseName=Cafeteria;integratedSecurity=true","slayer","tiopazhc")

        }catch (e:Exception){
            println(e.toString())
//        Toast.makeText(baseContext,"Ubo un error de conexion a la base de datos ", Toast.LENGTH_SHORT).show()
        }
        return conexion

//        var conn:Connection?=null
//        val connectionProps = Properties()
//        connectionProps.put("user", "slayer")
//        connectionProps.put("password", "tiopazhc")
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance()
//            conn = DriverManager.getConnection(
//                "jdbc:" + "mysql" + "://" +
//                        "127.0.0.1" +
//                        ":" + "3306" + "/" +
//       } catch (ex: SQLException) {
////            // handle any errors
////            ex.printStackTrace()
////        } catch (ex: Exception) {
////            // handle any errors
////                           "",
//                connectionProps)
//          ex.printStackTrace()
//        }

    }
}