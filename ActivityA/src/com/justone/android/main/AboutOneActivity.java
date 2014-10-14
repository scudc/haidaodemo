package com.justone.android.main;



import java.util.Stack;

import com.justone.android.util.PicUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
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
	private View versionInfoView;
	private View aboutZaoDongView;
	
	
	private EditText feedback_email_et = null;
	private EditText feedback_phone_et = null;
	private EditText feedback_text_et = null;
	
	 public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        
	       
	        
	        LayoutInflater inflater = LayoutInflater.from(this);
			//details_page_setitem = inflater.inflate(R.layout.one_details_page_setitem, null);
			bind_item = inflater.inflate(R.layout.one_details_page_binditem, null);
			about_one = inflater.inflate(R.layout.about_one, null);
			aboutZaoDongView = inflater.inflate(R.layout.aboutzaodong, null);
			//microblog = inflater.inflate(R.layout.one_details_page_microblog, null);

			feedback = inflater.inflate(R.layout.feedback, null);
			
	
			
			versionInfoView = inflater.inflate(R.layout.showversion_item,
					(ViewGroup)findViewById(R.id.showversion_item));
			TextView tw = (TextView) versionInfoView.findViewById(R.id.currentVersion);
			tw.setText("当前版本 : "+JustOne.versionName);
			 this.main_item = inflater.inflate(R.layout.detail_item, null);
		     setContentView(main_item); 
			
			View aboutOnereturnBackToIndexView = this.findViewById(R.id.aboutOnereturnBackToIndex);
	        aboutOnereturnBackToIndexView.setOnClickListener(new OnClickListener()
	        {
	        	@Override  
			    public void onClick(View v) {  
	        		//returnOnClick(v);
	        		AboutOneActivity.this.finish();
			    }
	        });
	        
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
									//feedback_email_et.setTextColor(0x333333);
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
									//feedback_phone_et.setTextColor(0x333333);
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
									//feedback_text_et.setTextColor(0x333333);
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
		
		/*关于躁动的监听函数 */
		public void aboutZaoDongClick(View view) {
			context.push(main_item);
			final ImageView iw= (ImageView) aboutZaoDongView.findViewById(R.id.aboutzaodong);
			iw.setOnLongClickListener(new View.OnLongClickListener() {
	              
	              @Override
	              public boolean onLongClick(View view) {
	            	  String strFileName = "aboutzaodong.jpg";
	                  String strPath = PicUtil.saveImage(strFileName,iw);
	                  Toast.makeText(view.getContext(), "图片"+strFileName+"已经下载到"+strPath, Toast.LENGTH_SHORT).show();
	                  return false;
	              }
	      });
			setContentView(aboutZaoDongView);
		}
		
		
		
		
		public void feedbackOnClick(View view) {
			context.push(this.main_item);

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
	    	String result = JustOne.getDataOp().postData(email, phone, text);
	    	System.out.println(result);
	    	
	    	feedback_email_et = (EditText)findViewById(R.id.feedback_email_content);
			feedback_phone_et = (EditText)findViewById(R.id.feedback_phone_content);
			feedback_text_et = (EditText)findViewById(R.id.feedback_text_content);
			feedback_email_et.setText(R.string.feedback_null);
			feedback_phone_et.setText(R.string.feedback_null);
			feedback_text_et.setText(R.string.feedback_null);
			//feedback_email_et.setTextColor(R.color.darkgray);
			//feedback_phone_et.setTextColor(R.color.darkgray);
			//feedback_text_et.setTextColor(R.color.darkgray);
			
			
	    	setContentView(context.pop());
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
			Log.i("returnOnClick",view1.toString());
			setContentView(view1);
		}
		
		/*弹出版本提示框*/
		public void versionOnclick(View view)
		{

			context.push(main_item);
			Log.i("version info",main_item.toString());
			setContentView(versionInfoView);
		
		
		}
		

}
