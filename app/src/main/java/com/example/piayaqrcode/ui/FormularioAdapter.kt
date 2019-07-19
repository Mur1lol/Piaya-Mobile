package com.example.piayaqrcode.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.piayaqrcode.R
import com.example.piayaqrcode.entidades.Formulario

class FormularioAdapter(private var formularios: List<Formulario>):
    RecyclerView.Adapter<FormularioAdapter.FormularioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormularioViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.tipo_adapter, parent, false)
        return FormularioViewHolder(view)
    }

    override fun getItemCount() = formularios.size

    override fun onBindViewHolder(holder: FormularioViewHolder, position: Int) {
        val formulario = formularios[position]
        holder.preencherView(formulario)
    }

    inner class FormularioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun preencherView(formulario: Formulario) {
            formulario.tipo
            formulario.local
            formulario.info
            formulario.acontecimento

            Log.e("Murilo", ":/")
        }
    }
}