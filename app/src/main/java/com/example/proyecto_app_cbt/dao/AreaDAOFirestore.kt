package com.example.proyecto_app_cbt.dao

import android.util.Log
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Area
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class AreaDAOFirestore {

    private val db = FirestoreProvider.db.collection("areas")

    suspend fun insertar(area: Area): String? {
        return try {
            val docRef = db.add(area).await()
            docRef.id
        } catch (e: Exception) {
            Log.e("Firestore", "Error al insertar área", e)
            null
        }
    }

    suspend fun obtenerTodos(): List<Area> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Area>()?.apply { id = doc.id }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener áreas", e)
            emptyList()
        }
    }

    suspend fun actualizar(area: Area): Boolean {
        return try {
            if (area.id.isNotEmpty()) {
                db.document(area.id).set(area).await()
                true
            } else {
                Log.e("Firestore", "El área debe tener un ID para actualizarse")
                false
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar área", e)
            false
        }
    }

    suspend fun eliminar(id: String): Boolean {
        return try {
            db.document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error al eliminar área", e)
            false
        }
    }
}