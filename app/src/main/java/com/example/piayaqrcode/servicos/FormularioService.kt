package com.example.piayaqrcode.servicos

import com.example.piayaqrcode.entidades.FormularioResponse
import retrofit2.Call
import retrofit2.http.*

interface FormularioService {
    @Headers("Accept: application/json")
    @GET("server.php")
    fun getFormulario(

    @Query("problema")
    problema: String,

    @Query("tipo")
    tipo: String,

    @Query("lixeira")
    lixeira: String?,

    @Query("acontecimento")
    acontecimento: String,

    @Query("local")
    local: String,

    @Query("status")
    status: Int

    ): Call<FormularioResponse>
}



