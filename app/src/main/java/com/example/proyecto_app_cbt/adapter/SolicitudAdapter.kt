package com.example.proyecto_app_cbt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Solicitud

class SolicitudAdapter(
    private var items: List<Solicitud>,
    private val onItemClick: (Solicitud) -> Unit
) : RecyclerView.Adapter<SolicitudAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMotivo = itemView.findViewById<TextView>(R.id.tvMotivo)
        private val tvFechas = itemView.findViewById<TextView>(R.id.tvFechas)

        fun bind(s: Solicitud) {
            tvMotivo.text = s.motivo
            tvFechas.text = "${s.fecha_inicio} - ${s.fecha_fin}"
            itemView.setOnClickListener { onItemClick(s) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_solicitud, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    /** Actualiza la lista en el adapter */
    fun actualizarLista(nueva: List<Solicitud>) {
        items = nueva
        notifyDataSetChanged()
    }
}
