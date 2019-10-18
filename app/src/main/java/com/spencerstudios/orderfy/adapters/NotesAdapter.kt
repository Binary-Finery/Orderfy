package com.spencerstudios.orderfy.adapters

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spencerstudios.orderfy.R
import com.spencerstudios.orderfy.activities.MainActivity
import com.spencerstudios.orderfy.activities.NoteEditorActivity
import com.spencerstudios.orderfy.constants.Const
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
        val item = items[i]
        var text = item.noteBody
        if (text.isEmpty()) {
            text = Const.EMPTY_NOTE_RECYCLER_VIEW_ITEM_TITLE
            v.title.setTextColor(Color.RED)
        } else
            text = item.noteBody.trim().split("\n")[0]

        v.title.text = text
        v.timestamp.text = DateFormat.getDateInstance(DateFormat.FULL).format(item.timestamp)
        v.item.setOnClickListener { view ->
            val activity: MainActivity = view.context as MainActivity
            val intent = Intent(activity, NoteEditorActivity::class.java)
            intent.putExtra(Const.IS_NEW_NOTE_KEY, false)
            intent.putExtra("id", item.id)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val item = v.note_item_parent!!
        val title = v.tv_note_title!!
        val timestamp = v.tv_note_timestamp!!
    }
}


