package com.example.piayaqrcode.bd.dao

import androidx.room.*
import com.example.piayaqrcode.entidades.Formulario

@Dao
interface FormularioDao {
    @Query("SELECT * FROM formularios")
    fun buscaTodas(): List<Formulario>

    @Query("SELECT * FROM formularios WHERE id = :id LIMIT 1")
    fun buscaFormulario(id: Int): Formulario

    @Update
    fun atualizar(formulario: Formulario)

    @Insert
    fun inserir(formulario: Formulario): Long

    @Delete
    fun apagar(formulario: Formulario)

}