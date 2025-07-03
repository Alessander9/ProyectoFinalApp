package com.example.proyecto_app_cbt

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.dao.UsuarioDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import com.example.proyecto_app_cbt.model.Rol
import com.example.proyecto_app_cbt.model.Usuario
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
class RegistrarUsuarioActivity : BaseActivity() {


    private lateinit var spinnerArea: Spinner
    private val areaDao = AreaDAOFirestore()
    private var listaAreas: List<Area> = emptyList()
    private lateinit var spinnerRol: Spinner
    private val rolDao = RolDAOFirestore()
    private var listaRoles: List<Rol> = emptyList()
    private val usuarioDao = UsuarioDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        spinnerArea = findViewById<Spinner>(R.id.spinnerArea)
        spinnerRol = findViewById(R.id.spinnerRol)
        lifecycleScope.launch {
            listaAreas = areaDao.obtenerTodos()
            val nombresAreas = mutableListOf("-- Seleccione --")
            nombresAreas.addAll(listaAreas.map { it.nombre })
            val adapterAreas = ArrayAdapter(
                this@RegistrarUsuarioActivity,
                android.R.layout.simple_spinner_item,
                nombresAreas
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerArea.adapter = adapterAreas

            spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        return
                    }
                    val areaSeleccionada = listaAreas[position - 1] // -1 porque agregamos "-- Seleccione --"
                    Toast.makeText(
                        this@RegistrarUsuarioActivity,
                        "Área seleccionada: ${areaSeleccionada.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            listaRoles = rolDao.obtenerTodos()
            val nombresRoles = mutableListOf("-- Seleccione --")
            nombresRoles.addAll(listaRoles.map { it.nombre })
            val adapterRoles = ArrayAdapter(
                this@RegistrarUsuarioActivity,
                android.R.layout.simple_spinner_item,
                nombresRoles
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerRol.adapter = adapterRoles

            spinnerRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        return
                    }
                    val rolSeleccionado = listaRoles[position - 1]
                    Toast.makeText(
                        this@RegistrarUsuarioActivity,
                        "Rol seleccionado: ${rolSeleccionado.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        val btnRegistrarUsuario = findViewById<Button>(R.id.btnRegistrarUsuario)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnRegistrarUsuario.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val rolPos = spinnerRol.selectedItemPosition
            val areaPos = spinnerArea.selectedItemPosition

            if (nombre.isEmpty()) {
                etNombre.error = "Este campo es obligatorio"
                etNombre.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Este campo es obligatorio"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Este campo es obligatorio"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (rolPos == 0) {
                Toast.makeText(this, "Debe seleccionar un Rol", Toast.LENGTH_SHORT).show()
                spinnerRol.requestFocus()
                return@setOnClickListener
            }

            if (areaPos == 0) {
                Toast.makeText(this, "Debe seleccionar un Área", Toast.LENGTH_SHORT).show()
                spinnerArea.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(this, "Formulario válido, ¡registrando usuario!", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                val nuevoUsuario = Usuario(
                    id = "",
                    nombre_completo = nombre,
                    correo = email,
                    contraseña = password,
                    id_rol = listaRoles[rolPos - 1].id,
                    id_area = listaAreas[areaPos - 1].id,
                    activo = true
                )

                val usuarioId = usuarioDao.insertar(nuevoUsuario)

                if (usuarioId != null) {
                    lifecycleScope.launch {
                        usuarioDao.actualizarCampoId(usuarioId)
                    }
                    Snackbar.make(
                        btnRegistrarUsuario,
                        "Usuario registrado correctamente",
                        Snackbar.LENGTH_LONG
                    ).show()

                    // Opcional: limpia el formulario
                    etNombre.text.clear()
                    etEmail.text.clear()
                    etPassword.text.clear()
                    spinnerRol.setSelection(0)
                    spinnerArea.setSelection(0)

                } else {
                    Snackbar.make(
                        btnRegistrarUsuario,
                        "Error al registrar usuario. Inténtelo nuevamente.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
