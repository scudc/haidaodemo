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


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.android.lifecycle.util.AsynImageLoader;
import com.example.android.lifecycle.util.DataOp;
import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;



/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnGestureListener   {



	private String mActivityName;
	private TextView mStatusView;
	private TextView mStatusAllView;
	private StatusTracker mStatusTracker = StatusTracker.getInstance();

	private ListView mListView;
	private ListView mListView1;
	private ListView homeListView;
	private ListView qaListView;
	private ListView detailView;
	private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
	private ArrayList<HashMap<String, Integer>> mGist = new ArrayList<HashMap<String, Integer>>();

	/* 要切换的view */
	private View details_page_setitem;
	private View main_item;
	private View bind_item;
	private View about_one;
	private View microblog;
	private View loading_item;

	private Map<String,ListViewAdapter> viewMap;
	// app 全局的application 类
	private OneApp oneApp;

	/* 一个保存view 切换的堆栈 */
	Stack<View> context = new Stack<View>();

	private TabHost tabs;
	private TabWidget tabWidget;
	
	
	//图片异步加载的全局对象
	private AsynImageLoader asynImageLoader = null;
	private DataOp dataOp = null;
	
	private GestureDetector detector;  
	
	
	/*list view adapter*/
	private ListViewAdapter listAdapter = null;
	private ListViewAdapter homeAdapter = null;
	private ListViewAdapter QAAdapter = null;
	private ListViewAdapter collectAdapter = null;
	private ListViewAdapter detailAdapter = null;
	
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//初始化保存listview中view对象得数据结构
		this.viewMap = new HashMap<String,ListViewAdapter>();
		//初始化图片异步加载的类
		this.asynImageLoader = new AsynImageLoader(); 
		this.dataOp = new DataOp(asynImageLoader);
	

		// 设置这个防止网络错误
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());


		oneApp = (OneApp) getApplication();
		oneApp.pushActivity(this);
		// 初始化要切换的view
		LayoutInflater inflater = LayoutInflater.from(this);
		details_page_setitem = inflater.inflate(
				R.layout.one_details_page_setitem, null);
		main_item = inflater.inflate(R.layout.activity_main, null);
		bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
		about_one = inflater.inflate(R.layout.about_one, null);
		microblog = inflater.inflate(R.layout.one_details_page_microblog, null);
		loading_item = inflater.inflate(R.layout.one_welcome_ad, null);
		
		setContentView(main_item);


		
		// 开始加载数据和生成view
		mListView = (ListView) findViewById(R.id.tab2);
		initData();
		
		//ArrayList<ArrayList<String>> list_data = loadData("list",data);
		listAdapter = new ListViewAdapter(this,this, mList,
				mGist, R.id.scrollview, R.layout.list_item,
				dataOp,asynImageLoader,"list");
		
		mListView.setAdapter(listAdapter);
		mListView1 = (ListView) findViewById(R.id.tab1);

		//ArrayList<ArrayList<String>> collect_data = loadData("collect",data);
		collectAdapter = new ListViewAdapter(this,this, mList,
				mGist, R.id.collectScrollview,
				R.layout.collect_item,
				dataOp,asynImageLoader,"collect");

		mListView1.setAdapter(collectAdapter);

		homeListView = (ListView) findViewById(R.id.homeTab);
		homeAdapter = new ListViewAdapter(this,
				this, mList, mGist,
				R.id.homeScrollView, R.layout.home_item, dataOp,asynImageLoader,"home");

		homeListView.setAdapter(homeAdapter);

		qaListView = (ListView) findViewById(R.id.QAtab);

		//ArrayList<ArrayList<String>> QA_data = loadData("QA",data);
		QAAdapter = new ListViewAdapter(this,this,
				mList, mGist, R.id.qaScrollView, R.layout.qa_item,
				dataOp,asynImageLoader,"QA");

		qaListView.setAdapter(QAAdapter);

		detailView = (ListView) findViewById(R.id.tab3);

		//ArrayList<ArrayList<String>> detail_data = loadData("detail",data);
		detailAdapter = new ListViewAdapter(this,
				this, mList, mGist,
				R.id.detailScrollView, R.layout.detail_item,
				dataOp,asynImageLoader,"detail");

		detailView.setAdapter(detailAdapter);

		
		
		tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);

		// tabWidget.setBackgroundColor(Color.BLACK);

		int width = 60;
		int height = 60;
		tabs.setup();
		tabs.addTab(tabs.newTabSpec("home tab").setIndicator("首页", null)
				.setContent(R.id.homeTab));
		tabs.addTab(tabs.newTabSpec("list tab").setIndicator("一篇", null)
				.setContent(R.id.tab2));
		tabs.addTab(tabs.newTabSpec("QA Tab").setIndicator("问答", null)
				.setContent(R.id.QAtab));
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("收藏", null)
				.setContent(R.id.tab1));
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("更多", null)
				.setContent(R.id.tab3));

		tabs.setCurrentTab(0);

		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			/**
			 * 设置高度、宽度，不过宽度由于设置为fill_parent，在此对它没效果
			 */
			tabWidget.getChildAt(i).getLayoutParams().height = height;
			tabWidget.getChildAt(i).getLayoutParams().width = width;

			/**
			 * 设置tab中标题文字的颜色，不然默认为黑色
			 */
			final TextView tv = (TextView) tabWidget.getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.white));
			tv.setTextSize(15);
			tv.setGravity(Gravity.TOP);
		}
		// setContentView(R.layout.activity_main);
		mActivityName = getString(R.string.activity_c_label);
		// mStatusView = (TextView)findViewById(R.id.status_view_c);
		// mStatusAllView = (TextView)findViewById(R.id.status_view_all_c);
		mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
		Utils.printStatus(mStatusView, mStatusAllView);

		// 开启异步获取数据的线程
		//GetDataTask task = new GetDataTask(this);
		//task.execute("http://csdnimg.cn/www/images/csdnindex_logo.gif");
		detector = new GestureDetector(this);  

		viewMap.put("home", this.homeAdapter);
		Log.i("getDataAsyn",String.valueOf(homeAdapter.hashCode()));
		//viewMap.put("QA", this.QAAdapter);
		//viewList.add(detailListViewadapter.getDisplayView());
		//viewList.add(adapter1.getDisplayView());
	
		
		
	
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

			@Override
			public void handleMessage(Message msg) {
			
				super.handleMessage(msg);
				try {
					//setContentView(bind_item);
					String data = dataOp.getDataAsyn("http://haidaoteam.sinaapp.com/?datatype=json", about_one, main_item,homeListView);
                	System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                	if(main_item.findViewById(R.id.QAtab) != null)
                		setDataToView(main_item.findViewById(R.id.QAtab),loadData("QA",data));
                	if(main_item.findViewById(R.id.homeTab) != null)
                		setDataToView(main_item.findViewById(R.id.homeTab),loadData("home",data));
                	if(main_item.findViewById(R.id.tab2) != null)
                		setDataToView(main_item.findViewById(R.id.tab2),loadData("list",data));
                	//setDataToView(main_item.findViewById(R.id.QAtab),loadData("QA",data));
                	//setDataToView(main_item.findViewById(R.id.tab2),loadData("list",data));
                	
                	//Thread.sleep(10000);
                	//setContentView(main_item);
                	
                	//TextView tx = (TextView) .findViewById(R.id.new_tView);
                	//tx.setText("xxxxxx");
                	//main_item.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
                	//main_item.invalidate();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		final ViewHandler viewHandler = new ViewHandler();
		/**
		 * 要在Activity中开启一个用于更新的线程
		 * timeViewHandler 继承自Handler，用于处理和发送消息
		 * MSG_UPDATE 是自定义的一个int常量，用于区分消息类型，可自由取值。
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
					
					//viewHandler.sendMessage(Message.obtain()); 
					
				
			}
		}).start();
	}

	//加载数据的函数
	private ArrayList<ArrayList<String>> loadData(String viewName,String data) throws JSONException {
				
				
				ArrayList<ArrayList<String>> tempResult = new ArrayList<ArrayList<String>>();
				try {
					String viewData =  new JSONObject(data).getString(viewName);
					JSONArray jsonArray = new JSONArray(viewData);
					

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONArray tempJson = (JSONArray) jsonArray.opt(i);
						ArrayList<String> tempArray = new ArrayList<String>();
						tempArray.add(String.valueOf(nameToIdMap(tempJson
								.getString(0))));
						tempArray.add(String.valueOf(tempJson.getString(1)));
						tempArray.add(String.valueOf(tempJson.getString(2)));
						tempResult.add(tempArray);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				return tempResult;

		}
		
		private int nameToIdMap(String name)
		{
				return this.getResources().getIdentifier(name, "id", this.getPackageName());
		}

		
		private void setDataToView(View targetView,ArrayList<ArrayList<String>> dataList)
		{
			Iterator<ArrayList<String>> it = dataList.iterator();
			Log.i("setDataToView",dataList.toString());
			Log.i("setDataToView",targetView.toString());
			while(it.hasNext())
			{
				ArrayList<String > tempArray = it.next();
				int targetViewId = Integer.parseInt(tempArray.get(0));
				String content = tempArray.get(1);
				String type = tempArray.get(2);
				
				if(type.equals("text"))
				{
				TextView textView = (TextView) targetView.findViewById(targetViewId);
		  		textView.setText(content);
		  		
				}else if (type.equals("image"))
				{
					ImageView imageView = (ImageView) targetView.findViewById(targetViewId);
					 
					asynImageLoader.showImageAsyn(imageView, content, R.drawable.one_image);  
					
				}else if (type.equals("shareUrl"))
				{
					TextView textView = (TextView) targetView.findViewById(targetViewId);
			  		textView.setText(content);
				}
				
				//Log.i("setDataToView", content+"_"+type);
			}
		}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		this.oneApp.setCurrentTabIndex(this.tabs.getCurrentTab());

		// outState.putSerializable("view",super.getCurrentFocus());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		this.oneApp.exit();

		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("onStart", "3");
		if (this.oneApp.getCurrentTabIndex() != 0) {
			tabs.setCurrentTab(this.oneApp.getCurrentTabIndex());
		}
		mStatusTracker.setStatus(mActivityName, getString(R.string.on_start));
		Utils.printStatus(mStatusView, mStatusAllView);
		
		//viewMap.put("list", this.listAdapter);
	

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





	public void finishActivityA(View v) {
		MainActivity.this.finish();
	}

	public void initData() {
		for (int i = 0; i < 1; i++) {

			HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
			hashmap.put("list", R.drawable.test);

			for (int j = 0; j < 1; j++) {

				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put("grid", R.drawable.ic_launcher);
				mGist.add(map);
			}
			mList.add(hashmap);
		}

	}

	/* 分享设置事件监听 */
	public void setButtonOnClick(View view) {
		context.push(main_item);

		setContentView(details_page_setitem);
	}

	/* 微博点击事件监听 */
	public void microBlogOnClick(View view) {
		context.push(main_item);
		setContentView(bind_item);
	}

	/* 关于一篇的事件监听 */
	public void aboutOneOnClick(View view) {
		context.push(main_item);

		setContentView(about_one);
	}

	/* 关于关注新浪微博的事件监听 */
	public void sineMicroBlogBindOnClick(View view) {
		context.push(bind_item);
		setContentView(microblog);
	}

	/* return 事件监听 */
	public void returnOnClick(View view) {
		setContentView(context.pop());
	}

	/* 分享功能 */
	public void shareOnClick(View view) {
		String shareTitle ="";
		String shareContent = "推荐海盗一篇";
		if(tabs.getCurrentTabTag() == "home tab" )
		{
			shareTitle = "home tab";
			shareContent = shareContent + ((TextView) this.findViewById(R.id.fPage_tView)).getText().toString();
		}else if(tabs.getCurrentTabTag() == "QA Tab" )
		{
			shareTitle = "QA Tab";
			shareContent = "";
			
		}else if(tabs.getCurrentTabTag() == "list tab")
		{
			shareTitle = "list tab";
			shareContent = "";
		}
			
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		// intent.setPackage("com.sina.weibo");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		//intent.putExtra(Intent.EXTRA_TEXT, shareTitle+"推荐海盗一篇   VOL.516 车站  (来自海盗团队) http://caodan.org/516-photo.html ");
		intent.putExtra(Intent.EXTRA_TEXT, shareContent);
		intent.putExtra(Intent.EXTRA_TITLE, shareTitle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "请选择"));
	}




		


		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}







		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		/* 以下是监听左右滑动事件 ；需要继承OnGestureListener*/
		
	    @Override  
	    public boolean onTouchEvent(MotionEvent event) {
	        return this.detector.onTouchEvent(event);  
	    }
	    /**
	     * 解决ScrollView后不执行左右移动监听事件OnGestureListener
	     * 在Activity中添加ScrollView实现滚动activity的效果后，activity的滑动效果却无法生效了
	     * 原因是因为activity没有处理滑动效果，重写以下方法即可解决。
	     */
	    @Override 
	    public boolean dispatchTouchEvent(MotionEvent ev) { 
	        detector.onTouchEvent(ev); 
	        return super.dispatchTouchEvent(ev); 
	    } 
	    /** 
	     * 监听滑动 
	     */  
	    @Override  
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
	            float velocityY) {
	    	System.out.println(this.tabs.getCurrentView().getLayoutDirection());
	    	System.out.println(R.layout.home_item);
	    	System.out.println(this.tabs.getCurrentView().getId());
	    	System.out.println(R.id.homeScrollView);
	    	System.out.println(this.tabs.getCurrentTab());
	    	System.out.println(this.homeListView.getId());
	    	int currentViewId = this.tabs.getCurrentView().getId();
	    	
	        if (e1.getX() - e2.getX() < -120) {
	        	System.out.println("=======================================");
	        }  
	        else if (e1.getX() - e2.getX() > 120) {
	        	System.out.println("+++++++++++++++++++++++++++++++++++++++");
	        }
	        return true;  
	    }
		
}
