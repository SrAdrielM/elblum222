package adrielmoreno.jaimeperla.hospitalbloom

import Modelo.ClaseConcexion
import Modelo.dataClassMedicina
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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



        GlobalScope.launch(Dispatchers.IO) {
            val listadoMedicina = Medicina()
            val nombreMedicina = listadoMedicina.map { it.Nombre_medicamento }
            withContext(Dispatchers.Main) {
                val medicinaAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, nombreMedicina)
                spMedicamento.adapter = medicinaAdapter
            }
        }

        txtFechaNacimiento.inputType = InputType.TYPE_NULL
        txtFechaNacimiento.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            // Fecha mínima (16 años atrás desde hoy)
            val fechaMinima = Calendar.getInstance()
            fechaMinima.set(anio - 16, mes, dia)

            // Fecha máxima (hoy)
            val fechaMaxima = Calendar.getInstance()
            fechaMaxima.set(anio, mes, dia)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaNacimiento.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )

            // Establecer los límites del DatePicker
            datePickerDialog.datePicker.minDate = fechaMinima.timeInMillis
            datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

            datePickerDialog.show()
        }

        icRegresar.setOnClickListener {
            val intent = Intent(this, ListaPacientes::class.java)
            startActivity(intent)
        }



        btnAgregarPacientes.setOnClickListener {
            if (txtNombresPaciente.text.toString().isEmpty() || txtApellidos.text.toString()
                    .isEmpty() || txtedad.text.toString()
                    .isEmpty() || txtFechaNacimiento.text.toString()
                    .isEmpty() || txtEnfermedad.text.toString().isEmpty() || spMedicamento.isEmpty()
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val objConexion = ClaseConcexion().CadenaConexion()
                        val medicina = Medicina()

                        val addPaciente = objConexion?.prepareStatement("INSERT INTO paciente (UUID_Paciente, Nombres, Apellidos, Edad, Efermedad, Fecha_Nacimiento, numero_habitacion, numero_cama, UUID_Medicamento, hora_aplicacion, medicamento_adiccional) VALUES (?,?,?,?,?,?,?,?,?,?,?)")!!
                        addPaciente.setString(1, UUID.randomUUID().toString())
                        addPaciente.setString(2, txtNombresPaciente.text.toString())
                        addPaciente.setString(3, txtApellidos.text.toString())
                        addPaciente.setInt(4, txtedad.text.toString().toInt())
                        addPaciente.setString(5, txtEnfermedad.text.toString())
                        addPaciente.setString(6, txtFechaNacimiento.text.toString())
                        addPaciente.setInt(7, txtNumerohabitacion.text.toString().toInt())
                        addPaciente.setInt(8, txtnumeroCama.text.toString().toInt())
                        addPaciente.setString(9, medicina[spMedicamento.selectedItemPosition].UUID_Medicamento)
                        addPaciente.setString(10, txtAplicaion.text.toString())
                        addPaciente.setString(11, txtMedicamentosExtas.text.toString())

                        addPaciente.executeUpdate()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Paciente creado exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Error al crear el Paciente: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        btnAgregarMedicamentos.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Ingrese el nombre de la medicina")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val txtinputMedicina = EditText(this)
            txtinputMedicina.hint = "Nombre de la medicina"
            layout.addView(txtinputMedicina)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val nombreMedicina = txtinputMedicina.text.toString().trim()
                if (nombreMedicina.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val objConexion = ClaseConcexion().CadenaConexion()
                            val addMedicamento = objConexion?.prepareStatement("INSERT INTO Medicamento (UUID_Medicamento, Nombre_medicamento) VALUES (?,?)")!!
                            addMedicamento.setString(1, UUID.randomUUID().toString())
                            addMedicamento.setString(2, nombreMedicina)
                            addMedicamento.executeUpdate()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Medicina ingresada: $nombreMedicina", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Error al ingresar la medicina: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Complete el campo", Toast.LENGTH_SHORT).show()
                }
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun Medicina(): List<dataClassMedicina> {
        val conexion = ClaseConcexion().CadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Medicamento")!!

        val listaMedicina = mutableListOf<dataClassMedicina>()

        while (resultSet.next()) {
            val uuid_Medicina = resultSet.getString("UUID_Medicamento")
            val nombreMedicina = resultSet.getString("Nombre_medicamento")

            val medicinaCompleta = dataClassMedicina(uuid_Medicina, nombreMedicina)

            listaMedicina.add(medicinaCompleta)
        }
        return listaMedicina
    }
}