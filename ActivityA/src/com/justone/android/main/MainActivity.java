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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;






import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
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
import android.view.ViewGroup;

import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.EditText;


import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


import com.justone.android.service.UpdateService;
import com.justone.android.util.AsynImageLoader;
import com.justone.android.util.DataOp;
import com.justone.android.util.PicUtil;
import com.justone.android.util.StatusTracker;
import com.justone.android.util.Utils;



/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
@SuppressLint({ "NewApi", "HandlerLeak", "ResourceAsColor", "WorldReadableFiles" })
public class MainActivity extends BaseActivity implements OnGestureListener   {



	private String mActivityName;
	private TextView mStatusView;
	private TextView mStatusAllView;
	private StatusTracker mStatusTracker = StatusTracker.getInstance();

	private ListView mListView;
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

	private View feedback;

	private Map<String,ListViewAdapter> viewMap;
	// app 全局的application 类
	private JustOne justOne;

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
	private ListViewAdapter detailAdapter = null;
	//是否需要更新ui
	private boolean isUpdate = false;
	
	//判断是否id增加还是减小的字段  1为左 2 为右 0为不变化
	private int leftOrRight = 0;
	
	//当前最大的id
	private int maxId = JustOne.getMaxId();

	
	final ViewHandler viewHandler = new ViewHandler();
	

	
	//当前view的数据
	private String data = "";
	
	//获取数据的url
	private String url = "http://haidaoteam.sinaapp.com/?datatype=json&type=";

	private EditText feedback_email_et = null;
	private EditText feedback_phone_et = null;
	private EditText feedback_text_et = null;
	
	
	

	//数据资源对象
	private Resources res = null;
	
	//context 
	private Context currentContext = null;
	
	//当前的最新id
	private int currentId = JustOne.getCurrentId();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentContext = this;
		//初始化保存listview中view对象得数据结构
		this.viewMap = new HashMap<String,ListViewAdapter>();
		//初始化图片异步加载的类
		this.asynImageLoader = JustOne.getAsynImageLoader();
		this.dataOp = JustOne.getDataOp();
	
		//初始化资源对象
		this.res = getResources(); // Resource object to get Drawables  

