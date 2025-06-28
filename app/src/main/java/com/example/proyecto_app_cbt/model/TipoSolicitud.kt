package com.example.proyecto_app_cbt.model

import android.graphics.Color

enum class TipoSolicitud(val colorPrimario: Int, val colorSecundario: Int) {
    VACACIONES(Color.GREEN, Color.parseColor("#C8E6C9")),
    PERMISO(Color.parseColor("#FF9800"), Color.parseColor("#FFE0B2")),
    LICENCIA_MEDICA(Color.RED, Color.parseColor("#FFCDD2")),
    CAPACITACION(Color.BLUE, Color.parseColor("#BBDEFB")),
    COMISION(Color.parseColor("#9C27B0"), Color.parseColor("#E1BEE7")),
    OTRO(Color.parseColor("#607D8B"), Color.parseColor("#CFD8DC"));
}
