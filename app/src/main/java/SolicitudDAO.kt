//package com.example.proyecto_app_cbt.dao
////
////import android.content.ContentValues
////import android.database.sqlite.SQLiteDatabase
////import com.example.proyecto_app_cbt.model.Solicitud
////import java.time.LocalDate
////
////class SolicitudDAO(private val db: SQLiteDatabase) {
////
////    fun insertar(solicitud: Solicitud): Long {
////        val values = ContentValues().apply {
////            put("id_usuario", solicitud.id_usuario)
////            put("fecha_inicio", solicitud.fecha_inicio.toString())
////            put("fecha_fin", solicitud.fecha_fin.toString())
////            put("motivo", solicitud.motivo)
////            put("estado", solicitud.estado)
////            put("observaciones", solicitud.observaciones)
////            put("fecha_crea", solicitud.fecha_crea.toString())
////            put("fecha_edita", solicitud.fecha_edita.toString())
////            put("revisado_por", solicitud.revisado_por)
////        }
////        return db.insert("solicitud", null, values)
////    }
////
////    fun obtenerTodos(): List<Solicitud> {
////        val lista = mutableListOf<Solicitud>()
////        val cursor = db.rawQuery("SELECT * FROM solicitud", null)
////        while (cursor.moveToNext()) {
////            lista.add(
////                Solicitud(
////                    id = cursor.getInt(0),
////                    id_usuario = cursor.getInt(1),
////                    fecha_inicio = LocalDate.parse(cursor.getString(2)),
////                    fecha_fin = LocalDate.parse(cursor.getString(3)),
////                    motivo = cursor.getString(4),
////                    estado = cursor.getString(5),
////                    observaciones = cursor.getString(6),
////                    fecha_crea = LocalDate.parse(cursor.getString(7)),
////                    fecha_edita = LocalDate.parse(cursor.getString(8)),
////                    revisado_por = cursor.getInt(9)
////                )
////            )
////        }
////        cursor.close()
////        return lista
////    }
////
////    fun actualizar(solicitud: Solicitud): Int {
////        val values = ContentValues().apply {
////            put("id_usuario", solicitud.id_usuario)
////            put("fecha_inicio", solicitud.fecha_inicio.toString())
////            put("fecha_fin", solicitud.fecha_fin.toString())
////            put("motivo", solicitud.motivo)
////            put("estado", solicitud.estado)
////            put("observaciones", solicitud.observaciones)
////            put("fecha_crea", solicitud.fecha_crea.toString())
////            put("fecha_edita", solicitud.fecha_edita.toString())
////            put("revisado_por", solicitud.revisado_por)
////        }
////        return db.update("solicitud", values, "id=?", arrayOf(solicitud.id.toString()))
////    }
////
////    fun eliminar(id: Int): Int {
////        return db.delete("solicitud", "id=?", arrayOf(id.toString()))
////    }
////}