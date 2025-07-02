package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.databinding.ActivityRolBinding
import com.example.proyecto_app_cbt.model.Rol
import kotlinx.coroutines.launch

class RolActivity : BaseActivity() {

    private lateinit var binding: ActivityRolBinding
    private val dao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rolId = intent.getStringExtra("rol_id")

        if (rolId != null) {
            // Modo edición
            lifecycleScope.launch {
                val rolExistente = dao.obtenerPorId(rolId)
                if (rolExistente != null) {
                    binding.etNombreRol.setText(rolExistente.nombre)
                }
            }
        }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreRol.text.toString().trim()
            if (nombre.isBlank()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("El nombre no puede estar vacío.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            val rol = Rol(
                id = rolId ?: "", // Si es nuevo, que DAO genere ID
                nombre = nombre
            )

            lifecycleScope.launch {
                val idGuardado = dao.insertar(rol)
                if (idGuardado != null) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    AlertDialog.Builder(this@RolActivity)
                        .setTitle("Error")
                        .setMessage("No se pudo guardar el rol.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }
}