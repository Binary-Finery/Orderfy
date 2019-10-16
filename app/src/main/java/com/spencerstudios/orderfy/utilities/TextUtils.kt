package com.spencerstudios.orderfy.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.widget.Toast

class TextUtils(private val ctx: Context, private val text: String) {

    fun copy() {
        val cm = ctx.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        cm?.primaryClip = ClipData.newPlainText("text", text)
        Toast.makeText(ctx, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun share() {
        val i = Intent()
        i.action = Intent.ACTION_SEND
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT, text)
        ctx.startActivity(Intent.createChooser(i, "share to..."))
    }
}