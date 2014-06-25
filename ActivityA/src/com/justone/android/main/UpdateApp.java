package com.justone.android.main;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import com.justone.android.entity.UpdataInfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;

public class UpdateApp extends BaseActivity{
	
	
	/*  
	 * 获取当前程序的版本号   
	 */   
	private String getVersionName() throws Exception{   
	    //获取packagemanager的实例     
	    PackageManager packageManager = getPackageManager();   
	    //getPackageName()是你当前类的包名，0代表是获取版本信息    
	    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);   
	    return packInfo.versionName;    
	}  


	/*  
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)  
	 */   
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{   

	    UpdataInfo info = new UpdataInfo();//实体    
	    info.set_version_id("1");
	    info.setVersion_name("0.0.1");
	    info.set_download_href("");
	    return info;   
	}  
}
