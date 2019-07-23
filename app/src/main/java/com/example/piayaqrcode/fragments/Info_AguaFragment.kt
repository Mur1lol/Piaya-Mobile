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
import kotlinx.android.synthetic.main.fragment_info_agua.*

class Info_AguaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info_agua, container, false)

        val radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup

        val radioEntupimento = view.findViewById(R.id.radioEntupimento) as RadioButton
        val radioTorneira = view.findViewById(R.id.radioTorneira) as RadioButton
        val radioOutroAgua = view.findViewById(R.id.radioOutroAgua) as RadioButton

        val prefsInfo = this.activity!!.getSharedPreferences("info", Context.MODE_PRIVATE)
        var edInfo = prefsInfo.edit()
        val infos = prefsInfo.getString("info", null)

        when (infos) {
            "Entupimento" -> radioEntupimento.isChecked = true
            "Torneira" -> radioTorneira.isChecked = true
            "Outro-Agua" -> radioOutroAgua.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioEntupimento -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Entupimento")
                    edInfo.apply()
                }
                R.id.radioTorneira -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Torneira")
                    edInfo.apply()
                }
                R.id.radioOutroAgua -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Outro-Agua")
                    edInfo.apply()
                }
            }
        }
        return view
    }


}
