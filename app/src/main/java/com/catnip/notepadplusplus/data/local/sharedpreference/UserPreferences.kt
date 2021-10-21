package com.catnip.notepadplusplus.data.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class UserPreferences(context: Context) {
    private var preference: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    companion object {
        private const val NAME = "NotepadPlusPlus" //app name or else
        private const val MODE = Context.MODE_PRIVATE
        private val PREF_PASSWORD_NOTES = Pair("PREF_PASSWORD_NOTES", null)
    }

    var password: String?
        get() = preference.getString(PREF_PASSWORD_NOTES.first, PREF_PASSWORD_NOTES.second)
        set(value) = preference.edit {
            it.putString(PREF_PASSWORD_NOTES.first, value)
        }

}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}

private fun SharedPreferences.delete() {
    edit().clear().apply()
}
