package com.example.proyecto_app_cbt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Rol

class RolAdapter(
    private var items: List<Rol>,
    private val onItemClick: (Rol) -> Unit,
    private val onEditarClick: (Rol) -> Unit
) : RecyclerView.Adapter<RolAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreRol = itemView.findViewById<TextView>(R.id.tvNombreRol)
        private val btnEditarRol = itemView.findViewById<Button>(R.id.btnEditarRol)

        fun bind(rol: Rol) {
            tvNombreRol.text = rol.nombre

            // Click en todo el item
            itemView.setOnClickListener {
                onItemClick(rol)
            }

            // Click solo en el botón
            btnEditarRol.setOnClickListener {
                onEditarClick(rol)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rol, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun actualizarLista(nueva: List<Rol>) {
        items = nueva
        notifyDataSetChanged()
    }
}