package com.example.piayaqrcode.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formularios")
data class Formulario(
    var tipo: String,
    var acontecimento: String,
    val local: String,
    var info: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}