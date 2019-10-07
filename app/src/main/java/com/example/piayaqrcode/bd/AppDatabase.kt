package com.example.piayaqrcode.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piayaqrcode.bd.dao.DenunciaDao
import com.example.piayaqrcode.entidades.Denuncia

@Database(entities = arrayOf(Denuncia::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun denunciaDao(): DenunciaDao
}