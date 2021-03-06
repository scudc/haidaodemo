package com.justone.android.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.justone.android.bean.IndexBean;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;  
import android.os.Handler;  
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.ViewGroup;  
import android.widget.AbsListView;  
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.BaseAdapter;  
 

 
@SuppressLint("HandlerLeak")
public class IndexActivity extends Activity implements IXListViewListener {  
      
    private XListView listView;  
    //可加载记录的条数  
    private int count = 100;  
    private int lastItem;  
    //当前可见页面中的Item总数  
    private int visibleItemCount;  
      
    private ListAdapter mAdapter;
    
	private Handler mHandler;
	
	
	private ArrayList<IndexBean> indexBeanList;
	
	//刷新数据的url
	private String refreshUrl = "http://haidaoteam.sinaapp.com/?datatype=json&type=index";
	
	//获取更多数据的url
	private String moreUrl = "http://haidaoteam.sinaapp.com/?datatype=json&type=index&more=true&id=";
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	
    private ArrayList<View> items = new ArrayList<View>();
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        this.indexBeanList = new ArrayList<IndexBean>();
        setContentView(R.layout.index);  
        //geneItems();
        listView = (XListView) findViewById(R.id.index_list);
        listView.setPullLoadEnable(true);
        mAdapter = new ListAdapter(this.indexBeanList);
        listView.setAdapter(mAdapter);
        
