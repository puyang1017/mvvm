package com.android.puy.puymvvm.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.puy.mvvm.base.XFragmentation
import com.android.puy.puymvvm.R
import com.android.puy.puymvvm.databinding.FragmentPageOneBinding
import kotlinx.android.synthetic.main.fragment_page_one.*

/**
 * Created by puy on 2021/6/15 16:30
 */
class PageOne :XFragmentation<PageOneViewModel, FragmentPageOneBinding>() {
    override fun layoutId() = R.layout.fragment_page_one

    override fun initViewMode() = getViewModel(PageOneViewModel::class.java)

    override fun initView(savedInstanceState: Bundle?) {
        button.setOnClickListener {
            mViewModel.getData()
        }
    }

    override fun initData() {
        mViewModel.mDatas.observe(this, Observer{
            text.text = it.name
        })
    }
}