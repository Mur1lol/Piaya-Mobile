package com.example.piayaqrcode.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piayaqrcode.R
import com.example.piayaqrcode.fragments.*
import com.example.piayaqrcode.listener.TipoListener
import com.example.piayaqrcode.servicos.FormularioService
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.ArrayAdapter
import androidx.room.Room
import com.example.piayaqrcode.bd.AppDatabase
import com.example.piayaqrcode.bd.dao.DenunciaDao
import com.example.piayaqrcode.entidades.Denuncia
import com.example.piayaqrcode.entidades.FormularioResponse
import kotlinx.android.synthetic.main.fragment_acontecimento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), TipoListener {

    lateinit var db: AppDatabase
    lateinit var denunciaDao: DenunciaDao
    lateinit var adapter: ArrayAdapter<Denuncia>

    lateinit var retrofit: Retrofit
    lateinit var service: FormularioService

    private val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.framezz, TipoFragment()).commit()
        }

        limpaCampos()
        configuraRetrofit()

        //Banco Local - (Excluir Depois??)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "denuncia.db"
        )
            .allowMainThreadQueries()
            .addMigrations()
            .build()
        denunciaDao = db.denunciaDao()

        //Campo de busca por Locais
        txtLocal.threshold = 1 //Definindo a quantidade minima de letras para a busca funcionar
        val adapter = ArrayAdapter.createFromResource(this, R.array.locais, android.R.layout.simple_dropdown_item_1line)
        txtLocal.setAdapter(adapter) //Colocando os dados dentro do TxtLocal
        txtLocal.setOnClickListener {
            txtLocal.showDropDown() //Mostrando itens da busca
        }

        btCadastra.setOnClickListener { //Cadastrar Denuncia no banco
            //Mudar O SharedPreferences `tipo=>problema && info=>tipo`
            val prefsProblema = getSharedPreferences("tipo", Context.MODE_PRIVATE)
            val problema = prefsProblema.getString("tipo", null)

            val prefsTipo = getSharedPreferences("info", Context.MODE_PRIVATE)
            val tipo = prefsTipo.getString("info", null)

            val lixeira = "--"
            val acontecimento = txtAcontecimento.text.toString()
            val local = txtLocal.text.toString()

            if (local == "") {
                Toast.makeText(this, "Indicar o Local", Toast.LENGTH_SHORT).show()
            }
            else {
                val denuncia = Denuncia(problema, tipo, lixeira, acontecimento, local)
                denunciaDao.inserir(denuncia) //Inserir no banco local

                cadastraResposta(problema, tipo, lixeira,acontecimento, local) //Inserir no banco online

                Toast.makeText(this, "Problema Registrado", Toast.LENGTH_SHORT).show()

                supportFragmentManager.beginTransaction().replace(R.id.framezz, TipoFragment()).commit()
                limpaCampos()
                atualizaLista()
            }
        }
        atualizaLista()

        btScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            intentIntegrator.setPrompt("PIAYA SCAN")
            intentIntegrator.setCameraId(0)
            intentIntegrator.initiateScan()
        }
    }

    fun atualizaLista(){
        val denuncias = denunciaDao.buscaTodas()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, denuncias)
        listDenuncias.adapter = adapter
    }

    fun limpaCampos() {
        val prefsProblema = getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edProblema = prefsProblema.edit()

        val prefsTipo = getSharedPreferences("info", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()

        edProblema.putString("tipo", "")
        edProblema.apply()

        edTipo.putString("info", "")
        edTipo.apply()

        txtLocal.setText("")

        btCadastra.visibility = View.INVISIBLE
    }

    fun configuraRetrofit() {
        //http://localhost/App/SERVER/server.php
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.6/App/SERVER/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(FormularioService::class.java)
    }

    fun cadastraResposta(problema:String, tipo: String, lixeira: String, acontecimento: String, local: String) {
        service.getFormulario(problema,tipo,lixeira,acontecimento,local, 0).enqueue(object: Callback<FormularioResponse> {
            override fun onFailure(call: Call<FormularioResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falha ao conectar ao servidor!", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<FormularioResponse>, response: Response<FormularioResponse>) {
                var formulario: FormularioResponse = response.body()!!
                if(response.body()!=null) {
                    Log.e("FOI", formulario.mensagem)
                    Toast.makeText(this@MainActivity, formulario.mensagem, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (intentResult.contents != null) {
            alert(intentResult.contents.toString())
        } else {
            alert("Scan cancelado")
        }
    }

    fun alert(msg: String) {
        var local = false
        val items = arrayOf("Sala 1","Sala 2","Sala 3","Sala 4","Sala 5","Sala 6","Sala 7",
            "Sala 8","Sala 9","Sala 10","Sala 11","Sala 12",
            "Laboratório 1 - Informática", "Laboratório 2 - Informática", "Laboratório 3 - Informática",
            "Laboratório 4 - Informática", "Laboratório 5 - Informática", "Laboratório Biologia",
            "Laboratório Química", "Laboratório Física")

        for (item in items) {
            if (msg == item) {
                local = true
            }
        }

        if (local) {
            txtLocal.setText(msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(applicationContext, getString(R.string.invalido), Toast.LENGTH_LONG).show()
        }
    }

    override fun getTipo(tipos: String) {
        trocaTela(tipos)
    }

    override fun getInfo() {
        supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
        btCadastra.visibility = View.VISIBLE
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
