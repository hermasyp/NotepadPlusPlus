package com.catnip.notepadplusplus.ui.noteform

import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.utils.Constant
import com.catnip.todolistapp.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteFormPresenter(
    private val view: NoteFormContract.View,
    private val repository: NoteFormContract.Repository
) : NoteFormContract.Presenter, BasePresenterImpl() {
    override fun insertNote(note: Note) {
        view.onDataCallback(Resource.Loading())
        scope.launch {
            try {
                val notes = repository.insertNote(note)
                scope.launch(Dispatchers.Main) {
                    if (notes > 0) {
                        view.onDataCallback(Resource.Success(Pair(true, Constant.ACTION_INSERT)))
                    } else {
                        view.onDataCallback(Resource.Success(Pair(false,Constant.ACTION_INSERT)))
                    }
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    view.onDataCallback(Resource.Error(e.message.orEmpty()))
                }
            }
        }
    }

    override fun updateNote(note: Note) {
        view.onDataCallback(Resource.Loading())
        scope.launch {
            try {
                val notes = repository.updateNote(note)
                scope.launch(Dispatchers.Main) {
                    if (notes > 0) {
                        view.onDataCallback(Resource.Success(Pair(true, Constant.ACTION_EDIT)))
                    } else {
                        view.onDataCallback(Resource.Success(Pair(false,Constant.ACTION_EDIT)))
                    }
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    view.onDataCallback(Resource.Error(e.message.orEmpty()))
                }
            }
        }
    }

    override fun deleteNote(note: Note) {
        view.onDataCallback(Resource.Loading())
        scope.launch {
            try {
                val notes = repository.deleteNote(note)
                scope.launch(Dispatchers.Main) {
                    if (notes > 0) {
                        view.onDataCallback(Resource.Success(Pair(true, Constant.ACTION_DELETE)))
                    } else {
                        view.onDataCallback(Resource.Success(Pair(false,Constant.ACTION_DELETE)))
                    }
                }
            } catch (e: Exception) {
                scope.launch(Dispatchers.Main) {
                    view.onDataCallback(Resource.Error(e.message.orEmpty()))
                }
            }
        }
    }

}