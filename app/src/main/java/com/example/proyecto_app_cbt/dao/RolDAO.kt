package com.example.proyecto_app_cbt.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.proyecto_app_cbt.model.Rol

class RolDAO(private val db: SQLiteDatabase) {

    fun insertar(rol: Rol): Long {
        val values = ContentValues().apply {
            put("nombre", rol.nombre)
        }
        return db.insert("rol", null, values)
    }

    fun obtenerTodos(): List<Rol> {
        val lista = mutableListOf<Rol>()
        val cursor = db.rawQuery("SELECT * FROM rol", null)
        while (cursor.moveToNext()) {
            lista.add(
                Rol(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun actualizar(rol: Rol): Int {
        val values = ContentValues().apply {
            put("nombre", rol.nombre)
        }
        return db.update("rol", values, "id=?", arrayOf(rol.id.toString()))
    }

    fun eliminar(id: Int): Int {
        return db.delete("rol", "id=?", arrayOf(id.toString()))
    }
}
