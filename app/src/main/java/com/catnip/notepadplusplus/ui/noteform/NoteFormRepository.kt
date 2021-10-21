package com.catnip.notepadplusplus.ui.noteform

import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSource
import com.catnip.notepadplusplus.data.model.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteFormRepository(private val dataSource: NotesDataSource) : NoteFormContract.Repository {
    override suspend fun insertNote(note: Note): Long {
        return dataSource.insertNote(note)
    }

    override suspend fun updateNote(note: Note): Int {
        return dataSource.updateNote(note)
    }

    override suspend fun deleteNote(note: Note): Int {
        return dataSource.deleteNote(note)
    }
}