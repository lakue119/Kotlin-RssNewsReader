package com.lakue.newsreader.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splish.*
import android.os.Handler
import com.lakue.newsreader.BuildConfig
import com.lakue.newsreader.R


class ActivitySplish : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splish)

        val versionName = BuildConfig.VERSION_NAME

        //현재 앱 버전이름을 가져옴
        tv_version.text = versionName

        Handler().postDelayed({
            // 1.3초 후 메인액티비티로 화면전
            val nextIntent = Intent(this, ActivityNewsList::class.java)
            startActivity(nextIntent)
            finish()
        }, 1300)
    }
}
