package com.catnip.notepadplusplus.data.local.room.dao

import androidx.room.*
import com.catnip.notepadplusplus.data.model.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
@Dao
interface NotesDao {
    @Query("SELECT * FROM NOTES WHERE is_archived == 0")
    suspend fun getAllNotes() : List<Note>

    @Query("SELECT * FROM NOTES WHERE is_archived == :isArchived")
    suspend fun getNotesByArchivedStatus(isArchived : Boolean) : List<Note>

    @Query("SELECT * FROM NOTES WHERE id == :id")
    suspend fun getNoteById(id : Int) : Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(Note: Note) : Long

    @Update
    suspend fun updateNote(Note: Note) : Int

    @Delete
    suspend fun deleteNote(Note: Note) : Int
}