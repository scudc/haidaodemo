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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.Toast;

import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {

	private HashMap<String, String> nameToIdMap;

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

	// app ȫ�ֵ�application ��
	private OneApp oneApp;

	/* һ������view �л��Ķ�ջ */
	Stack<View> context = new Stack<View>();

	private TabHost tabs;
	private TabWidget tabWidget;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �����첽��ȡ���ݵ��߳�
		GetDataTask task = new GetDataTask(this);
		task.execute("http://csdnimg.cn/www/images/csdnindex_logo.gif");

		// ���������ֹ�������
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		// ��ʼ��nameToIdMap
		this.nameToIdMap = new HashMap<String, String>();
		this.nameToIdMap.put("new_tView", Integer.toString(R.id.new_tView));
		this.nameToIdMap.put("question_title",
				Integer.toString(R.id.question_title));
		this.nameToIdMap.put("fPage_tView", Integer.toString(R.id.fPage_tView));
		this.nameToIdMap.put("imageView1", Integer.toString(R.id.imageView1));

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

		setContentView(main_item);
		/*
		 * 
		 * mListView = (ListView) findViewById(R.id.tab2); initData();
		 * ListViewAdapter adapter = null; try {
		 * 
		 * adapter = new ListViewAdapter(this,
		 * mList,mGist,R.id.scrollview,R.layout
		 * .list_item,loadData("home",this.nameToIdMap)); } catch (JSONException
		 * e) { // TODO Auto-generated catch block
		 * Log.v("DEBUG","you are not ok"); e.printStackTrace(); }
		 * mListView.setAdapter(adapter);
		 * 
		 * mListView1 = (ListView) findViewById(R.id.tab1); ListViewAdapter
		 * adapter1 = null; try { adapter1 = new ListViewAdapter(this,
		 * mList,mGist
		 * ,R.id.collectScrollview,R.layout.collect_item,loadData("home"
		 * ,this.nameToIdMap)); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * mListView1.setAdapter(adapter1);
		 * 
		 * homeListView = (ListView) findViewById(R.id.homeTab); ListViewAdapter
		 * homeListViewadapter = null; try { homeListViewadapter = new
		 * ListViewAdapter(this,
		 * mList,mGist,R.id.homeScrollView,R.layout.home_item
		 * ,loadData("home",this.nameToIdMap)); } catch (JSONException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 * homeListView.setAdapter(homeListViewadapter);
		 * 
		 * qaListView = (ListView) findViewById(R.id.QAtab); ListViewAdapter
		 * qaListViewadapter = null; try { qaListViewadapter = new
		 * ListViewAdapter
		 * (this,mList,mGist,R.id.qaScrollView,R.layout.qa_item,loadData
		 * ("QA",this.nameToIdMap)); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * qaListView.setAdapter(qaListViewadapter);
		 * 
		 * detailView = (ListView) findViewById(R.id.tab3); ListViewAdapter
		 * detailListViewadapter = null; try { detailListViewadapter = new
		 * ListViewAdapter
		 * (this,mList,mGist,R.id.detailScrollView,R.layout.detail_item
		 * ,loadData("home",this.nameToIdMap)); } catch (JSONException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 * detailView.setAdapter(detailListViewadapter);
		 */
		tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);

		// tabWidget.setBackgroundColor(Color.BLACK);

		int width = 60;
		int height = 60;
		tabs.setup();
		tabs.addTab(tabs.newTabSpec("home tab").setIndicator("��ҳ", null)
				.setContent(R.id.homeTab));
		tabs.addTab(tabs.newTabSpec("second tab").setIndicator("һƪ", null)
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

		Log.i("share", "true");

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		// intent.setPackage("com.sina.weibo");
		intent.putExtra(Intent.EXTRA_SUBJECT, "����");
		intent.putExtra(Intent.EXTRA_TEXT, "��� ");
		intent.putExtra(Intent.EXTRA_TITLE, "���Ǳ���");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "��ѡ��"));
	}

	/* �첽�������ݵ��� */
	class GetDataTask extends AsyncTask<String, Integer, Bitmap> {// �̳�AsyncTask

		private MainActivity mainActivity;

		public GetDataTask(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Bitmap doInBackground(String... params) {// �����ִ̨�е������ں�̨�߳�ִ��
			publishProgress(0);// �������onProgressUpdate(Integer... progress)����
			HttpClient hc = new DefaultHttpClient();
			publishProgress(30);
			HttpGet hg = new HttpGet(params[0]);// ��ȡcsdn��logo
			final Bitmap bm;
			try {
				HttpResponse hr = hc.execute(hg);
				bm = BitmapFactory.decodeStream(hr.getEntity().getContent());
			} catch (Exception e) {

				return null;
			}
			publishProgress(100);
			// mImageView.setImageBitmap(result); �����ں�̨�̲߳���ui
			return bm;
		}

		protected void onProgressUpdate(Integer... progress) {// �ڵ���publishProgress֮�󱻵��ã���ui�߳�ִ��
			// mProgressBar.setProgress(progress[0]);//���½������Ľ���
		}

		protected void onPostExecute(Bitmap result) {// ��̨����ִ����֮�󱻵��ã���ui�߳�ִ��
			if (result != null) {
				// Toast.makeText(AsyncTaskActivity.this, "�ɹ���ȡͼƬ",
				// Toast.LENGTH_LONG).show();
				// mImageView.setImageBitmap(result);

				// ��ʼ�������ݺ�����view
				mListView = (ListView) findViewById(R.id.tab2);
				initData();
				ListViewAdapter adapter = null;
				try {

					adapter = new ListViewAdapter(this.mainActivity, mList,
							mGist, R.id.scrollview, R.layout.list_item,
							loadData("home", nameToIdMap));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.v("DEBUG", "you are not ok");
					e.printStackTrace();
				}
				mListView.setAdapter(adapter);

				mListView1 = (ListView) findViewById(R.id.tab1);
				ListViewAdapter adapter1 = null;
				try {
					adapter1 = new ListViewAdapter(this.mainActivity, mList,
							mGist, R.id.collectScrollview,
							R.layout.collect_item,
							loadData("home", nameToIdMap));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mListView1.setAdapter(adapter1);

				homeListView = (ListView) findViewById(R.id.homeTab);
				ListViewAdapter homeListViewadapter = null;
				try {
					homeListViewadapter = new ListViewAdapter(
							this.mainActivity, mList, mGist,
							R.id.homeScrollView, R.layout.home_item, loadData(
									"home", nameToIdMap));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				homeListView.setAdapter(homeListViewadapter);

				qaListView = (ListView) findViewById(R.id.QAtab);
				ListViewAdapter qaListViewadapter = null;
				try {
					qaListViewadapter = new ListViewAdapter(this.mainActivity,
							mList, mGist, R.id.qaScrollView, R.layout.qa_item,
							loadData("QA", nameToIdMap));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				qaListView.setAdapter(qaListViewadapter);

				detailView = (ListView) findViewById(R.id.tab3);
				ListViewAdapter detailListViewadapter = null;
				try {
					detailListViewadapter = new ListViewAdapter(
							this.mainActivity, mList, mGist,
							R.id.detailScrollView, R.layout.detail_item,
							loadData("home", nameToIdMap));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				detailView.setAdapter(detailListViewadapter);
			} else {
				// Toast.makeText(AsyncTaskActivity.this, "��ȡͼƬʧ��",
				// Toast.LENGTH_LONG).show();
			}
		}

		protected void onPreExecute() {// ��
										// doInBackground(Params...)֮ǰ�����ã���ui�߳�ִ��
			// mImageView.setImageBitmap(null);
			// mProgressBar.setProgress(0);//��������λ
		}

		protected void onCancelled() {// ��ui�߳�ִ��
			// mProgressBar.setProgress(0);//��������λ
		}

		//�������ݵĺ���
		private ArrayList<ArrayList<String>> loadData(String viewName,
				HashMap<String, String> nameToIdMap) throws JSONException {
			//
			String data = "{'home':[['new_tView','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['fPage_tView','VOL.284','text'],['imageView1','http://pic.yupoo.com/hanapp/DyfFOBnd/custom.jpg','image']],'QA':[['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text']]}";
			ArrayList<ArrayList<String>> tempResult = new ArrayList<ArrayList<String>>();
			try {
				JSONArray jsonArray = new JSONObject(data).getJSONArray(viewName);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONArray tempJson = (JSONArray) jsonArray.opt(i);
					ArrayList<String> tempArray = new ArrayList<String>();
					tempArray.add(String.valueOf(nameToIdMap.get(tempJson
							.getString(0))));
					tempArray.add(String.valueOf(tempJson.getString(1)));
					tempArray.add(String.valueOf(tempJson.getString(2)));
					tempResult.add(tempArray);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return tempResult;

		}
	}

}
