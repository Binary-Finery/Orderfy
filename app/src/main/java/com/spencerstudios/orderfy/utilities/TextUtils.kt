package com.spencerstudios.orderfy.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.widget.Toast

class TextUtils(private val ctx: Context, private val text: String) {

    fun copy() {
        val cm = ctx.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        cm?.primaryClip = ClipData.newPlainText("text", text)
        msg("Copied to clipboard")
    }

    fun share() {
        val i = Intent()
        i.action = Intent.ACTION_SEND
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT, text)
        ctx.startActivity(Intent.createChooser(i, "share to..."))
    }

    fun find(target: String, isCaseSensitive: Boolean): SpannableStringBuilder {

        val spannable = SpannableStringBuilder(text)
        var qty = 0
        val len = target.length

        if (isCaseSensitive) {
            for (i in 0 until text.length - len) {
                if (spannable.substring(i, i + len) == target) {
                    qty++
                    spannable.setSpan(
                        BackgroundColorSpan(if (qty % 2 == 0) Color.LTGRAY else Color.GRAY),
                        i,
                        i + len,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        } else {
            for (i in 0 until text.length - len) {
                if (spannable.substring(i, i + len).toLowerCase() == target.toLowerCase()) {
                    qty++
                    spannable.setSpan(
                        BackgroundColorSpan(if (qty % 2 == 0) Color.LTGRAY else Color.GRAY),
                        i,
                        i + len,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
        msg("$qty occurrence(s) found")
        return spannable
    }

    fun replace(oldVal: String, replacementVal: String, ignoreCase: Boolean): String {
        return text.replace(oldVal, replacementVal, !ignoreCase)
    }

    private fun msg(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }
}