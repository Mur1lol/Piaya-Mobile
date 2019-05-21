package com.example.piayaqrcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.widget.Toast
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       btnScan.setOnClickListener {
           val intentIntegrator = IntentIntegrator(activity)
           intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
           intentIntegrator.setPrompt("SCAN")
           intentIntegrator.setCameraId(0)
           intentIntegrator.initiateScan()
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (intentResult != null) {
            if (intentResult.contents != null) {
                alert(intentResult.contents.toString())
            } else {
                alert("Scan cancelado")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun alert(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        txtLocal.text = msg
    }
}
