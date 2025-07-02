package com.example.proyecto_app_cbt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Area

class AreaAdapter(
    private var items: List<Area>,
    private val onItemClick: (Area) -> Unit,
    private val onEditarClick: (Area) -> Unit,
    private val onEliminarClick: (Area) -> Unit



) : RecyclerView.Adapter<AreaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreArea: TextView = itemView.findViewById(R.id.tvNombreArea)
        private val btnEditarArea: Button = itemView.findViewById(R.id.btnEditarArea)
        private val btnEliminarArea: Button = itemView.findViewById(R.id.btnEliminarArea)

        fun bind(area: Area) {
            tvNombreArea.text = area.nombre

            // Click en todo el ítem
            itemView.setOnClickListener {
                onItemClick(area)
            }

            // Click en Editar
            btnEditarArea.setOnClickListener {
                onEditarClick(area)
            }

            // Click en Eliminar
            btnEliminarArea.setOnClickListener {
                onEliminarClick(area)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_area, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    /**
     * Actualiza la lista de áreas y refresca el RecyclerView
     */
    fun actualizarLista(nueva: List<Area>) {
        items = nueva
        notifyDataSetChanged()
    }
}
