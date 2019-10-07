package com.example.piayaqrcode.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "denuncias")
data class Denuncia(
    var problema: String,
    var tipo: String,
    var lixeira: String?,
    var acontecimento: String,
    var local: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val tarefaCompleta get() = "$problema - $tipo - $lixeira - $acontecimento - $local"
    override fun toString() = tarefaCompleta
}