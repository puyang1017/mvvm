package com.android.puy.mvvm.router

import android.app.Activity
import android.os.Process
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by puy on 2020/9/1 13:14
 */
class AppManagerDelegate private constructor() {
    // 使用弱引用是因为存在未使用AppManager的finish方法来释放的activity，但mActivityStack并未断开对其应用导致内存泄露的问题
    private var mActivityStack: Stack<WeakReference<Activity>>? = null

    /**
     * 添加Activity到堆栈
     *
     * @param activity activity实例
     */
    fun addActivity(activity: Activity) {
        if (mActivityStack == null) {
            mActivityStack = Stack()
        }
        mActivityStack!!.add(WeakReference(activity))
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    fun checkWeakReference() {
        if (mActivityStack != null) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val temp = activityReference.get()
                if (temp == null) {
                    it.remove() // 使用迭代器来进行安全的加锁操作
                }
            }
        }
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     *
     * @return 当前（栈顶）activity
     */
    fun currentActivity(): Activity? {
        checkWeakReference()
        return if (mActivityStack != null && !mActivityStack!!.isEmpty()) {
            mActivityStack!!.lastElement().get()
        } else null
    }

    /**
     * 结束除当前activtiy以外的所有activity
     * 注意：不能使用foreach遍历并发删除，会抛出java.util.ConcurrentModificationException的异常
     *
     * @param activtiy 不需要结束的activity
     */
    fun finishOtherActivity(activtiy: Activity?) {
        if (mActivityStack != null && activtiy != null) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val temp = activityReference.get()
                if (temp == null) { // 清理掉已经释放的activity
                    it.remove()
                    continue
                }
                if (temp !== activtiy) {
                    it.remove() // 使用迭代器来进行安全的加锁操作
                    temp.finish()
                }
            }
        }
    }

    /**
     * 结束除这一类activtiy以外的所有activity
     *
     * @param cls 指定的某类activity
     */
    fun finishOtherActivity(cls: Class<*>) {
        if (mActivityStack != null) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                if (activity == null) { // 清理掉已经释放的activity
                    it.remove()
                    continue
                }
                if (activity.javaClass != cls) {
                    it.remove() // 使用迭代器来进行安全的加锁操作
                    activity.finish()
                }
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = currentActivity()
        activity?.let { finishActivity(it) }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 指定的activity实例
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val temp = activityReference.get()
                if (temp == null) { // 清理掉已经释放的activity
                    it.remove()
                    continue
                }
                if (temp === activity) {
                    it.remove()
                }
            }
            activity.finish()
        }
    }

    /**
     * 结束指定类名的所有Activity
     *
     * @param cls 指定的类的class
     */
    fun finishActivity(cls: Class<*>) {
        if (mActivityStack != null) {
            val it = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                if (activity == null) { // 清理掉已经释放的activity
                    it.remove()
                    continue
                }
                if (activity.javaClass == cls) {
                    it.remove()
                    activity.finish()
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (mActivityStack != null) {
            val it: Iterator<WeakReference<Activity>> = mActivityStack!!.iterator()
            while (it.hasNext()) {
                val activityReference = it.next()
                val activity = activityReference.get()
                activity?.finish()
            }
            mActivityStack!!.clear()
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
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

    companion object {
        @Volatile
        private var sInstance: AppManagerDelegate? = null

        /**
         * 单例
         *
         * @return 返回AppManager的单例
         */
        val instance: AppManagerDelegate?
            get() {
                if (sInstance == null) {
                    synchronized(AppManagerDelegate::class.java) {
                        if (sInstance == null) {
                            sInstance = AppManagerDelegate()
                        }
                    }
                }
                return sInstance
            }
    }
}