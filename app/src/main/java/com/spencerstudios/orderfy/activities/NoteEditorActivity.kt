package com.spencerstudios.orderfy.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.spencerstudios.orderfy.models.Note
import com.spencerstudios.orderfy.ObjectBox
import com.spencerstudios.orderfy.R
import io.objectbox.kotlin.boxFor

import kotlinx.android.synthetic.main.activity_note_editor.*
import kotlinx.android.synthetic.main.content_note_editor.*

class NoteEditorActivity : AppCompatActivity() {

    private var noteBox = ObjectBox.boxStore.boxFor<Note>()
    private lateinit var note: Note
    private lateinit var originalContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        setSupportActionBar(toolbar)

        val noteId = intent.getLongExtra("id", 0)
        note = noteBox.get(noteId)
        originalContent = note.noteBody
        et_note_body.setText(originalContent)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val newContent = et_note_body.text.toString()

        if (newContent != originalContent) {
            note.noteBody = newContent
            note.timestamp = System.currentTimeMillis()
            noteBox.put(note)
            Toast.makeText(this@NoteEditorActivity, "note saved", Toast.LENGTH_SHORT).show()
        }
    }
}
