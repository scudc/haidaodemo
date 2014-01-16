package com.example.android.lifecycle;
import java.util.Stack;

import android.app.Application;  
import android.view.View;
import android.app.Activity;
  
public class OneApp extends Application{  
  
	private int currentTabIndex = 0;
    private Stack<Activity> activityStack = new Stack<Activity>();
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
		System.exit(0);
	}

	@Override  
    public void onCreate() {  
        // TODO Auto-generated method stub  
        super.onCreate();  
    }  
  
}  