package com.lakue.newsreader.Base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {

    val TAG: String = "NewsReaderTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showToast(toastMessage: String){
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun errorLog(logMessage: String){
        Log.e(TAG,logMessage)
    }

    fun infoLog(logMessage: String){
        Log.i(TAG,logMessage)
    }

    fun debugLog(logMessage: String){
        Log.d(TAG,logMessage)
    }

    fun warnLog(logMessage: String){
        Log.w(TAG,logMessage)
    }
}
