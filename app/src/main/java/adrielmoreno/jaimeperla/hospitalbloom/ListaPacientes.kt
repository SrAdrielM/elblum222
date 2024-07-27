package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassCombinados
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

    private fun obtenerPacientes(): List<dataClassCombinados> {
        val objConexion = ClaseConcexion().CadenaConexion() ?: return emptyList()
        val listaPacientes = mutableListOf<dataClassCombinados>()

        try {
            val statement = objConexion.createStatement()
            val resultSet = statement.executeQuery("""
            SELECT 
                p.UUID_Paciente,
                p.Nombres,
                p.Apellidos,
                p.Edad,
                p.Efermedad,
                p.Fecha_Nacimiento,
                p.numero_habitacion,
                p.numero_cama,
                m.Nombre_medicamento,
                p.hora_aplicacion,
                p.medicamento_adiccional
            FROM 
                paciente p
            INNER JOIN 
                Medicamento m
            ON 
                p.UUID_Medicamento = m.UUID_Medicamento
        """)

            while (resultSet.next()) {
                val uuidM = resultSet.getString("UUID_Medicamento")
                val nombreMedicamento = resultSet.getString("Nombre_medicamento")
                val uuidP = resultSet.getString("UUID_Paciente")
                val nombres = resultSet.getString("Nombres")
                val apellidos = resultSet.getString("Apellidos")
                val edad = resultSet.getInt("Edad")
                val enfermedad = resultSet.getString("Efermedad")
                val nacimiento = resultSet.getString("Fecha_Nacimiento")
                val habitacion = resultSet.getInt("numero_habitacion")
                val cama = resultSet.getInt("numero_cama")
                val horaAplicacion = resultSet.getString("hora_aplicacion")
                val medicamentoExtra = resultSet.getString("medicamento_adiccional")

                val pacienteConMedicamento = dataClassCombinados(uuidM, nombreMedicamento, uuidP, nombres,
                    apellidos, edad, enfermedad, nacimiento, habitacion, cama, horaAplicacion,
                    medicamentoExtra)

                listaPacientes.add(pacienteConMedicamento)
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

