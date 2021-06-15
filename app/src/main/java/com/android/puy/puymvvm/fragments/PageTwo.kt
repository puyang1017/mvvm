package com.android.puy.puymvvm.fragments

import android.os.Bundle
import com.android.puy.mvvm.base.XFragmentation
import com.android.puy.puymvvm.R
import com.android.puy.puymvvm.databinding.FragmentPageOneBinding

/**
 * Created by puy on 2021/6/15 16:30
 */
class PageTwo : XFragmentation<PageTwoViewModel, FragmentPageOneBinding>() {
    override fun layoutId() = R.layout.fragment_page_two

    override fun initViewMode() = getViewModel(PageTwoViewModel::class.java)

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }
}