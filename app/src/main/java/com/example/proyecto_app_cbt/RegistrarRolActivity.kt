package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Rol
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegistrarRolActivity : BaseActivity() {

    private lateinit var etNombreRol: TextInputEditText
    private lateinit var btnCrearRol: Button
    private val rolDao = RolDAOFirestore()
    private lateinit var layoutAccesos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_rol)

        etNombreRol = findViewById(R.id.etNombreRol)
        layoutAccesos = findViewById(R.id.layoutAccesos)
        btnCrearRol  = findViewById(R.id.btnCrearRol)

        cargarOpcionesAccesos()

        btnCrearRol.setOnClickListener {
            val nombre = etNombreRol.text.toString().trim()
            if (nombre.isEmpty()) {
                etNombreRol.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            val accesosSeleccionados = obtenerAccesosSeleccionados()

            lifecycleScope.launch {
                val nuevoRol = Rol(
                    nombre = nombre,
                    accesos = accesosSeleccionados
                )
                val idGenerado = rolDao.insertar(nuevoRol)
                if (idGenerado != null) {
                    Toast.makeText(this@RegistrarRolActivity, "Rol creado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegistrarRolActivity, "Error al crear rol", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cargarOpcionesAccesos() {
        val accesosDisponibles = listOf(
            "AREAS" to "Áreas",
            "ROLES" to "Roles",
            "SOLICITUDES" to "Solicitudes",
            "USUARIOS" to "Usuarios",
            "MICUENTA" to "Mi cuenta"
        )
        accesosDisponibles.forEach { (codigo, nombre) ->
            val checkBox = CheckBox(this).apply {
                text = nombre
                tag = codigo
            }
            layoutAccesos.addView(checkBox)
        }
    }

    private fun obtenerAccesosSeleccionados(): List<String> {
        val accesos = mutableListOf<String>()
        for (i in 0 until layoutAccesos.childCount) {
            val view = layoutAccesos.getChildAt(i)
            if (view is CheckBox && view.isChecked) {
                accesos.add(view.tag.toString())
            }
        }
        return accesos
    }
}
