package com.example.taskmanager_prma_r21207

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.DocumentReference
import java.util.*


class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var titleNoteText: EditText
    private lateinit var contentNoteText: EditText
    private lateinit var saveNoteButton: ImageButton
    private lateinit var pageTitleText: TextView
    private lateinit var title : String
    private lateinit var content : String
    private lateinit var docId : String
    private var isEditMode : Boolean = false
    private lateinit var deleteTextViewButton : TextView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        titleNoteText = findViewById(R.id.titleNoteText)
        contentNoteText = findViewById(R.id.contentNoteText)
        saveNoteButton = findViewById(R.id.saveNoteButton)
        pageTitleText = findViewById(R.id.pageTitle)
        deleteTextViewButton = findViewById(R.id.deleteNoteTextViewButton)


        title = intent.getStringExtra("title").toString()
        content = intent.getStringExtra("content").toString()
        docId = intent.getStringExtra("docId") ?: ""


        if (docId.isNotEmpty()) {
            isEditMode = true
        }

        if(isEditMode){
            pageTitleText.text = "Edit your note"
            deleteTextViewButton.visibility = View.VISIBLE
            titleNoteText.setText(title)
            contentNoteText.setText(content)
        }
        else{
            pageTitleText.text = "Add your note"
            deleteTextViewButton.visibility = View.GONE
        }

        saveNoteButton.setOnClickListener{ savenote()}

        deleteTextViewButton.setOnClickListener{ deleteNoteFromFirebase()}

    }

    private fun deleteNoteFromFirebase() {

        val documentReference : DocumentReference = Utility.referenceForNotes().document(docId)

            documentReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utility.showToast(this,"Note deleted successfully")
            } else {
                Utility.showToast(this,"Failed while deleting note")
            }
            finish()
        }
    }

    private fun savenote() {
        val title: String = titleNoteText.text.toString().trim()
        val content: String = contentNoteText.text.toString().trim()
        if (title.isEmpty()) {
            titleNoteText.error = "Title is required!"
            return
        }

        val note = Note()
        note.setTitle(title)
        note.content = content
        note.setTimestamp(Date())

        saveNoteToFirebase(note)
    }

    private fun saveNoteToFirebase(note : Note){
        val documentReference : DocumentReference = if(isEditMode){
            Utility.referenceForNotes().document(docId)
        }else{
            Utility.referenceForNotes().document()
        }
        documentReference.set(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
               if(isEditMode){
                   Utility.showToast(this,"Note update successfully")
               }
                else{
                   Utility.showToast(this,"Note added successfully")
               }
            } else {
                Utility.showToast(this,"Failed while adding note")
            }
            isEditMode = false
            finish()
        }
    }
}