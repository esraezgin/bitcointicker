package com.android.app.loodos.bitcointicker.core.common

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.text.toLowerCase

object Helper {
    fun setToolbarTitle(title: String,textView: AppCompatTextView){
        textView.text = title.uppercase()
    }

    fun setVisibility(isVisible:Boolean,view:View){
        if(isVisible)  view.visibility = View.VISIBLE
        else   view.visibility = View.GONE
    }

    fun controlStringData(data : String?) :String{
        return if(data.isNullOrEmpty()){
          "----"
        }else {
            data
        }
    }
    fun controlDoubleData(data: Double?) :Double{
        return data ?: 0.0
    }


}