package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

/**
 * Activity para crear una nueva Área en Firestore.
 * Layout esperado: activity_crear_area.xml
 * con un TextInputEditText id="etNombreArea"
 * y un Button id="btnCrearArea".
 */
class CrearAreaActivity : BaseActivity() {

    private lateinit var etNombreArea: TextInputEditText
    private lateinit var btnCrearArea: Button
    private val areaDao = AreaDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_area)

        // 1. Vincular vistas
        etNombreArea = findViewById(R.id.etNombreArea)
        btnCrearArea = findViewById(R.id.btnCrearArea)

        // 2. Configurar botón Crear
        btnCrearArea.setOnClickListener {
            val nombre = etNombreArea.text.toString().trim()
            if (nombre.isEmpty()) {
                etNombreArea.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            // 3. Insertar nueva área usando coroutines
            lifecycleScope.launch {
                val nuevaArea = Area(nombre = nombre)
                val idGenerado = areaDao.insertar(nuevaArea)
                if (idGenerado != null) {
                    Toast.makeText(
                        this@CrearAreaActivity,
                        "Área creada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CrearAreaActivity,
                        "Error al crear área",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
