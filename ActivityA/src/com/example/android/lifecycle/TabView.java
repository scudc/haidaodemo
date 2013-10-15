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
        setClickable(true); //设置是否能点击  
    }  
  
    /** 
     * 通过canvas 画控件 
     */  
    @Override  
    public void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        mPaint = new Paint();  
        //设置画笔颜色   
        mPaint.setColor(Color.RED);  
        //设置填充   
        mPaint.setStyle(Style.FILL);  
        //画一个矩形,前俩个是矩形左上角坐标，后面俩个是右下角坐标   
        canvas.drawRect(new Rect(10, 10, 100, 100), mPaint);  
        mPaint.setColor(Color.BLUE);  
        //绘制文字   
        canvas.drawText("Hello WORLD", 10, 110, mPaint);  
    }  
  
}  
