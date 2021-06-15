package com.android.puy.mvvm.base

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.umeng.analytics.MobclickAgent
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * Created by puy on 2021/6/15 13:40
 */
abstract class XFragmentationActivity<VM : XViewModel, DB : ViewDataBinding> : SupportActivity() {
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: DB
    lateinit var context: Activity
    private lateinit var mImmersionBar: ImmersionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        initViewDataBinding()
        mViewModel = initViewMode()
        lifecycle.addObserver(mViewModel)
        initView(savedInstanceState)
        if (useEventBus()) EventBus.getDefault().register(this)
        initData()
    }

    override fun onResume() {
        super.onResume()
        if (useUmeng()) MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        if (useUmeng()) MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
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
            mBinding.lifecycleOwner = this
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

    //获取viewModel
    protected fun <VM : ViewModel?> getViewModel(viewModelClass: Class<VM>): VM {
        return ViewModelProvider(this)[viewModelClass]
    }

    open fun initStatusBar(id: Int) {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.titleBar(id)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    open fun initStatusBar(id: Int, statusBarDark: Boolean) {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.titleBar(id)
        mImmersionBar.statusBarDarkFont(statusBarDark)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    open fun initStatusBar(id: Int, statusBarDark: Boolean, navigationBarColor: Int) {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.titleBar(id)
        mImmersionBar.statusBarDarkFont(statusBarDark)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.navigationBarColor(navigationBarColor)
        mImmersionBar.init()
    }

    //是否使用eventBus
    open fun useEventBus(): Boolean {
        return false
    }

    //是否使用友盟统计 默认开启
    open fun useUmeng(): Boolean {
        return true
    }
}