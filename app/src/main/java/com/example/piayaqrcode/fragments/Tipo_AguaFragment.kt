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

class Tipo_AguaFragment : Fragment() {

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tipo_agua, container, false)

        val radioGroup = view.findViewById(R.id.radioGroupAgua) as RadioGroup

        val radioEntupimento = view.findViewById(R.id.radioEntupimento) as RadioButton
        val radioTorneira = view.findViewById(R.id.radioTorneira) as RadioButton
        val radioTorneiraDefeito = view.findViewById(R.id.radioTorneiraDefeito) as RadioButton
        val radioFaltaAgua = view.findViewById(R.id.radioFaltaAgua) as RadioButton

        val prefsTipo = this.activity!!.getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()
        val tipos = prefsTipo.getString("tipo", null)

        when (tipos) {
            "Entupimento" -> radioEntupimento.isChecked = true
            "Torneira aberta" -> radioTorneira.isChecked = true
            "Torneiras com defeito" -> radioTorneiraDefeito.isChecked = true
            "Falta de agua" -> radioFaltaAgua.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioEntupimento -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Entupimento")
                    edTipo.apply()
                    mListener.getTipo()
                }
                R.id.radioTorneira -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Torneira aberta")
                    edTipo.apply()
                    mListener.getTipo()
                }
                R.id.radioTorneiraDefeito -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Torneiras com defeito")
                    edTipo.apply()
                    mListener.getTipo()
                }
                R.id.radioFaltaAgua -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Falta de agua")
                    edTipo.apply()
                    mListener.getTipo()
                }
            }
        }
        return view
    }


}
