package com.cetcme.radiostation.MyClass;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by qiuhong on 21/09/2017.
 */

public class DragView extends android.support.v7.widget.AppCompatTextView {

    private int lastX;
    private int lastY;

    private int originalTop = 0;

    String TAG = "DragView";

    public DragView(Context context) {
        super(context);
        initView();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalTop == 0) {
            originalTop = getTop();
        }

    }

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
                    layout(getLeft(), originalTop - 300, getRight(), getBottom());
                } else {
                    layout(getLeft(), originalTop, getRight(), getBottom());
                }
                break;
        }
        return true;
    }

}
