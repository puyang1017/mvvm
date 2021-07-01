package com.android.puy.puymvvm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.puy.mvvm.base.XFragmentationActivity
import com.android.puy.puymvvm.beans.TestBean
import com.android.puy.puymvvm.databinding.ActivityMainBinding
import com.android.puy.puymvvm.fragments.FragmentPager
import com.android.puy.puymvvm.fragments.PageOne
import com.android.puy.puymvvm.fragments.PageTwo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : XFragmentationActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main

    override fun initViewMode() = getViewModel(MainViewModel::class.java)

    override fun initView(savedInstanceState: Bundle?) {
        val list = ArrayList<Fragment>()
        list.add(PageOne())
        list.add(PageTwo())
        ViewPager.adapter = FragmentPager(supportFragmentManager, list)

    }
    val testBean = TestBean()
    override fun initData() {
        mViewModel?.mDatas?.observe(this, Observer {
            mBinding.userBean = it
        })
        testBean.setName("双向绑定测试")
        mBinding.testBean = testBean
//        mBinding.setVariable(BR.testBean, testBean)
    }

    fun getData(v: View) {
        println("testBean "+testBean.getName())
        mViewModel?.getData()
    }
}

