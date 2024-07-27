package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConcexion {
    fun CadenaConexion(): Connection?{
        try{
            val ip="jdbc:oracle:thin:@192.168.1.22:1521:xe"
            val usuario= "system"
            val contrasena= "desarrollo"
            val conexion= DriverManager.getConnection(ip, usuario , contrasena)
            return conexion
        }catch (e:Exception){
            println("El error es este $e")
            return null
        }
    }
}