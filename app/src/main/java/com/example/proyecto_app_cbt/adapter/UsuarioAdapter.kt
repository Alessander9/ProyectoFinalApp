import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_app_cbt.R
import com.example.proyecto_app_cbt.model.Area
import com.example.proyecto_app_cbt.model.Rol
import com.example.proyecto_app_cbt.model.Usuario

class UsuarioAdapter(
    private val usuarios: MutableList<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onInactivar: (Usuario) -> Unit,
    private var listaAreas: List<Area> = emptyList(),
    private var listaRoles: List<Rol> = emptyList()
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFotoUsuario: ImageView = itemView.findViewById(R.id.ivFotoUsuario)
        val tvNombreUsuario: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        val tvRolAreaUsuario: TextView = itemView.findViewById(R.id.tvRolAreaUsuario)
        val btnEditarUsuario: Button = itemView.findViewById(R.id.btnEditarUsuario)
        val btnInactivarUsuario: Button = itemView.findViewById(R.id.btnInactivarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.tvNombreUsuario.text = usuario.nombre_completo

        // Mapea solo para mostrar: NO sobrescribas el objeto original
        val rolNombre = listaRoles.find { it.id == usuario.id_rol }?.nombre ?: "Sin rol"
        val areaNombre = listaAreas.find { it.id == usuario.id_area }?.nombre ?: "Sin área"
        holder.tvRolAreaUsuario.text = "$rolNombre • $areaNombre"

        Glide.with(holder.itemView.context)
            .load(usuario.foto_url)
            .placeholder(R.drawable.ic_user_placeholder)
            .error(R.drawable.ic_user_placeholder)
            .into(holder.ivFotoUsuario)

        holder.btnEditarUsuario.setOnClickListener { onEditar(usuario) }

        holder.btnInactivarUsuario.text = if (usuario.activo) "Inactivar" else "Activar"
        holder.btnInactivarUsuario.setOnClickListener {
            onInactivar(usuario)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarListas(areas: List<Area>, roles: List<Rol>) {
        listaAreas = areas
        listaRoles = roles
        notifyDataSetChanged()
    }
}