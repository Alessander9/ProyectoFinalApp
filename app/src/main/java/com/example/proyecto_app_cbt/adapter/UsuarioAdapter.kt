import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.model.Usuario
import com.example.proyecto_app_cbt.R

class UsuarioAdapter(
    private val usuarios: MutableList<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onInactivar: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarUsuario)
        val btnInactivar: Button = itemView.findViewById(R.id.btnInactivarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nombre.text = usuario.nombre_completo

        holder.btnEditar.setOnClickListener { onEditar(usuario) }

        holder.btnInactivar.text = if (usuario.activo) "Inactivar" else "Activar"
        holder.btnInactivar.setOnClickListener {
            usuario.activo = !usuario.activo
            onInactivar(usuario)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = usuarios.size
}