package com.catnip.notepadplusplus.data.local.room.datasource

import com.catnip.notepadplusplus.data.local.room.dao.NotesDao
import com.catnip.notepadplusplus.data.model.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NotesDataSourceImpl(private val dao: NotesDao) : NotesDataSource {
    override suspend fun getAllNotes(): List<Note> {
        return dao.getAllNotes()
    }

    override suspend fun getNotesByArchivedStatus(isArchived: Boolean): List<Note> {
        return dao.getNotesByArchivedStatus(isArchived)
    }

    override suspend fun getNoteById(id: Int): Note {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note): Long {
        note.apply{
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        }
        return dao.insertNote(note)
    }

    override suspend fun updateNote(note: Note): Int {
        note.apply{
            modifiedAt = System.currentTimeMillis()
        }
        return dao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note): Int {
        return dao.deleteNote(note)
    }
}