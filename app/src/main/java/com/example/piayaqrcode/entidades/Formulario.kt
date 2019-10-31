package com.example.piayaqrcode.entidades

data class Formulario(
    var problema: String,
    var tipo: String,
    var lixeira: String?,
    var acontecimento: String,
    var local: String,
    var status: Int
)