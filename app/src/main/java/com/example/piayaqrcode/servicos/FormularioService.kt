package com.example.piayaqrcode.servicos


import com.example.piayaqrcode.entidades.FormularioResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FormularioService {
    //@Headers("Accept: application/json")
    //@FormUrlEncoded
    @POST("api/Formularios")
    fun getFormulario(

        @Query("tipo")
        tipo: String,

        @Query("local")
        local: String,

        @Query("acontecimento")
        acontecimento: String,

        @Query("info")
        info: String?

    ): Call<FormularioResponse>
}