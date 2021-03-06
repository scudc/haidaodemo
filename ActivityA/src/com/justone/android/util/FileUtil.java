package com.justone.android.util;



import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;



public class FileUtil {
	private static final String TAG = "FileUtil";
	
	public static File updateDir = null;
	public static File updateFile = null;

	/***
	 * 创建文件
	 */
	public static void createFile(String name,String downloadDir) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory()
					+ "/" + downloadDir);
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static File getCacheFile(String imageUri) throws Exception{
		File cacheFile = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			//File sdCardDir = Environment.getExternalStorageDirectory();
			String fileName = getFileName(imageUri);
			/*File dir = new File(sdCardDir.getCanonicalPath()+"/"
					+ AsynImageLoader.CACHE_DIR);*/
			File dir = new File("mnt/sdcard"+"/"
					+ AsynImageLoader.CACHE_DIR);
			if (!dir.exists()) {
				Log.i("getCacheFile",String.valueOf(dir.mkdirs()));
			}
			
			cacheFile = new File(dir, fileName);
			Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);
		}
		
		return cacheFile;
	}
	
	public static String getFileName(String path) throws Exception {
		//int index = path.lastIndexOf("/");
		
		//return path.substring(index + 1);
		return MD5Util.getMD5(path);
	}
}
