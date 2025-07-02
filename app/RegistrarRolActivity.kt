package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Rol
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

/**
 * Activity para registrar un nuevo Rol en Firestore.
 * Layout esperado: activity_registrar_rol.xml
 * con TextInputEditText id="etNombreRol" y Button id="btnCrearRol".
 */
class RegistrarRolActivity : AppCompatActivity() {

    private lateinit var etNombreRol: TextInputEditText
    private lateinit var btnCrearRol: Button
    private val rolDao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_rol)

        // 1. Vincular vistas
        etNombreRol = findViewById(R.id.etNombreRol)
        btnCrearRol  = findViewById(R.id.btnCrearRol)

        // 2. Configurar botón Crear
        btnCrearRol.setOnClickListener {
            val nombre = etNombreRol.text.toString().trim()
            if (nombre.isEmpty()) {
                etNombreRol.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            // 3. Insertar nuevo rol con coroutine
            lifecycleScope.launch {
                val nuevoRol = Rol(nombre = nombre)
                val idGenerado = rolDao.insertar(nuevoRol)
                if (idGenerado != null) {
                    Toast.makeText(
                        this@RegistrarRolActivity,
                        "Rol creado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegistrarRolActivity,
                        "Error al crear rol",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}