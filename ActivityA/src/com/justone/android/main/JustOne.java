package com.justone.android.main;
import java.util.Stack;

import com.justone.android.util.AsynImageLoader;
import com.justone.android.util.DataOp;

import cn.sharesdk.framework.ShareSDK;

import android.app.Application;  

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
  
public class JustOne extends Application{  
  
	private int currentTabIndex = 0;
    private Stack<Activity> activityStack = new Stack<Activity>();
    private static int localVersion = 0;// 本地安装版本
    
	private static int serverVersion = 0;// 服务器版本

	public static String downloadDir = "jj/";// 安装目录
	
	public static String download_href = ""; //下载地址

	private static String versionDesc = ""; // 新版本描述
	
	public static String versionName = ""; //版本名称
	
	//图片异步加载的全局对象
	private static AsynImageLoader asynImageLoader = null;
	
	//全卷的id，用来表示当前主题的id
	private static int currentId;
	
	private static DataOp dataOp = null;
	
	public static int getCurrentId() {
		return currentId;
	}

	public static void setCurrentId(int currentId) {
		JustOne.currentId = currentId;
	}

	public static int getMaxId() {
		return maxId;
	}

	public static void setMaxId(int maxId) {
		JustOne.maxId = maxId;
	}

	//全局的最大的id，用来标识是否已经是最新的内容
	private static int maxId;
	
	//全局的最小的id，用来标识是否已经是最新的内容
	private static int minId;
	public static int getMinId() {
		return minId;
	}

	public static void setMinId(int minId) {
		JustOne.minId = minId;
	}

	public static AsynImageLoader getAsynImageLoader() {
		return asynImageLoader;
	}

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
			this.versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
       
       JustOne.asynImageLoader = new AsynImageLoader(); 
       this.dataOp = new DataOp(asynImageLoader);
    }

	public static DataOp getDataOp() {
		return dataOp;
	}

	public void setDataOp(DataOp dataOp) {
		this.dataOp = dataOp;
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