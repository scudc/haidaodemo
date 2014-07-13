package com.justone.android.main;


import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;  

public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Integer>> mList;

	//要展示view的id
	private int viewId;
	//要展示的layout id
	private int layoutId;
	

	
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, Integer>> list,
			ArrayList<HashMap<String, Integer>> gist,int viewId,int layoutId) {

		this.mContext = context;
		this.mList = list;
		this.viewId = viewId;
		this.layoutId = layoutId;



    
     

		//初始化数据
		
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
			holder.gv =  (ScrollView) convertView.findViewById(viewId);
			

			convertView.setTag(holder);

			return convertView;
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
    
		
		//LayoutParams params = new LayoutParams(ii * (48 + 10),
		//		LayoutParams.FILL_PARENT);
		//holder.gv.setLayoutParams(params);
		//holder.gv.setColumnWidth(cWidth);
		//holder.gv.setHorizontalSpacing(hSpacing);
		//holder.gv.setStretchMode(GridView.NO_STRETCH);
		//holder.gv.setNumColumns(ii);
		//holder.gv.setAdapter(ga);

		//convertView.setTag(viewName)


		return convertView;
	}


	
	public static class ViewHolder {

		//ImageView iv;
		ScrollView gv;
	}
	
	
    
	
	
		
}
