package com.example.proyecto_app_cbt

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import kotlinx.coroutines.launch
class RegistrarUsuarioActivity : BaseActivity() {


    private lateinit var spinnerArea: Spinner
    private val areaDao = AreaDAOFirestore()
    private var listaAreas: List<Area> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla el layout que creaste
        setContentView(R.layout.activity_registrar_usuario)

        // 1. Vincula el Spinner
        spinnerArea = findViewById<Spinner>(R.id.spinnerArea)

        // 2. Carga las áreas de Firestore y arma el Adapter
        lifecycleScope.launch {
            listaAreas = areaDao.obtenerTodos()
            val nombres = listaAreas.map { it.nombre }
            val adapter = ArrayAdapter(
                this@RegistrarUsuarioActivity,
                android.R.layout.simple_spinner_item,
                nombres
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerArea.adapter = adapter

            // 3. Manejar selección opcional
            spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val areaSeleccionada = listaAreas[position]
                    Toast.makeText(
                        this@RegistrarUsuarioActivity,
                        "Área seleccionada: ${areaSeleccionada.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Guarda o usa areaSeleccionada según sea necesario
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No hay selección
                }
    }}}
}
