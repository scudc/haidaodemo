package com.justone.android.main;



import java.util.Stack;

import com.justone.android.util.PicUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class AboutOneActivity extends Activity {
	
	 /* 一个保存view 切换的堆栈 */
	 private Stack<View> context = new Stack<View>();
	 private View main_item = null;
	 
	 /* 要切换的view */
	private View details_page_setitem;
	private View bind_item;
	private View about_one;
	private View microblog;
	private View feedback;
	
	
	 public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        
	       
	        
	        LayoutInflater inflater = LayoutInflater.from(this);
			//details_page_setitem = inflater.inflate(R.layout.one_details_page_setitem, null);
			bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
			about_one = inflater.inflate(R.layout.about_one, null);
			//microblog = inflater.inflate(R.layout.one_details_page_microblog, null);

			feedback = inflater.inflate(R.layout.feedback, null);
			
			 this.main_item = inflater.inflate(R.layout.detail_item, null);
		     setContentView(main_item); 
			
			View aboutOnereturnBackToIndexView = this.findViewById(R.id.aboutOnereturnBackToIndex);
	        aboutOnereturnBackToIndexView.setOnClickListener(new OnClickListener()
	        {
	        	@Override  
			    public void onClick(View v) {  
			    	AboutOneActivity.this.finish();
			    }
	        });
				
	}
	 
	 /* 微博点击事件监听 */
		public void microBlogOnClick(View view) {
			context.push(main_item);
			final ImageView iw= (ImageView) bind_item.findViewById(R.id.erweima);
			iw.setOnLongClickListener(new View.OnLongClickListener() {
	              
	              @Override
	              public boolean onLongClick(View view) {
	            	  String strFileName = "weixincode.jpg";
	                  String strPath = PicUtil.saveImage(strFileName,iw);
	                  Toast.makeText(view.getContext(), "图片"+strFileName+"已经下载到"+strPath, Toast.LENGTH_SHORT).show();
	                  return false;
	              }
	      });
			setContentView(bind_item);
		}

		/* 关于一篇的事件监听 */
		public void aboutOneOnClick(View view) {
			context.push(main_item);

			setContentView(about_one);
		}

		/* 关于关注新浪微博的事件监听 */
		public void sineMicroBlogBindOnClick(View view) {
			context.push(bind_item);
			setContentView(microblog);
		}

		public void goToRank(View view)
		{
			Uri uri = Uri.parse("market://details?id="+getPackageName()); 
			Intent intent = new Intent(Intent.ACTION_VIEW,uri); 
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intent);
			Log.i("goToRank",String.valueOf(context.size()));
		}
		/* return 事件监听 */
		public void returnOnClick(View view) {
			
			View view1 = context.pop();
			setContentView(view1);
		}
		
		/*弹出版本提示框*/
		public void versionOnclick(View view)
		{
			LayoutInflater inflater = getLayoutInflater();
			
			View layout = inflater.inflate(R.layout.showversion_item,
					(ViewGroup)findViewById(R.id.showversion_item));
			TextView tw = (TextView) layout.findViewById(R.id.currentVersion);
			tw.setText("当前版本 : "+JustOne.versionName);
			
		
		
		}
		

}
