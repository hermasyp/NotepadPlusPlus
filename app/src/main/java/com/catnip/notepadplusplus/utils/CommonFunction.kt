package com.catnip.notepadplusplus.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
object CommonFunction {
    fun dpToPixels(context: Context?, dp: Int): Int {
        if (context != null) {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
        return 0
    }
    fun fromHtmlContent(content: String?): Spanned? {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            HtmlCompat.fromHtml(
                content!!,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            Html.fromHtml(content)
        }
    }
}