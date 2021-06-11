package com.android.puy.puymvvm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.android.puy.mvvm.base.BaseActivity
import com.android.puy.puymvvm.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main

    override fun initViewMode(): MainViewModel {
        return getViewModel(MainViewModel::class.java)!!
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        mViewModel?.mDatas?.observe(this, Observer {
            mBinding.userBean = it
        })
    }

    fun getData(v: View) {
        mViewModel?.getData()
    }
}

