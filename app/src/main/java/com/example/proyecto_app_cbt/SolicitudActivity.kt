package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class SolicitudActivity : AppCompatActivity() {

    private lateinit var fechaInicio: EditText
    private lateinit var fechaFin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitud)

        fechaInicio = findViewById(R.id.etFechaInicio)
        fechaFin = findViewById(R.id.etFechaFin)

        fechaInicio.setOnClickListener {
            mostrarCalendario(fechaInicio)
        }

        fechaFin.setOnClickListener {
            mostrarCalendario(fechaFin)
        }

        val tiposVacaciones = listOf(
            "Vacaciones regulares",
            "Adelanto de vacaciones",
            "Sin goce de haber",
            "Vacaciones proporcionales",
            "Permiso especial"
        )

        val spinner = findViewById<Spinner>(R.id.spTipoVacaciones)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposVacaciones)
        spinner.adapter = adapter

        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Solicitud enviada")
                .setMessage("Tu solicitud de vacaciones ha sido enviada satisfactoriamente.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()

                    finish()
                    startActivity(intent)
                }
                .show()
        }

    }

    private fun mostrarCalendario(campoDestino: EditText) {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            val fechaFormateada = "${d}/${m + 1}/$y"
            campoDestino.setText(fechaFormateada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }
}