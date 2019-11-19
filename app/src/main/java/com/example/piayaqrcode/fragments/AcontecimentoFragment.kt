package com.example.piayaqrcode.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.example.piayaqrcode.R

class AcontecimentoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_acontecimento, container, false)

//        val txtAcontecimento = view.findViewById(R.id.txtAcontecimento) as EditText
//        val prefsAcontecimento = this.activity!!.getSharedPreferences("acontecimento", Context.MODE_PRIVATE)
//        var edAcontecimento = prefsAcontecimento.edit()
//        edAcontecimento.putString("acontecimento", txtAcontecimento.text.toString())
//        edAcontecimento.apply()

        return view
    }


}
