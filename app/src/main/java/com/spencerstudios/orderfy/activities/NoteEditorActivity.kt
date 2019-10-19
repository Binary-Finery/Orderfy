package com.spencerstudios.orderfy.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.spencerstudios.orderfy.ObjectBox
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.constants.Const
import com.spencerstudios.orderfy.models.Note
import com.spencerstudios.orderfy.utilities.TextUtils
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_note_editor.*
import kotlinx.android.synthetic.main.content_note_editor.*

class NoteEditorActivity : AppCompatActivity() {

    private var noteBox = ObjectBox.boxStore.boxFor<Note>()
    private lateinit var note: Note
    private var originalContent: String = ""
    private var isNewNote: Boolean = true
    private var isSharedText: Boolean = false
    private var isInSearchMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        val intent = intent
        val action = intent.action
        val type = intent.type
        if ("android.intent.action.SEND" == action && type != null && "text/plain" == type) {
            val sharedText = intent.getStringExtra("android.intent.extra.TEXT")
            if (sharedText.isNotEmpty()) {
                isNewNote = true
                isSharedText = true
                et_note_body.setText(sharedText)
            }
        }

        if (!isSharedText) {
            isNewNote = intent.getBooleanExtra(Const.IS_NEW_NOTE_KEY, isNewNote)
            if (!isNewNote) {
                note = noteBox.get(intent.getLongExtra("id", 0))
                originalContent = note.noteBody
            }
            et_note_body.setText(originalContent)
        }
    }

    private fun saveNote() {

        val content = et_note_body.text.toString()

        if (content != originalContent) {
            if (isNewNote)
                note = Note()
            note.noteBody = content
            note.timestamp = dateNow()
            noteBox.put(note)
            originalContent = content
            Toast.makeText(this@NoteEditorActivity, "note saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_text -> {
                if (hasContent()) displayClearTextDialog()
                true
            }
            R.id.action_delete_note -> {
                if (!isNewNote) displayDeleteNoteDialog()
                true
            }
            R.id.action_share_note -> {
                shareOrCopyNote(false)
                true
            }
            R.id.action_copy_note -> {
                shareOrCopyNote(true)
                true
            }
            R.id.action_save_note -> {
                saveNote()
                true
            }
            R.id.action_search_note -> {
                if (hasContent()) displaySearchDialog()
                true
            }
            android.R.id.home -> {
                saveNote()
                navigateToHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareOrCopyNote(isCopy: Boolean) {
        if (hasContent()) {
            val utils = TextUtils(this, et_note_body.text.toString())
            if (isCopy) utils.copy() else utils.share()
        } else Toast.makeText(this, if (isCopy) "nothing to copy?" else "nothing to share?", Toast.LENGTH_SHORT).show()
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
            navigateToHome()
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> navigateToHome() }
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun displaySearchDialog() {
        val builder = AlertDialog.Builder(this)

        val v = LayoutInflater.from(this).inflate(R.layout.find_dialog, null)
        val etFind = v.findViewById<EditText>(R.id.et_find_string)
        val etReplace = v.findViewById<EditText>(R.id.et_replacement_string)
        val cbCaseSensitive = v.findViewById<CheckBox>(R.id.cb_case_sensitive)

        builder.setTitle("Search")
        builder.setView(v)

        builder.setPositiveButton("find") { _, _ ->
            val target = etFind.text.toString()
            if (target.isNotEmpty()) {
                val txt: String = et_note_body.text.toString()
                val utils = TextUtils(this@NoteEditorActivity, txt)
                et_note_body.text = utils.find(target, cbCaseSensitive.isChecked)
                isInSearchMode = true
            }
        }

        builder.setNegativeButton("replace all") { _, _ ->
            val target = etFind.text.toString()
            val replace = etReplace.text.toString()

            if (target.isNotEmpty() && replace.isNotEmpty()) {
                val txt: String = et_note_body.text.toString()
                val utils = TextUtils(this@NoteEditorActivity, txt)
                et_note_body.setText(utils.replace(target, replace, cbCaseSensitive.isChecked))
                isInSearchMode = true
            }
        }


        builder.setNeutralButton(android.R.string.no) { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        if (isInSearchMode) {
            et_note_body.setText(et_note_body.text.toString())
            Toast.makeText(this, "highlighting removed", Toast.LENGTH_LONG).show()
            isInSearchMode = false
        } else {
            saveNote()
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun dateNow(): Long {
        return System.currentTimeMillis()
    }

    private fun hasContent(): Boolean {
        return et_note_body.text.isNotEmpty()
    }
}
