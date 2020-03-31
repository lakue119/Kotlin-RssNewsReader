package com.lakue.newsreader.Base

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatDialog

import com.airbnb.lottie.LottieAnimationView
import com.lakue.newsreader.R

class LoadingDialog {

    private var progressDialog: AppCompatDialog? = null
    private val TIME_OUT = 30000

    //로딩창 보여주기
    fun progressON(activity: Activity?, message: String, showImage: Boolean?) {

        if (activity == null || activity.isFinishing) {
            return
        }

        if (progressDialog != null && progressDialog!!.isShowing) {
            progressSET(message)
        } else {

            progressDialog = AppCompatDialog(activity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.dialog_loading)
            progressDialog!!.show()

        }
        //final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);

        val lottieLoading =
            progressDialog!!.findViewById<View>(R.id.lottie_loading) as LottieAnimationView?
        val tvProgressMessage =
            progressDialog!!.findViewById<View>(R.id.tv_progress_message) as TextView?

        if (showImage!!) {
            lottieLoading!!.visibility = View.VISIBLE
            tvProgressMessage!!.visibility = View.VISIBLE
        } else {
            lottieLoading!!.visibility = View.GONE
            tvProgressMessage!!.visibility = View.GONE
        }


        if (!TextUtils.isEmpty(message)) {
            tvProgressMessage.text = message
        }

        Handler().postDelayed({
            //여기에 딜레이 후 시작할 작업들을 입력
            progressOFF(0)
        }, TIME_OUT.toLong())// 0.5초 정도 딜레이를 준 후 시작
    }

    //로딩창 생성
    fun progressSET(message: String) {

        if (progressDialog == null || !progressDialog!!.isShowing) {
            return
        }

        val tvProgressMessage =
            progressDialog!!.findViewById<View>(R.id.tv_progress_message) as TextView?
        if (!TextUtils.isEmpty(message)) {
            tvProgressMessage!!.text = message
        }

    }

    //로딩창 종료
    fun progressOFF(sleepTime: Int) {
        Handler().postDelayed({
            //여기에 딜레이 후 시작할 작업들을 입력
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }, sleepTime.toLong())// 0.5초 정도 딜레이를 준 후 시작

    }

}
