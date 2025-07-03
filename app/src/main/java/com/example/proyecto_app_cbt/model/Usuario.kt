package com.example.proyecto_app_cbt.model

data class Usuario(
    var id: String = "",
    var nombre_completo: String = "",
    var correo: String = "",
    var contraseña: String = "",
    var id_rol: String = "",
    var id_area: String = "",
    var activo: Boolean = true,
    var dni: String = "",
    var telefono: String = "",
    var direccion: String = "",
    var fecha_ingreso: String = ""
)