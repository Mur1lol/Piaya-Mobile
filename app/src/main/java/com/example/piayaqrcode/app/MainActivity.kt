package com.example.piayaqrcode.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.widget.Toast
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.piayaqrcode.R
import com.example.piayaqrcode.bd.AppDatabase
import com.example.piayaqrcode.bd.dao.FormularioDao
import com.example.piayaqrcode.entidades.Formulario
import com.example.piayaqrcode.ui.FormularioAdapter


class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var formularioDao: FormularioDao
    lateinit var adapter: FormularioAdapter

    private val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "formulario"
        )
            .allowMainThreadQueries()
            .addMigrations()
            .build()
        formularioDao = db.formularioDao()


        btnScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setCameraId(0)
            intentIntegrator.initiateScan()
        }

        configuraRecyclerView()
        atualizaLista()
    }

    fun atualizaLista() {
        val tarefas = formularioDao.buscaTodas()
        adapter = FormularioAdapter(tarefas.toList())
        listFormulario.adapter = adapter
    }

    fun configuraRecyclerView(formulario: List<Formulario>) {
        adapter = FormularioAdapter(formulario)
        listFormulario.adapter = adapter
        listFormulario.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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

    private fun alert(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        txtLocal.text = msg
    }
}
