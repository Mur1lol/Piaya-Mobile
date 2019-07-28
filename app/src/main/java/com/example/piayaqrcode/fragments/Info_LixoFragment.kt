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
import kotlinx.android.synthetic.main.fragment_info_lixo.*

class Info_LixoFragment : Fragment() {

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

        val view = inflater.inflate(R.layout.fragment_info_lixo, container, false)

        val radioGroupTipo = view.findViewById(R.id.radioGroupTipo) as RadioGroup
        //val radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup

        val radioPapel = view.findViewById(R.id.radioPapel) as RadioButton
        val radioReciclavel = view.findViewById(R.id.radioReciclavel) as RadioButton
        val radioComum = view.findViewById(R.id.radioComum) as RadioButton
        val radioInfectante = view.findViewById(R.id.radioInfectante) as RadioButton
        val radioQuimico = view.findViewById(R.id.radioQuimico) as RadioButton
        val radioPerfurocortante = view.findViewById(R.id.radioPerfurocortante) as RadioButton

        val prefsInfo = this.activity!!.getSharedPreferences("info", Context.MODE_PRIVATE)
        var edInfo = prefsInfo.edit()
        val infos = prefsInfo.getString("info", null)

        when (infos) {
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
                    edInfo.putString("info", "Papel")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioReciclavel -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Reciclavel")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioComum -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Comum")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioInfectante -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Infectante")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioQuimico -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Quimico")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioPerfurocortante -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Perfurocortante")
                    edInfo.apply()
                    mListener.getInfo()
                }
            }
        }

//        radioGroup.setOnCheckedChangeListener { _, checkedId ->
//            // checkedId is the RadioButton selected
//            when (checkedId) {
//                R.id.radioLixeiraReciclavel -> { //abrir AcontecimentoFragment
////                    edInfo.putString("info", "Entupimento")
////                    edInfo.apply()
//                }
//                R.id.radioLixeiraNaoReciclavel -> { //abrir AcontecimentoFragment
////                    edInfo.putString("info", "Torneira")
////                    edInfo.apply()
//                }
//            }
//        }

        return view
    }


}
