package com.example.proyecto_app_cbt

import android.content.Intent
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        val baseLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val container = baseLayout.findViewById<FrameLayout>(R.id.baseContent)
        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseLayout)

        // Aquí puedes poner lógica común del header
        val prefs = getSharedPreferences("dataUser", MODE_PRIVATE)
        val nombre = prefs.getString("fullName", "Usuario")
        val rolId = prefs.getString("rolId", "")

        baseLayout.findViewById<TextView>(R.id.tvNombreUsuario)?.text = nombre
        baseLayout.findViewById<TextView>(R.id.tvRolUsuario)?.text = when (rolId) {
            "" -> "Administrador"
            "" -> "Supervisor"
            "" -> "Usuario"
            else -> "Sin rol"
        }
        println("nombre: " + nombre);
        println("rolId: " + rolId);

        val btnMenu = baseLayout.findViewById<ImageButton>(R.id.btnMenuUsuario)
        btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(this, view)

            // Mostrar íconos (reflexión necesaria en versiones modernas)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true)
            } else {
                try {
                    val fields = popup.javaClass.declaredFields
                    for (field in fields) {
                        if ("mPopup" == field.name) {
                            field.isAccessible = true
                            val menuPopupHelper = field.get(popup)
                            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                            val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                            setForceIcons.invoke(menuPopupHelper, true)
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            popup.menuInflater.inflate(R.menu.menu_usuario, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_ver_usuario -> {
                        val nombreMostrado = prefs.getString("fullName", "Usuario")
                        Toast.makeText(this, "Usuario: $nombreMostrado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_cerrar_sesion -> {
                        prefs.edit().clear().apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }
}