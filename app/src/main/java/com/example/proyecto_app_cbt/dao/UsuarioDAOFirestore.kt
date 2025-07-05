package com.example.proyecto_app_cbt.dao

import android.util.Log
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Usuario
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class UsuarioDAOFirestore {

    private val db = FirestoreProvider.db.collection("usuarios")

    suspend fun insertar(usuario: Usuario): String? {
        return try {
            val docRef = db.add(usuario).await()
            docRef.id
        } catch (e: Exception) {
            Log.e("Firestore", "Error al insertar usuario", e)
            null
        }
    }

    suspend fun obtenerPorId(id: String): Usuario? {
        return try {
            val doc = db.document(id).get().await()
            if (doc.exists()) doc.toObject(Usuario::class.java)?.apply { this.id = doc.id } else null
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener usuario por ID", e)
            null
        }
    }

    suspend fun actualizarCampoFotoUrl(usuarioId: String, fotoUrl: String) {
        try {
            db.document(usuarioId).update("foto_url", fotoUrl).await()
            Log.i("Firestore", "Foto URL actualizada para el usuario $usuarioId")
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar foto URL", e)
        }
    }

    suspend fun actualizarCampoId(usuarioId: String) {
        try {
            db.document(usuarioId).update("id", usuarioId).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar campo ID", e)
        }
    }

    suspend fun obtenerTodos(): List<Usuario> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Usuario>()?.apply { id = doc.id }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener usuarios", e)
            emptyList()
        }
    }

    suspend fun actualizar(usuario: Usuario): Boolean {
        return try {
            if (usuario.id.isNotEmpty()) {
                db.document(usuario.id).set(usuario).await()
                true
            } else {
                Log.e("Firestore", "El usuario debe tener un ID para actualizarse")
                false
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar usuario", e)
            false
        }
    }

    suspend fun autenticar(correo: String, contraseña: String): Usuario? {
        return try {
            val snapshot = db
                .whereEqualTo("correo", correo.uppercase())
                .whereEqualTo("contraseña", contraseña)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                doc.toObject(Usuario::class.java)?.apply { id = doc.id }
            } else null
        } catch (e: Exception) {
            Log.e("Firestore", "Error en autenticación", e)
            null
        }
    }

    suspend fun eliminar(id: String): Boolean {
        return try {
            db.document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error al eliminar usuario", e)
            false
        }
    }

    suspend fun existeCorreo(correo: String, excluirId: String? = null): Boolean {
        return try {
            val snapshot = db.whereEqualTo("correo", correo.uppercase()).get().await()
            snapshot.documents.any { doc ->
                excluirId == null || doc.id != excluirId
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al validar correo duplicado", e)
            false
        }
    }

    suspend fun getRolPorId(rolId: String) = try {
        FirestoreProvider.db.collection("roles").document(rolId).get().await()
    } catch (e: Exception) {
        Log.e("Firestore", "Error al obtener rol por ID", e)
        null
    }
}