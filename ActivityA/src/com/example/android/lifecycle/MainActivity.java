/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.lifecycle;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;
/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	

    private String mActivityName;
    private TextView mStatusView;
    private TextView mStatusAllView;
    private StatusTracker mStatusTracker = StatusTracker.getInstance();
    
    /* (non-Javadoc)
     * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 setContentView(R.layout.activity_main);
    	 
    	 
  		 final TabHost tabs=(TabHost)findViewById(android.R.id.tabhost); 
  		 final TabWidget tabWidget=(TabWidget) findViewById(android.R.id.tabs);
  		 
  		 
         tabWidget.setBackgroundColor(Color.BLACK);

         int width =60;
         int height =60;
         tabs.setup();   
         tabs.addTab(tabs.newTabSpec("first tab")
                 .setIndicator("动态",null)
                 .setContent(R.id.tab1));
         tabs.addTab(tabs.newTabSpec("second tab")
                 .setIndicator("产品",null)
                 .setContent(R.id.tab2));
         tabs.addTab(tabs.newTabSpec("third tab")
                 .setIndicator("职位",null)
                 .setContent(R.id.tab3)
                 );
         tabs.addTab(tabs.newTabSpec("four tab")
                 .setIndicator("观点",null)
                 .setContent(R.id.tab4)
                 );
            
         tabs.setCurrentTab(0);  
         
         for (int i =0; i <tabWidget.getChildCount(); i++) {
             /**
              * 设置高度、宽度，不过宽度由于设置为fill_parent，在此对它没效果
              */
             tabWidget.getChildAt(i).getLayoutParams().height = height;
             tabWidget.getChildAt(i).getLayoutParams().width = width;


          /**
           * 设置tab中标题文字的颜色，不然默认为黑色
           */
           final TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
           tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
           tv.setTextSize(15);
           tv.setGravity(Gravity.TOP);
         }
         //setContentView(R.layout.activity_main);
         mActivityName = getString(R.string.activity_c_label);
       //  mStatusView = (TextView)findViewById(R.id.status_view_c);
       //  mStatusAllView = (TextView)findViewById(R.id.status_view_all_c);
         mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
         Utils.printStatus(mStatusView, mStatusAllView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("dc","cccc");
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_start));
        Utils.printStatus(mStatusView, mStatusAllView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_restart));
        Utils.printStatus(mStatusView, mStatusAllView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_resume));
        Utils.printStatus(mStatusView, mStatusAllView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_pause));
        Utils.printStatus(mStatusView, mStatusAllView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_stop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_destroy));
        mStatusTracker.clear();
    }

    public void startDialog(View v) {
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }

    public void startActivityB(View v) {
        Intent intent = new Intent(MainActivity.this, ActivityB.class);
        startActivity(intent);
    }

    public void startActivityC(View v) {
        Intent intent = new Intent(MainActivity.this, ActivityC.class);
        startActivity(intent);
    }

    public void finishActivityA(View v) {
        MainActivity.this.finish();
    }

}
