package com.android.puy.puymvvm.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.*
import com.android.puy.puymvvm.R

/**
 * 双向绑定测试view
 * Created by puy on 2021/6/29 13:27
 */
class MyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mContext: Context
    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private var listener: InverseBindingListener? = null
    private var text: String = "123456"

    init {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        val view = LayoutInflater.from(context).inflate(R.layout.layout_my_view, this, true)
        editText = view.findViewById(R.id.editText)
        textView = view.findViewById(R.id.textView)
        editText.doAfterTextChanged {
            text = it.toString()
            println("setBindText 4")
            listener?.onChange()
        }
    }
    fun getViewTextMsg() = textView.text.toString()

    fun getTextMsg() = text

    fun setViewTextMsg(msg: String?) {
        textView.text = msg
    }

    fun setListener(listener: InverseBindingListener?) {
        this.listener = listener
    }

}