package com.example.taskmanager_prma_r21207

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    private lateinit var addNoteButton : FloatingActionButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var menuButton : ImageButton
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNoteButton = findViewById(R.id.addNoteButton)
        recyclerView = findViewById(R.id.recyclerView)
        menuButton = findViewById(R.id.menuButton)


        menuButton.setOnClickListener{ showMenu()}
        addNoteButton.setOnClickListener { val intent = Intent(this@MainActivity, NoteDetailsActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val query : Query = Utility.referenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING)
        val options : FirestoreRecyclerOptions<Note> = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query,Note::class.java)
            .build()
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(options,this)
            recyclerView.adapter = noteAdapter
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }

    private fun showMenu() {
        val popUpMenu = PopupMenu(this, menuButton)
        popUpMenu.menu.add("Logout")
        popUpMenu.show()
        popUpMenu.setOnMenuItemClickListener { menuItem ->

            if (menuItem.title == "Logout"){
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            false
        }
    }
}