package com.example.proyecto_app_cbt

import android.content.Intent // ¡IMPORTANTE! Asegúrate de que esta línea esté presente
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import com.example.proyecto_app_cbt.databinding.ActivityLoginBinding

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

        // --- INICIO DE LOS CAMBIOS PARA EL BOTÓN DE INICIO DE SESIÓN ---

        // Asumiendo que tu botón de inicio de sesión en activity_login.xml tiene el ID 'login_button'.
        // Si tiene un ID diferente (por ejemplo, 'myLoginButton'), DEBES CAMBIAR 'binding.loginButton'
        // por 'binding.myLoginButton' aquí.
        binding.loginButton.setOnClickListener {
            // Este código se ejecutará cuando el botón sea clickeado
            val intent = Intent(this, Actividad_principal::class.java)
            startActivity(intent)

            // Opcional: Si quieres que el usuario no pueda volver a la pantalla de login con el botón Atrás,
            // descomenta la siguiente línea:
            // finish()
        }
        // --- FIN DE LOS CAMBIOS PARA EL BOTÓN DE INICIO DE SESIÓN ---
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
        // Puedes liberar recursos aquí si quieres, o en onDestroy
        // releasePlayer()
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
