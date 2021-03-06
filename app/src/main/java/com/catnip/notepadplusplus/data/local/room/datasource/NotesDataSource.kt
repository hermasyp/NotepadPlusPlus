package com.catnip.notepadplusplus.data.local.room.datasource

import com.catnip.notepadplusplus.data.model.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface NotesDataSource {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNotesByArchivedStatus(isArchived: Boolean): List<Note>
    suspend fun getNoteById(id: Int): Note
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note): Int
    suspend fun deleteNote(note: Note): Int
}