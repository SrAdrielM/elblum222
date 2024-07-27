package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import RecyclerViewHelpers.Adaptador
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException

class ListaPacientes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_pacientes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarPaciente = findViewById<Button>(R.id.btnAgregar)
        val rcvListaPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        rcvListaPacientes.layoutManager = LinearLayoutManager(this)


        CoroutineScope(Dispatchers.IO).launch {
            val pacientesRCV = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(pacientesRCV)
                rcvListaPacientes.adapter = adapter
            }
        }


        btnAgregarPaciente.setOnClickListener {
            // Lógica para agregar un paciente
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        cargarPacientes()
    }

    private fun cargarPacientes() {
        CoroutineScope(Dispatchers.IO).launch {
            val pacientesList = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adaptador = Adaptador(pacientesList)
                val rcvPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)
                rcvPacientes.adapter = adaptador
            }
        }
    }

    private fun obtenerPacientes(): List<dataClassPaciente> {
        val objConexion = ClaseConcexion().CadenaConexion() ?: return emptyList()
        val listaPacientes = mutableListOf<dataClassPaciente>()

        try {
            val statement = objConexion.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM paciente")

            while (resultSet.next()) {
                val uuid = resultSet.getString("UUID_Paciente")
                val nombres = resultSet.getString("Nombres")
                val apellidos = resultSet.getString("Apellidos")
                val edad = resultSet.getInt("Edad")
                val enfermedad = resultSet.getString("Efermedad")
                val nacimiento = resultSet.getString("Fecha_Nacimiento")
                val habitacion = resultSet.getInt("numero_habitacion")
                val cama = resultSet.getInt("numero_cama")
                val medicamento = resultSet.getString("UUID_Medicamento")
                val horaaplicacion = resultSet.getString("hora_aplicacion")
                val medicamentoextra = resultSet.getString("medicamento_adiccional")

                val paciente = dataClassPaciente(uuid, nombres, apellidos, edad, enfermedad, nacimiento, habitacion, cama, medicamento, horaaplicacion, medicamentoextra)

                listaPacientes.add(paciente)
            }
            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            Log.e("obtenerPacientes", "Error al obtener pacientes: ${e.message}")
        } finally {
            try {
                objConexion.close()
            } catch (e: SQLException) {
                Log.e("obtenerPacientes", "Error al cerrar la conexión: ${e.message}")
            }
        }

        return listaPacientes
    }
}


    private fun obtenerPacientes(): List<dataClassPaciente> {
        val objconexion = ClaseConcexion().CadenaConexion()
        val statement = objconexion?.createStatement()
        val resultSet = statement?.executeQuery(
            "SELECT paciente.UUID_Paciente,paciente.Nombres, paciente.Apellidos, paciente.edad ,paciente.Efermedad, paciente.Fecha_Nacimiento, paciente.numero_habitacion, paciente.numero_cama, Medicamento.Nombre_medicamento, paciente.hora_aplicacion, paciente.medicamento_adiccional FROM paciente INNER JOIN Medicamento ON paciente.UUID_Medicamento = Medicamento.UUID_Medicamento;")!!

        val listadoPacientes = mutableListOf<dataClassPaciente>()

        resultSet.use { rs ->
            while (rs.next()) {
                val uuid = rs.getString("UUID_Paciente")
                val nombres = rs.getString("Nombres")
                val apellidos = rs.getString("Apellidos")
                val edad = rs.getInt("Edad")
                val enfermedad = rs.getString("Efermedad")
                val nacimiento = rs.getString("Fecha_Nacimiento")
                val habitacion = rs.getInt("numero_habitacion")
                val cama = rs.getInt("numero_cama")
                val medicamento = rs.getString("Nombre_medicamento")
                val horaaplicacion = rs.getString("hora_aplicacion")
                val medicamentoextra = rs.getString("medicamento_adiccional")

                val paciente = dataClassPaciente(
                    uuid, nombres, apellidos, edad, enfermedad, nacimiento, habitacion, cama,
                    medicamento, horaaplicacion, medicamentoextra
                )

                listadoPacientes.add(paciente)
            }
        }

        return listadoPacientes
    }


