package com.example.proyecto_app_cbt.model

data class Usuario(
    var id: String = "",
    var nombre_completo: String = "",
    var correo: String = "",
    var contraseña: String = "123",
    var id_rol: Int = 0,
    var id_area: Int = 0,
    var activo: Boolean = true
)