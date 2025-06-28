package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Actividad_principal : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad_principal)

        val cardViewSolicitud = findViewById<CardView>(R.id.cardSolicitud)
        val cardViewUsuario = findViewById<CardView>(R.id.cardUsuarios)
        val btnVerCalendario = findViewById<Button>(R.id.btnVerCalendario)


        cardViewSolicitud.setOnClickListener {
            val intent = Intent(this, ListadoSolicitudesActivity::class.java)
            startActivity(intent)
        }

        cardViewUsuario.setOnClickListener {
            val intent = Intent(this, UsuariosActivity::class.java)
            startActivity(intent)
        }

        btnVerCalendario.setOnClickListener {
            val intent = Intent(this, CalendarioActivity::class.java)
            startActivity(intent)
        }
    }
}