package com.android.puy.puymvvm.viewbinds

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.android.puy.puymvvm.widget.MyView

/**
 * Created by puy on 2021/6/29 19:58
 */
class ViewBinding {
    companion object {
        @JvmStatic
        @BindingAdapter("bindText")
        fun setBindText(myView: MyView, newMsg: String?) {
            println("setBindText 1")
            println("setBindText newMsg-$newMsg myView.getTextMsg()-${myView.getViewTextMsg()}   ${myView.getViewTextMsg() != newMsg}")
            if (myView.getViewTextMsg() != newMsg) {
                myView.setViewTextMsg(newMsg)
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "bindText", event = "bindTextAttrChanged")
        fun getBindText(myView: MyView): String {
            println("setBindText 2")
            return myView.getTextMsg()
        }

        @JvmStatic
        @BindingAdapter("bindTextAttrChanged", requireAll = false)
        fun bindingListener(myView: MyView, listener: InverseBindingListener?) {
            println("setBindText 3")
            if (listener == null) {
                myView.setListener(null)
            } else {
                myView.setListener(listener)
            }
        }
    }
}