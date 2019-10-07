package com.example.piayaqrcode.bd.dao

import androidx.room.*
import com.example.piayaqrcode.entidades.Denuncia

@Dao
interface DenunciaDao {
    @Query("SELECT * FROM denuncias")
    fun buscaTodas(): List<Denuncia>

    @Insert
    fun inserir(denuncia: Denuncia)

    @Delete
    fun apagar(denuncia: Denuncia)
}