package com.example.android.lifecycle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.example.android.lifecycle.util.AsynImageLoader;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;  

public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Integer>> mList;
	private ArrayList<ArrayList<String>> dataList;
	//要展示view的id
	private int viewId;
	//要展示的layout id
	private int layoutId;
	
	private AsynImageLoader asynImageLoader;
	
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, Integer>> list,
			ArrayList<HashMap<String, Integer>> gist,int viewId,int layoutId,ArrayList<ArrayList<String>> dataList,AsynImageLoader asynImageLoader) {

		this.mContext = context;
		this.mList = list;
		this.viewId = viewId;
		this.layoutId = layoutId;
		this.dataList = dataList;
		this.asynImageLoader = asynImageLoader;
		Log.i("ListViewAdapter","ListViewAdapter");
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


		
		//LayoutParams params = new LayoutParams(ii * (48 + 10),
		//		LayoutParams.FILL_PARENT);
		//holder.gv.setLayoutParams(params);
		//holder.gv.setColumnWidth(cWidth);
		//holder.gv.setHorizontalSpacing(hSpacing);
		//holder.gv.setStretchMode(GridView.NO_STRETCH);
		//holder.gv.setNumColumns(ii);
		//holder.gv.setAdapter(ga);
		Log.i("getView","getView");
		Log.i("loadData",dataList.toString());
		setDataToView(convertView);
		return convertView;
	}

	private void setDataToView(View targetView)
	{
		Iterator<ArrayList<String>> it = dataList.iterator();
		
		while(it.hasNext())
		{
			ArrayList<String > tempArray = (ArrayList<String >)it.next();
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
		//Log.i("setDataToViewEnd", targetView.toString());
	}
	public static class ViewHolder {

		//ImageView iv;
		ScrollView gv;
	}
	
	
	
		
}
