package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.databinding.ActivityDetalleRolBinding
import kotlinx.coroutines.launch

class DetalleRolActivity : BaseActivity() {

    private lateinit var binding: ActivityDetalleRolBinding
    private val dao = RolDAOFirestore()
    private var rolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rolId = intent.getStringExtra("rol_id")
        if (rolId.isNullOrBlank()) {
            finish()
            return
        }

        cargarDatos()

        binding.btnEditar.setOnClickListener {
            startActivity(Intent(this, RolActivity::class.java).apply {
                putExtra("rol_id", rolId)
            })
        }

        binding.btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar rol")
                .setMessage("¿Estás seguro de que quieres eliminar este rol?")
                .setPositiveButton("Sí") { _, _ ->
                    eliminarRol()
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            val rol = dao.obtenerPorId(rolId!!)
            if (rol != null) {
                binding.tvNombreRol.text = rol.nombre
            } else {
                AlertDialog.Builder(this@DetalleRolActivity)
                    .setTitle("Error")
                    .setMessage("No se encontró el rol.")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }
        }
    }

    private fun eliminarRol() {
        lifecycleScope.launch {
            val ok = dao.eliminar(rolId!!)
            if (ok) {
                setResult(RESULT_OK)
                finish()
            } else {
                AlertDialog.Builder(this@DetalleRolActivity)
                    .setTitle("Error")
                    .setMessage("No se pudo eliminar el rol.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

}