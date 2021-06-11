package com.android.puy.mvvm.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by puy on 2021/6/11 15:22
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        mViewModel = initViewMode()
        lifecycle.addObserver(mViewModel)
        initView(savedInstanceState)
        initData()
    }

    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java
            if (layoutId() == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            (mBinding as ViewDataBinding).lifecycleOwner = this
        } else throw IllegalArgumentException("Generic error")
    }

    //布局
    abstract fun layoutId(): Int

    //初始化viewModel
    abstract fun initViewMode(): VM

    //初试化UI相关
    abstract fun initView(savedInstanceState: Bundle?)

    //初始化数据相关
    abstract fun initData()

    protected fun <VM : ViewModel?> getViewModel(viewModelClass: Class<VM>): VM? {
        return ViewModelProvider(this)[viewModelClass]
    }
}