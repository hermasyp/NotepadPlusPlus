package com.catnip.notepadplusplus.ui.noteform

import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.todolistapp.base.BaseContract

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface NoteFormContract {
    interface View : BaseContract.BaseView {
        //callback insert/update data
        fun onDataCallback(response: Resource<Pair<Boolean, Int>>)
        //initialize data
        fun getIntentData()
        fun initializeForm()
        fun validateForm() : Boolean
        fun saveNote()
        fun deleteNote()
        fun showColorPickerDialog()
        fun setClickListeners()
        fun checkPasswordAvailability()
    }

    interface Presenter : BaseContract.BasePresenter {
        fun insertNote(note: Note)
        fun updateNote(note: Note)
        fun deleteNote(note: Note)
    }

    interface Repository {
        suspend fun insertNote(note: Note): Long
        suspend fun updateNote(note: Note): Int
        suspend fun deleteNote(note: Note): Int
    }



}