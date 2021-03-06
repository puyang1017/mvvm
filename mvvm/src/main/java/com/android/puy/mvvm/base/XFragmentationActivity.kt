package com.android.puy.mvvm.base

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.puy.mvvm.router.AppManager
import com.gyf.immersionbar.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
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
    private var rxPermissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        initViewDataBinding()
        mViewModel = initViewMode()
        lifecycle.addObserver(mViewModel)
        initView(savedInstanceState)
        if (useEventBus()) EventBus.getDefault().register(this)
        initData()
        AppManager.instance.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
//        if (useUmeng()) MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
//        if (useUmeng()) MobclickAgent.onPause(this)
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

    //??????
    abstract fun layoutId(): Int

    //?????????viewModel
    abstract fun initViewMode(): VM

    //?????????UI??????
    abstract fun initView(savedInstanceState: Bundle?)

    //?????????????????????
    abstract fun initData()

    //??????viewModel
    protected fun <VM : ViewModel?> getViewModel(viewModelClass: Class<VM>): VM {
        return ViewModelProvider(this)[viewModelClass]
    }

    //??????????????????
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

    //????????????
    open fun getRxPermissions(): RxPermissions {
        rxPermissions = RxPermissions(this)
        return rxPermissions!!
    }

    //????????????eventBus
    open fun useEventBus(): Boolean {
        return false
    }

    //???????????????????????? ????????????
    open fun useUmeng(): Boolean {
        return true
    }
}