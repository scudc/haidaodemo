
package com.example.android.lifecycle;
/**   
 * �ļ�����BaseActivity.java   
 * �汾�ţ�        
 * ���ڣ�2012-6-20 
 * �����ˣ�
 * Copyright wadata ��Ȩ����
 * �����
 */

 
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.View;
 
/**
 * ���ƣ�BaseActivity 
 * ������ 
 * �����ˣ� 
 * ���ڣ�2012-6-20 ����5:53:35 
 * �����
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