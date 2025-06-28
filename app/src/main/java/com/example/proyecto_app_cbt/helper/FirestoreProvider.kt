package com.example.proyecto_app_cbt.helper

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreProvider {
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}