package com.example.piayaqrcode.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piayaqrcode.R

class TipoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_tipo, container, false)

//        val b = view.findViewById(R.id.bt12) as Button
//        b.setOnClickListener {
//            Toast.makeText(activity!!.applicationContext, "Estou no fragmento!!!", Toast.LENGTH_LONG).show()
//        }
        return view
    }


}
