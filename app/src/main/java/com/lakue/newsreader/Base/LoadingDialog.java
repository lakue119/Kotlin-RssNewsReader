package com.lakue.newsreader.Base;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.lakue.newsreader.R;

public class LoadingDialog {

    AppCompatDialog progressDialog;
    final int TIME_OUT = 15000;

    public LoadingDialog(){
    }

    public void progressON(Activity activity, String message, Boolean showImage) {



        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.show();

        }
        //final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);

        final LottieAnimationView lottie_loading = (LottieAnimationView) progressDialog.findViewById(R.id.lottie_loading);
        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);

        if(showImage){
            lottie_loading.setVisibility(View.VISIBLE);
            tv_progress_message.setVisibility(View.VISIBLE);
        } else {
            lottie_loading.setVisibility(View.GONE);
            tv_progress_message.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //여기에 딜레이 후 시작할 작업들을 입력
                progressOFF(0);
            }
        }, TIME_OUT);// 0.5초 정도 딜레이를 준 후 시작
    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF(int sleepTime) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //여기에 딜레이 후 시작할 작업들을 입력
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, sleepTime);// 0.5초 정도 딜레이를 준 후 시작

    }

}
