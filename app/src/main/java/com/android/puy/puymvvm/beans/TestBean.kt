package com.android.puy.puymvvm.beans

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.android.puy.puymvvm.BR

/**
 * Created by puy on 2021/6/29 20:08
 */
open class TestBean : BaseObservable() {
    private var name: String? = null

    @Bindable
    fun getName() = name

    fun setName(name: String) {
        this.name = name
        notifyPropertyChanged(BR.name)
        println("notifyPropertyChanged $name")
    }
}