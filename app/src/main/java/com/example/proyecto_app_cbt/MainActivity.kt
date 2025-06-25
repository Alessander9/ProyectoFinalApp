package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No necesitas layout de login de momento, puedes usar el mismo de listado o crear uno vacío
        setContentView(R.layout.activity_main)

        // Lanza inmediatamente la pantalla de listado
        startActivity(Intent(this, ListadoSolicitudesActivity::class.java))
        finish()
    }
}
