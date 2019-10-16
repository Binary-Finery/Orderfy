package com.spencerstudios.orderfy.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.spencerstudios.orderfy.ObjectBox
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.adapters.NotesAdapter
import com.spencerstudios.orderfy.models.Note
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var noteBox = ObjectBox.boxStore.boxFor<Note>()

    override fun onStart() {
        super.onStart()
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.adapter = NotesAdapter(noteBox.all.sortedWith(compareBy { -it.timestamp }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            addNote()
        }
    }

    private fun addNote(){
        val note = Note()
        noteBox.put(note)
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra("id", note.id)
        intent.putExtra("isNewNote", true)
        startActivity(intent)
    }
}
