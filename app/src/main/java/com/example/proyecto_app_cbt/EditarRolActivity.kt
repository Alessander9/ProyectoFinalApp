package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Rol
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class EditarRolActivity : BaseActivity() {

    private lateinit var etNombreRol: TextInputEditText
    private lateinit var btnGuardar: Button
    private lateinit var rolId: String
    private val rolDao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_rol)

        // 1. Vincular vistas
        etNombreRol = findViewById(R.id.etNombreRol)
        btnGuardar   = findViewById(R.id.btnGuardarCambios)

        // 2. Obtener el ID del rol desde el Intent
        rolId = intent.getStringExtra("ROL_ID") ?: run {
            Toast.makeText(this, "ID de rol no proporcionado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 3. Cargar datos actuales del rol con coroutine
        lifecycleScope.launch {
            val rol = rolDao.obtenerPorId(rolId)
            if (rol != null) {
                etNombreRol.setText(rol.nombre)
            } else {
                Toast.makeText(this@EditarRolActivity, "No se encontró el rol", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        // 4. Guardar cambios al hacer clic en el botón
        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombreRol.text.toString().trim()
            if (nuevoNombre.isEmpty()) {
                etNombreRol.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            // 5. Actualizar usando coroutine
            lifecycleScope.launch {
                // Construimos un objeto Rol con el mismo ID y nuevo nombre
                val rolActualizado = Rol(id = rolId, nombre = nuevoNombre)
                val exito = rolDao.actualizar(rolActualizado)
                if (exito) {
                    Toast.makeText(this@EditarRolActivity, "Rol actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditarRolActivity, "Error al actualizar rol", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
