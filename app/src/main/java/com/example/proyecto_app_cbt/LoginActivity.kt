package com.example.proyecto_app_cbt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import com.example.proyecto_app_cbt.dao.UsuarioDAO
import com.example.proyecto_app_cbt.databinding.ActivityLoginBinding
import com.example.proyecto_app_cbt.helper.AppDBHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var exoPlayer: ExoPlayer? = null

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePlayer()

        binding.loginButton.setOnClickListener {
            val correo = binding.username.text.toString().trim()
            val contraseña = binding.password.text.toString().trim()

            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = AppDBHelper(this)
            val usuarioDAO = UsuarioDAO(dbHelper.readableDatabase)
            val usuarios = usuarioDAO.obtenerTodos()

            val userVal = usuarios.find { it.correo == correo && it.contraseña == contraseña }

            if (userVal != null) {
                val intent = Intent(this, Actividad_principal::class.java)
                val prefs = getSharedPreferences("dataUser", MODE_PRIVATE)
                prefs.edit().putString("fullName", userVal.nombre_completo).apply()
                prefs.edit().putInt("rolId", userVal.id_rol).apply()

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(this).build().also { player ->
                binding.videoBackgroundPlayerView.player = player

                player.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e(TAG, "ExoPlayer error: ", error)
                    }
                })

                try {
                    val videoUri = Uri.parse("android.resource://$packageName/${R.raw.video1}")
                    val mediaItem = MediaItem.fromUri(videoUri)

                    player.setMediaItem(mediaItem)
                    player.repeatMode = Player.REPEAT_MODE_ONE
                    player.playWhenReady = true
                    player.volume = 0f
                    player.prepare()
                } catch (e: Exception) {
                    Log.e(TAG, "Error al cargar el video", e)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        exoPlayer?.play()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
