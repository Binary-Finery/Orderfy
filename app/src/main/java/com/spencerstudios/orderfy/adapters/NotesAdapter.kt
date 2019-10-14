package com.spencerstudios.orderfy.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.activities.NoteEditorActivity
import com.spencerstudios.orderfy.models.Note
import kotlinx.android.synthetic.main.rv_notes_item.view.*
import java.text.DateFormat

class NotesAdapter(private val items: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(vg: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(vg.context).inflate(R.layout.rv_notes_item, vg, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(v: ViewHolder, i: Int) {
        val it = items[i]
        v.title.text = it.noteBody
        v.timestamp.text = DateFormat.getDateInstance(DateFormat.FULL).format(it.timestamp)
        v.item.setOnClickListener { view ->
            val intent = Intent(view.context, NoteEditorActivity::class.java)
            intent.putExtra("id", it.id)
            view.context.startActivity(intent)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val item = v.note_item_parent!!
        val title = v.tv_note_title!!
        val timestamp = v.tv_note_timestamp!!
    }
}


