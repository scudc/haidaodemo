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
    private static int localVersion = 0;// ���ذ�װ�汾

	private static int serverVersion = 0;// �������汾

	public static String downloadDir = "jj/";// ��װĿ¼
	
	public static String download_href = ""; //���ص�ַ

	private static String versionDesc = ""; // �°汾����
	
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
			setLocalVersion(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
       
    }

	public static int getServerVersion() {
		return serverVersion;
	}

	public static void setServerVersion(int serverVersion) {
		JustOne.serverVersion = serverVersion;
	}

	public static int getLocalVersion() {
		return localVersion;
	}

	public static void setLocalVersion(int localVersion) {
		JustOne.localVersion = localVersion;
	}

	public static String getVersionDesc() {
		return versionDesc;
	}

	public static void setVersionDesc(String versionDesc) {
		JustOne.versionDesc = versionDesc;
	}  
  
}  