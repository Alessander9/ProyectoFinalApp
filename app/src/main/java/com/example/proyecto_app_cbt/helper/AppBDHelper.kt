package com.example.proyecto_app_cbt.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDBHelper(context: Context) : SQLiteOpenHelper(context, "proyectoDAM1.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE rol (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE area (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_completo TEXT NOT NULL,
                correo TEXT NOT NULL,
                contraseña TEXT NOT NULL,
                id_rol INTEGER NOT NULL,
                id_area INTEGER NOT NULL,
                FOREIGN KEY (id_rol) REFERENCES rol(id),
                FOREIGN KEY (id_area) REFERENCES area(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE solicitud (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                fecha_inicio TEXT NOT NULL,
                fecha_fin TEXT NOT NULL,
                motivo TEXT NOT NULL,
                estado TEXT NOT NULL,
                observaciones TEXT,
                fecha_crea TEXT NOT NULL,
                fecha_edita TEXT NOT NULL,
                revisado_por INTEGER,
                FOREIGN KEY (id_usuario) REFERENCES usuario(id),
                FOREIGN KEY (revisado_por) REFERENCES usuario(id)
            );
        """)

        // Datos maestros
        db.execSQL("INSERT INTO rol (nombre) VALUES ('Administrador'), ('Supervisor'), ('Usuario');")
        db.execSQL("INSERT INTO area (nombre) VALUES ('Contabilidad'), ('Logística'), ('Programación');")
        db.execSQL("""
            INSERT INTO usuario (nombre_completo, correo, contraseña, id_rol, id_area)
            VALUES ('Administrador General', 'admin@eva.com', '123', 1, 1);
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS solicitud")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        db.execSQL("DROP TABLE IF EXISTS rol")
        db.execSQL("DROP TABLE IF EXISTS area")
        onCreate(db)
    }
}
