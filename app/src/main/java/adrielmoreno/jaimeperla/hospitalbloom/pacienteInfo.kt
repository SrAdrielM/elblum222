package adrielmoreno.jaimeperla.hospitalbloom

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class pacienteInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente_info)

        val nombres = intent.getStringExtra("Nombres")
        val apellidos = intent.getStringExtra("Apellidos")
        val Edad = intent.getIntExtra("Edad",0)
        val Enfermedad = intent.getStringExtra("Efermedad")
        val FechaNacimiento = intent.getStringExtra("Fecha_Nacimiento")
        val numHabitacion = intent.getIntExtra("numero_habitacion",0)
        val numCama = intent.getIntExtra("numero_cama",0)
        val uuid_medicamento = intent.getStringExtra("UUID_Medicamento")
        val horaAplicacion = intent.getStringExtra("hora_aplicacion")
        val medicamentoExtra = intent.getStringExtra("medicamento_adiccional")


        val txtNombres =findViewById<TextView>(R.id.txtNombresShow)
        val txtApellidos = findViewById<TextView>(R.id.txtApellidosShow)
        val txtEdad = findViewById<TextView>(R.id.txtEdadShow)
        val txtEnfermedad = findViewById<TextView>(R.id.txtEnfermedadShow)
        val txtfechaNacimiento = findViewById<TextView>(R.id.txtNacimientoShow)
        val txtNumHabitacion =findViewById<TextView>(R.id.txtHabitacionShow)
        val txtNumCama = findViewById<TextView>(R.id.txtCamaShow)
        val txtmedicamentos = findViewById<TextView>(R.id.txtMedicamento1Show)
        val txtHoraAplicar = findViewById<TextView>(R.id.txtHoraMed1Show)
        val txtmedExtra = findViewById<TextView>(R.id.txtInfoExtraShow)
        val imgregresa = findViewById<ImageView>(R.id.imgRegresarAmain)
        imgregresa.setOnClickListener {
            val intent = Intent(this, ListaPacientes::class.java)
            startActivity(intent)
        }

        txtNombres.text=nombres
        txtApellidos.text=apellidos
        txtEdad.text=Edad.toString()
        txtEnfermedad.text=Enfermedad
        txtfechaNacimiento.text=FechaNacimiento
        txtNumHabitacion.text=numHabitacion.toString()
        txtNumCama.text=numCama.toString()
        txtmedicamentos.text=uuid_medicamento
        txtHoraAplicar.text=horaAplicacion
        txtmedExtra.text=medicamentoExtra


        }


    }
