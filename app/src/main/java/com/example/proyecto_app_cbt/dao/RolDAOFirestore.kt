package com.example.proyecto_app_cbt.dao

import android.util.Log
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Rol
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class RolDAOFirestore {

    private val db = FirestoreProvider.db.collection("roles")

    suspend fun insertar(rol: Rol): String? {
        return try {
            val docRef = db.add(rol).await()
            docRef.id
        } catch (e: Exception) {
            Log.e("Firestore", "Error al insertar rol", e)
            null
        }
    }

    suspend fun obtenerTodos(): List<Rol> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Rol>()?.apply { id = doc.id }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener roles", e)
            emptyList()
        }
    }

    suspend fun actualizar(rol: Rol): Boolean {
        return try {
            if (rol.id.isNotEmpty()) {
                db.document(rol.id).set(rol).await()
                true
            } else {
                Log.e("Firestore", "El rol debe tener un ID para actualizarse")
                false
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar rol", e)
            false
        }
    }

    suspend fun eliminar(id: String): Boolean {
        return try {
            db.document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error al eliminar rol", e)
            false
        }
    }

    suspend fun obtenerPorId(id: String): Rol? {
        return try {
            val doc = db.document(id).get().await()
            doc.toObject<Rol>()?.apply { this.id = doc.id }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener rol por ID", e)
            null
        }
    }
}