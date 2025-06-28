package com.example.proyecto_app_cbt.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Solicitud(
    var id: Int = 0,
    var id_usuario: Int,
    var fecha_inicio: LocalDate,
    var fecha_fin: LocalDate,
    var motivo: String,
    var estado: String,
    var observaciones: String,
    var fecha_crea: LocalDate,
    var fecha_edita: LocalDate,
    var revisado_por: Int
) {
    // Propiedades adicionales para el calendario
    val empleado: String = "Usuario $id_usuario" // Puedes cambiar esto por el nombre real
    val titulo: String = motivo
    val descripcion: String = observaciones
    val tipo: TipoSolicitud = when (motivo.lowercase()) {
        "vacaciones" -> TipoSolicitud.VACACIONES
        "permiso" -> TipoSolicitud.PERMISO
        "licencia medica", "licencia médica" -> TipoSolicitud.LICENCIA_MEDICA
        "capacitacion", "capacitación" -> TipoSolicitud.CAPACITACION
        "comision", "comisión" -> TipoSolicitud.COMISION
        else -> TipoSolicitud.OTRO
    }
    val estadoEnum: EstadoSolicitud = when (estado.lowercase()) {
        "pendiente" -> EstadoSolicitud.PENDIENTE
        "aprobada" -> EstadoSolicitud.APROBADA
        "rechazada" -> EstadoSolicitud.RECHAZADA
        "en proceso" -> EstadoSolicitud.EN_PROCESO
        "completada" -> EstadoSolicitud.COMPLETADA
        else -> EstadoSolicitud.PENDIENTE
    }
    val prioridad: PrioridadSolicitud = PrioridadSolicitud.MEDIA
    val diasTotales: Long = ChronoUnit.DAYS.between(fecha_inicio, fecha_fin) + 1

    // Funciones de utilidad
    fun estaActivaEnFecha(fecha: String): Boolean {
        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return !fechaLocal.isBefore(fecha_inicio) && !fechaLocal.isAfter(fecha_fin)
    }

    fun esInicioEnFecha(fecha: String): Boolean {
        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return fechaLocal.isEqual(fecha_inicio)
    }

    fun esFinEnFecha(fecha: String): Boolean {
        val fechaLocal = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return fechaLocal.isEqual(fecha_fin)
    }

    fun getFechaInicioString(): String = fecha_inicio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    fun getFechaFinString(): String = fecha_fin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

