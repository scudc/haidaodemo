package com.justone.android.main;

import java.util.ArrayList;
import java.util.HashMap;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ScrollViewAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Integer>> mGist;
	private Context mContext;
	
	public ScrollViewAdapter(Context context,ArrayList<HashMap<String, Integer>> gist){
	
		this.mContext = context;
		this.mGist = gist;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mGist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mGist.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GViewHolder holder;
		if(convertView ==null){
			holder = new GViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.scrollview_item, null, false);
			//holder.giv = (ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		}else{
			holder = (GViewHolder) convertView.getTag();
		}
		holder.giv.setImageResource(mGist.get(position).get("grid"));
		return convertView;
	}

	public static class GViewHolder{
		
		ImageView giv;
	}
}
