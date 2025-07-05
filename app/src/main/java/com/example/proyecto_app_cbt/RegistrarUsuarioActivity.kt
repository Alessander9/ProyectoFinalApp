package com.example.proyecto_app_cbt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.proyecto_app_cbt.dao.*
import com.example.proyecto_app_cbt.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import java.io.File
import java.util.UUID
import android.util.Log
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler

class RegistrarUsuarioActivity : BaseActivity() {

    private lateinit var spinnerArea: Spinner
    private lateinit var spinnerRol: Spinner
    private lateinit var layoutDatosTrabajador: LinearLayout
    private val areaDao = AreaDAOFirestore()
    private val rolDao = RolDAOFirestore()
    private val usuarioDao = UsuarioDAOFirestore()
    private var listaAreas: List<Area> = emptyList()
    private var listaRoles: List<Rol> = emptyList()
    private lateinit var transferUtility: TransferUtility
    private var rutaImagenSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)
        TransferNetworkLossHandler.getInstance(applicationContext)

        val credentials = BasicAWSCredentials(
            "AKIAYS2NURQPNNNYZQUP",
            "E4UGNIicS18htW/FCxeKaBwXtQKgj9Eb595ZRyzm"
        )

        val s3Client = AmazonS3Client(credentials)
        s3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_EAST_2))
        transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .s3Client(s3Client)
            .build()

        spinnerArea = findViewById(R.id.spinnerArea)
        spinnerRol = findViewById(R.id.spinnerRol)
        layoutDatosTrabajador = findViewById(R.id.layoutDatosTrabajador)

        cargarDatosSpinners()

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

            val dni = if (layoutDatosTrabajador.visibility == View.VISIBLE)
                findViewById<EditText>(R.id.etDNI).text.toString().trim() else ""
            val telefono = if (layoutDatosTrabajador.visibility == View.VISIBLE)
                findViewById<EditText>(R.id.etTelefono).text.toString().trim() else ""
            val direccion = if (layoutDatosTrabajador.visibility == View.VISIBLE)
                findViewById<EditText>(R.id.etDireccion).text.toString().trim() else ""
            val fechaIngreso = if (layoutDatosTrabajador.visibility == View.VISIBLE)
                findViewById<EditText>(R.id.etFechaIngreso).text.toString().trim() else ""

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
                return@setOnClickListener
            }
            if (areaPos == 0) {
                Toast.makeText(this, "Debe seleccionar un Área", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (layoutDatosTrabajador.visibility == View.VISIBLE) {
                if (dni.isEmpty()) {
                    findViewById<EditText>(R.id.etDNI).error = "Este campo es obligatorio"
                    return@setOnClickListener
                }
                if (telefono.isEmpty()) {
                    findViewById<EditText>(R.id.etTelefono).error = "Este campo es obligatorio"
                    return@setOnClickListener
                }
                if (direccion.isEmpty()) {
                    findViewById<EditText>(R.id.etDireccion).error = "Este campo es obligatorio"
                    return@setOnClickListener
                }
                if (fechaIngreso.isEmpty()) {
                    findViewById<EditText>(R.id.etFechaIngreso).error = "Este campo es obligatorio"
                    return@setOnClickListener
                }
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
                    activo = true,
                    dni = dni,
                    telefono = telefono,
                    direccion = direccion,
                    fecha_ingreso = fechaIngreso
                )

                val usuarioId = usuarioDao.insertar(nuevoUsuario)

                if (usuarioId != null) {
                    usuarioDao.actualizarCampoId(usuarioId)
                    Snackbar.make(
                        btnRegistrarUsuario,
                        "Usuario registrado correctamente",
                        Snackbar.LENGTH_LONG
                    ).show()
                    limpiarFormulario(etNombre, etEmail, etPassword)

                    rutaImagenSeleccionada?.let { ruta ->
                        subirImagen(ruta, usuarioId)
                    }

                    val intent = Intent(this@RegistrarUsuarioActivity, UsuariosActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(
                        btnRegistrarUsuario,
                        "Error al registrar usuario. Inténtelo nuevamente.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        val btnElegirFoto = findViewById<Button>(R.id.btnElegirFoto)
        val ivFotoPerfil = findViewById<ImageView>(R.id.ivFotoPerfil)

        btnElegirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), 1001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val filePath = getRealPathFromUri(uri)
                if (filePath != null) {
                    rutaImagenSeleccionada = filePath
                    findViewById<ImageView>(R.id.ivFotoPerfil).setImageURI(uri)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ruta de la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    private fun subirImagen(rutaArchivo: String, usuarioId: String) {
        val file = File(rutaArchivo)
        val key = "usuarios/${UUID.randomUUID()}.jpg"

        val uploadObserver = transferUtility.upload(
            "mycontainerrrhh", key, file
        )

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.i("S3", "¡Upload exitoso!")
                    val bucketName = "mycontainerrrhh"
                    val urlImagen = "https://$bucketName.s3.amazonaws.com/$key"

                    lifecycleScope.launch {
                        usuarioDao.actualizarCampoFotoUrl(usuarioId, urlImagen)
                    }
                } else if (state == TransferState.FAILED) {
                    Log.e("S3", "Error al subir imagen")
                }
            }
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
            override fun onError(id: Int, ex: Exception?) {
                Log.e("S3", "Error: ", ex)
            }
        })
    }

    private fun cargarDatosSpinners() {
        lifecycleScope.launch {
            listaAreas = areaDao.obtenerTodos()
            val nombresAreas = mutableListOf("-- Seleccione --").apply {
                addAll(listaAreas.map { it.nombre })
            }
            spinnerArea.adapter = ArrayAdapter(
                this@RegistrarUsuarioActivity,
                android.R.layout.simple_spinner_item,
                nombresAreas
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            listaRoles = rolDao.obtenerTodos()
            val nombresRoles = mutableListOf("-- Seleccione --").apply {
                addAll(listaRoles.map { it.nombre })
            }
            spinnerRol.adapter = ArrayAdapter(
                this@RegistrarUsuarioActivity,
                android.R.layout.simple_spinner_item,
                nombresRoles
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            spinnerRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        layoutDatosTrabajador.visibility = View.GONE
                        return
                    }
                    val rolSeleccionado = listaRoles[position - 1].nombre.lowercase()
                    layoutDatosTrabajador.visibility = if (rolSeleccionado == "trabajador") View.VISIBLE else View.GONE
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun limpiarFormulario(etNombre: EditText, etEmail: EditText, etPassword: EditText) {
        etNombre.text.clear()
        etEmail.text.clear()
        etPassword.text.clear()
        findViewById<EditText>(R.id.etDNI).text.clear()
        findViewById<EditText>(R.id.etTelefono).text.clear()
        findViewById<EditText>(R.id.etDireccion).text.clear()
        findViewById<EditText>(R.id.etFechaIngreso).text.clear()
        spinnerRol.setSelection(0)
        spinnerArea.setSelection(0)
    }
}