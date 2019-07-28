package com.example.piayaqrcode.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piayaqrcode.R
import com.example.piayaqrcode.entidades.FormularioResponse
import com.example.piayaqrcode.fragments.*
import com.example.piayaqrcode.listener.TipoListener
import com.example.piayaqrcode.servicos.FormularioService
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), TipoListener {

    lateinit var retrofit: Retrofit
    lateinit var service: FormularioService

    private val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
//        val tipos = prefsTipo.getString("tipo", null)

//        val prefsInfo = getSharedPreferences("info", Context.MODE_PRIVATE)
//        val infos = prefsInfo.getString("info", null)

//        configuraRetrofit()
//        cadastraResposta()


        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.framezz, TipoFragment())
                .commit()
        }

        bt1.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.framezz, TipoFragment())
                .commit()
        }

        bt2.setOnClickListener {
            val tipos = prefsTipo.getString("tipo", null)
            if(tipos!= null) {
                trocaTela(tipos)
            }
        }

        bt3.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
        }

        btScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setCameraId(0)
            intentIntegrator.initiateScan()
        }
    }

    fun configuraRetrofit() {
        //https://localhost:44375/api/Formularios
        retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:44375/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(FormularioService::class.java)
    }

    fun cadastraResposta() {
        service.getFormulario("1","1","12","").enqueue(object: Callback<FormularioResponse> {
            override fun onFailure(call: Call<FormularioResponse>, t: Throwable) {
                Log.e("ERRO", ""+t)
            }

            override fun onResponse(call: Call<FormularioResponse>, response: Response<FormularioResponse>) {
                val articles = response.body()
                Log.e("FUCK", "YEAH")

                Log.e("MENSAGEM", articles?.msg)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (intentResult != null) {
            if (intentResult.contents != null) {
                alert(intentResult.contents.toString())
            } else {
                alert("Scan cancelado")
            }
        }
    }

    fun alert(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        txtLocal.setText(msg)
    }

    override fun getTipo(tipos: String) {
        trocaTela(tipos)
    }

    override fun getInfo() {
        supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
    }

    fun trocaTela(tipos: String) {
        when (tipos) {
            "Lixo" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Info_LixoFragment()).commit()
            "Luz" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Info_LuzFragment()).commit()
            "Agua" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Info_AguaFragment()).commit()
            "Outro" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
        }
    }
}
