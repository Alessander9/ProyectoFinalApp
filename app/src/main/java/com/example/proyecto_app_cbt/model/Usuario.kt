package com.example.proyecto_app_cbt.model

data class Usuario(
    var id: Int = 0,
    var nombre_completo: String,
    var correo: String,
    var contraseña: String,
    var activo: Boolean = true,
    var id_rol: Int,
    var id_area: Int
)
