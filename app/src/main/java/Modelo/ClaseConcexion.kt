package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConcexion {
    fun CadenaConexion(): Connection?{
        try{
            val ip="jdbc:oracle:thin:@192.168.3.4:1521:xe"
            val usuario= "lacobraa"
            val contrasena= "qatar24"
            val conexion= DriverManager.getConnection(ip, usuario , contrasena)
            return conexion
        }catch (e:Exception){
            println("El error es este $e")
            return null
        }
    }
}