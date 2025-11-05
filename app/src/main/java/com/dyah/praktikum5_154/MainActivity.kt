package com.dyah.praktikum5_154

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyah.praktikum5_154.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.toString

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private val posts = ArrayList<Post>()

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stories = listOf(
            Story("intan_dwi", R.drawable.ic_user),
            Story("minda_04", R.drawable.ic_user),
            Story("rubi_community", R.drawable.ic_user),
            Story("rizka", R.drawable.ic_user),
            Story("dyah", R.drawable.ic_user),
            Story("rahma", R.drawable.ic_user),
            Story("alifiyah", R.drawable.ic_user),
            Story("154", R.drawable.ic_user)
        )

        posts.add(Post("intan_dwi", "valo ges", R.drawable.valo))
        posts.add(Post("minda_04", "valo ges", R.drawable.valoo))
        posts.add(Post("rubi_community", "valo ges", R.drawable.valooo))

        binding.rvStories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvStories.adapter = StoryAdapter(stories)

        binding.rvFeed.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(posts)
        binding.rvFeed.adapter = feedAdapter

        feedAdapter.onEditClick = { position ->
            val post = posts[position]
            showEditPostDialog(position, post)
        }

        feedAdapter.onDeleteClick = { position ->
            posts.removeAt(position)
            feedAdapter.notifyItemRemoved(position)
            Toast.makeText(this, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
        }

        binding.fabAddPost.setOnClickListener {
            showAddPostDialog()
        }
    }

    private fun showAddPostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_post, null)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etCaption = dialogView.findViewById<EditText>(R.id.etCaption)
        val imgPreview = dialogView.findViewById<ImageView>(R.id.imgPreview)
        val btnPick = dialogView.findViewById<FloatingActionButton>(R.id.btnPickImage)

        selectedImageUri = null

        btnPick.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Postingan Baru")
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnSave.setOnClickListener {
                val username = etUsername.text.toString().trim()
                val caption = etCaption.text.toString().trim()

                if (username.isEmpty() || caption.isEmpty() || selectedImageUri == null) {
                    Toast.makeText(this, "Semua harus diisi", Toast.LENGTH_SHORT).show()
                } else {
                    val newPost = Post(username, caption, imageUri = selectedImageUri)
                    posts.add(0, newPost)
                    feedAdapter.notifyItemInserted(0)
                    binding.rvFeed.scrollToPosition(0)
                    Toast.makeText(this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    selectedImageUri = null
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showEditPostDialog(position: Int, post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_post, null)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etCaption = dialogView.findViewById<EditText>(R.id.etCaption)
        val imgPreview = dialogView.findViewById<ImageView>(R.id.imgPreview)
        val btnPick = dialogView.findViewById<FloatingActionButton>(R.id.btnPickImage)

        etUsername.setText(post.username)
        etCaption.setText(post.caption)
        if (post.imageUri != null) {
            imgPreview.setImageURI(post.imageUri)
        } else {
            imgPreview.setImageResource(post.imageRes)
        }

        selectedImageUri = post.imageUri

        btnPick.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Postingan")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val username = etUsername.text.toString()
                val caption = etCaption.text.toString()

                post.username = username
                post.caption = caption
                if (selectedImageUri != null) {
                    post.imageUri = selectedImageUri
                }

                feedAdapter.notifyItemChanged(position)
                Toast.makeText(this, "Postingan berhasil diedit", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
