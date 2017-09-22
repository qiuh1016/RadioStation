package com.cetcme.radiostation.MyClass;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cetcme.radiostation.R;

/**
 * Created by qiuhong on 21/09/2017.
 */

public class VoiceView extends LinearLayout {


    private int lastX;
    private int lastY;

    private int originalTop = 0;
    private Context context;
    private View view;

    String TAG = "VoiceView";

    public VoiceView(Context context) {
        super(context);
        this.context = context;
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.voice_view, this, true);
//        Button button = new Button(this.context);
//        button.setHeight(100);
//        button.setWidth(100);
//        button.setText("button");
//        button.setBackgroundColor(0xFFFFFF);
//        this.addView(button);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalTop == 0) {
            originalTop = getTop();
        }
        if (expanded) {
            layout(getLeft(), originalTop - 300, getRight(), getBottom());
            expanded = false;
        }
        Log.e(TAG, "onDraw: ");
    }

    boolean expanded = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录触摸点的坐标
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //在当前left,top,right,bottom的基础上加上偏移量
                //layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                if (getTop() + offsetY < originalTop) {
                    layout(getLeft(), getTop() + offsetY, getRight(), getBottom());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (originalTop - getTop() > 100) {
                    expanded = true;
                    layout(getLeft(), originalTop - 300, getRight(), getBottom());

                    TextView text = (TextView) view.findViewById(R.id.textView_1);
                    text.setText("123");


                } else {
                    layout(getLeft(), originalTop, getRight(), getBottom());
                }
                break;
        }
        return true;
    }
}
