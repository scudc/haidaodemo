package com.example.android.lifecycle;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import android.util.Log;  

public class ListViewAdapter extends BaseAdapter {

	//列宽
	private int cWidth = 120;
	//水平间距
	private int hSpacing = 10;
	private Context mContext;
	private ArrayList<HashMap<String, Integer>> mList;
	private ArrayList<HashMap<String, Integer>> mGist;
    
	//要展示view的id
	private int viewId;
	//要展示的layout id
	private int layoutId;
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, Integer>> list,
			ArrayList<HashMap<String, Integer>> gist,int viewId,int layoutId) {

		this.mContext = context;
		this.mList = list;
		this.mGist = gist;
		this.viewId = viewId;
		this.layoutId = layoutId;
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					layoutId, arg2, false);
			//holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			holder.gv = (ScrollView) convertView.findViewById(viewId);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.iv.setImageResource(mList.get(arg0).get("list"));

		ScrollViewAdapter ga = new ScrollViewAdapter(mContext, mGist);
		int ii = ga.getCount();
		//Log.v("DEBUG",String.valueOf(ii));
		//LayoutParams params = new LayoutParams(ii * (48 + 10),
		//		LayoutParams.FILL_PARENT);
		//holder.gv.setLayoutParams(params);
		//holder.gv.setColumnWidth(cWidth);
		//holder.gv.setHorizontalSpacing(hSpacing);
		//holder.gv.setStretchMode(GridView.NO_STRETCH);
		//holder.gv.setNumColumns(ii);
		//holder.gv.setAdapter(ga);
		

		return convertView;
	}

	public static class ViewHolder {

		//ImageView iv;
		ScrollView gv;
	}
}
