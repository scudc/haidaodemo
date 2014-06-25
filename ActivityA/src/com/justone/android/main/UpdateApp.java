package com.justone.android.main;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import com.justone.android.entity.UpdataInfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;

public class UpdateApp extends BaseActivity{
	
	
	/*  
	 * ��ȡ��ǰ����İ汾��   
	 */   
	private String getVersionName() throws Exception{   
	    //��ȡpackagemanager��ʵ��     
	    PackageManager packageManager = getPackageManager();   
	    //getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ    
	    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);   
	    return packInfo.versionName;    
	}  


	/*  
	 * ��pull�������������������ص�xml�ļ� (xml��װ�˰汾��)  
	 */   
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{   

	    UpdataInfo info = new UpdataInfo();//ʵ��    
	    info.set_version_id("1");
	    info.setVersion_name("0.0.1");
	    info.set_download_href("");
	    return info;   
	}  
}
