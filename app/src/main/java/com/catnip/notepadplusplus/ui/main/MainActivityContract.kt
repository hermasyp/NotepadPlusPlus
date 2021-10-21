package com.catnip.notepadplusplus.ui.main

import com.catnip.todolistapp.base.BaseContract

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface MainActivityContract {
    interface View : BaseContract.BaseView {
        fun setupFragment()
        fun setClickListeners()
        fun changePassword()
        fun showDialogChangePassword()
    }

    interface Presenter : BaseContract.BasePresenter {
    }


}