package com.dyah.praktikum5_154

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

    var onEditClick: ((Int) -> Unit)? = null
    var onDeleteClick: ((Int) -> Unit)? = null

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgPost)
        val username: TextView = itemView.findViewById(R.id.tvUsername)
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val menuButton: ImageButton = itemView.findViewById(R.id.buttonMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.username.text = post.username
        holder.caption.text = post.caption

        if (post.imageUri != null) {
            holder.imageView.setImageURI(post.imageUri)
        } else {
            holder.imageView.setImageResource(post.imageRes)
        }

        holder.menuButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            MenuInflater(holder.itemView.context).inflate(R.menu.menu_post_options, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        onEditClick?.invoke(position)
                        true
                    }
                    R.id.menu_delete -> {
                        onDeleteClick?.invoke(position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = postList.size
}
