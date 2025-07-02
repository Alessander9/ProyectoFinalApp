package com.example.proyecto_app_cbt.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Solicitud
import com.example.proyecto_app_cbt.view.DiaMesView
import java.text.SimpleDateFormat
import java.util.*

class CalendarioPersonalizadoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val meses = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    private val diasSemana = arrayOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")

    private var fechaActual = Calendar.getInstance()
    private var solicitudes: List<Solicitud> = emptyList()
    private var onDiaClickListener: ((String, List<Solicitud>) -> Unit)? = null

    private lateinit var tvTituloMes: TextView
    private lateinit var gridDias: GridLayout

    init {
        orientation = VERTICAL
        inicializarVista()
    }

    private fun inicializarVista() {
        // Header con navegación
        val headerLayout = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(16, 16, 16, 8)
        }

        val btnAnterior = Button(context).apply {
            text = "←"
            textSize = 18f
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnClickListener { navegarMes(-1) }
        }

        tvTituloMes = TextView(context).apply {
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
        }

        val btnSiguiente = Button(context).apply {
            text = "→"
            textSize = 18f
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnClickListener { navegarMes(1) }
        }

        headerLayout.addView(btnAnterior)
        headerLayout.addView(tvTituloMes)
        headerLayout.addView(btnSiguiente)
        addView(headerLayout)

        // Header de días de la semana
        val headerDias = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(8, 8, 8, 8)
        }

        diasSemana.forEach { dia ->
            val tvDia = TextView(context).apply {
                text = dia
                textSize = 12f
                setTypeface(null, Typeface.BOLD)
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
                setPadding(4, 8, 4, 8)
            }
            headerDias.addView(tvDia)
        }
        addView(headerDias)

        // Grid de días
        gridDias = GridLayout(context).apply {
            columnCount = 7
            rowCount = 6
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        }
        addView(gridDias)

        actualizarCalendario()
    }

    private fun navegarMes(direccion: Int) {
        fechaActual.add(Calendar.MONTH, direccion)
        actualizarCalendario()
    }

    fun setSolicitudes(nuevasSolicitudes: List<Solicitud>) {
        solicitudes = nuevasSolicitudes
        actualizarCalendario()
    }

    fun setOnDiaClickListener(listener: (String, List<Solicitud>) -> Unit) {
        onDiaClickListener = listener
    }

    private fun actualizarCalendario() {
        // Actualizar título
        val mesAnio = "${meses[fechaActual.get(Calendar.MONTH)]} ${fechaActual.get(Calendar.YEAR)}"
        tvTituloMes.text = mesAnio

        // Limpiar grid
        gridDias.removeAllViews()

        val calendar = Calendar.getInstance()
        calendar.time = fechaActual.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val primerDiaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val diasEnMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Agregar días vacíos al inicio
        repeat(primerDiaSemana) {
            val diaVacio = View(context).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 120
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }
            gridDias.addView(diaVacio)
        }

        // Agregar días del mes
        for (dia in 1..diasEnMes) {
            calendar.set(Calendar.DAY_OF_MONTH, dia)
            val fechaDia = formatoFecha.format(calendar.time)

            // Obtener solicitudes para este día
            val solicitudesDia = solicitudes.filter { it.estaActivaEnFecha(fechaDia) }

            val diaMesView = DiaMesView(context).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 120
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(1, 1, 1, 1)
                }

                configurarDia(dia, fechaDia, solicitudesDia, fechaDia == hoy)

                setOnClickListener {
                    onDiaClickListener?.invoke(fechaDia, solicitudesDia)
                }

                // Efecto visual al hacer click
                isClickable = true
                isFocusable = true
                background = ContextCompat.getDrawable(context, R.drawable.bg_dia_clickeable)
            }

            gridDias.addView(diaMesView)
        }
    }
}