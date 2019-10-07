package com.example.piayaqrcode.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_acontecimento.*


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

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "denuncia.db"
        )
            .allowMainThreadQueries()
            .addMigrations()
            .build()
        denunciaDao = db.denunciaDao()

        limpaCampos()

        bt3.setOnClickListener {
            // Tipo = Problema, Info = Tipo, Lixeira = NULL, Acontecimento = Acontecimento, Local = local

            val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
            val tipo = prefsTipo.getString("tipo", null)
            val prefsInfo = getSharedPreferences("info", Context.MODE_PRIVATE)
            val info = prefsInfo.getString("info", null)
            val lixeira = "--"
            val acontecimento = txtAcontecimento.text.toString()

            val local = txtLocal.text.toString()

            if (local == "") {
                Toast.makeText(this, "Indicar o Local", Toast.LENGTH_SHORT).show()
            }
            else {
                val denuncia = Denuncia(tipo, info, lixeira, acontecimento, local)

                denunciaDao.inserir(denuncia)
                Toast.makeText(this, "Problema Registrado", Toast.LENGTH_SHORT).show()

                supportFragmentManager.beginTransaction().replace(R.id.framezz, TipoFragment()).commit()
                limpaCampos()
                atualizaLista()
            }
        }
        atualizaLista()

        listDenuncias.setOnItemLongClickListener { _, _, position, _ ->
            val denuncia = adapter.getItem(position)
            denunciaDao.apagar(denuncia)
            Toast.makeText(this, "Tarefa Apagada!", Toast.LENGTH_SHORT).show()
            atualizaLista()
            true
        }
        atualizaLista()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.framezz, TipoFragment()).commit()
        }

        val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
//        val tipos = prefsTipo.getString("tipo", null)
//
//        val prefsInfo = getSharedPreferences("info", Context.MODE_PRIVATE)
//        val infos = prefsInfo.getString("info", null)
//
//        var local = txtLocal.text.toString()

//        configuraRetrofit()
//        cadastraResposta(tipos, local, infos)

        txtLocal.threshold = 1

        val adapter = ArrayAdapter.createFromResource(this, R.array.locais, android.R.layout.simple_dropdown_item_1line)
        txtLocal.setAdapter(adapter)

        txtLocal.setOnClickListener {
            txtLocal.showDropDown()
        }

        local.setOnLongClickListener {
            bt1.visibility = View.VISIBLE
            bt2.visibility = View.VISIBLE
            bt3.visibility = View.VISIBLE

            true
        }

        bt1.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.framezz, TipoFragment()).commit()
            bt1.visibility = View.INVISIBLE
            bt2.visibility = View.INVISIBLE
            bt3.visibility = View.INVISIBLE
        }

        bt2.setOnClickListener {
            val tipos = prefsTipo.getString("tipo", null)
            if(tipos!= null) { trocaTela(tipos) }
        }

//        bt3.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
//        }

        btScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            intentIntegrator.setPrompt("SCAN")
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
        val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()

        val prefsInfo = getSharedPreferences("info", Context.MODE_PRIVATE)
        var edInfo = prefsInfo.edit()

        edTipo.putString("tipo", "")
        edTipo.apply()

        edInfo.putString("info", "")
        edInfo.apply()

        txtLocal.setText("")
        txtAcontecimento.setText("")

        bt3.visibility = View.INVISIBLE
    }

    fun configuraRetrofit() {
        //https://localhost:44375/api/Formularios
        retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(FormularioService::class.java)
    }

//    fun cadastraResposta(tipo, local, acontecimento, info) {
//        service.getFormulario(tipo, local, "", info).enqueue(object: Callback<FormularioResponse> {
//            override fun onFailure(call: Call<FormularioResponse>, t: Throwable) { }
//            override fun onResponse(call: Call<FormularioResponse>, response: Response<FormularioResponse>) { }
//        })
//    }

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
        bt3.visibility = View.VISIBLE
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
