package com.example.proyecto_app_cbt.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.proyecto_app_cbt.model.Area

class AreaDAO(private val db: SQLiteDatabase) {
    fun insertar(area: Area): Long {
        val values = ContentValues().apply {
            put("nombre", area.nombre)
        }
        return db.insert("area", null, values)
    }

    fun obtenerTodos(): List<Area> {
        val lista = mutableListOf<Area>()
        val cursor = db.rawQuery("SELECT * FROM area", null)
        while (cursor.moveToNext()) {
            lista.add(
                Area(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun actualizar(area: Area): Int {
        val values = ContentValues().apply {
            put("nombre", area.nombre)
        }
        return db.update("area", values, "id=?", arrayOf(area.id.toString()))
    }

    fun eliminar(id: Int): Int {
        return db.delete("area", "id=?", arrayOf(id.toString()))
    }
}