package com.catnip.notepadplusplus.ui.noteform

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.catnip.notepadplusplus.R
import com.catnip.notepadplusplus.base.BaseActivity
import com.catnip.notepadplusplus.data.local.NotesDatabase
import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSourceImpl
import com.catnip.notepadplusplus.data.local.sharedpreference.UserPreferences
import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.databinding.ActivityNoteFormBinding
import com.catnip.notepadplusplus.utils.Constant
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

class NoteFormActivity : BaseActivity<ActivityNoteFormBinding, NoteFormContract.Presenter>(
    ActivityNoteFormBinding::inflate
), NoteFormContract.View {

    private var formMode: Int = FORM_MODE_INSERT
    private var pickedCardColor = DEFAULT_CARD_COLOR
    private var note: Note? = null

    override fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getIntentData()
        initializeForm()
        setClickListeners()
    }

    override fun initPresenter() {
        val dataSource = NotesDataSourceImpl(NotesDatabase.getInstance(this).noteDao())
        val repository = NoteFormRepository(dataSource)
        setPresenter(NoteFormPresenter(this, repository))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form_note, menu)
        val item = menu?.findItem(R.id.menu_delete);
        item?.isVisible = formMode == FORM_MODE_EDIT
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveNote()
                true
            }
            R.id.menu_delete -> {
                deleteNote()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDataCallback(response: Resource<Pair<Boolean, Int>>) {
        when (response) {
            is Resource.Loading -> {
            }
            is Resource.Success -> {
                showLoading(false)
                response.data?.let { result ->
                    var msg: Pair<String, String>? = null
                    when (result.second) {
                        Constant.ACTION_INSERT -> {
                            msg = Pair("Insert Success", "Insert Failed")
                        }
                        Constant.ACTION_EDIT -> {
                            msg = Pair("Edit Success", "Edit Failed")
                        }
                        Constant.ACTION_DELETE -> {
                            msg = Pair("Delete Success", "Delete Failed")
                        }
                    }
                    if (result.first) {
                        Toast.makeText(this, msg?.first, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, msg?.second, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            is Resource.Error -> {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getIntentData() {
        formMode = intent.getIntExtra(INTENT_FORM_MODE, FORM_MODE_INSERT)
        note = intent.getParcelableExtra(INTENT_NOTE_DATA)
    }

    override fun initializeForm() {
        checkPasswordAvailability()
        if (formMode == FORM_MODE_EDIT) {
            note?.let {
                getViewBinding().etNoteTitle.setText(it.title)
                getViewBinding().etNoteBody.setText(it.body)
                getViewBinding().swArchiveNote.isChecked = it.isArchived
                getViewBinding().swProtectNote.isChecked = it.isLocked
                pickedCardColor = it.hexCardColor
            }
            //"Edit Data"
            supportActionBar?.title = getString(R.string.text_title_edit_note_form_activity)
        } else {
            supportActionBar?.title = getString(R.string.text_title_insert_note_form_activity)
        }
        getViewBinding().ivColorPreview.setBackgroundColor(Color.parseColor(pickedCardColor))

    }

    override fun validateForm(): Boolean {
        val title = getViewBinding().etNoteTitle.text.toString()
        var isFormValid = true

        //for checking is title empty
        if (title.isEmpty()) {
            isFormValid = false
            getViewBinding().tilNoteTitle.isErrorEnabled = true
            getViewBinding().tilNoteTitle.error = getString(R.string.error_form_note_title_empty)
        } else {
            getViewBinding().tilNoteTitle.isErrorEnabled = false
        }

        return isFormValid
    }

    override fun saveNote() {
        if (validateForm()) {
            if (formMode == FORM_MODE_EDIT) {
                // do edit
                note = note?.copy()?.apply {
                    title = getViewBinding().etNoteTitle.text.toString()
                    body = getViewBinding().etNoteBody.text.toString()
                    isArchived = getViewBinding().swArchiveNote.isChecked
                    isLocked = getViewBinding().swProtectNote.isChecked
                    hexCardColor = pickedCardColor
                }
                note?.let { getPresenter().updateNote(it) }
            } else {
                //do insert
                note = Note(
                    title = getViewBinding().etNoteTitle.text.toString(),
                    body = getViewBinding().etNoteBody.text.toString(),
                    isArchived = getViewBinding().swArchiveNote.isChecked,
                    isLocked = getViewBinding().swProtectNote.isChecked,
                    hexCardColor = pickedCardColor
                )
                note?.let { getPresenter().insertNote(it) }
            }
        }
    }

    override fun deleteNote() {
        note?.let {
            getPresenter().deleteNote(it)
        }
    }

    override fun showColorPickerDialog() {
        MaterialColorPickerDialog
            .Builder(this)                            // Pass Activity Instance
            .setTitle(getString(R.string.text_color_picker_title))                // Default "Choose Color"
            .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
            .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
            .setDefaultColor(pickedCardColor)        // Pass Default Color
            .setColorListener { color, colorHex ->
                // Handle Color Selection
                pickedCardColor = colorHex
                getViewBinding().ivColorPreview.setBackgroundColor(Color.parseColor(pickedCardColor))

            }
            .show()
    }

    override fun setClickListeners() {
        getViewBinding().llCardColor.setOnClickListener {
            showColorPickerDialog()
        }
    }

    override fun checkPasswordAvailability() {
        val currentPassword = UserPreferences(this).password
        getViewBinding().swProtectNote.isEnabled = !currentPassword.isNullOrEmpty()
    }

    companion object {
        private const val INTENT_FORM_MODE = "INTENT_FORM_MODE"
        private const val INTENT_NOTE_DATA = "INTENT_NOTE_DATA"
        private const val DEFAULT_CARD_COLOR = "#d3bdff"
        const val FORM_MODE_INSERT = 1
        const val FORM_MODE_EDIT = 2

        fun startActivity(context: Context, formMode: Int, note: Note?) {
            val intent = Intent(context, NoteFormActivity::class.java)
            intent.putExtra(INTENT_FORM_MODE, formMode)
            note?.let {
                intent.putExtra(INTENT_NOTE_DATA, note)
            }
            context.startActivity(intent)
        }

        fun startActivity(context: Context, formMode: Int) {
            startActivity(context, formMode, null)
        }
    }
}