        listView.setXListViewListener(this);
		mHandler = new Handler()
		{
			 	@Override  
	            public void handleMessage(Message msg) {  
	                super.handleMessage(msg);  
	                if(msg.what == 3) //是否需要onload
	                {
	                	onLoad();
	                	mAdapter.notifyDataSetChanged();
	                	return ;
	                }
	                
	                indexBeanList.add(0,(IndexBean) msg.obj);
	                Log.i("xxxx",indexBeanList.toString());
	                Log.v("@@@@@@", "this is get message");  
	                mAdapter.refresh(indexBeanList);  
//	              mAdapter.notifyDataSetChanged();  
	            }  
			
		};
		
		
		//设置更多button的按钮事件
		Button moreButton = (Button) this.findViewById(R.id.indexMoreButton);
		moreButton.setOnClickListener(new OnClickListener(){  
		    @Override  
		    public void onClick(View v) {  
		    	Intent intent = new Intent(IndexActivity.this, AboutOneActivity.class);
				startActivity(intent);
		    }
		});
		
		
		//启动加载数据的线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				refreshHelper(refreshUrl,0);
				onLoad();
				
			}
			
			
		}).start();
		
    }  
 
 
 

    private void onLoad() {
    	
    	listView.stopRefresh();
    	listView.stopLoadMore();
    	//listView.setRefreshTime("刚刚");
	}
    public void onScrollStateChanged(AbsListView view, int scrollState) {  
    }  
      
    /**  
     * 数据匹配器  
     * @author qin_lei  
     *  
     */ 
    public class ListAdapter extends BaseAdapter{  
 
        private int count = 3;  
        
        private ArrayList<IndexBean> mList;
        
        public ListAdapter(ArrayList<IndexBean> list)
        {
        	this.mList = list;
        }
          
        
        public int getCount() {  
            return mList.size(); 
        }  
 
        public Object getItem(int position) {  
            return mList.get(position);  
        }  
 
        public long getItemId(int position) {  
            return position;  
        }  
 
        public View getView(int position, View convertView, ViewGroup parent) {  
        	Holder holder = null;
        	if(convertView == null)
        	{
        		LayoutInflater inflater = LayoutInflater.from(IndexActivity.this);  
        		convertView = inflater.inflate(R.layout.index_item, null); 
        		holder = new Holder();
        		holder.indexImageView = (ImageView)convertView.findViewById(R.id.index_image_view);
        		holder.indexTextView = (TextView)convertView.findViewById(R.id.index_title_view);
        		convertView.setTag(holder);
        	}else
        	{
        		holder = (Holder)convertView.getTag();
        	}
        	
        	View loadingView = convertView.findViewById(R.id.indexImageLoading);
        	if(loadingView == null)
        	{
        		Log.i("loading","loading");
        	}
        	Message message = new Message();
			message.what = 3;
        	JustOne.getAsynImageLoader().showImageAsyn(holder.indexImageView, (mList.get(getCount() - position - 1)).getImageUrl(),loadingView,message,mHandler );  
        	holder.indexTextView.setText("VOL."+ (mList.get(getCount() - position - 1).getIndexId()+" "+(mList.get(getCount() - position - 1)).getIndexTitle()));
        	holder.indexImageView.setTag(Integer.parseInt((mList.get(getCount() - position - 1).getIndexId())));
        	holder.indexImageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					JustOne.setCurrentId((Integer) (arg0.getTag()));
					Intent intent = new Intent(IndexActivity.this, MainActivity.class);
					startActivity(intent);
				}
			});
        	return convertView;  
        }
        
        class Holder {
        	ImageView indexImageView;
        	TextView indexTextView;
        }
        
        public void refresh(ArrayList<IndexBean> list) {
    		mList = list;
    		notifyDataSetChanged();
    	}
    }

    @Override
	public void onRefresh() {
    	//启动加载数据的线程
    			new Thread(new Runnable() {

    				@Override
    				public void run() {
    					
    					refreshHelper(refreshUrl,1);
    					
    					Message message = new Message();
    					 message.what = 3;
    					 mHandler.sendMessage(message);  
    					 //onLoad();	
    					
    				}
    				
    				
    			}).start();
    	
    	/*
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				
				//listView.removeAllViews();
				refreshHelper(refreshUrl,1);
		
				 onLoad();
				 Log.i("onRefresh","onRefresh");
	
			}
			
			
		}, 2000)*/;
		
	}

    //flag用来标识是否是不是刷新，1为刷新
    private void refreshHelper(String url,int flag)
    {
    	int maxId = 0;
		int minId = Integer.MAX_VALUE;
		try {
			String data = JustOne.getDataOp().getDataAsyn(url);
			Log.i("refreshHelper",url);
			
			Log.i("refreshxxxxr",data);
			
			
			 ArrayList<HashMap<String, String>> dataList = JustOne.getDataOp().resolveData(data,"data");
			 for(int i =0;i<dataList.size();i++)
			 {
				 
				 IndexBean indexBean = new IndexBean();
				 HashMap<String,String> tempMap = dataList.get(i);
				 indexBean.setImageUrl(tempMap.get("imageView1"));
				 indexBean.setIndexId(String.valueOf(tempMap.get("id")));
			     indexBean.setIndexTitle(tempMap.get("imageBelow_tView"));
				 Message message = new Message();
				 message.obj = indexBean;
				 message.what = 1;
				 mHandler.sendMessage(message);  
				 
				 
				 
				 if(Integer.parseInt(tempMap.get("id")) > maxId)
					 maxId = Integer.parseInt(tempMap.get("id"));
				 if(Integer.parseInt(tempMap.get("id")) < minId)
					 minId = Integer.parseInt(tempMap.get("id"));
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JustOne.setMaxId(maxId);
		
		JustOne.setMinId(minId);
		// TODO Auto-generated method stub
    }
	@Override
	public void onLoadMore() {
		/*
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshHelper(moreUrl+JustOne.getMinId(),0);
                  onLoad();
			}
		}, 2000);*/
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				refreshHelper(moreUrl+JustOne.getMinId(),0);
				Message message = new Message();
				 message.what = 3;
				 mHandler.sendMessage(message);  
                //onLoad();
				
			}
			
			
		}).start();
	}
	
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
		   /*Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            this.startActivity(mHomeIntent);
            */
			((JustOne)getApplication()).exit();
			android.os.Process.killProcess(android.os.Process.myPid()) ;   //获取PID
			System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //调用双击退出函数  
	    		//this.finish();
	       }  
	    return false; 
	}
	
}
      