package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassMedicina
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent

        val txtNombresPaciente = findViewById<EditText>(R.id.txtNombresPaciente)
        val txtApellidos = findViewById<EditText>(R.id.txtApellidos)
        val txtedad = findViewById<EditText>(R.id.txtEdad)
        val txtFechaNacimiento = findViewById<EditText>(R.id.txtNacimiento)
        val txtEnfermedad = findViewById<EditText>(R.id.txtEnfermedad)
        val txtNumerohabitacion = findViewById<EditText>(R.id.txtHabitacion)
        val txtnumeroCama = findViewById<EditText>(R.id.txtCama)
        val spMedicamento = findViewById<Spinner>(R.id.spnMedicamento1)
        val txtAplicaion = findViewById<EditText>(R.id.txtHoraMed1)
        val txtMedicamentosExtas = findViewById<EditText>(R.id.txtMedicamentosExtras)
        val icRegresar = findViewById<ImageView>(R.id.icRegresar)
        val btnAgregarPacientes = findViewById<Button>(R.id.btnAgregarPaciente)
        val btnAgregarMedicamentos = findViewById<Button>(R.id.btnMedeicamento)

        txtFechaNacimiento.inputType = InputType.TYPE_NULL
        txtFechaNacimiento.setOnClickListener {
        }

       btnAgregarPacientes.setOnClickListener {
           if (txtNombresPaciente.text.toString().isEmpty() || txtApellidos.text.toString().isEmpty() || txtedad.text.toString().isEmpty() || txtFechaNacimiento.text.toString().isEmpty()|| txtEnfermedad.text.toString().isEmpty()||spMedicamento.isEmpty())
           {
               Toast.makeText(
                   this@MainActivity,
                   "Por favor, complete todos los campos.",
                   Toast.LENGTH_SHORT
               ).show()
           }else {
               GlobalScope.launch(Dispatchers.IO)
               {
                   try {
                       val objConexion = ClaseConcexion().CadenaConexion()
                       val medicina = getMedicina()

                       val addPaciente = objConexion?.prepareStatement("INSERT INTO pacientee (UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacio, medicamento_adiccional)VALUES (?,?,?,?,?,?,?,?,?,?,?)")!!
                       addPaciente.setString(1, UUID.randomUUID().toString())
                       addPaciente.setString(2,txtNombresPaciente.text.toString())
                       addPaciente.setString(3,txtApellidos.text.toString())
                       addPaciente.setInt(4,txtedad.text.toString().toInt())
                       addPaciente.setString(5,txtEnfermedad.text.toString())
                       addPaciente.setString(6,txtFechaNacimiento.text.toString())
                       addPaciente.setString(7,txtNumerohabitacion.text.toString())
                       addPaciente.setInt(8,txtnumeroCama.text.toString().toInt())
                       addPaciente.setString(9,medicina[spMedicamento.selectedItemPosition].UUID_Medicamento)
                       addPaciente.setString(10,txtAplicaion.text.toString())
                       addPaciente.setString(11,txtMedicamentosExtas.text.toString())

                       addPaciente.executeUpdate()
                       withContext(Dispatchers.Main) {
                           Toast.makeText(
                               this@MainActivity,
                               "Cita agendada exitosamente.",
                               Toast.LENGTH_SHORT
                           ).show()
                       }
                   }catch (e: Exception) {
                       withContext(Dispatchers.Main) {
                           Toast.makeText(this@MainActivity,
                               "Error al agendar la cita: ${e.message}",
                               Toast.LENGTH_LONG
                           ).show()
                       }
                   }
               }

           }

       }
    }
}

fun getMedicina(): List<dataClassMedicina>{
    val conexion = ClaseConcexion().CadenaConexion()
    val statement = conexion?.createStatement()
    val resultSet = statement?.executeQuery("SELECT * FROM Medicamentoo")!!

    val listaMedicina = mutableListOf<dataClassMedicina>()

    while (resultSet.next())
    {
        val uuid_Medicina =resultSet.getString("UUID_Medicamento")
        val nombreMedicina = resultSet.getString("Nombre_medicamento")

        val medicinaCompleta = dataClassMedicina(uuid_Medicina, nombreMedicina)

        listaMedicina.add(medicinaCompleta)
    }
    return listaMedicina
}

