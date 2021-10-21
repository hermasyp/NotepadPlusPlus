package com.catnip.notepadplusplus.ui.main

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.catnip.notepadplusplus.R
import com.catnip.notepadplusplus.base.BaseActivity
import com.catnip.notepadplusplus.data.local.sharedpreference.UserPreferences
import com.catnip.notepadplusplus.databinding.ActivityMainBinding
import com.catnip.notepadplusplus.ui.changepassword.ChangePasswordBottomSheet
import com.catnip.notepadplusplus.ui.enterpassword.EnterPasswordBottomSheet
import com.catnip.notepadplusplus.ui.main.notelist.NoteListFragment
import com.catnip.notepadplusplus.ui.noteform.NoteFormActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityContract.Presenter>(
    ActivityMainBinding::inflate
), MainActivityContract.View {

    private var allNotesFragment = NoteListFragment()
    private var archivedNotesFragment = NoteListFragment(true)
    private var activeFragment: Fragment = allNotesFragment

    override fun initView() {
        setupFragment()
        setClickListeners()
    }

    override fun initPresenter() {
        val presenter = MainActivityPresenter(this)
        setPresenter(presenter)
    }

    override fun setupFragment() {
        getViewBinding().bottomNavigationView.background = null
        // delete all fragment in fragment manager first
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        // add fragment to fragment manager
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fl_container, allNotesFragment, TAG_FRAGMENT_ALL_NOTES)
            add(R.id.fl_container, archivedNotesFragment, TAG_FRAGMENT_ARCHIVED_NOTES).hide(
                archivedNotesFragment
            )
        }.commit()
        // set title for first fragment
        supportActionBar?.title = getString(R.string.title_toolbar_menu_all_notes)
        // set click menu for changing fragment
        getViewBinding().bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_all_notes -> {
                    supportActionBar?.title = getString(R.string.title_toolbar_menu_all_notes)
                    showFragment(allNotesFragment)
                    true
                }
                else -> {
                    supportActionBar?.title = getString(R.string.title_toolbar_menu_archived_notes)
                    showFragment(archivedNotesFragment)
                    true
                }
            }
        }
    }

    override fun setClickListeners() {
        getViewBinding().fab.setOnClickListener {
            NoteFormActivity.startActivity(this, NoteFormActivity.FORM_MODE_INSERT)
        }
    }


    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }

    companion object {
        private const val TAG_FRAGMENT_ALL_NOTES = "TAG_FRAGMENT_ALL_NOTES"
        private const val TAG_FRAGMENT_ARCHIVED_NOTES = "TAG_FRAGMENT_ARCHIVED_NOTES"
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_change_password -> {
                changePassword()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun changePassword() {
        val currentPassword = UserPreferences(this).password
        if (currentPassword.isNullOrEmpty()) {
            showDialogChangePassword()
        } else {
            EnterPasswordBottomSheet { isPasswordCorrect ->
                if (isPasswordCorrect) {
                    showDialogChangePassword()
                }
            }.show(supportFragmentManager, null)
        }
    }

    override fun showDialogChangePassword() {
        ChangePasswordBottomSheet {}.show(supportFragmentManager, null)
    }
}