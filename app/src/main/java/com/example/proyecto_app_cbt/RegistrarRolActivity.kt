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

class RegistrarRolActivity : BaseActivity() {

    private lateinit var etNombreRol: TextInputEditText
    private lateinit var btnCrearRol: Button
    private val rolDao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1) Inflamos el layout que debe contener:
        //    - TextInputEditText @+id/etNombreRol
        //    - Button @+id/btnCrearRol
        setContentView(R.layout.activity_registrar_rol)

        // 2) Vinculamos las vistas
        etNombreRol = findViewById(R.id.etNombreRol)
        btnCrearRol  = findViewById(R.id.btnCrearRol)

        // 3) Configuramos el botón para crear el rol
        btnCrearRol.setOnClickListener {
            val nombre = etNombreRol.text.toString().trim()
            if (nombre.isEmpty()) {
                etNombreRol.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            // 4) Insertar en Firestore con coroutine
            lifecycleScope.launch {
                val nuevoRol = Rol(nombre = nombre)
                val idGenerado = rolDao.insertar(nuevoRol)
                if (idGenerado != null) {
                    Toast.makeText(
                        this@RegistrarRolActivity,
                        "Rol creado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // cierra la Activity y vuelve al listado
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
