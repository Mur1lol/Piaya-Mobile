package com.example.piayaqrcode.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piayaqrcode.bd.dao.FormularioDao
import com.example.piayaqrcode.entidades.Formulario

@Database(entities = arrayOf(Formulario::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun formularioDao(): FormularioDao
}
