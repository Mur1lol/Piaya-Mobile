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
import com.example.piayaqrcode.entidades.Formulario
import com.example.piayaqrcode.entidades.FormularioResponse
import kotlinx.android.synthetic.main.fragment_acontecimento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), TipoListener {

    private lateinit var retrofit: Retrofit
    private lateinit var service: FormularioService

    private val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.framezz, ProblemaFragment()).commit()

        limpaCampos()
        configuraRetrofit()

        //Campo de busca por Locais
        //Definindo a quantidade minima de letras para a busca funcionar
        txtLocal.threshold = 1
        val adapter = ArrayAdapter.createFromResource(this, R.array.locais, android.R.layout.simple_dropdown_item_1line)
        txtLocal.setAdapter(adapter) //Colocando os dados dentro do TxtLocal
        txtLocal.setOnClickListener {
            txtLocal.showDropDown() //Mostrando itens da busca
        }

        //Cadastrar Denuncia no banco
        btCadastra.setOnClickListener {
            val prefsProblema = getSharedPreferences("problema", Context.MODE_PRIVATE)
            val problema = prefsProblema.getString("problema", null)

            val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
            val tipo = prefsTipo.getString("tipo", null)

            val prefsLixeira = getSharedPreferences("lixeira", Context.MODE_PRIVATE)
            val lixeira = prefsLixeira.getString("lixeira", "--")

            val acontecimentoPadrao = txtAcontecimento.text.toString()
            val nfdNormalizedString = Normalizer.normalize(acontecimentoPadrao, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            val acontecimento = pattern.matcher(nfdNormalizedString).replaceAll("")

            val local = txtLocal.text.toString()

            val denuncia = Formulario(problema,tipo, lixeira, acontecimento, local, 0)
            Log.e("ABC", denuncia.toString())

            if (local == "") {
                Toast.makeText(this, "Indicar o Local", Toast.LENGTH_SHORT).show()
            }
            else if (acontecimento == "") {
                Toast.makeText(this, "Informar o ocorrido", Toast.LENGTH_SHORT).show()
            }
            else {
                var teste = false
                val items = arrayOf("Sala 01","Sala 02","Sala 03","Sala 04","Sala 05","Sala 06","Sala 07",
                    "Sala 08","Sala 09","Sala 10","Sala 11","Sala 12",
                    "Laboratorio 01 - Informatica", "Laboratorio 02 - Informatica", "Laboratorio 03 - Informatica",
                    "Laboratorio 04 - Informatica", "Laboratorio 05 - Informatica", "Laboratorio Biologia",
                    "Laboratorio Quimica", "Laboratorio Fisica")

                for (item in items) {
                    if (local == item) {
                        teste = true
                    }
                }
                if (teste) {
                    cadastraResposta(problema, tipo, lixeira, acontecimento, local) //Inserir no banco online
                    supportFragmentManager.beginTransaction().replace(R.id.framezz, ProblemaFragment()).commit()
                    limpaCampos()
                }
                else {
                    Toast.makeText(applicationContext, getString(R.string.invalido), Toast.LENGTH_LONG).show()
                }
            }
        }

        btVoltar.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.framezz, ProblemaFragment()).commit()
            val prefsProblema = getSharedPreferences("problema", Context.MODE_PRIVATE)
            val edProblema = prefsProblema.edit()

            val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
            val edTipo = prefsTipo.edit()

            val prefsLixeira = getSharedPreferences("lixeira", Context.MODE_PRIVATE)
            val edLixeira = prefsLixeira.edit()

            edProblema.putString("problema", "")
            edProblema.apply()

            edTipo.putString("tipo", "")
            edTipo.apply()

            edLixeira.putString("lixeira", "--")
            edLixeira.apply()

            btVoltar.visibility = View.INVISIBLE
            btCadastra.visibility = View.INVISIBLE
        }

        btScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            intentIntegrator.setPrompt("PIAYA SCAN")
            intentIntegrator.setCameraId(0)
            intentIntegrator.initiateScan()
        }
    }

    private fun limpaCampos() {
        val prefsProblema = getSharedPreferences("problema", Context.MODE_PRIVATE)
        val edProblema = prefsProblema.edit()

        val prefsTipo = getSharedPreferences("tipo", Context.MODE_PRIVATE)
        val edTipo = prefsTipo.edit()

        val prefsLixeira = getSharedPreferences("lixeira", Context.MODE_PRIVATE)
        val edLixeira = prefsLixeira.edit()

//        val prefsAcontecimento = getSharedPreferences("acontecimento", Context.MODE_PRIVATE)
//        var edAcontecimento = prefsAcontecimento.edit()
//        edAcontecimento.putString("acontecimento", "")
//        edAcontecimento.apply()

        edProblema.putString("problema", "")
        edProblema.apply()

        edTipo.putString("tipo", "")
        edTipo.apply()

        edLixeira.putString("lixeira", "--")
        edLixeira.apply()

        txtLocal.setText("")

        btVoltar.visibility = View.INVISIBLE
        btCadastra.visibility = View.INVISIBLE
    }

    private fun configuraRetrofit() {
        //http://10.1.1.105:8000/tcc/js/cadastra.json.php
        retrofit = Retrofit.Builder()
            .baseUrl("http://10.1.1.104:8000/tcc/js/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(FormularioService::class.java)
    }

    private fun cadastraResposta(problema:String?, tipo: String?, lixeira: String?, acontecimento: String?, local: String) {
        service.getFormulario(problema,tipo,lixeira,acontecimento,local, 0).enqueue(object: Callback<FormularioResponse> {
            override fun onFailure(call: Call<FormularioResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falha ao conectar ao servidor!", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<FormularioResponse>, response: Response<FormularioResponse>) {
                val formulario: FormularioResponse = response.body()!!
                if(response.body()!=null) {
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

    private fun alert(msg: String) {
        var local = false
        val items = arrayOf("Sala 01","Sala 02","Sala 03","Sala 04","Sala 05","Sala 06","Sala 07",
            "Sala 08","Sala 09","Sala 10","Sala 11","Sala 12",
            "Laboratorio 01 - Informatica", "Laboratorio 02 - Informatica", "Laboratorio 03 - Informatica",
            "Laboratorio 04 - Informatica", "Laboratorio 05 - Informatica", "Laboratorio Biologia",
            "Laboratorio Quimica", "Laboratorio Fisica")

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

    override fun getProblema(problemas: String) {
        trocaTela(problemas)
        btVoltar.visibility = View.VISIBLE
    }

    override fun getTipo() {
        supportFragmentManager.beginTransaction().replace(R.id.framezz, AcontecimentoFragment()).commit()
        btCadastra.visibility = View.VISIBLE
    }

    override fun getLixeira() {
        supportFragmentManager.beginTransaction().replace(R.id.framezz, LixeiraFragment()).commit()
    }

    private fun trocaTela(problemas: String) {
        when (problemas) {
            "Descarte incorreto de lixo ou residuos" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Tipo_LixoFragment()).commit()
            "Uso inadequado da luz" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Tipo_LuzFragment()).commit()
            "Problemas relacionados a agua" -> supportFragmentManager.beginTransaction().replace(R.id.framezz, Tipo_AguaFragment()).commit()
        }
    }
}
