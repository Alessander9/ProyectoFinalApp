package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.activity.enableEdgeToEdge

class Actividad_principal : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividad_principal)

        val prefs = getSharedPreferences("dataUser", MODE_PRIVATE)
        val rolNombre = prefs.getString("rolNombre", "Sin rol") ?: "Sin rol"
        val userId = prefs.getString("userId", null)

        val cardViewSolicitud = findViewById<CardView>(R.id.cardSolicitud)
        val cardViewUsuario = findViewById<CardView>(R.id.cardUsuarios)
        val cardViewRoles = findViewById<CardView>(R.id.cardRoles)
        val cardViewAreas = findViewById<CardView>(R.id.cardAreas)
        val cardViewMiUser = findViewById<CardView>(R.id.cardMiCuenta)

        when (rolNombre) {
            "Administrador" -> {
                cardViewSolicitud.visibility = View.VISIBLE
                cardViewUsuario.visibility = View.VISIBLE
                cardViewRoles.visibility = View.VISIBLE
                cardViewAreas.visibility = View.VISIBLE
                cardViewMiUser.visibility = View.VISIBLE
            }
            "Calificador" -> {
                cardViewSolicitud.visibility = View.VISIBLE
                cardViewUsuario.visibility = View.GONE
                cardViewRoles.visibility = View.GONE
                cardViewAreas.visibility = View.GONE
                cardViewMiUser.visibility = View.VISIBLE
            }
            "Trabajador" -> {
                cardViewSolicitud.visibility = View.VISIBLE
                cardViewUsuario.visibility = View.GONE
                cardViewRoles.visibility = View.GONE
                cardViewAreas.visibility = View.GONE
                cardViewMiUser.visibility = View.VISIBLE
            }
            else -> {
                cardViewSolicitud.visibility = View.GONE
                cardViewUsuario.visibility = View.GONE
                cardViewRoles.visibility = View.GONE
                cardViewAreas.visibility = View.GONE
                cardViewMiUser.visibility = View.VISIBLE
            }
        }

        if (cardViewSolicitud.isEnabled) {
            cardViewSolicitud.setOnClickListener {
                val intent = Intent(this, ListadoSolicitudesActivity::class.java)
                startActivity(intent)
            }
        }

        if (cardViewUsuario.isEnabled) {
            cardViewUsuario.setOnClickListener {
                val intent = Intent(this, UsuariosActivity::class.java)
                startActivity(intent)
            }
        }

        if (cardViewRoles.isEnabled) {
            cardViewRoles.setOnClickListener {
                val intent = Intent(this, ListadoRolesActivity::class.java)
                startActivity(intent)
            }
        }

        if (cardViewAreas.isEnabled) {
            cardViewAreas.setOnClickListener {
                val intent = Intent(this, AreasActivity::class.java)
                startActivity(intent)
            }
        }

        if (cardViewMiUser.isEnabled) {
            cardViewMiUser.setOnClickListener {
                val intent = Intent(this, RegistrarUsuarioActivity::class.java).apply {
                    putExtra("usuarioId", userId)
                    putExtra("modoVista", true)
                }
                startActivity(intent)
            }
        }
    }
}