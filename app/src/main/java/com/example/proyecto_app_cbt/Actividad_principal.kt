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
        val accesosRaw = prefs.getString("rolAccesos", "") ?: ""
        val accesos = accesosRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val userId = prefs.getString("userId", null)

        val cardViewSolicitud = findViewById<CardView>(R.id.cardSolicitud)
        val cardViewUsuario = findViewById<CardView>(R.id.cardUsuarios)
        val cardViewRoles = findViewById<CardView>(R.id.cardRoles)
        val cardViewAreas = findViewById<CardView>(R.id.cardAreas)
        val cardViewMiUser = findViewById<CardView>(R.id.cardMiCuenta)

        cardViewSolicitud.visibility = if ("SOLICITUDES" in accesos) View.VISIBLE else View.GONE
        cardViewUsuario.visibility = if ("USUARIOS" in accesos) View.VISIBLE else View.GONE
        cardViewRoles.visibility = if ("ROLES" in accesos) View.VISIBLE else View.GONE
        cardViewAreas.visibility = if ("AREAS" in accesos) View.VISIBLE else View.GONE
        cardViewMiUser.visibility = if ("MICUENTA" in accesos) View.VISIBLE else View.GONE

        cardViewSolicitud.setOnClickListener {
            startActivity(Intent(this, ListadoSolicitudesActivity::class.java))
        }

        cardViewUsuario.setOnClickListener {
            startActivity(Intent(this, UsuariosActivity::class.java))
        }

        cardViewRoles.setOnClickListener {
            startActivity(Intent(this, ListadoRolesActivity::class.java))
        }

        cardViewAreas.setOnClickListener {
            startActivity(Intent(this, AreasActivity::class.java))
        }

        cardViewMiUser.setOnClickListener {
            startActivity(Intent(this, RegistrarUsuarioActivity::class.java).apply {
                putExtra("usuarioId", userId)
                putExtra("modoVista", true)
            })
        }
    }
}