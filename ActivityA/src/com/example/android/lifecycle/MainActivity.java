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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

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

import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


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

	/* Ҫ�л���view */
	private View details_page_setitem;
	private View main_item;
	private View bind_item;
	private View about_one;
	private View microblog;
	private View loading_item;

	private Map<String,ListViewAdapter> viewMap;
	// app ȫ�ֵ�application ��
	private OneApp oneApp;

	/* һ������view �л��Ķ�ջ */
	Stack<View> context = new Stack<View>();

	private TabHost tabs;
	private TabWidget tabWidget;
	
	
	//ͼƬ�첽���ص�ȫ�ֶ���
	private AsynImageLoader asynImageLoader = null;
	private DataOp dataOp = null;
	
	private GestureDetector detector;  
	
	
	/*list view adapter*/
	private ListViewAdapter listAdapter = null;
	private ListViewAdapter homeAdapter = null;
	private ListViewAdapter QAAdapter = null;
	private ListViewAdapter collectAdapter = null;
	private ListViewAdapter detailAdapter = null;
	
	//�Ƿ���Ҫ����ui
	private boolean isUpdate = false;
	
	//�ж��Ƿ�id���ӻ��Ǽ�С���ֶ�  1Ϊ�� 2 Ϊ�� 0Ϊ���仯
	private int leftOrRight = 0;
	
	//��ǰ����id
	private int maxId = 1;

	
	final ViewHandler viewHandler = new ViewHandler();
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//��ʼ������listview��view��������ݽṹ
		this.viewMap = new HashMap<String,ListViewAdapter>();
		//��ʼ��ͼƬ�첽���ص���
		this.asynImageLoader = new AsynImageLoader(); 
		this.dataOp = new DataOp(asynImageLoader);
	

		// ���������ֹ�������
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());


		oneApp = (OneApp) getApplication();
		oneApp.pushActivity(this);
		// ��ʼ��Ҫ�л���view
		LayoutInflater inflater = LayoutInflater.from(this);
		details_page_setitem = inflater.inflate(
				R.layout.one_details_page_setitem, null);
		main_item = inflater.inflate(R.layout.activity_main, null);
		bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
		about_one = inflater.inflate(R.layout.about_one, null);
		microblog = inflater.inflate(R.layout.one_details_page_microblog, null);
		loading_item = inflater.inflate(R.layout.one_welcome_ad, null);
		
		setContentView(main_item);


		
		// ��ʼ�������ݺ�����view
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
		tabs.addTab(tabs.newTabSpec("home tab").setIndicator("��ҳ", null)
				.setContent(R.id.homeTab));
		tabs.addTab(tabs.newTabSpec("list tab").setIndicator("һƪ", null)
				.setContent(R.id.tab2));
		tabs.addTab(tabs.newTabSpec("QA Tab").setIndicator("�ʴ�", null)
				.setContent(R.id.QAtab));
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("�ղ�", null)
				.setContent(R.id.tab1));
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("����", null)
				.setContent(R.id.tab3));

		tabs.setCurrentTab(0);

		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			/**
			 * ���ø߶ȡ���ȣ����������������Ϊfill_parent���ڴ˶���ûЧ��
			 */
			tabWidget.getChildAt(i).getLayoutParams().height = height;
			tabWidget.getChildAt(i).getLayoutParams().width = width;

			/**
			 * ����tab�б������ֵ���ɫ����ȻĬ��Ϊ��ɫ
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

		// �����첽��ȡ���ݵ��߳�
		//GetDataTask task = new GetDataTask(this);
		//task.execute("http://csdnimg.cn/www/images/csdnindex_logo.gif");
		detector = new GestureDetector(this);  

		viewMap.put("home", this.homeAdapter);
		Log.i("getDataAsyn",String.valueOf(homeAdapter.hashCode()));
		//viewMap.put("QA", this.QAAdapter);
		//viewList.add(detailListViewadapter.getDisplayView());
		//viewList.add(adapter1.getDisplayView());
	
		
		
		
		
		
		
		/**
		 * Ҫ��Activity�п���һ�����ڸ��µ��߳�
		 * timeViewHandler �̳���Handler�����ڴ���ͷ�����Ϣ
		 * MSG_UPDATE ���Զ����һ��int����������������Ϣ���ͣ�������ȡֵ��
		 */
		
		new Thread(new Runnable() {
			@Override
			public void run() {
					
				    isUpdate = true;
				    int currentTab = 0;
				    String id = "";
					TextView idView ;
					View targetView ;
					do
					{
					if(currentTab != tabs.getCurrentTab())
						isUpdate = true;
					if(isUpdate)
					{
					currentTab = tabs.getCurrentTab();
					
					switch(currentTab)
					{
						//home ҳ��
						case  0:
							if(main_item.findViewById(R.id.homeTab).findViewById(R.id.home_id) != null)
							{
								targetView = main_item.findViewById(R.id.homeTab);
								idView = (TextView) targetView.findViewById(R.id.home_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		int currentId = Integer.parseInt(id);
		                		Log.i("thread",String.valueOf(leftOrRight));
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
		
		                			viewHandler.sendMessage(Message.obtain(viewHandler, currentTab, currentId, 1, targetView));
		                		}
								
								/*synchronized (this){
								Thread.yield();}
								System.out.println("wait.................");*/
								isUpdate = false;
							}
							break;
						//һƪ����
						case  1:
							if(main_item.findViewById(R.id.tab2).findViewById(R.id.list_id) != null)
							{
								targetView = main_item.findViewById(R.id.tab2);
								idView = (TextView) targetView.findViewById(R.id.list_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";

		                		int currentId = Integer.parseInt(id);
		                		Log.i("thread",String.valueOf(leftOrRight));
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
		                			Log.i("currentId",String.valueOf(currentId));
		                			
		                			Log.i("maxId",String.valueOf(maxId));
		                		viewHandler.sendMessage(Message.obtain(viewHandler, currentTab, currentId, 1, targetView));
		                		}
								isUpdate = false;
							}
							break;
						//һƪ�ʴ�
						case 2:
							if(main_item.findViewById(R.id.QAtab).findViewById(R.id.QA_id) != null)
							{
								targetView = main_item.findViewById(R.id.QAtab);
								idView = (TextView) targetView.findViewById(R.id.QA_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		int currentId = Integer.parseInt(id);
		                		Log.i("thread",String.valueOf(leftOrRight));
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
		                			Log.i("currentId",String.valueOf(currentId));
		                			
		                			Log.i("maxId",String.valueOf(maxId));
		                			viewHandler.sendMessage(Message.obtain(viewHandler, currentTab, currentId, 1, targetView));
		                		}
								isUpdate = false;
							}
							break;
					
					
					}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}while(true);
							
					
					
				
			}
		}).start();
		setContentView(main_item);
	}

	/**
	 * Handlerʾ��������ˢ��ʱ��
	 * DateHelper�����Լ�д�����ڸ�ʽ������Ŷ
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
			try {
				

				String id = String.valueOf(msg.arg1);
				String url = "http://haidaoteam.sinaapp.com/?datatype=json&type=";
				boolean isNew = false;
				//main_item.findViewById(R.id.homeScrollView).setVisibility(1);
            	//if(main_item.findViewById(R.id.QAtab) != null)
            	//	setDataToView(main_item.findViewById(R.id.QAtab),loadData("QA",data));
            	switch(msg.what)
            	{
            	case 0:
            		if(id.equals( "0" ))
            		{
            			url = url + "home";
            			isNew = true;
            		}
            		else
            			url = url + "home&id="+id;
            		break;
            	case 1:
            		if(id.equals( "0" ))
            		{
            			url = url + "list";
            			isNew = true;
            		}
            		else
            			url = url + "list&id="+id;	
            		break;
            	case 2:
            		if(id.equals( "0" ))
            		{
            			url = url + "QA";
            			isNew = true;
            		}
            		else
            			url = url + "QA&id="+id;
            			
            		break;
            	}

            	String data = dataOp.getDataAsyn(url, about_one, main_item,homeListView);
            	if(isNew)
            		maxId= getDataId(data);
        		setDataToView((View)msg.obj,loadData("data",data));
            	//if(main_item.findViewById(R.id.tab2) != null)
            	//	setDataToView(main_item.findViewById(R.id.tab2),loadData("list",data));
            	//setDataToView(main_item.findViewById(R.id.QAtab),loadData("QA",data));
            	//setDataToView(main_item.findViewById(R.id.tab2),loadData("list",data));
            	//Thread.sleep(3000);
            	//System.out.println("continue ................");
            	//setContentView(main_item);
            	//System.out.println("end ................");
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
	//�������ݵĺ���
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
	
		//�ӷ��ص����ݻ�ȡid
		private int getDataId(String data) throws JSONException
		{
			JSONObject jsonOb = new JSONObject(data);
			String id =  jsonOb.getString("id");
			return Integer.parseInt(id);
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

	/* ���������¼����� */
	public void setButtonOnClick(View view) {
		context.push(main_item);

		setContentView(details_page_setitem);
	}

	/* ΢������¼����� */
	public void microBlogOnClick(View view) {
		context.push(main_item);
		setContentView(bind_item);
	}

	/* ����һƪ���¼����� */
	public void aboutOneOnClick(View view) {
		context.push(main_item);

		setContentView(about_one);
	}

	/* ���ڹ�ע����΢�����¼����� */
	public void sineMicroBlogBindOnClick(View view) {
		context.push(bind_item);
		setContentView(microblog);
	}

	/* return �¼����� */
	public void returnOnClick(View view) {
		setContentView(context.pop());
	}

	/* ������ */
	public void shareOnClick(View view) {
		String shareTitle ="";
		String shareContent = "�Ƽ�����һƪ";
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
		intent.putExtra(Intent.EXTRA_SUBJECT, "����");
		//intent.putExtra(Intent.EXTRA_TEXT, shareTitle+"�Ƽ�����һƪ   VOL.516 ��վ  (���Ժ����Ŷ�) http://caodan.org/516-photo.html ");
		intent.putExtra(Intent.EXTRA_TEXT, shareContent);
		intent.putExtra(Intent.EXTRA_TITLE, shareTitle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "��ѡ��"));
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

		/* �����Ǽ������һ����¼� ����Ҫ�̳�OnGestureListener*/
		
	    @Override  
	    public boolean onTouchEvent(MotionEvent event) {
	        return this.detector.onTouchEvent(event);  
	    }
	    /**
	     * ���ScrollView��ִ�������ƶ������¼�OnGestureListener
	     * ��Activity�����ScrollViewʵ�ֹ���activity��Ч����activity�Ļ���Ч��ȴ�޷���Ч��
	     * ԭ������Ϊactivityû�д�����Ч������д���·������ɽ����
	     */
	    @Override 
	    public boolean dispatchTouchEvent(MotionEvent ev) { 
	        detector.onTouchEvent(ev); 
	        return super.dispatchTouchEvent(ev); 
	    } 
	    /** 
	     * �������� 
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
	        if (e1.getX() - e2.getX() < -120) {
	        	this.leftOrRight = 1;
	        	this.isUpdate = true;
	        	System.out.println("=======================================");
	        }  
	        else if (e1.getX() - e2.getX() > 120) {
	        	this.leftOrRight = 2;
	        	this.isUpdate = true;
	        	System.out.println("+++++++++++++++++++++++++++++++++++++++");
	        }
	        return true;  
	    }
		
}
