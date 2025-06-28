package com.example.proyecto_app_cbt.model

import com.google.firebase.Timestamp

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
)