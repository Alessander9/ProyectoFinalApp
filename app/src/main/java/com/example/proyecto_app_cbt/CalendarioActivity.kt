package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.CalendarioPersonalizadoView
import com.example.proyecto_app_cbt.model.Solicitud
import com.example.proyecto_app_cbt.model.TipoSolicitud
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarioActivity : AppCompatActivity() {
    private lateinit var calendarioPersonalizado: CalendarioPersonalizadoView
    private lateinit var chipGroupFiltros: ChipGroup
    private lateinit var chipTodos: Chip
    private lateinit var chipVacaciones: Chip
    private lateinit var chipPermisos: Chip
    private lateinit var fabAgregarSolicitud: FloatingActionButton

    private val todasLasSolicitudes = mutableListOf<Solicitud>()
    private var filtroActivo: TipoSolicitud? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        inicializarVistas()
        configurarFiltros()
        configurarCalendario()
        cargarDatosEjemplo()
        actualizarCalendario()
    }

    private fun inicializarVistas() {
        calendarioPersonalizado = findViewById(R.id.calendarioPersonalizado)
        chipGroupFiltros = findViewById(R.id.chipGroupFiltros)
        chipTodos = findViewById(R.id.chipTodos)
        chipVacaciones = findViewById(R.id.chipVacaciones)
        chipPermisos = findViewById(R.id.chipPermisos)
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

        chipVacaciones.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = TipoSolicitud.VACACIONES
                actualizarCalendario()
            }
        }

        chipPermisos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActivo = TipoSolicitud.PERMISO
                actualizarCalendario()
            }
        }
    }

    private fun configurarCalendario() {
        calendarioPersonalizado.setOnDiaClickListener { fecha, solicitudes ->
            mostrarSolicitudesDia(fecha, solicitudes)
        }
    }

    private fun cargarDatosEjemplo() {
        todasLasSolicitudes.addAll(listOf(
            Solicitud(
                id = 1,
                id_usuario = 101,
                fecha_inicio = LocalDate.of(2025, 6, 25),
                fecha_fin = LocalDate.of(2025, 7, 25),
                motivo = "Vacaciones",
                estado = "Aprobada",
                observaciones = "Período de vacaciones anuales",
                fecha_crea = LocalDate.now(),
                fecha_edita = LocalDate.now(),
                revisado_por = 1
            ),

            Solicitud(
                id = 2,
                id_usuario = 102,
                fecha_inicio = LocalDate.of(2025, 6, 28),
                fecha_fin = LocalDate.of(2025, 7, 2),
                motivo = "Capacitación",
                estado = "Aprobada",
                observaciones = "Curso de desarrollo backend",
                fecha_crea = LocalDate.now(),
                fecha_edita = LocalDate.now(),
                revisado_por = 1
            ),

            Solicitud(
                id = 3,
                id_usuario = 103,
                fecha_inicio = LocalDate.of(2025, 7, 1),
                fecha_fin = LocalDate.of(2025, 7, 5),
                motivo = "Licencia médica",
                estado = "Aprobada",
                observaciones = "Reposo médico",
                fecha_crea = LocalDate.now(),
                fecha_edita = LocalDate.now(),
                revisado_por = 1
            ),

            Solicitud(
                id = 4,
                id_usuario = 104,
                fecha_inicio = LocalDate.of(2025, 7, 10),
                fecha_fin = LocalDate.of(2025, 7, 15),
                motivo = "Comisión",
                estado = "Pendiente",
                observaciones = "Visita comercial",
                fecha_crea = LocalDate.now(),
                fecha_edita = LocalDate.now(),
                revisado_por = 1
            ),

            Solicitud(
                id = 5,
                id_usuario = 105,
                fecha_inicio = LocalDate.of(2025, 6, 30),
                fecha_fin = LocalDate.of(2025, 6, 30),
                motivo = "Permiso",
                estado = "Aprobada",
                observaciones = "Trámites personales",
                fecha_crea = LocalDate.now(),
                fecha_edita = LocalDate.now(),
                revisado_por = 1
            )
        ))
    }

    private fun actualizarCalendario() {
        val solicitudesFiltradas = if (filtroActivo != null) {
            todasLasSolicitudes.filter { it.tipo == filtroActivo }
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
            mensaje.append("   ${solicitud.tipo.name}: ${solicitud.titulo}\n")
            mensaje.append("   Estado: ${solicitud.estadoEnum.name}\n")
            mensaje.append("   Período: ${solicitud.getFechaInicioString()} al ${solicitud.getFechaFinString()}\n\n")
        }

        builder.setMessage(mensaje.toString())
            .setPositiveButton("Cerrar", null)
            .setNeutralButton("Ver Detalles") { _, _ ->
                mostrarDetallesCompletos(solicitudes)
            }
            .show()
    }

    private fun mostrarDetallesCompletos(solicitudes: List<Solicitud>) {
        Toast.makeText(this, "Vista detallada - Por implementar", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogAgregarSolicitud() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Nueva Solicitud")
            .setMessage("¿Qué tipo de solicitud deseas agregar?")
            .setPositiveButton("Vacaciones") { _, _ ->
                // Implementar dialog para vacaciones
            }
            .setNeutralButton("Permiso") { _, _ ->
                // Implementar dialog para permisos
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}