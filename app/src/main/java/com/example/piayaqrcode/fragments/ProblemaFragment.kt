package com.example.piayaqrcode.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.piayaqrcode.R
import android.widget.RadioGroup
import com.example.piayaqrcode.listener.TipoListener

class ProblemaFragment : Fragment() {

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

        val view = inflater.inflate(R.layout.fragment_problema, container, false)

        val radioGroup = view.findViewById(R.id.radioTipo) as RadioGroup

        val radioLixo = view.findViewById(R.id.radioLixo) as RadioButton
        val radioLuz = view.findViewById(R.id.radioLuz) as RadioButton
        val radioAgua = view.findViewById(R.id.radioAgua) as RadioButton

        val prefsProblema = this.activity!!.getSharedPreferences("problema", Context.MODE_PRIVATE)
        var edProblema = prefsProblema.edit()
        val problemas = prefsProblema.getString("problema", null)

        when (problemas) {
            "Descarte incorreto de lixo ou residuos" -> radioLixo.isChecked = true
            "Uso inadequado da luz" -> radioLuz.isChecked = true
            "Problemas relacionados a agua" -> radioAgua.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLixo -> { //abrir Tipo_LixoFragment
                    edProblema.putString("problema", "Descarte incorreto de lixo ou residuos")
                    edProblema.apply()
                    mListener.getProblema("Descarte incorreto de lixo ou residuos")
                }
                R.id.radioLuz -> { //abrir Tipo_LuzFragment
                    edProblema.putString("problema", "Uso inadequado da luz")
                    edProblema.apply()
                    mListener.getProblema("Uso inadequado da luz")
                }
                R.id.radioAgua -> { //abrir Tipo_AguaFragment
                    edProblema.putString("problema", "Problemas relacionados a agua")
                    edProblema.apply()
                    mListener.getProblema("Problemas relacionados a agua")
                }
            }
        }
        return view
    }
}


