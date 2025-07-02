package com.example.proyecto_app_cbt.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Solicitud

class DiaMesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val tvNumero: TextView
    private val containerSolicitudes: LinearLayout
    private var solicitudes: List<Solicitud> = emptyList()
    private var fecha: String = ""

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        setPadding(4, 4, 4, 4)

        // Número del día
        tvNumero = TextView(context).apply {
            textSize = 12f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }
        addView(tvNumero)

        // Container para solicitudes
        containerSolicitudes = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
        addView(containerSolicitudes)
    }

    fun configurarDia(numeroDia: Int, fecha: String, solicitudesDia: List<Solicitud>, esHoy: Boolean = false) {
        this.fecha = fecha
        this.solicitudes = solicitudesDia

        tvNumero.text = numeroDia.toString()

        // Destacar el día actual
        if (esHoy) {
            tvNumero.setBackgroundResource(R.drawable.bg_dia_actual)
            tvNumero.setTextColor(Color.WHITE)
        } else {
            tvNumero.background = null
            tvNumero.setTextColor(Color.BLACK)
        }

        // Limpiar solicitudes anteriores
        containerSolicitudes.removeAllViews()

        // Agregar indicadores de solicitudes
        solicitudesDia.take(3).forEach { solicitud ->
            agregarIndicadorSolicitud(solicitud)
        }

        // Si hay más de 3, mostrar indicador "+X más"
        if (solicitudesDia.size > 3) {
            val tvMas = TextView(context).apply {
                text = "+${solicitudesDia.size - 3} más"
                textSize = 8f
                setTextColor(Color.GRAY)
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
            containerSolicitudes.addView(tvMas)
        }
    }

    private fun agregarIndicadorSolicitud(solicitud: Solicitud) {
        val indicador = TextView(context).apply {
            text = "${solicitud.empleado.split(" ")[0]} - ${getAbreviaturaTipo(solicitud.motivo)}"
            textSize = 7f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(2, 1, 2, 1)
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 1, 0, 1)
            }

            // Color según el tipo y estado
            val colorFondo = when (solicitud.estado.lowercase()) {
                "aprobada", "aprobado" -> getColorPorTipo(solicitud.motivo)
                "pendiente" -> Color.parseColor("#FFA726")
                "rechazada", "rechazado" -> Color.parseColor("#EF5350")
                "en proceso" -> Color.parseColor("#42A5F5")
                "completada", "completado" -> Color.parseColor("#66BB6A")
                else -> getColorPorTipo(solicitud.motivo)
            }

            val drawable = GradientDrawable().apply {
                setColor(colorFondo)
                cornerRadius = 8f
            }
            background = drawable
        }

        containerSolicitudes.addView(indicador)
    }

    private fun getAbreviaturaTipo(motivo: String): String {
        return when (motivo.lowercase()) {
            "vacaciones" -> "VAC"
            "permiso" -> "PER"
            "licencia medica", "licencia médica" -> "LIC"
            "capacitacion", "capacitación" -> "CAP"
            "comision", "comisión" -> "COM"
            else -> motivo.take(3).uppercase()
        }
    }

    private fun getColorPorTipo(motivo: String): Int {
        return when (motivo.lowercase()) {
            "vacaciones" -> Color.parseColor("#4CAF50") // Verde
            "permiso" -> Color.parseColor("#2196F3") // Azul
            "licencia medica", "licencia médica" -> Color.parseColor("#F44336") // Rojo
            "capacitacion", "capacitación" -> Color.parseColor("#FF9800") // Naranja
            "comision", "comisión" -> Color.parseColor("#9C27B0") // Púrpura
            else -> Color.parseColor("#607D8B") // Gris azulado por defecto
        }
    }

    fun getSolicitudes(): List<Solicitud> = solicitudes
    fun getFecha(): String = fecha
}