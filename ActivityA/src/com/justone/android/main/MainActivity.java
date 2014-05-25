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
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.EditText;


import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.justone.android.util.AsynImageLoader;
import com.justone.android.util.DataOp;
import com.justone.android.util.StatusTracker;
import com.justone.android.util.Utils;



/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
@SuppressLint({ "NewApi", "HandlerLeak", "ResourceAsColor" })
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

	private View feedback;

	private Map<String,ListViewAdapter> viewMap;
	// app ȫ�ֵ�application ��
	private JustOne justOne;

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
	private ListViewAdapter loadingAdapter = null;
	
	//�Ƿ���Ҫ����ui
	private boolean isUpdate = false;
	
	//�Ƿ���Ҫset main_item 	�л���������
	private boolean isNeedSetToMain = false;
	
	//�ж��Ƿ�id���ӻ��Ǽ�С���ֶ�  1Ϊ�� 2 Ϊ�� 0Ϊ���仯
	private int leftOrRight = 0;
	
	//��ǰ����id
	private int maxId = 1;

	
	final ViewHandler viewHandler = new ViewHandler();
	

	
	//��ǰview������
	private String data = "";
	
	//��ȡ���ݵ�url
	private String url = "http://haidaoteam.sinaapp.com/?datatype=json&type=";

	private EditText feedback_email_et = null;
	private EditText feedback_phone_et = null;
	private EditText feedback_text_et = null;

	//������Դ����
	private Resources res = null;
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
	
		//��ʼ����Դ����
		this.res = getResources(); // Resource object to get Drawables  

		// ���������ֹ�������
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());


		justOne = (JustOne) getApplication();
		justOne.pushActivity(this);
		// ��ʼ��Ҫ�л���view
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
								feedback_email_et.setTextColor(0x333333);
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
								feedback_phone_et.setTextColor(0x333333);
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
								feedback_text_et.setTextColor(0x333333);
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
		

		
		// ��ʼ�������ݺ�����view
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

		detailView = (ListView) findViewById(R.id.tab3);

		//ArrayList<ArrayList<String>> detail_data = loadData("detail",data);
		detailAdapter = new ListViewAdapter(this,
				mList, mGist,
				R.id.detailScrollView, R.layout.detail_item);

		detailView.setAdapter(detailAdapter);
		
		
		/*//����loading �����list view ������
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
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("�ղ�", null)
				.setContent(R.id.tab1));*/
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("",this.res.getDrawable(R.drawable.more_tab_selector))
				.setContent(R.id.tab3));
	
		tabs.setCurrentTab(0);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			/**
			 * ���ø߶ȡ����ȣ�����������������Ϊfill_parent���ڴ˶���ûЧ��
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
		mActivityName = getString(R.string.activity_c_label);
		mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
		Utils.printStatus(mStatusView, mStatusAllView);
		detector = new GestureDetector(this);  
		viewMap.put("home", this.homeAdapter);

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
				Log.i("setDataToView",tempArray.get(0));
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
		this.justOne.setCurrentTabIndex(this.tabs.getCurrentTab());

		// outState.putSerializable("view",super.getCurrentFocus());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		this.justOne.exit();

		return true;
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
		 * Ҫ��Activity�п���һ�����ڸ��µ��߳�
		 * timeViewHandler �̳���Handler�����ڴ����ͷ�����Ϣ
		 * MSG_UPDATE ���Զ����һ��int����������������Ϣ���ͣ�������ȡֵ��
		 */
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				
				
				
				    isUpdate = true;
				    int currentTab = 0;
				    String id = "";
					TextView idView ;
					View targetView  = null;
					boolean isNew = false;
					String currentUrl = "";
					do
					{
					if(currentTab != tabs.getCurrentTab())
						isUpdate = true;
					if(isUpdate && main_item!=null)
					{
					currentTab = tabs.getCurrentTab();
					int currentId = 0;
					switch(currentTab)
					{
						//home ҳ��
						case  0:
							
							if(main_item != null & main_item.findViewById(R.id.homeTab)!=null && main_item.findViewById(R.id.homeTab).findViewById(R.id.home_id) != null)
							{
								
								targetView = main_item.findViewById(R.id.homeTab);
								idView = (TextView) targetView.findViewById(R.id.home_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		currentId = Integer.parseInt(id);
	
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
						//һƪ����
						case  1:
							if(main_item.findViewById(R.id.tab2).findViewById(R.id.list_id) != null)
							{
								targetView = main_item.findViewById(R.id.tab2);
								idView = (TextView) targetView.findViewById(R.id.list_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";

		                		currentId = Integer.parseInt(id);
		 
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
						//һƪ�ʴ�
						case 2:
							if(main_item.findViewById(R.id.QAtab).findViewById(R.id.QA_id) != null)
							{
								targetView = main_item.findViewById(R.id.QAtab);
								idView = (TextView) targetView.findViewById(R.id.QA_id);
		                		id = (String) idView.getText();
		                		if(id == "")
		                			id = "0";
		                		currentId = Integer.parseInt(id);
		                	
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
						
						data = dataOp.getDataAsyn(currentUrl, about_one, main_item,homeListView);
						
						if(isNew)
						{
							try {
								maxId= getDataId(data);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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


	//����loading״̬  ��ȡ��loading״̬   0Ϊ���� 1Ϊȡ��
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
		if (null != feedback_email_et)
		{
			feedback_email_et = (EditText)findViewById(R.id.feedback_email_content);
			feedback_phone_et = (EditText)findViewById(R.id.feedback_phone_content);
			feedback_text_et = (EditText)findViewById(R.id.feedback_text_content);
			feedback_email_et.setText(R.string.feedback_null);
			feedback_phone_et.setText(R.string.feedback_null);
			feedback_text_et.setText(R.string.feedback_null);
			feedback_email_et.setTextColor(R.color.darkgray);
			feedback_phone_et.setTextColor(R.color.darkgray);
			feedback_text_et.setTextColor(R.color.darkgray);
		}
		setContentView(context.pop());
	}

	/* �������� */
	public void shareOnClick(View view) {
		String shareTitle ="";
		String shareContent = "�Ƽ�����һƪ    ";
		if(tabs.getCurrentTabTag() == "home tab" )
		{
			shareTitle = "home tab";
			shareContent = shareContent + ((TextView) this.findViewById(R.id.fPage_tView)).getText().toString()+  ((TextView) this.findViewById(R.id.imageBelow_tView)).getText().toString() +  "(���Ժ����Ŷ�)  " + ((TextView) this.findViewById(R.id.home_share_url)).getText().toString();
		}else if(tabs.getCurrentTabTag() == "QA Tab" )
		{
			shareTitle = "QA Tab";
			shareContent = shareContent + ((TextView) this.findViewById(R.id.question_content)).getText().toString()+ " - �Ķ�ȫ�� (���Ժ����Ŷ�)  " + ((TextView) this.findViewById(R.id.qa_share_url)).getText().toString();
			
			
		}else if(tabs.getCurrentTabTag() == "list tab")
		{
			shareTitle = "list tab";
			shareContent = shareContent + "��" + ((TextView) this.findViewById(R.id.one_content_title)).getText().toString()+ "�� by "+ ((TextView) this.findViewById(R.id.one_content_author)).getText().toString() +  "- �Ķ�ȫ��(���Ժ����Ŷ�)  " + ((TextView) this.findViewById(R.id.list_share_url)).getText().toString();
			
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

	/* һ������ �¼�����*/
    public void showOnekeyshare(View view) {
    	ShareSDK.initSDK(this,"1b1cba59b108");
        OnekeyShare oks = new OnekeyShare();
        
      
        // ����ʱNotification��ͼ�������
        oks.setNotification(R.drawable.ic_launcher, 
        this.getString(R.string.app_name));
        // address�ǽ����˵�ַ��������Ϣ���ʼ�ʹ��
        oks.setAddress("12345678901");
        // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
        oks.setTitle(this.getString(R.string.share));
        // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
        //oks.setTitleUrl("http://sharesdk.cn");
        // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
        oks.setText("����Ƿ������ı�");
        // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
        //oks.setImagePath(MainActivity.TEST_IMAGE);
        // imageUrl��ͼƬ������·��������΢������������QQ�ռ䡢
        // ΢�ŵ�����ƽ̨��Linked-In֧�ִ��ֶ�
        oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
        // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
        oks.setUrl("http://sharesdk.cn");
        // appPath�Ǵ�����Ӧ�ó���ı���·��������΢����ʹ��
        //oks.setAppPath(MainActivity.TEST_IMAGE);
        // comment���Ҷ��������������ۣ�������������QQ�ռ�ʹ��
        //oks.setComment(getContext().getString(R.string.share));
        // site�Ƿ��������ݵ���վ���ƣ�����QQ�ռ�ʹ��
        //oks.setSite(context.getString(R.string.app_name));
        // siteUrl�Ƿ��������ݵ���վ��ַ������QQ�ռ�ʹ��
       // oks.setSiteUrl("http://sharesdk.cn");
        // venueName�Ƿ����������ƣ�����Foursquareʹ��
       // oks.setVenueName("Southeast in China");
        // venueDescription�Ƿ�����������������Foursquareʹ��
        //oks.setVenueDescription("This is a beautiful place!");
        // latitude��ά�����ݣ���������΢������Ѷ΢����Foursquareʹ��
        oks.setLatitude(23.122619f);
        // longitude�Ǿ������ݣ���������΢������Ѷ΢����Foursquareʹ��
        oks.setLongitude(113.372338f);
        // �Ƿ�ֱ�ӷ�����true��ֱ�ӷ�����
        oks.setSilent(false);
        // ָ������ƽ̨����slientһ��ʹ�ÿ���ֱ�ӷ�����ָ����ƽ̨
        //if (platform != null) {
        //        oks.setPlatform(platform);
        //}
        // ȥ��ע�Ϳ�ͨ��OneKeyShareCallback�������ݷ����Ĵ������
        // oks.setCallback(new OneKeyShareCallback());
        //ͨ��OneKeyShareCallback���޸Ĳ�ͬƽ̨����������
       // oks.setShareContentCustomizeCallback(
      //  new ShareContentCustomizeDemo());
        
        oks.show(this);
    }
	public void feedbackOnClick(View view) {
		context.push(about_one);

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

		/* �����Ǽ������һ����¼� ����Ҫ�̳�OnGestureListener*/
		
	    @Override  
	    public boolean onTouchEvent(MotionEvent event) {
	        return this.detector.onTouchEvent(event);  
	    }
	    /**
	     * ���ScrollView��ִ�������ƶ������¼�OnGestureListener
	     * ��Activity������ScrollViewʵ�ֹ���activity��Ч����activity�Ļ���Ч��ȴ�޷���Ч��
	     * ԭ������Ϊactivityû�д�������Ч������д���·������ɽ����
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
	        if (e1.getX() - e2.getX() < -300) {
	        	this.leftOrRight = 1;
	        	this.isUpdate = true;
	        	
	        }  
	        else if (e1.getX() - e2.getX() > 300) {
	        	this.leftOrRight = 2;
	        	this.isUpdate = true;
	        	
	        }
	        return true;  
	    }
	    
	    
	    
		
}