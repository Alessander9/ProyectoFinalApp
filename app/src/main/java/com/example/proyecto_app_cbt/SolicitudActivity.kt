package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.SolicitudDAOFirestore
import com.example.proyecto_app_cbt.model.Solicitud
import com.example.proyecto_app_cbt.databinding.ActivitySolicitudBinding
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class SolicitudActivity : BaseActivity() {

    private lateinit var binding: ActivitySolicitudBinding
    private val dao = SolicitudDAOFirestore()
    private val userId = "1"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolicitudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etFechaInicio.setOnClickListener { mostrarCalendario(binding.etFechaInicio) }
        binding.etFechaFin.setOnClickListener   { mostrarCalendario(binding.etFechaFin)   }

        val tiposVacaciones = listOf(
            "Vacaciones regulares",
            "Adelanto de vacaciones",
            "Sin goce de haber",
            "Vacaciones proporcionales",
            "Permiso especial"
        )
        binding.spTipoVacaciones.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tiposVacaciones
        )

        binding.btnEnviar.setOnClickListener {
            val inicioTxt = binding.etFechaInicio.text.toString()
            val finTxt    = binding.etFechaFin.text.toString()
            if (inicioTxt.isBlank() || finTxt.isBlank()) {
                AlertDialog.Builder(this)
                    .setTitle("Campos faltantes")
                    .setMessage("Por favor, selecciona las fechas de inicio y fin.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            val inicio = LocalDate.parse(inicioTxt.split("/").let {
                "${it[2]}-${it[1].padStart(2,'0')}-${it[0].padStart(2,'0')}"
            })
            val fin = LocalDate.parse(finTxt.split("/").let {
                "${it[2]}-${it[1].padStart(2,'0')}-${it[0].padStart(2,'0')}"
            })
            val fechaInicioTs = inicio.toTimestamp()
            val fechaFinTs    = fin.toTimestamp()
            val hoyTs         = LocalDate.now().toTimestamp()

            val tipo     = binding.spTipoVacaciones.selectedItem as String
            val motivo   = binding.etMotivo.text.toString().trim()
            val obs      = binding.etObservaciones.text.toString().trim()

            val nueva = Solicitud(
                id_usuario  = userId,
                fecha_inicio= fechaInicioTs,
                fecha_fin   = fechaFinTs,
                motivo      = "$tipo\n$motivo",
                estado      = "Pendiente",
                observaciones= obs,
                fecha_crea  = hoyTs,
                fecha_edita = hoyTs,
                revisado_por= ""
            )

            lifecycleScope.launch {
                val id = dao.insertar(nueva)
                if (id != null) {
                    AlertDialog.Builder(this@SolicitudActivity)
                        .setTitle("Solicitud enviada")
                        .setMessage("Tu solicitud ha sido guardada y enviada satisfactoriamente.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            setResult(RESULT_OK)
                            finish()
                        }
                        .show()
                } else {
                    AlertDialog.Builder(this@SolicitudActivity)
                        .setTitle("Error")
                        .setMessage("Ocurrió un error al guardar la solicitud.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private fun mostrarCalendario(campo: android.widget.EditText) {
        val c = Calendar.getInstance()
        DatePickerDialog(this,
            { _, y, m, d ->
                campo.setText("%02d/%02d/%04d".format(d, m + 1, y))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun LocalDate.toTimestamp(): Timestamp {
        val instant = this.atStartOfDay(ZoneId.systemDefault()).toInstant()
        return Timestamp(Date.from(instant))
    }
}