package com.android.puy.puymvvm.fragments

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.puy.mvvm.base.XViewModel
import com.android.puy.puymvvm.UserBean

/**
 * Created by puy on 2021/6/15 16:32
 */

class PageOneViewModel(application: Application) : XViewModel(application) {
    val mDatas = MutableLiveData<UserBean>()
    fun getData() {
        mDatas.value = UserBean("new page one")
    }
}