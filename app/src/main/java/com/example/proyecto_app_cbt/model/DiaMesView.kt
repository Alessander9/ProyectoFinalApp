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
import com.example.proyecto_app_cbt.model.EstadoSolicitud

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
            text = "${solicitud.empleado.split(" ")[0]} - ${solicitud.tipo.name.take(3)}"
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
            val colorFondo = when (solicitud.estadoEnum) {
                EstadoSolicitud.APROBADA -> solicitud.tipo.colorPrimario
                EstadoSolicitud.PENDIENTE -> Color.parseColor("#FFA726")
                EstadoSolicitud.RECHAZADA -> Color.parseColor("#EF5350")
                else -> solicitud.tipo.colorPrimario
            }

            val drawable = GradientDrawable().apply {
                setColor(colorFondo)
                cornerRadius = 8f
            }
            background = drawable
        }

        containerSolicitudes.addView(indicador)
    }

    fun getSolicitudes(): List<Solicitud> = solicitudes
    fun getFecha(): String = fecha
}