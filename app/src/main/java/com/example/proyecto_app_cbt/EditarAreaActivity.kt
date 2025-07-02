package com.example.proyecto_app_cbt

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Area
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class EditarAreaActivity : AppCompatActivity() {

    private lateinit var etNombreArea: TextInputEditText
    private lateinit var btnGuardarArea: Button
    private lateinit var areaId: String
    private val areaDao = AreaDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_area)

        // Vincular vistas
        etNombreArea = findViewById(R.id.etNombreArea)
        btnGuardarArea = findViewById(R.id.btnGuardarArea)

        // Obtener ID de área
        areaId = intent.getStringExtra("AREA_ID") ?: run {
            Toast.makeText(this, "ID de área no proporcionado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Cargar datos del área desde Firestore
        lifecycleScope.launch {
            try {
                val snapshot = FirestoreProvider.db
                    .collection("areas")
                    .document(areaId)
                    .get()
                    .await()
                val area = snapshot.toObject(Area::class.java)
                if (area != null) {
                    etNombreArea.setText(area.nombre)
                } else {
                    Toast.makeText(this@EditarAreaActivity, "Área no encontrada", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditarAreaActivity, "Error al cargar área: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        // Guardar cambios
        btnGuardarArea.setOnClickListener {
            val nuevoNombre = etNombreArea.text.toString().trim()
            if (nuevoNombre.isEmpty()) {
                etNombreArea.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val updatedArea = Area(id = areaId, nombre = nuevoNombre)
                val success = areaDao.actualizar(updatedArea)
                if (success) {
                    Toast.makeText(this@EditarAreaActivity, "Área actualizada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditarAreaActivity, "Error al actualizar área", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
