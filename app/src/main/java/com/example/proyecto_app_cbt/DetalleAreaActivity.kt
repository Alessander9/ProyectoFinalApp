package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Area
import com.google.firebase.firestore.ktx.toObject

class DetalleAreaActivity : BaseActivity() {

    private lateinit var tvNombreArea: TextView
    private lateinit var btnEditarArea: Button
    private lateinit var btnEliminarArea: Button
    private lateinit var btnCancelarArea: Button
    private lateinit var areaId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_area)

        // 1. Vincular vistas
        tvNombreArea    = findViewById(R.id.tvNombreDetalleArea)
        btnEditarArea   = findViewById(R.id.btnEditarArea)
        btnEliminarArea = findViewById(R.id.btnEliminarArea)
        btnCancelarArea = findViewById(R.id.btnCancelarArea)

        // 2. Obtener ID del área
        areaId = intent.getStringExtra("AREA_ID") ?: run {
            Toast.makeText(this, "No se recibió ID de área", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 3. Cargar datos del área desde Firestore
        FirestoreProvider.db.collection("areas").document(areaId).get()
            .addOnSuccessListener { doc ->
                val area = doc.toObject(Area::class.java)
                if (area != null) {
                    tvNombreArea.text = area.nombre
                } else {
                    Toast.makeText(this, "Área no encontrada", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar área: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }

        // 4. Editar → EditarAreaActivity
        btnEditarArea.setOnClickListener {
            val intent = Intent(this, EditarAreaActivity::class.java)
            intent.putExtra("AREA_ID", areaId)
            startActivity(intent)
        }

        // 5. Eliminar con confirmación
        btnEliminarArea.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar área")
                .setMessage("¿Estás seguro de que deseas eliminar esta área?")
                .setPositiveButton("Sí") { _, _ ->
                    FirestoreProvider.db.collection("areas").document(areaId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Área eliminada", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // 6. Cancelar → cerrar actividad
        btnCancelarArea.setOnClickListener {
            finish()
        }
    }
}
