package com.example.proyecto_app_cbt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Solicitud

class SolicitudAdapter(
    private var lista: List<Solicitud>
) : RecyclerView.Adapter<SolicitudAdapter.ViewHolder>() {

    fun actualizarLista(nuevaLista: List<Solicitud>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvFechas: TextView = view.findViewById(R.id.tvFechas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitud, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val solicitud = lista[position]
        holder.tvMotivo.text = solicitud.motivo
        holder.tvEstado.text = "Estado: ${solicitud.estado}"
        holder.tvFechas.text = "Del ${solicitud.fecha_inicio} al ${solicitud.fecha_fin}"
    }
}
