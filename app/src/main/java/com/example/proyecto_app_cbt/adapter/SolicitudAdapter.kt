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
            tvFechas.text = formatearFechasLegible(s)
            itemView.setOnClickListener { onItemClick(s) }
        }
    }

    private fun formatearFechasLegible(s: Solicitud): String {
        return try {
            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())

            val inicioStr = s.fecha_inicio?.let {
                sdf.format(it.toDate())
            } ?: "Sin inicio"

            val finStr = s.fecha_fin?.let {
                sdf.format(it.toDate())
            } ?: "Sin fin"

            "$inicioStr - $finStr"
        } catch (e: Exception) {
            "${s.getFechaInicioString()} - ${s.getFechaFinString()}"
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

    fun actualizarLista(nueva: List<Solicitud>) {
        items = nueva
        notifyDataSetChanged()
    }
}
