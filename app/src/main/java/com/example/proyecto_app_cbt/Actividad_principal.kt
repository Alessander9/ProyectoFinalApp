package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Actividad_principal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad_principal)

        val cardViewSolicitud = findViewById<CardView>(R.id.cardViewSolicitud)

        cardViewSolicitud.setOnClickListener {
            val intent = Intent(this, SolicitudActivity::class.java)
            startActivity(intent)
        }
    }
}