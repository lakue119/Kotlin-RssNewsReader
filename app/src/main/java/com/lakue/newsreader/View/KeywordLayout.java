package com.lakue.newsreader.View;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lakue.newsreader.Base.BaseUtil;
import com.lakue.newsreader.R;

public class KeywordLayout extends LinearLayout {

    int count = 3;
    public KeywordLayout(Context context) {
        super(context);
        init();
    }

    public KeywordLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeywordLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
//
//        String infService = Context.LAYOUT_INFLATER_SERVICE;
//        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
//        View v = li.inflate(R.layout.linearlayout, this, false);
//        addView(v);
//
//        ll_bg = (LinearLayout) findViewById(R.id.ll_bg);
//
        setOrientation(VERTICAL);
//        LayoutParams layoutParams = new LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        setLayoutParams(layoutParams);
    }
//    Boolean isNextLine = false;
    public void addKeyWord(final String... keyword){
        if(getCount() > 2){
            return;
        }
        LinearLayout ll  = new LinearLayout(getContext());
        ll.setOrientation(HORIZONTAL);
//        LayoutParams linearlayoutParams = new LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        ll.setLayoutParams(linearlayoutParams);
//        ll.setBackgroundColor(getContext().getColor(R.color.colorPrimary));


        for(int i = 0;i<keyword.length; i++){
            final TextView textView = new TextView(getContext());
            final String keywordText = keyword[i];

            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0,0,
                    BaseUtil.convertPixelsToDp(8,getContext()),
                    BaseUtil.convertPixelsToDp(8,getContext()));
            textView.setText(keywordText);
            textView.setMaxWidth(
                    (BaseUtil.getDeviceWidth(getContext()))/3);   //키워드 하나의 크기 = (디바이스 가로크기 - 왼쪽 피드이미지)/3

            textView.setLayoutParams(layoutParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(getContext().getColor(R.color.colorLightBlack));
                textView.setPadding(
                        BaseUtil.convertPixelsToDp(12,getContext()),
                        BaseUtil.convertPixelsToDp(4,getContext()),
                        BaseUtil.convertPixelsToDp(12,getContext()),
                        BaseUtil.convertPixelsToDp(4,getContext()));
                textView.setTextSize(12);
                textView.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
                textView.setBackground(getContext().getDrawable(R.drawable.background_rounding_20_white_stroke_black));
            }

//            final int finalI = i;
//            textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    if(textView.getHeight() > 73){
//                        if(!isNextLine){
//                            removeView(textView);
//                            Log.i("QWKJREKL", "ExcessText : " + keywordText);
//                            LinearLayout ll = new LinearLayout(getContext());
//                            LayoutParams layoutParams = new LayoutParams(
//                                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                            layoutParams.setMargins(0,0,
//                                    BaseUtil.convertPixelsToDp(8,getContext()),
//                                    BaseUtil.convertPixelsToDp(8,getContext()));
//                            final TextView textView1 = new TextView(getContext());
//
//                            textView1.setText(keywordText);
//                            textView1.setLayoutParams(layoutParams);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                textView1.setTextColor(getContext().getColor(R.color.colorLightBlack));
//                                textView1.setPadding(
//                                        BaseUtil.convertPixelsToDp(12,getContext()),
//                                        BaseUtil.convertPixelsToDp(4,getContext()),
//                                        BaseUtil.convertPixelsToDp(12,getContext()),
//                                        BaseUtil.convertPixelsToDp(4,getContext()));
//                                textView1.setTextSize(12);
//                                textView1.setGravity(TextView.TEXT_ALIGNMENT_CENTER);
//                                textView1.setBackground(getContext().getDrawable(R.drawable.background_rounding_20_white_stroke_black));
//                            }
//
//                            ll.setOrientation(LinearLayout.HORIZONTAL);
//                            ll.addView(textView1);
//                            addView(ll);
//                            isNextLine = true;
//                        }
//
//                    } else{
//                        Log.i("QWKJREKL", "textView.getHeight()" + textView.getHeight());
//                    }
//                }
//            });

            ll.addView(textView);
        }

        addView(ll);


        //Log.i("QWKJREKL", String.valueOf(textView.getHeight()));
    }

    public void reset(){
        for(int i=0;i<getChildCount();i++){
            View view = getChildAt(i);
            removeView(view);
        }
    }

    public int getCount(){
       return getChildCount();
    }

    public Boolean isExcessText(int height){
        if(height > 173){
            return true;
        } else{
             return false;
        }
    }
}