		// 设置这个防止网络错误
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());


		justOne = (JustOne) getApplication();
		justOne.pushActivity(this);
		// 初始化要切换的view
		LayoutInflater inflater = LayoutInflater.from(this);
		//details_page_setitem = inflater.inflate(R.layout.one_details_page_setitem, null);
		main_item = inflater.inflate(R.layout.activity_main, null);
		bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
		about_one = inflater.inflate(R.layout.about_one, null);
		//microblog = inflater.inflate(R.layout.one_details_page_microblog, null);

		feedback = inflater.inflate(R.layout.feedback, null);
    	
		setContentView(main_item);


		initData();

		feedback.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(){

			@Override
			public void onViewAttachedToWindow(View v) {
				// TODO Auto-generated method stub
				System.out.println("here!!!");
				feedback_email_et = (EditText)findViewById(R.id.feedback_email_content);
				feedback_email_et.setOnFocusChangeListener(new OnFocusChangeListener(){
		
					@Override
					public void onFocusChange(View arg0, boolean hasfocus) {
						// TODO Auto-generated method stub
						if (hasfocus)
						{
							if (feedback_email_et.getText().toString().equalsIgnoreCase("input"))
							{
								feedback_email_et.setText("");
								//feedback_email_et.setTextColor(0x333333);
							}
						}
					}
				});
		    	feedback_phone_et = (EditText)findViewById(R.id.feedback_phone_content);
		    	feedback_phone_et.setOnFocusChangeListener(new OnFocusChangeListener(){

					@Override
					public void onFocusChange(View arg0, boolean hasfocus) {
						// TODO Auto-generated method stub
						if (hasfocus)
						{
							if (feedback_phone_et.getText().toString().equalsIgnoreCase("input"))
							{
								feedback_phone_et.setText("");
								//feedback_phone_et.setTextColor(0x333333);
							}
						}
					}
				});
		    	feedback_text_et = (EditText)findViewById(R.id.feedback_text_content);
		    	feedback_text_et.setOnFocusChangeListener(new OnFocusChangeListener()
		    	{

					@Override
					public void onFocusChange(View arg0, boolean hasfocus) {
						// TODO Auto-generated method stub
						if (hasfocus)
						{
							if (feedback_text_et.getText().toString().equalsIgnoreCase("input"))
							{
								feedback_text_et.setText("");
								//feedback_text_et.setTextColor(0x333333);
							}
						}
					}
				});
			}

			@Override
			public void onViewDetachedFromWindow(View v) {
				// TODO Auto-generated method stub
			}
		});
		

		
		// 开始加载数据和生成view
		mListView = (ListView) findViewById(R.id.tab2);
		//ArrayList<ArrayList<String>> list_data = loadData("list",data);
		listAdapter = new ListViewAdapter(this,mList,
				mGist, R.id.scrollview, R.layout.list_item);
		
		mListView.setAdapter(listAdapter);
		
		/*
		mListView1 = (ListView) findViewById(R.id.tab1);

		//ArrayList<ArrayList<String>> collect_data = loadData("collect",data);
		collectAdapter = new ListViewAdapter(this,mList,
				mGist, R.id.collectScrollview,
				R.layout.collect_item);

		mListView1.setAdapter(collectAdapter);
		*/
		homeListView = (ListView) findViewById(R.id.homeTab);
		homeAdapter = new ListViewAdapter(this,
				mList, mGist,
				R.id.homeScrollView, R.layout.home_item);

		homeListView.setAdapter(homeAdapter);

		qaListView = (ListView) findViewById(R.id.QAtab);

		//ArrayList<ArrayList<String>> QA_data = loadData("QA",data);
		QAAdapter = new ListViewAdapter(this,mList, mGist, R.id.qaScrollView, R.layout.qa_item);

		qaListView.setAdapter(QAAdapter);

		//detailView = (ListView) findViewById(R.id.tab3);

		//ArrayList<ArrayList<String>> detail_data = loadData("detail",data);
		//detailAdapter = new ListViewAdapter(this,
		//		mList, mGist,
		//		R.id.detailScrollView, R.layout.detail_item);

		//detailView.setAdapter(detailAdapter);
		
		
		/*//设这loading 界面的list view 适配器
		loadingView = (ListView) findViewById(R.id.loading_view);

		//ArrayList<ArrayList<String>> detail_data = loadData("detail",data);
		loadingAdapter = new ListViewAdapter(this,
				mList, mGist,
				R.id.loadingScrollView, R.layout.one_welcome_ad);

		loadingView.setAdapter(loadingAdapter);

		*/
		
		tabs = (TabHost) findViewById(R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		int width = 100;
		int height = 100;
		tabs.setup();
		tabs.addTab(tabs.newTabSpec("home tab").setIndicator("", this.res.getDrawable(R.drawable.home_tab_selector))
				.setContent(R.id.homeTab));
		tabs.addTab(tabs.newTabSpec("list tab").setIndicator("", this.res.getDrawable(R.drawable.one_tab_selector))
				.setContent(R.id.tab2));
		tabs.addTab(tabs.newTabSpec("QA Tab").setIndicator("",this.res.getDrawable(R.drawable.qa_tab_selector))
				.setContent(R.id.QAtab));
		
		/*
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("收藏", null)
				.setContent(R.id.tab1));*/
		//tabs.addTab(tabs.newTabSpec("second tab").setIndicator("",this.res.getDrawable(R.drawable.more_tab_selector))
		//		.setContent(R.id.tab3));
	
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
		mActivityName = getString(R.string.activity_c_label);
		mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
		Utils.printStatus(mStatusView, mStatusAllView);
		detector = new GestureDetector(this);  
		viewMap.put("home", this.homeAdapter);
		
		
		//设置返回码按钮的监听函数
		View returnBackToIndex = main_item.findViewById(R.id.returnBackToIndex_layout);
		returnBackToIndex.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.this.finish();
				// TODO Auto-generated method stub
				
			}
		});
		
		
		userFirstTips();

	}
	

	
	 /***
		 * 检查是否更新版本
	 * @throws JSONException 
		 */
		public void checkVersion() throws JSONException {
			
			
			if(!Utils.isWifi(this))
				return;
			
			String versionInfo = this.dataOp.getUpdateVersionInfo();
			JSONObject jsonOb = new JSONObject(versionInfo);
			String viewData =  jsonOb.getString("data");
			JSONArray jsonArray = new JSONArray(viewData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray tempJson = (JSONArray) jsonArray.opt(i);
			
				if(tempJson.optString(0).equals("version_id"))
				{
					JustOne.setServerVersion(tempJson.optInt(1));
				}
				if(tempJson.optString(0).equals("version_name"))
					JustOne.versionName = tempJson.optString(1);
				if(tempJson.optString(0).equals("version_desc"))
					JustOne.setVersionDesc(tempJson.optString(1));
				if(tempJson.optString(0).equals("download_href"))
					JustOne.download_href = tempJson.optString(1);
				/*ArrayList<String> tempArray = new ArrayList<String>();
				
				tempJson.getJSONArray(index)
				tempArray.add(String.valueOf(nameToIdMap(tempJson
						.getString(0))));
				tempArray.add(String.valueOf(tempJson.getString(1)));
				tempArray.add(String.valueOf(tempJson.getString(2)));
				tempResult.add(tempArray);*/
			}
			if (JustOne.getLocalVersion() < JustOne.getServerVersion()) {
				// 发现新版本，提示用户更新
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("软件升级")
						.setMessage(JustOne.getVersionDesc())
						.setPositiveButton("更新",new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent updateIntent = new Intent(
												MainActivity.this,
												UpdateService.class);
										updateIntent.putExtra(
												"app_name",
												getResources().getString(
														R.string.app_name));
										startService(updateIntent);
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				alert.create().show();

			}
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
			
			
			

			switch(msg.what)
			{
			case 0:
				setLoadingView(0);
				break;
			case 1:
				
				try {
					setDataToView((View)msg.obj,loadData("data",data));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setLoadingView(1);
				break;
			//当消息等于2时，说明已经是最新
			case 2:
				Toast.makeText(currentContext,"已经是最新内容", Toast.LENGTH_SHORT).show();
				break;
			//当消息等于3时，说明已经是最后一个
			case 3:
				Toast.makeText(currentContext,"已经是最后一个", Toast.LENGTH_SHORT).show();
				break;
			}

		}
		
	}
	//加载数据的函数
	private ArrayList<ArrayList<String>> loadData(String viewName,String data) throws JSONException {
				
				viewName = "data";
				JSONObject jsonOb = new JSONObject(data);
				ArrayList<ArrayList<String>> tempResult = new ArrayList<ArrayList<String>>();
				try {
					String viewData =  jsonOb.getString(viewName);
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
				
				String id =  jsonOb.getString("id");
				ArrayList<String> tempArray = new ArrayList<String>();
				tempArray.add(String.valueOf(nameToIdMap(jsonOb.getString("type")+"_id")));
				tempArray.add(id);
				tempArray.add("text");
				tempResult.add(tempArray);
				
				return tempResult;

		}
	
		private int nameToIdMap(String name)
		{
				return this.getResources().getIdentifier(name, "id", this.getPackageName());
		}

		
		private void setDataToView(View targetView,ArrayList<ArrayList<String>> dataList)
		{
			Iterator<ArrayList<String>> it = dataList.iterator();

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
					
					asynImageLoader.showImageAsyn(imageView, content, findViewById(R.id.homeloadingLayout));  
					
					
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
		this.justOne.setCurrentTabIndex(this.tabs.getCurrentTab());

		// outState.putSerializable("view",super.getCurrentFocus());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           //exitBy2Click();      //调用双击退出函数  
	    		this.finish();
	       }  
	    return false; 
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();

		if (this.justOne.getCurrentTabIndex() != 0) {
			tabs.setCurrentTab(this.justOne.getCurrentTabIndex());
		}
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
				
				
				
				
				
				
				    isUpdate = true;
				    int currentTab = 0;
				    String id = "";
					TextView idView ;
					View targetView  = null;
					boolean isNew = false;
					String currentUrl = "";
					do
					{
						
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					if(currentTab != tabs.getCurrentTab())
						isUpdate = true;
					if(isUpdate && main_item!=null)
					{
					currentTab = tabs.getCurrentTab();
					
					switch(currentTab)
					{
						//home 页面
						case  0:
							if(main_item == null)
								break;
							targetView = main_item.findViewById(R.id.homeTab);
							if(targetView == null)
								break;
							
							if( targetView.findViewById(R.id.home_id) != null)
							{
								
								targetView = main_item.findViewById(R.id.homeTab);
								idView = (TextView) targetView.findViewById(R.id.home_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		
		                		if(currentId != 0)
		                		{
		                		if(leftOrRight == 1)
		                		{
		                			currentId = currentId -1 ;
		                			leftOrRight = 0;
		                			if(currentId<=0)
		                				break;
		                		}
		                		else if(leftOrRight == 2)
		                		{
		                			currentId = currentId+ 1;
		                			leftOrRight = 0;
		                			if(currentId > maxId)
		                				break;
		                		}
		                		}

		                		if(currentId > -1 && currentId <=maxId)
		                		{
		                			isUpdate = false;
		                		}
		                		if(currentId == 0)
		                		{
		                			currentUrl = url + "home";
		                			isNew = true;
		                		}
		                		else
		                			currentUrl = url + "home&id="+currentId;

		                		
								/*synchronized (this){
								Thread.yield();}
								System.out.println("wait.................");*/
								
							}
							break;
						//一篇文章
						case  1:
							if(main_item == null)
								break;
							if(main_item.findViewById(R.id.tab2) == null)
								break;
							if(main_item.findViewById(R.id.tab2).findViewById(R.id.list_id) != null)
							{
								targetView = main_item.findViewById(R.id.tab2);
								idView = (TextView) targetView.findViewById(R.id.list_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		 
		                		if(currentId != 0)
		                		{
		                		if(leftOrRight == 1)
		                		{
		                			currentId = currentId -1 ;
		                			leftOrRight = 0;
		                			if(currentId<=0)
		                				break;
		                		}
		                		else if(leftOrRight == 2)
		                		{
		                			currentId = currentId+ 1;
		                			leftOrRight = 0;
		                			if(currentId > maxId)
		                				break;
		                		}
		                		}
		                		
		                		if(currentId > -1 && currentId <=maxId)
		                		{

		                		isUpdate = false;
		                		}
		                		
		                		if(currentId == 0)
		                		{
		                			currentUrl = url + "list";
		                			isNew = true;
		                		}
		                		else
		                			currentUrl = url + "list&id="+currentId;	
								
							}
							break;
						//一篇问答
						case 2:
							if(main_item == null)
								break;
							if(main_item.findViewById(R.id.QAtab) == null)
								break;
							if(main_item.findViewById(R.id.QAtab).findViewById(R.id.QA_id) != null)
							{
								targetView = main_item.findViewById(R.id.QAtab);
								idView = (TextView) targetView.findViewById(R.id.QA_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		if(currentId != 0)
		                		{
		                		if(leftOrRight == 1)
		                		{
		                			currentId = currentId -1 ;
		                			leftOrRight = 0;
		                			if(currentId<=0)
		                			{
		                				break;
		                			}
		                		}
		                		else if(leftOrRight == 2)
		                		{
		                			currentId = currentId+ 1;
		                			leftOrRight = 0;
		                			if(currentId > maxId)
		                			{
		                				break;
		                			}
		                		}
		                		}
		                		
		                		if(currentId > -1 && currentId <=maxId)
		                		{

		                			isUpdate = false;
		                			
		                		}
		                		
		                		if(currentId == 0)
		                		{
		                			currentUrl = url + "QA";
		                			isNew = true;
		                		}
		                		else
		                			currentUrl = url + "QA&id="+currentId;
		                			
								
							}
							break;
						
					}
					
				
					
					if(isUpdate == false)
					{
					try {
						viewHandler.sendMessage(Message.obtain(viewHandler, 0, currentId, 1, targetView));
						
						data = dataOp.getDataAsyn(currentUrl);
						
						if(isNew)
						{
							//maxId= getDataId(data);
							maxId = JustOne.getMaxId();
							isNew = false;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
					viewHandler.sendMessage(Message.obtain(viewHandler, 1, currentId, 1, targetView));
					
					}
					
					
					}
					
					}while(true);
							
					
					
				
			}
		}).start();
		
		//viewMap.put("list", this.listAdapter);
		
		try {
			checkVersion();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	

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
		//MobclickAgent.onResume(this);
		mStatusTracker.setStatus(mActivityName, getString(R.string.on_resume));
		Utils.printStatus(mStatusView, mStatusAllView);
	}

	@Override
	protected void onPause() {

		super.onPause();
		//MobclickAgent.onPause(this);
		
		
		//在去评分跳转出去之后，会出发这个函数，把当前的context写入上下文，但其实并不需要，这样会引起程序crash
		
		//context.push(super.getCurrentFocus());
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


	//设置loading状态  和取消loading状态   0为设置 1为取消
	private void setLoadingView(int flag)
	{
		if(flag==1)
		{
			this.main_item.findViewById(R.id.loadingLayout).setVisibility(View.GONE);
			this.main_item.findViewById(android.R.id.tabcontent).setVisibility(View.VISIBLE);
		}else
		{
			this.main_item.findViewById(R.id.loadingLayout).setVisibility(View.VISIBLE);
			this.main_item.findViewById(android.R.id.tabcontent).setVisibility(View.GONE);
		}
		
		this.main_item.invalidate();
	}
	
	
	
	
	
	


	public void finishActivityA(View v) {
		MainActivity.this.finish();
	}

	public void initData() {
		for (int i = 0; i < 1; i++) {

			HashMap<String, Integer> hashmap = new HashMap<String, Integer>();

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

	
	
	
	
	/*弹出用户指引提示框*/
	public void userFirstTips()
	{
		@SuppressWarnings("deprecation")
		SharedPreferences preferences = getSharedPreferences("count",MODE_WORLD_READABLE); 
		int count = preferences.getInt("count", 0); 

		//判断程序与第几次运行，如果是第一次运行则跳转到引导页面 
		if (count == 0) { 
		
			LayoutInflater inflater = getLayoutInflater();
			
			View layout = inflater.inflate(R.layout.first_tips,
					(ViewGroup)findViewById(R.id.first_tips));
			
			final AlertDialog dialog = new AlertDialog.Builder(this).setView(layout)
			.show();
			
			
			
			
		    layout.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	            	dialog.dismiss();
	                return true;
	            }
	        });
			dialog.getWindow().setLayout(650, 650);
		} 
		Editor editor = preferences.edit(); 
		//存入数据 
		editor.putInt("count", ++count); 
		//提交修改 
		editor.commit();
		
		
	
	}
	

	/* 分享功能 */
	public void shareOnClick(View view) {
		String shareTitle ="";
		String shareContent = "推荐海盗一篇    ";
		if(tabs.getCurrentTabTag() == "home tab" )
		{
			shareTitle = "home tab";
			shareContent = shareContent + ((TextView) this.findViewById(R.id.fPage_tView)).getText().toString()+  ((TextView) this.findViewById(R.id.imageBelow_tView)).getText().toString() +  "(来自海盗团队)  " + ((TextView) this.findViewById(R.id.home_share_url)).getText().toString();
		}else if(tabs.getCurrentTabTag() == "QA Tab" )
		{
			shareTitle = "QA Tab";
			shareContent = shareContent + ((TextView) this.findViewById(R.id.question_content)).getText().toString()+ " - 阅读全文 (来自海盗团队)  " + ((TextView) this.findViewById(R.id.qa_share_url)).getText().toString();
			
			
		}else if(tabs.getCurrentTabTag() == "list tab")
		{
			shareTitle = "list tab";
			shareContent = shareContent + "《" + ((TextView) this.findViewById(R.id.one_content_title)).getText().toString()+ "》 by "+ ((TextView) this.findViewById(R.id.one_content_author)).getText().toString() +  "- 阅读全文(来自海盗团队)  " + ((TextView) this.findViewById(R.id.list_share_url)).getText().toString();
			
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

	/* 一键分享 事件监听*/
    public void showOnekeyshare(View view) {
    	
    	String imageUrl = "http://pic.yupoo.com/hanapp/DGhs5c5g/custom.jpg";
    	
    	/*
    	 OnekeyShare oks = new OnekeyShare();
         
         // 分享时Notification的图标和文字
         oks.setNotification(R.drawable.ic_launcher, 
         this.getString(R.string.app_name));
         // address是接收人地址，仅在信息和邮件使用
         oks.setAddress("12345678901");
         // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
         oks.setTitle(this.getString(R.string.share));
         // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
         oks.setTitleUrl("http://sharesdk.cn");
         // text是分享文本，所有平台都需要这个字段
         oks.setText("tests");
         // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
         //oks.setImagePath(MainActivity.TEST_IMAGE);
         // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
         // 微信的两个平台、Linked-In支持此字段
         oks.setImageUrl("http://pic.yupoo.com/hanapp/DGhs5c5g/custom.jpg");
         // url仅在微信（包括好友和朋友圈）中使用
         oks.setUrl("http://sharesdk.cn");
         // appPath是待分享应用程序的本地路劲，仅在微信中使用
         //oks.setAppPath("test");
         // comment是我对这条分享的评论，仅在人人网和QQ空间使用
         oks.setComment(this.getString(R.string.share));
         // site是分享此内容的网站名称，仅在QQ空间使用
         oks.setSite(this.getString(R.string.app_name));
         // siteUrl是分享此内容的网站地址，仅在QQ空间使用
         oks.setSiteUrl("http://sharesdk.cn");
         // venueName是分享社区名称，仅在Foursquare使用
        oks.setVenueName("Southeast in China");
         // venueDescription是分享社区描述，仅在Foursquare使用
         oks.setVenueDescription("This is a beautiful place!");
         // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
        oks.setLatitude(23.122619f);
         // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
        oks.setLongitude(113.372338f);
         // 是否直接分享（true则直接分享）
         
         // 指定分享平台，和slient一起使用可以直接分享到指定的平台
       
         // 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
         // oks.setCallback(new OneKeyShareCallback());
         //通过OneKeyShareCallback来修改不同平台分享的内容
       
         
         oks.show(this);
         
    	*/
  
    	
    
      	String shareText = "";
      	String shareUrl = "";
      	String titleText = "";
    	ShareSDK.initSDK(this,"1b1cba59b108");
        OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher, 
        this.getString(R.string.app_name));
    	if(tabs.getCurrentTabTag() == "home tab" )
		{
    		imageUrl =   (String)(this.findViewById(R.id.imageView1)).getTag();
    		titleText = (String) ((TextView)this.findViewById(R.id.imageBelow_tView)).getText();
    		oks.setImageUrl(imageUrl);
    		shareUrl = (String) ((TextView)this.findViewById(R.id.home_share_url)).getText();
    		shareText = (String) ((TextView)this.findViewById(R.id.imageBelow_tView)).getText() + " " + (String) ((TextView)this.findViewById(R.id.imageBelow_tView1)).getText() + "  "+shareUrl;

		}else if(tabs.getCurrentTabTag() == "QA Tab" )
		{
			oks.setImageUrl(imageUrl);
			shareUrl = (String) ((TextView)this.findViewById(R.id.qa_share_url)).getText();
			shareText = (String) ((TextView)this.findViewById(R.id.question_title)).getText() + "  " + shareUrl;
			titleText = (String) ((TextView)this.findViewById(R.id.question_title)).getText();
			
		}else if(tabs.getCurrentTabTag() == "list tab")
		{
		
			oks.setImageUrl(imageUrl);
			shareUrl = (String) ((TextView)this.findViewById(R.id.list_share_url)).getText();
			shareText = (String) ((TextView)this.findViewById(R.id.one_content_title)).getText() + "  "+ shareUrl;
			titleText =  (String) ((TextView)this.findViewById(R.id.one_content_title)).getText() ;
		}
    	
    	
    
    	
    	
  	oks.setComment(this.getString(R.string.share));
      
        // 分享时Notification的图标和文字
        
        // address是接收人地址，仅在信息和邮件使用
        //oks.setAddress("12345678901");
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(titleText + " - " + this.getString(R.string.share));
 
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareText);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(MainActivity.TEST_IMAGE);
        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
        // 微信的两个平台、Linked-In支持此字段
        
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrl);
        
        // appPath是待分享应用程序的本地路劲，仅在微信中使用
        //oks.setAppPath(MainActivity.TEST_IMAGE);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment(getContext().getString(R.string.share));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(titleText + " - " + this.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareUrl);
        // venueName是分享社区名称，仅在Foursquare使用
       // oks.setVenueName("Southeast in China");
        // venueDescription是分享社区描述，仅在Foursquare使用
        //oks.setVenueDescription("This is a beautiful place!");
        // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
        //oks.setLatitude(23.122619f);
        // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
       // oks.setLongitude(113.372338f);
        // 是否直接分享（true则直接分享）
        oks.setSilent(false);
        // 指定分享平台，和slient一起使用可以直接分享到指定的平台
        //if (platform != null) {
        //        oks.setPlatform(platform);
        //}
        // 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
        // oks.setCallback(new OneKeyShareCallback());
        //通过OneKeyShareCallback来修改不同平台分享的内容
       // oks.setShareContentCustomizeCallback(
      //  new ShareContentCustomizeDemo());
        
        oks.show(this);
        
        
        
        
    }
	public void feedbackOnClick(View view) {
		context.push(this.main_item);

		setContentView(feedback);
	}	

    public void feedbackSubmitOnClick(View view)
    {
    	feedback_email_et = (EditText)findViewById(R.id.feedback_email_content);
    	feedback_phone_et = (EditText)findViewById(R.id.feedback_phone_content);
    	feedback_text_et = (EditText)findViewById(R.id.feedback_text_content);
    	String email = feedback_email_et.getText().toString();
    	String phone = feedback_phone_et.getText().toString();
    	String text = feedback_text_et.getText().toString();
    	String result = dataOp.postData(email, phone, text);
    	System.out.println(result);
    	
    	feedback_email_et = (EditText)findViewById(R.id.feedback_email_content);
		feedback_phone_et = (EditText)findViewById(R.id.feedback_phone_content);
		feedback_text_et = (EditText)findViewById(R.id.feedback_text_content);
		feedback_email_et.setText(R.string.feedback_null);
		feedback_phone_et.setText(R.string.feedback_null);
		feedback_text_et.setText(R.string.feedback_null);
		//feedback_email_et.setTextColor(R.color.darkgray);
		//feedback_phone_et.setTextColor(R.color.darkgray);
		//feedback_text_et.setTextColor(R.color.darkgray);
		
		
    	setContentView(context.pop());
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
	    	
	    	
	    	
	    	
			
			
			
			
	    	/*System.out.println(this.tabs.getCurrentView().getLayoutDirection());
	    	System.out.println(R.layout.home_item);
	    	System.out.println(this.tabs.getCurrentView().getId());
	    	System.out.println(R.id.homeScrollView);
	    	System.out.println(this.tabs.getCurrentTab());
	    	System.out.println(this.homeListView.getId());
	    	int currentViewId = this.tabs.getCurrentView().getId();
	    	*/
	        if (e1.getX() - e2.getX() < -200) {
	        	
	        
	        	
	        	if(currentId == maxId || currentId== 0)
				{
					viewHandler.sendMessage(Message.obtain(viewHandler, 2));
				}else
				{
					this.leftOrRight = 2;
		        	this.isUpdate = true;
				}
	        	
	        }  
	        else if (e1.getX() - e2.getX() > 200) {

	        	if(currentId==1)
					viewHandler.sendMessage(Message.obtain(viewHandler, 3));
	        	else
	        	{
		        	this.leftOrRight = 1;
		        	this.isUpdate = true;
	        	}
	        	
	        }
	        return true;  
	    }
	    
	    
	    
		
}
