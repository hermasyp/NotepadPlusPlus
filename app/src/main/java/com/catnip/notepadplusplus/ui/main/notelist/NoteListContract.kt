package com.catnip.notepadplusplus.ui.main.notelist

import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.todolistapp.base.BaseContract

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface NoteListContract {
    interface View : BaseContract.BaseView{
        //getting data from presenter
        fun getData()

        //callback get data
        fun onDataCallback(response: Resource<List<Note>>)

        //initialize list
        fun initList()

        fun setListData(data : List<Note>)

        fun showDialogPassword(note : Note)
    }

    interface Presenter : BaseContract.BasePresenter {
        fun getAllNotes()
        fun getArchivedNotes()
    }


    interface Repository {
        suspend fun getAllNotes(): List<Note>
        suspend fun getNotesByArchivedStatus(): List<Note>
    }


}