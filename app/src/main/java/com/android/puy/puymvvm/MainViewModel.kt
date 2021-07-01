package com.android.puy.puymvvm

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.puy.mvvm.base.XViewModel
import com.android.puy.puymvvm.beans.UserBean

/**
 * Created by puy on 2021/6/11 16:38
 */
class MainViewModel(application: Application) : XViewModel(application) {
    val mDatas = MutableLiveData<UserBean>()
    var msg = ObservableField<String>()

    fun getData() {
        println("getData msg "+msg.get())
        mDatas.value = UserBean("123")
    }
}