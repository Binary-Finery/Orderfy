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
import java.util.regex.Pattern

class TextUtils(
    private val ctx: Context,
    private val text: String
) {

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
        val pattern = Pattern.compile(if (isCaseSensitive) target else target.toLowerCase())
        val matcher = pattern.matcher(if (isCaseSensitive) text else text.toLowerCase())

        var qty = 0

        while (matcher.find()) {
            qty++
            spannable.setSpan(
                BackgroundColorSpan(if (qty % 2 == 0) Color.LTGRAY else Color.GRAY),
                matcher.start(),
                matcher.end(),
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        msg("$qty occurrence(s) found")
        return spannable
    }

    fun replace(oldVal: String, replacementVal: String, isCaseSensitive: Boolean): String {
        val builder = StringBuilder(text)
        val oldValLength = oldVal.length
        var qty = 0
        for (i in 0 until (builder.length - 1)) {
            if (builder.substring(i, i.plus(oldValLength)) == oldVal) {
                qty++
                builder.replace(i, i.plus(oldValLength), replacementVal)
            }
        }
        msg("$qty occurrence(s) replaced")
        return builder.toString()
    }

    private fun msg(msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }
}