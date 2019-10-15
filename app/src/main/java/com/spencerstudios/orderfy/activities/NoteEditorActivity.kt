package com.spencerstudios.orderfy.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.spencerstudios.orderfy.ObjectBox
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.models.Note
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_text -> {
                displayClearTextDialog()
                true
            }
            R.id.action_delete_note -> {
                displayDeleteNoteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayClearTextDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Clear Text")
        builder.setMessage("are you sure?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            et_note_body.setText("")
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }

    private fun displayDeleteNoteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete This Note")
        builder.setMessage("are you sure?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            noteBox.remove(note)
            finish()
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }

}
