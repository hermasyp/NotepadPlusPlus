package com.catnip.notepadplusplus.ui.main.notelist

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.catnip.notepadplusplus.R
import com.catnip.notepadplusplus.base.BaseFragment
import com.catnip.notepadplusplus.data.local.NotesDatabase
import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSourceImpl
import com.catnip.notepadplusplus.data.model.Note
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.databinding.FragmentNoteListBinding
import com.catnip.notepadplusplus.ui.main.notelist.adapter.NotesListAdapter
import com.catnip.notepadplusplus.ui.noteform.NoteFormActivity
import com.catnip.notepadplusplus.utils.CommonFunction
import com.catnip.notepadplusplus.utils.SpacesItemDecoration

import android.widget.Toast
import com.catnip.notepadplusplus.ui.enterpassword.EnterPasswordBottomSheet


class NoteListFragment(private val isArchivedOnly: Boolean = false) :
    BaseFragment<FragmentNoteListBinding, NoteListContract.Presenter>(FragmentNoteListBinding::inflate),
    NoteListContract.View {

    private lateinit var adapter: NotesListAdapter

    override fun initView() {
        initList()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun showContent(isContentVisible: Boolean) {
        getViewBinding().rvNotes.visibility = if (isContentVisible) View.VISIBLE else View.GONE
    }

    override fun showLoading(isLoading: Boolean) {
        getViewBinding().layoutScenario.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showError(isErrorEnabled: Boolean, msg: String?) {
        getViewBinding().layoutScenario.tvMessage.visibility =
            if (isErrorEnabled) View.VISIBLE else View.GONE
        getViewBinding().layoutScenario.tvMessage.text = msg
    }

    override fun getData() {
        if (isArchivedOnly) getPresenter().getArchivedNotes() else getPresenter().getAllNotes()
    }

    override fun onDataCallback(response: Resource<List<Note>>) {
        when (response) {
            is Resource.Loading -> {
                showLoading(true)
                showError(false, null)
                showContent(false)
            }
            is Resource.Success -> {
                showLoading(false)
                response.data?.let {
                    if (it.isEmpty()) {
                        showError(true, getString(R.string.text_empty_notes))
                        showContent(false)
                    } else {
                        showError(false, null)
                        showContent(true)
                        setListData(it)
                    }
                }
            }
            is Resource.Error -> {
                showLoading(false)
                showError(true, response.message)
                showContent(false)
            }
        }
    }

    override fun initList() {
        adapter = NotesListAdapter {
            if(it.isLocked){
                showDialogPassword(it)
            }else{
                navigateToForm(it)
            }
        }
        getViewBinding().rvNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            addItemDecoration(SpacesItemDecoration(CommonFunction.dpToPixels(context, 8)))
            adapter = this@NoteListFragment.adapter
        }
    }
    private fun navigateToForm(note : Note){
        context?.let { context ->
            NoteFormActivity.startActivity(
                context,
                NoteFormActivity.FORM_MODE_EDIT,
                note
            )
        }
    }

    override fun setListData(data: List<Note>) {
        adapter.setItems(data)
    }

    override fun showDialogPassword(note : Note) {
        EnterPasswordBottomSheet { isPasswordCorrect ->
            if(isPasswordCorrect){
                navigateToForm(note)
            }else{
                Toast.makeText(context, getString(R.string.text_wrong_password), Toast.LENGTH_SHORT).show()
            }
        }.show(childFragmentManager, null)
    }

    override fun initPresenter() {
        context?.let {
            val dataSource = NotesDataSourceImpl(NotesDatabase.getInstance(it).noteDao())
            val repository = NoteListRepository(dataSource)
            setPresenter(NoteListPresenter(this, repository))
        }
    }

}