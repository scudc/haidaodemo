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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
public class MainActivity extends BaseActivity {
	
	

    private String mActivityName;
    private TextView mStatusView;
    private TextView mStatusAllView;
    private StatusTracker mStatusTracker = StatusTracker.getInstance();
    
	private ListView mListView;
	private ListView mListView1;
	private ListView homeListView;
	private ListView qaListView;
	private ListView detailView;
	private ArrayList<HashMap<String,Integer>> mList = new ArrayList<HashMap<String,Integer>>();
	private ArrayList<HashMap<String,Integer>> mGist = new ArrayList<HashMap<String,Integer>>();
	
	
	/*要切换的view*/
	private View details_page_setitem;
	private View main_item;
	private View bind_item;
	private View about_one;
	private View microblog;

	
	 //app 全局的application 类
	 private OneApp oneApp;  
	
	/*一个保存view 切换的堆栈*/
	Stack<View> context = new Stack<View>();
	
	
	private TabHost tabs;
	private TabWidget tabWidget;
	
    /* (non-Javadoc)
     * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	 oneApp = (OneApp) getApplication();
    	 oneApp.pushActivity(this);
    	 //初始化要切换的view
    	 LayoutInflater inflater = LayoutInflater.from(this);
    	 details_page_setitem = inflater.inflate(R.layout.one_details_page_setitem, null);
    	 main_item = inflater.inflate(R.layout.activity_main, null);
    	 bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
    	 about_one = inflater.inflate(R.layout.about_one,null);
    	 microblog = inflater.inflate(R.layout.one_details_page_microblog, null);

    	 
    	
    
    	setContentView(main_item);
    	 
    	 
    	 mListView = (ListView) findViewById(R.id.tab2);
 		 initData();
 		 ListViewAdapter adapter = new ListViewAdapter(this, mList,mGist,R.id.scrollview,R.layout.list_item);
 		 mListView.setAdapter(adapter);
 		 
 		 mListView1 = (ListView) findViewById(R.id.tab1);
 		 ListViewAdapter adapter1 = new ListViewAdapter(this, mList,mGist,R.id.collectScrollview,R.layout.collect_item);
 		 mListView1.setAdapter(adapter1);
 		 
 		homeListView = (ListView) findViewById(R.id.homeTab);
 		ListViewAdapter homeListViewadapter = new ListViewAdapter(this, mList,mGist,R.id.homeScrollView,R.layout.home_item);
 		homeListView.setAdapter(homeListViewadapter);
 		
 		 qaListView = (ListView) findViewById(R.id.QAtab);
 		ListViewAdapter qaListViewadapter = new ListViewAdapter(this,mList,mGist,R.id.qaScrollView,R.layout.qa_item);
 		qaListView.setAdapter(qaListViewadapter);
 		
 		detailView = (ListView) findViewById(R.id.tab3);
 		ListViewAdapter detailListViewadapter = new ListViewAdapter(this,mList,mGist,R.id.detailScrollView,R.layout.detail_item);
 		detailView.setAdapter(detailListViewadapter);
  		tabs=(TabHost)findViewById(android.R.id.tabhost); 
  		tabWidget=(TabWidget) findViewById(android.R.id.tabs);
  		 
  		 
  		 
  		 
        // tabWidget.setBackgroundColor(Color.BLACK);

         int width =60;
         int height =60;
         tabs.setup();   
         tabs.addTab(tabs.newTabSpec("home tab")
         		 .setIndicator("首页",null)
         		 .setContent(R.id.homeTab));
         tabs.addTab(tabs.newTabSpec("second tab")
                 .setIndicator("一篇",null)
                 .setContent(R.id.tab2));
         tabs.addTab(tabs.newTabSpec("QA Tab")
        		 .setIndicator("问答",null)
        		 .setContent(R.id.QAtab));
         tabs.addTab(tabs.newTabSpec("second tab")
                 .setIndicator("收藏",null)
                 .setContent(R.id.tab1));
         tabs.addTab(tabs.newTabSpec("second tab")
                 .setIndicator("更多",null)
                 .setContent(R.id.tab3));
         
         
            
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
    protected void onSaveInstanceState(Bundle outState)
    {
    	super.onSaveInstanceState(outState);
    	this.oneApp.setCurrentTabIndex(this.tabs.getCurrentTab());

    	
    	//outState.putSerializable("view",super.getCurrentFocus());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
    	
    	this.oneApp.exit();
    	
    	
        return true; 
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStart","3");
        if(this.oneApp.getCurrentTabIndex() != 0)
        {
        	tabs.setCurrentTab(this.oneApp.getCurrentTabIndex());
        }
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
        context.push(super.getCurrentFocus());
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
    
	public void initData(){
		for(int i = 0;i<1;i++){
			
			HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
			hashmap.put("list", R.drawable.test);
			
			for(int j = 0;j<1;j++){
				
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put("grid", R.drawable.ic_launcher);
				mGist.add(map);
			}
			mList.add(hashmap);
		}
		
		
	}
	
	/*分享设置事件监听*/
	public void setButtonOnClick(View view)
	{
			context.push(main_item);
			setContentView(details_page_setitem);
	}
	
	/*微博点击事件监听*/
	public void microBlogOnClick(View view)
	{
			context.push(main_item);
			setContentView(bind_item);
	}
	
	
	/*关于一篇的事件监听*/
	public void aboutOneOnClick(View view)
	{
		    context.push(main_item);
			setContentView(about_one);
	}
	
	
	/*关于关注新浪微博的事件监听*/
	public void sineMicroBlogBindOnClick(View view)
	{
			context.push(bind_item);
			setContentView(microblog);
	}
	/*return 事件监听*/
	public void returnOnClick(View view)
	{
			setContentView(context.pop());
	}
	
	

	

}
