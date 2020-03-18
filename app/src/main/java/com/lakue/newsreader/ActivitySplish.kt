package com.lakue.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splish.*
import android.os.Handler




class ActivitySplish : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splish)

        val versionName = BuildConfig.VERSION_NAME

        //현재 앱 버전이름을 가져옴
        tv_version.text = versionName

        Handler().postDelayed({
            // 1.3초 후 메인액티비티로 화면전
            val nextIntent = Intent(this, ActivityMain::class.java)
            startActivity(nextIntent)
        }, 1300)
    }
}
