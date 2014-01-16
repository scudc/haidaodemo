
package com.example.android.lifecycle;
/**   
 * 文件名：BaseActivity.java   
 * 版本号：        
 * 日期：2012-6-20 
 * 创建人：
 * Copyright wadata 版权所有
 * 变更：
 */

 
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.View;
 
/**
 * 名称：BaseActivity 
 * 描述： 
 * 创建人： 
 * 日期：2012-6-20 下午5:53:35 
 * 变更：
 */
 
public class BaseActivity extends Activity {
       private int currentView = 0 ;

	public int getCurrentView() {
		return currentView;
	}

	public void setCurrentView(int currentView) {
		this.currentView = currentView;
	}



	
       
       
}