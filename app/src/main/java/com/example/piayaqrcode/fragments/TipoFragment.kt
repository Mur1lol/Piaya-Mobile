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
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_tipo.*

class TipoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_tipo, container, false)

        val radioGroup = view.findViewById(R.id.radioTipo) as RadioGroup

        val radioLixo = view.findViewById(R.id.radioLixo) as RadioButton
        val radioLuz = view.findViewById(R.id.radioLuz) as RadioButton
        val radioAgua = view.findViewById(R.id.radioAgua) as RadioButton
        val radioOutro = view.findViewById(R.id.radioOutro) as RadioButton

        val prefsTipo = this.activity!!.getSharedPreferences("tipo", Context.MODE_PRIVATE)
        var edTipo = prefsTipo.edit()
        val tipos = prefsTipo.getString("tipo", null)

        when (tipos) {
            "Lixo" -> radioLixo.isChecked = true
            "Luz" -> radioLuz.isChecked = true
            "Agua" -> radioAgua.isChecked = true
            "Outro" -> radioOutro.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.radioLixo -> { //abrir Info_LixoFragment
                    edTipo.putString("tipo", "Lixo")
                    edTipo.apply()
                }
                R.id.radioLuz -> { //abrir Info_LuzFragment
                    edTipo.putString("tipo", "Luz")
                    edTipo.apply()
                }
                R.id.radioAgua -> { //abrir Info_AguaFragment
                    edTipo.putString("tipo", "Agua")
                    edTipo.apply()
                }
                R.id.radioOutro -> { //abrir AcontecimentoFragment
                    edTipo.putString("tipo", "Outro")
                    edTipo.apply()
                }
            }
        }
        return view
    }


}
