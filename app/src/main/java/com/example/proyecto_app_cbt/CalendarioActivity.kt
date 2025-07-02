package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.SolicitudDAOFirestore
import com.example.proyecto_app_cbt.model.Solicitud
import com.example.proyecto_app_cbt.view.CalendarioPersonalizadoView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {
    private lateinit var calendarioPersonalizado: CalendarioPersonalizadoView
    private lateinit var chipGroupFiltros: ChipGroup
    private lateinit var chipTodos: Chip
    private lateinit var chipVacacionesRegulares: Chip
    private lateinit var chipPermisoEspecial: Chip
    private lateinit var chipSinGoceHaber: Chip
    private lateinit var chipAdelantoVacaciones: Chip
    private lateinit var chipVacacionesProporcionales: Chip
    private lateinit var fabAgregarSolicitud: FloatingActionButton

    private val solicitudDAO = SolicitudDAOFirestore()
    private val todasLasSolicitudes = mutableListOf<Solicitud>()
    private var filtroActivo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        inicializarVistas()
        configurarFiltros()
        configurarCalendario()
        cargarSolicitudesDesdeFirestore()
    }

    private fun inicializarVistas() {
        calendarioPersonalizado = findViewById(R.id.calendarioPersonalizado)
        chipGroupFiltros = findViewById(R.id.chipGroupFiltros)
        chipTodos = findViewById(R.id.chipTodos)
        chipVacacionesRegulares = findViewById(R.id.chipVacacionesRegulares)
        chipPermisoEspecial = findViewById(R.id.chipPermisoEspecial)
        chipAdelantoVacaciones = findViewById(R.id.chipAdelantoVacaciones)
        chipSinGoceHaber = findViewById(R.id.chipSinGoceHaber)
        chipVacacionesProporcionales = findViewById(R.id.chipVacacionesProporcionales)
        fabAgregarSolicitud = findViewById(R.id.fabAgregarSolicitud)

        fabAgregarSolicitud.setOnClickListener {
            mostrarDialogAgregarSolicitud()
        }
    }

    private fun configurarFiltros() {
        chipTodos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = null
                actualizarCalendario()
            }
        }

        chipVacacionesRegulares.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = "Vacaciones regulares"
                actualizarCalendario()
            }
        }

        chipPermisoEspecial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = "Permiso especial"
                actualizarCalendario()
            }
        }

        chipAdelantoVacaciones.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = "Adelanto de vacaciones"
                actualizarCalendario()
            }
        }

        chipSinGoceHaber.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = "Sin goce de haber"
                actualizarCalendario()
            }
        }

        chipVacacionesProporcionales.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = "Vacaciones proporcionales"
                actualizarCalendario()
            }
        }
    }

    private fun configurarCalendario() {
        calendarioPersonalizado.setOnDiaClickListener { fecha, solicitudes ->
            mostrarSolicitudesDia(fecha, solicitudes)
        }
    }

    private fun cargarSolicitudesDesdeFirestore() {
        lifecycleScope.launch {
            try {
                // Mostrar indicador de carga si tienes alguno
                val solicitudes = solicitudDAO.obtenerTodos()

                todasLasSolicitudes.clear()
                todasLasSolicitudes.addAll(solicitudes)

                actualizarCalendario()

                if (solicitudes.isEmpty()) {
                    Toast.makeText(this@CalendarioActivity, "No se encontraron solicitudes", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@CalendarioActivity, "Error al cargar solicitudes: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarCalendario() {
        val solicitudesFiltradas = if (filtroActivo != null) {
            todasLasSolicitudes.filter {
                it.motivo.lowercase().contains(filtroActivo!!.lowercase())
            }
        } else {
            todasLasSolicitudes
        }

        calendarioPersonalizado.setSolicitudes(solicitudesFiltradas)
    }

    private fun mostrarSolicitudesDia(fecha: String, solicitudes: List<Solicitud>) {
        if (solicitudes.isEmpty()) {
            Toast.makeText(this, "No hay solicitudes para esta fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        val fechaFormateada = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
            .format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fecha)!!)

        builder.setTitle("Solicitudes del $fechaFormateada")

        val mensaje = StringBuilder()
        solicitudes.forEachIndexed { index, solicitud ->
            mensaje.append("${index + 1}. ${solicitud.empleado}\n")
            mensaje.append("   Tipo: ${solicitud.motivo}\n")
            mensaje.append("   Estado: ${solicitud.estado}\n")
            mensaje.append("   Período: ${solicitud.getFechaInicioString()} al ${solicitud.getFechaFinString()}\n")
            if (solicitud.observaciones.isNotEmpty()) {
                mensaje.append("   Observaciones: ${solicitud.observaciones}\n")
            }
            mensaje.append("\n")
        }

        builder.setMessage(mensaje.toString())
            .setPositiveButton("Cerrar", null)
            .setNeutralButton("Ver Detalles") { _, _ ->
                mostrarDetallesCompletos(solicitudes)
            }
            .show()
    }

    private fun mostrarDetallesCompletos(solicitudes: List<Solicitud>) {
        // Aquí puedes implementar una vista más detallada
        // Por ejemplo, abrir una nueva actividad o mostrar un dialog más completo
        Toast.makeText(this, "Vista detallada - Por implementar", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogAgregarSolicitud() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Nueva Solicitud")
            .setMessage("¿Qué tipo de solicitud deseas agregar?")
            .setPositiveButton("Vacaciones") { _, _ ->
                // Aquí puedes abrir una actividad para crear solicitud de vacaciones
                // o implementar un dialog más completo
                Toast.makeText(this, "Crear solicitud de vacaciones - Por implementar", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Permiso") { _, _ ->
                Toast.makeText(this, "Crear solicitud de permiso - Por implementar", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Función para refrescar las solicitudes (puedes llamarla cuando sea necesario)
    fun refrescarSolicitudes() {
        cargarSolicitudesDesdeFirestore()
    }

    override fun onResume() {
        super.onResume()
        // Recargar solicitudes cuando la actividad vuelve a estar visible
        // por si se agregaron/modificaron solicitudes en otra pantalla
        refrescarSolicitudes()
    }
}