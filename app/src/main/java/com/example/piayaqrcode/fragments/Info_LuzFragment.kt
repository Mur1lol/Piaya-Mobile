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

class Info_LuzFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_info_luz, container, false)

        val radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup

        val radioAcesa = view.findViewById(R.id.radioAcesa) as RadioButton
        val radioAr = view.findViewById(R.id.radioAr) as RadioButton
        val radioOutroLuz = view.findViewById(R.id.radioOutroLuz) as RadioButton

        val prefsInfo = this.activity!!.getSharedPreferences("info", Context.MODE_PRIVATE)
        var edInfo = prefsInfo.edit()
        val infos = prefsInfo.getString("info", null)

        when (infos) {
            "Acesa" -> radioAcesa.isChecked = true
            "Ar" -> radioAr.isChecked = true
            "Outro-Luz" -> radioOutroLuz.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioAcesa -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Acesa")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioAr -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Ar")
                    edInfo.apply()
                    mListener.getInfo()
                }
                R.id.radioOutroLuz -> { //abrir AcontecimentoFragment
                    edInfo.putString("info", "Outro-Luz")
                    edInfo.apply()
                    mListener.getInfo()
                }
            }
        }
        return view
    }


}
