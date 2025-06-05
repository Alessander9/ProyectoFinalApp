package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_app_cbt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Tiempo de delay en milisegundos (ejemplo: 2 segundos)
    private val splashDelayMillis: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aquí podrías configurar animaciones, logos, etc. en binding

        // Retraso para simular carga y luego lanzar LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finaliza esta actividad para no volver con "back"
        }, splashDelayMillis)
    }
}
