package com.android.puy.puymvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.puy.mvvm.base.XViewModel

/**
 * Created by puy on 2021/6/11 16:38
 */
class MainViewModel(application: Application) : XViewModel(application) {
    val mDatas = MutableLiveData<UserBean>()
    fun getData() {
        mDatas.value = UserBean("123")
    }
}