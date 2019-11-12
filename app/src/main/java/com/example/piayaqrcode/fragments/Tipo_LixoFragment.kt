package com.example.piayaqrcode.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup

import com.example.piayaqrcode.R
import com.example.piayaqrcode.listener.TipoListener

class Tipo_LixoFragment : Fragment() {

    lateinit var mListener: TipoListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TipoListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement InteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tipo_lixo, container, false)

        val radioGroupTipo = view.findViewById(R.id.radioGroupTipo) as RadioGroup

        val radioPapel = view.findViewById(R.id.radioPapel) as RadioButton
        val radioReciclavel = view.findViewById(R.id.radioReciclavel) as RadioButton
        val radioComum = view.findViewById(R.id.radioComum) as RadioButton
        val radioInfectante = view.findViewById(R.id.radioInfectante) as RadioButton
        val radioQuimico = view.findViewById(R.id.radioQuimico) as RadioButton
        val radioPerfurocortante = view.findViewById(R.id.radioPerfurocortante) as RadioButton

        val prefsTipo = this.activity!!.getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()
        val tipos = prefsTipo.getString("tipo", null)

        when (tipos) {
            "Papel" -> radioPapel.isChecked = true
            "Reciclavel" -> radioReciclavel.isChecked = true
            "Comum" -> radioComum.isChecked = true
            "Infectante" -> radioInfectante.isChecked = true
            "Quimico" -> radioQuimico.isChecked = true
            "Perfurocortante" -> radioPerfurocortante.isChecked = true
        }

        radioGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioPapel -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Papel")
                    edTipo.apply()
                    mListener.getLixeira()
                }
                R.id.radioReciclavel -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Reciclavel")
                    edTipo.apply()
                    mListener.getLixeira()
                }
                R.id.radioComum -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Comum")
                    edTipo.apply()
                    mListener.getLixeira()
                }
                R.id.radioInfectante -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Infectante")
                    edTipo.apply()
                    mListener.getLixeira()
                }
                R.id.radioQuimico -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Quimico")
                    edTipo.apply()
                    mListener.getLixeira()
                }
                R.id.radioPerfurocortante -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Perfurocortante")
                    edTipo.apply()
                    mListener.getLixeira()
                }
            }
        }

        return view
    }


}
