package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassPaciente
import RecyclerViewHelpers.AdaptadorPacientes
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
            val pacientesDB = obtenerPacientesCard()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorPacientes(pacientesDB)
                rcvListaPacientes.adapter = adapter
            }
        }

        btnAgregarPaciente.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtenerPacientesCard(): List<dataClassPaciente> {
        val objconexion = ClaseConcexion().CadenaConexion()
        val statement = objconexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT paciente.UUID_Paciente,paciente.Nombres, paciente.Apellidos, paciente.edad ,paciente.Efermedad, paciente.Fecha_Nacimiento, paciente.numero_habitacion, paciente.numero_cama, Medicamento.Nombre_medicamento, paciente.hora_aplicacion, paciente.medicamento_adiccional FROM paciente INNER JOIN Medicamento ON paciente.UUID_Medicamento = Medicamento.UUID_Medicamento")!!

        val listadoPacientes = mutableListOf<dataClassPaciente>()

        while (resultSet.next()) {
            try {
                val uuid = resultSet.getString("UUID_Paciente")
                val nombres = resultSet.getString("Nombres")
                val apellidos = resultSet.getString("Apellidos")
                val edad = resultSet.getInt("Edad")
                val enfermedad = resultSet.getString("Efermedad")
                val nacimiento = resultSet.getString("Fecha_Nacimiento")
                val habitacion = resultSet.getInt("numero_habitacion")
                val cama = resultSet.getInt("numero_cama")
                val medicamento = resultSet.getString("Nombre_medicamento")
                val horaaplicacion = resultSet.getString("hora_aplicacion")
                val medicamentoextra = resultSet.getString("medicamento_adiccional")

                val paciente = dataClassPaciente(
                    uuid,
                    nombres,
                    apellidos,
                    edad,
                    nacimiento,
                    enfermedad,
                    habitacion,
                    cama,
                    medicamento,
                    horaaplicacion,
                    medicamentoextra
                )
                listadoPacientes.add(paciente)
            } catch (e: Exception) {
                Log.e("Paciente", "Error fetching paciente data", e)
            }
        }
        return listadoPacientes
    }
}