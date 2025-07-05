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

class EditarRolActivity : BaseActivity() {

    private lateinit var etNombreRol: TextInputEditText
    private lateinit var layoutAccesos: LinearLayout
    private lateinit var btnGuardarRol: Button
    private val rolDao = RolDAOFirestore()
    private var rolId: String? = null
    private var accesosDisponibles = listOf(
        "AREAS" to "Áreas",
        "ROLES" to "Roles",
        "SOLICITUDES" to "Solicitudes",
        "USUARIOS" to "Usuarios",
        "MICUENTA" to "Mi cuenta"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_rol)

        etNombreRol = findViewById(R.id.etNombreRol)
        layoutAccesos = findViewById(R.id.layoutAccesos)
        btnGuardarRol  = findViewById(R.id.btnCrearRol)

        rolId = intent.getStringExtra("ROL_ID")
        if (rolId == null) {
            Toast.makeText(this, "ID del rol no proporcionado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarOpcionesAccesos()

        lifecycleScope.launch {
            val rol = rolDao.obtenerPorId(rolId!!)
            rol?.let {
                etNombreRol.setText(it.nombre)
                marcarAccesosSeleccionados(it.accesos)
            }
        }

        btnGuardarRol.text = "Guardar cambios"
        btnGuardarRol.setOnClickListener {
            val nombre = etNombreRol.text.toString().trim()
            if (nombre.isEmpty()) {
                etNombreRol.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }
            val accesosSeleccionados = obtenerAccesosSeleccionados()

            lifecycleScope.launch {
                val rolActualizado = Rol(
                    id = rolId!!,
                    nombre = nombre,
                    accesos = accesosSeleccionados
                )
                val exito = rolDao.actualizar(rolActualizado)
                if (exito) {
                    Toast.makeText(this@EditarRolActivity, "Rol actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditarRolActivity, "Error al actualizar rol", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cargarOpcionesAccesos() {
        accesosDisponibles.forEach { (codigo, nombre) ->
            val checkBox = CheckBox(this).apply {
                text = nombre
                tag = codigo
            }
            layoutAccesos.addView(checkBox)
        }
    }

    private fun marcarAccesosSeleccionados(accesos: List<String>) {
        for (i in 0 until layoutAccesos.childCount) {
            val view = layoutAccesos.getChildAt(i)
            if (view is CheckBox) {
                view.isChecked = accesos.contains(view.tag.toString())
            }
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