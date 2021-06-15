package com.android.puy.mvvm.router

import android.app.Activity
import android.os.Process
import java.util.*
import kotlin.jvm.javaClass as javaClass1

/**
 * Created by puy on 2020/9/1 13:15
 */
class AppManager private constructor(isBridge: Boolean) {
    /** 是否使用桥接模式  */
    private var isBridge = false

    /** AppManager管理activity的委托类  */
    private val mDelegate: AppManagerDelegate

    /** 维护activity的栈结构  */
    private var mActivityStack: Stack<Activity?>? = null

    /**
     * 添加Activity到堆栈
     *
     * @param activity activity实例
     */
    fun addActivity(activity: Activity) {
        if (isBridge) {
            mDelegate.addActivity(activity)
        } else {
            if (mActivityStack == null) {
                mActivityStack = Stack()
            }
            mActivityStack!!.add(activity)
        }
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return 当前（栈顶）activity
     */
    fun currentActivity(): Activity? {
        return if (isBridge) {
            mDelegate.currentActivity()
        } else {
            if (mActivityStack != null && !mActivityStack!!.isEmpty()) {
                mActivityStack!!.lastElement()
            } else null
        }
    }

    /**
     * 结束除当前activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param activity 不需要结束的activity
     */
    fun finishOtherActivity(activity: Activity) {
        if (isBridge) {
            mDelegate.finishOtherActivity(activity)
        } else {
            if (mActivityStack != null) {
                val it: Iterator<Activity?> = mActivityStack!!.iterator()
                while (it.hasNext()) {
                    val temp = it.next()
                    if (temp != null && temp !== activity) {
                        finishActivity(temp)
                    }
                }
            }
        }
    }

    /**
     * 结束除这一类activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param cls 不需要结束的activity
     */
    fun finishOtherActivity(cls: Class<*>) {
        if (isBridge) {
            mDelegate.finishOtherActivity(cls)
        } else {
            if (mActivityStack != null) {
                val it: Iterator<Activity?> = mActivityStack!!.iterator()
                while (it.hasNext()) {
                    val activity = it.next()
                    if (activity?.javaClass1 != cls) {
                        finishActivity(activity)
                    }
                }
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        if (isBridge) {
            mDelegate.finishActivity()
        } else {
            if (mActivityStack != null && !mActivityStack!!.isEmpty()) {
                val activity = mActivityStack!!.lastElement()
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 指定的activity实例
     */
    fun finishActivity(activity: Activity?) {
        if (isBridge) {
            mDelegate.finishActivity(activity)
        } else {
            if (activity != null) {
                if (mActivityStack != null && mActivityStack!!.contains(activity)) { // 兼容未使用AppManager管理的实例
                    mActivityStack!!.remove(activity)
                }
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的所有Activity
     *
     * @param cls 指定的类的class
     */
    fun finishActivity(cls: Class<*>) {
        if (isBridge) {
            mDelegate.finishActivity(cls)
        } else {
            if (mActivityStack != null) {
                val it: Iterator<Activity?> = mActivityStack!!.iterator()
                while (it.hasNext()) {
                    val activity = it.next()
                    if (activity?.javaClass1 == cls) {
                        finishActivity(activity)
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (isBridge) {
            mDelegate.finishAllActivity()
        } else {
            if (mActivityStack != null) {
                var i = 0
                val size = mActivityStack!!.size
                while (i < size) {
                    if (null != mActivityStack!![i]) {
                        mActivityStack!![i]!!.finish()
                    }
                    i++
                }
                mActivityStack!!.clear()
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        if (isBridge) {
            mDelegate.exitApp()
        } else {
            try {
                finishAllActivity()
                // 退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
                System.exit(0)
                // 从操作系统中结束掉当前程序的进程
                Process.killProcess(Process.myPid())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @Volatile
        private var sInstance: AppManager? = null

        /**
         * 单例
         *
         * @return 返回AppManager的单例
         */
        val instance: AppManager
            get() {
                if (sInstance == null) {
                    synchronized(AppManager::class.java) {
                        if (sInstance == null) {
                            sInstance = AppManager(true)
                        }
                    }
                }
                return sInstance!!
            }
    }

    /**
     * 隐藏构造器
     *
     * @param isBridge 是否开启桥接模式
     */
    init {
        this.isBridge = isBridge
        mDelegate = AppManagerDelegate.instance!!
    }
}