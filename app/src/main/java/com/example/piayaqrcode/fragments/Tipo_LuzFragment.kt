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

class Tipo_LuzFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_tipo_luz, container, false)

        val radioGroup = view.findViewById(R.id.radioGroupLuz) as RadioGroup

        val radioLuzAcesa = view.findViewById(R.id.radioLuzAcesa) as RadioButton
        val radioArCondicionado = view.findViewById(R.id.radioArCondicionado) as RadioButton
        val radioComputadorLigado = view.findViewById(R.id.radioComputadorLigado) as RadioButton

        val prefsTipo = this.activity!!.getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()
        val tipos = prefsTipo.getString("tipo", null)

        when (tipos) {
            "Luz acesa em ambiente vazio" -> radioLuzAcesa.isChecked = true
            "Ar condicionado ligado em sala vazia" -> radioArCondicionado.isChecked = true
            "Computadores ligados com a sala vazia" -> radioComputadorLigado.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioLuzAcesa -> {  
                    edTipo.putString("tipo", "Acesa")
                    edTipo.apply()
                    mListener.getTipo()
                }
                R.id.radioArCondicionado -> {  
                    edTipo.putString("tipo", "Ar condicionado ligado em sala vazia")
                    edTipo.apply()
                    mListener.getTipo()
                }
                R.id.radioComputadorLigado -> {  
                    edTipo.putString("tipo", "Computadores ligados com a sala vazia")
                    edTipo.apply()
                    mListener.getTipo()
                }
            }
        }
        return view
    }


}
