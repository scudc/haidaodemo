package com.justone.android.main;
import java.util.Stack;

import cn.sharesdk.framework.ShareSDK;

import android.app.Application;  

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
  
public class JustOne extends Application{  
  
	private int currentTabIndex = 0;
    private Stack<Activity> activityStack = new Stack<Activity>();
    public static int localVersion = 0;// ���ذ�װ�汾

	public static int serverVersion = 0;// �������汾

	public static String downloadDir = "jj/";// ��װĿ¼
	
	public static String download_href = ""; //���ص�ַ

	public static String versionDesc = ""; // �°汾����
	
	public static String versionName = ""; //�汾����
	public int getCurrentTabIndex() {
		return currentTabIndex;
	}

	public void setCurrentTabIndex(int currentTabIndex) {
		this.currentTabIndex = currentTabIndex;
	}
	
	public void pushActivity(Activity activity)
	{
		activityStack.push(activity);
	}
	
	public void exit()
	{
		for(Activity activity:activityStack)
		{
			activity.finish();
		}
		ShareSDK.stopSDK(this);
		System.exit(0);
	}

	@Override  
    public void onCreate() {  
        // TODO Auto-generated method stub  
        super.onCreate();  
       ShareSDK.initSDK(this,"1b1cba59b108");
       try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
       
    }  
  
}  