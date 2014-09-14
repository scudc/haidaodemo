package com.justone.android.main;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    //�ɼ��ؼ�¼������  
    private int count = 100;  
    private int lastItem;  
    //��ǰ�ɼ�ҳ���е�Item����  
    private int visibleItemCount;  
      
    private ListAdapter mAdapter;
    
	private Handler mHandler;
	
	
	private ArrayList<IndexBean> indexBeanList;
	
	/** 
	 * ˫���˳����� 
	 */  
	private static Boolean isExit = false;  
	
    private ArrayList<View> items = new ArrayList<View>();
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        this.indexBeanList = new ArrayList<IndexBean>();
        setContentView(R.layout.index);  
        geneItems();
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
	                indexBeanList.add((IndexBean) msg.obj);  
	                Log.v("@@@@@@", "this is get message");  
	                mAdapter.refresh(indexBeanList);  
//	              mAdapter.notifyDataSetChanged();  
	            }  
			
		};
		
		//���ø���button�İ�ť�¼�
		Button moreButton = (Button) this.findViewById(R.id.indexMoreButton);
		moreButton.setOnClickListener(new OnClickListener(){  
		    @Override  
		    public void onClick(View v) {  
		    	Intent intent = new Intent(IndexActivity.this, AboutOneActivity.class);
				startActivity(intent);
		    }
		});
		
		//�����������ݵ��߳�
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				//��ȡ�����10����¼
				for(int i =0;i<5;i++)
				{
					IndexBean indexBean = new IndexBean();
					indexBean.setImageUrl("xxx");
					indexBean.setIndexId(String.valueOf(i+1));
					indexBean.setIndexTitle("��������");
					Message message = new Message();
					message.obj = indexBean;
				
					mHandler.sendMessage(message);  
				}
				
				JustOne.setMaxId(18);
				
				
				// TODO Auto-generated method stub
				
			}
			
			
		}).start();

    }  
 
 
 
    private void geneItems() {
		for (int i = 0; i != 5; ++i) {
			LayoutInflater inflater = LayoutInflater.from(IndexActivity.this);  
            View view = inflater.inflate(R.layout.index_item, null);  
			items.add(view);
		}
	}
    private void onLoad() {
    	listView.stopRefresh();
    	listView.stopLoadMore();
    	listView.setRefreshTime("�ո�");
	}
    public void onScrollStateChanged(AbsListView view, int scrollState) {  
    }  
      
    /**  
     * ����ƥ����  
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
        	JustOne.getAsynImageLoader().showImageAsyn(holder.indexImageView, "http://haidaoteam-cdn.stor.sinaapp.com/1387808504418.jpg",loadingView );  
        	holder.indexTextView.setText("VOL. "+ (mList.get(getCount() - position - 1).getIndexId()+" "+(mList.get(getCount() - position - 1)).getIndexTitle()));
        	holder.indexImageView.setTag(17);
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
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				//��ȡ�����10����¼
				for(int i =0;i<5;i++)
				{
					IndexBean indexBean = new IndexBean();
					indexBean.setImageUrl("xxx");
					indexBean.setIndexId(String.valueOf(i+1));
					indexBean.setIndexTitle("��������");
					Message message = new Message();
					message.obj = indexBean;
					mHandler.sendMessage(message);  
				}
				 onLoad();
	
			}
			
			
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for(int i =0;i<5;i++)
				{
					IndexBean indexBean = new IndexBean();
					indexBean.setImageUrl("xxx");
					indexBean.setIndexId(String.valueOf(i+1));
					indexBean.setIndexTitle("��������");
					Message message = new Message();
					message.obj = indexBean;
					
					mHandler.sendMessage(message);  
				}
                  onLoad();
			}
		}, 2000);
	}
	
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // ׼���˳�
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // ȡ���˳�
				}
			}, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����

		} else {
		   /*Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            this.startActivity(mHomeIntent);
            */
			((JustOne)getApplication()).exit();
			android.os.Process.killProcess(android.os.Process.myPid()) ;   //��ȡPID
			System.exit(0);   //����java��c#�ı�׼�˳���������ֵΪ0���������˳�
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //����˫���˳�����  
	    		//this.finish();
	       }  
	    return false; 
	}
	
}
      