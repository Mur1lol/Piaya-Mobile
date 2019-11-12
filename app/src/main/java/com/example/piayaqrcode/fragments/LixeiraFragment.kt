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

class LixeiraFragment : Fragment(){
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
        val view = inflater.inflate(R.layout.fragment_lixeira, container, false)

        val radioGroupLixeira = view.findViewById(R.id.radioGroupLixeira) as RadioGroup

        val radioLixeiraReciclavel = view.findViewById(R.id.radioLixeiraReciclavel) as RadioButton
        val radioLixeiraComum = view.findViewById(R.id.radioLixeiraComum) as RadioButton

        val prefsLixeira = this.activity!!.getSharedPreferences("lixeira", Context.MODE_PRIVATE)
        var edLixeira = prefsLixeira.edit()

        radioGroupLixeira.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLixeiraReciclavel -> { //abrir AcontecimentoFragment
                    edLixeira.putString("lixeira", "Reciclavel")
                    edLixeira.apply()
                    mListener.getTipo()
                }
                R.id.radioLixeiraComum -> { //abrir AcontecimentoFragment
                    edLixeira.putString("lixeira", "Comum")
                    edLixeira.apply()
                    mListener.getTipo()
                }
            }
        }

        return view
    }
}