package com.example.proyecto_app_cbt.model

import java.time.LocalDate

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
)
