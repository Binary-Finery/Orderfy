package com.spencerstudios.orderfy.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.spencerstudios.orderfy.ObjectBox
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.adapters.NotesAdapter
import com.spencerstudios.orderfy.constants.Const
import com.spencerstudios.orderfy.models.Note
import com.spencerstudios.orderfy.models.Note_
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var noteBox = ObjectBox.boxStore.boxFor<Note>()
    private lateinit var adapter: NotesAdapter
    private lateinit var items: ArrayList<Note>
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rv_notes.layoutManager = LinearLayoutManager(this)
        items = ArrayList()
        populateListFromDb()
        adapter = NotesAdapter(items)
        rv_notes.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            intent.putExtra(Const.IS_NEW_NOTE_KEY, true)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.search)
        searchView = menuItem.actionView as SearchView
        searchView?.setIconifiedByDefault(true)
        searchView?.isSubmitButtonEnabled = false
        searchView?.queryHint = "search notes..."
        searchView?.setOnQueryTextListener(onQueryTextListener)

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                populateListFromDb()
                adapter.notifyDataSetChanged()
                return true
            }
        })
        return true
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isNotEmpty()) {
                items.clear()
                items.addAll(noteBox.query().contains(Note_.noteBody, query).build().find())
                adapter.notifyDataSetChanged()
            }
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    }

    private fun populateListFromDb(): ArrayList<Note> {
        items.clear()
        items.addAll(noteBox.all.sortedBy { -it.timestamp })
        return items
    }
}
