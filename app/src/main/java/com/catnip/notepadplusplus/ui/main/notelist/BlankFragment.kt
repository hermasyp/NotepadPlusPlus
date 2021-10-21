package com.catnip.notepadplusplus.ui.main.notelist

import com.catnip.notepadplusplus.base.BaseFragment
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.databinding.FragmentNoteListBinding

class BlankFragment : BaseFragment<FragmentNoteListBinding, NoteListContract.Presenter>(
    FragmentNoteListBinding::inflate
), NoteListContract.View {
    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initPresenter() {
        TODO("Not yet implemented")
    }

    override fun getData() {
        TODO("Not yet implemented")
    }

    override fun onDataCallback(response: Resource<List<Note>>) {
        TODO("Not yet implemented")
    }

    override fun initList() {
        TODO("Not yet implemented")
    }

    override fun setListData(data: List<Note>) {
        TODO("Not yet implemented")
    }

    override fun showDialogPassword(note: Note) {
        TODO("Not yet implemented")
    }


}