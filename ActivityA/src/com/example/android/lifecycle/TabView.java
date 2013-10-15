package com.example.android.lifecycle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.view.View;  
  
public class TabView extends View {  
  
    private Paint mPaint;  
  
    public TabView(Context context) {  
        super(context);  
    }  
  
    public TabView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        setFocusable(true);  
        setClickable(true); //�����Ƿ��ܵ��  
    }  
  
    /** 
     * ͨ��canvas ���ؼ� 
     */  
    @Override  
    public void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        mPaint = new Paint();  
        //���û�����ɫ   
        mPaint.setColor(Color.RED);  
        //�������   
        mPaint.setStyle(Style.FILL);  
        //��һ������,ǰ�����Ǿ������Ͻ����꣬�������������½�����   
        canvas.drawRect(new Rect(10, 10, 100, 100), mPaint);  
        mPaint.setColor(Color.BLUE);  
        //��������   
        canvas.drawText("Hello WORLD", 10, 110, mPaint);  
    }  
  
}  
