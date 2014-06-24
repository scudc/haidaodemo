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

package com.justone.android.main;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


import com.justone.android.main.MainActivity.ViewHandler;
import com.justone.android.service.UpdateService;
import com.justone.android.util.StatusTracker;
import com.justone.android.util.Utils;

/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
public class StartActivity extends BaseActivity {

    private String mActivityName;
    private TextView mStatusView;
    private TextView mStatusAllView;
    private StatusTracker mStatusTracker = StatusTracker.getInstance();
    
    
    //app 全局的application 类
    private JustOne justOne;   

    private final ViewHandler viewHandler = new ViewHandler();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.one_welcome);
        mActivityName = getString(R.string.activity_a);
        
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
        Utils.printStatus(mStatusView, mStatusAllView);
        
        justOne = (JustOne) getApplication();
        justOne.pushActivity(this);
        
         

        
    }
    
    
   
	/**
	 * Handler示例，用于刷新时间
	 * DateHelper是我自己写的日期格式化工具哦
	 * @author Dreamworker
	 *
	 */
	class ViewHandler extends Handler {
		
	

		public ViewHandler() {
			super();

		}

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
		
			
			super.handleMessage(msg);

			Intent intent = new Intent(StartActivity.this, MainActivity.class);
	        //Intent intent = new Intent(StartActivity.this, ActivityC.class);
	        startActivity(intent);


		}
		
	}
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
       /* if(savedInstanceState != null)
        {
            View view = (View)savedInstanceState.getSerializable("view");
            setContentView(view);
        }*/
        //Log.i("onSaveInstanceState","2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStatusTracker.setStatus(mActivityName, getString(R.string.on_start));
        Utils.printStatus(mStatusView, mStatusAllView);
        /**
		 * 要在Activity中开启一个用于更新的线程
		 * timeViewHandler 继承自Handler，用于处理和发送消息
		 * MSG_UPDATE 是自定义的一个int常量，用于区分消息类型，可自由取值。
		 */
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					viewHandler.sendMessage(Message.obtain(viewHandler, 0));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
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




    public void finishActivityA(View v) {
        StartActivity.this.finish();
    }
    
    public void startMainActivity(View v)
    {
    	Intent intent = new Intent(StartActivity.this, MainActivity.class);
        //Intent intent = new Intent(StartActivity.this, ActivityC.class);
        startActivity(intent);

	    //v1.setText("sdc");

    }
 
}
