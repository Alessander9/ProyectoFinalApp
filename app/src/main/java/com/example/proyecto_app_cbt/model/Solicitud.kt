package com.example.proyecto_app_cbt.model

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Solicitud(
    var id: String = "",
    var id_usuario: String = "",
    var fecha_inicio: Timestamp? = null,
    var fecha_fin: Timestamp? = null,
    var motivo: String = "",
    var estado: String = "",
    var observaciones: String = "",
    var fecha_crea: Timestamp? = null,
    var fecha_edita: Timestamp? = null,
    var revisado_por: String = ""
) {
    // Propiedades adicionales para el calendario
    val empleado: String = "Usuario $id_usuario" // Puedes cambiar esto por el nombre real
    val titulo: String = motivo
    val descripcion: String = observaciones

    val diasTotales: Long
        get() = if (fecha_inicio != null && fecha_fin != null) {
            val inicio = fecha_inicio!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val fin = fecha_fin!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            ChronoUnit.DAYS.between(inicio, fin) + 1
        } else 0

    // Funciones de utilidad
    fun estaActivaEnFecha(fecha: String): Boolean {
        if (fecha_inicio == null || fecha_fin == null) return false

        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val inicioLocal = fecha_inicio!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val finLocal = fecha_fin!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        return !fechaLocal.isBefore(inicioLocal) && !fechaLocal.isAfter(finLocal)
    }

    fun esInicioEnFecha(fecha: String): Boolean {
        if (fecha_inicio == null) return false

        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val inicioLocal = fecha_inicio!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        return fechaLocal.isEqual(inicioLocal)
    }

    fun esFinEnFecha(fecha: String): Boolean {
        if (fecha_fin == null) return false

        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val finLocal = fecha_fin!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        return fechaLocal.isEqual(finLocal)
    }

    fun getFechaInicioString(): String {
        return if (fecha_inicio != null) {
            val localDate = fecha_inicio!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else ""
    }

    fun getFechaFinString(): String {
        return if (fecha_fin != null) {
            val localDate = fecha_fin!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else ""
    }
}