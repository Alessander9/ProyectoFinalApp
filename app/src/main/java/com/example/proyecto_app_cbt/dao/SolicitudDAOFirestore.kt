package com.example.proyecto_app_cbt.dao

import android.util.Log
import com.example.proyecto_app_cbt.helper.FirestoreProvider
import com.example.proyecto_app_cbt.model.Solicitud
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class SolicitudDAOFirestore {

    private val db = FirestoreProvider.db.collection("solicitudes")

    suspend fun insertar(solicitud: Solicitud): String? {
        return try {
            val docRef = db.add(solicitud).await()
            docRef.id
        } catch (e: Exception) {
            Log.e("Firestore", "Error al insertar solicitud", e)
            null
        }
    }

    suspend fun obtenerTodos(): List<Solicitud> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Solicitud>()?.apply { id = doc.id }
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener solicitudes", e)
            emptyList()
        }
    }

    suspend fun obtenerPorId(id: String): Solicitud? {
        return try {
            val docSnapshot = db.document(id).get().await()
            if (docSnapshot.exists()) {
                docSnapshot.toObject<Solicitud>()?.apply { this.id = docSnapshot.id }
            } else null
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener solicitud por ID", e)
            null
        }
    }

    suspend fun actualizar(solicitud: Solicitud): Boolean {
        return try {
            if (solicitud.id.isNotEmpty()) {
                db.document(solicitud.id).set(solicitud).await()
                true
            } else {
                Log.e("Firestore", "La solicitud debe tener un ID para actualizarse")
                false
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al actualizar solicitud", e)
            false
        }
    }

    suspend fun eliminar(id: String): Boolean {
        return try {
            db.document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error al eliminar solicitud", e)
            false
        }
    }
}