package com.example.proyecto_app_cbt.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.proyecto_app_cbt.model.Usuario

class UsuarioDAO(private val db: SQLiteDatabase) {

    fun insertar(usuario: Usuario): Long {
        val values = ContentValues().apply {
            put("nombre_completo", usuario.nombre_completo)
            put("correo", usuario.correo)
            put("contraseña", usuario.contraseña)
            put("id_rol", usuario.id_rol)
            put("id_area", usuario.id_area)
        }
        return db.insert("usuario", null, values)
    }

    fun obtenerTodos(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val cursor = db.rawQuery("SELECT * FROM usuario", null)
        while (cursor.moveToNext()) {
            lista.add(
                Usuario(
                    id = cursor.getInt(0),
                    nombre_completo = cursor.getString(1),
                    correo = cursor.getString(2),
                    contraseña = cursor.getString(3),
                    id_rol = cursor.getInt(4),
                    id_area = cursor.getInt(5)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun actualizar(usuario: Usuario): Int {
        val values = ContentValues().apply {
            put("nombre_completo", usuario.nombre_completo)
            put("correo", usuario.correo)
            put("contraseña", usuario.contraseña)
            put("id_rol", usuario.id_rol)
            put("id_area", usuario.id_area)
        }
        return db.update("usuario", values, "id=?", arrayOf(usuario.id.toString()))
    }

    fun eliminar(id: Int): Int {
        return db.delete("usuario", "id=?", arrayOf(id.toString()))
    }
}
