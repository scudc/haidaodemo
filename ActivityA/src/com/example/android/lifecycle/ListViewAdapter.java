package com.example.android.lifecycle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.example.android.lifecycle.util.AsynImageLoader;
import com.example.android.lifecycle.util.DataOp;


import android.app.Activity;
import android.content.Context;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;  

public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Integer>> mList;
	private DataOp dataOp;
	
	private ArrayList<ArrayList<String>> dataList;
	//要展示view的id
	private int viewId;
	//要展示的layout id
	private int layoutId;
	
	private AsynImageLoader asynImageLoader;
	
	//监听listview中的手势事件
	private GestureDetector detector;
	FlingListeber listener;
	
	private String viewName;
	//显示的view对象
	private View dispalyView; 
	
	public ListViewAdapter(Activity ac,Context context,
			ArrayList<HashMap<String, Integer>> list,
			ArrayList<HashMap<String, Integer>> gist,int viewId,int layoutId,DataOp dataOp,AsynImageLoader asynImageLoader, String string) {

		this.mContext = context;
		this.mList = list;
		this.viewId = viewId;
		this.layoutId = layoutId;

		this.asynImageLoader = asynImageLoader;
        listener = new FlingListeber();
        detector = new GestureDetector(listener);
        this.viewName = string;
		
		this.dispalyView = null;
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
			
			//holder.gv =  new ScrollView(this.mContext);
			//holder.gv.add
			//holder.gv.addView(convertView.findViewById(viewId));
			convertView.setTag(holder);
			//setDataToView(convertView);
			
			this.dispalyView = convertView;
			Log.i("dc",this.dispalyView.toString());
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


	public View getDisplayView()
	{
		return this.dispalyView;
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
	
	class FlingListeber implements GestureDetector.OnGestureListener{

		View item;
        ViewHolder holder;
        
        public View getItem() {
            return item;
        }

        public void setItem(View convertView) {
            this.item = convertView;
        }

        
        
        public ViewHolder getHolder() {
            return holder;
        }

        public void setHolder(ViewHolder holder) {
            this.holder = holder;
        }


    

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
        	Log.i("onFling",String.valueOf(e1.getX())+"-"+String.valueOf(e1.getY())+","+String.valueOf(e2.getX())+"-"+String.valueOf(e2.getY()));
            // TODO Auto-generated method stub
            if(e2.getX()-e1.getX()>5){
                Log.i("onFling","1");
            }else if(e1.getX()-e2.getX()>5){
            	Log.i("onFling","2");
                
            }
            return false;
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
        
    }
	
	
		
}
