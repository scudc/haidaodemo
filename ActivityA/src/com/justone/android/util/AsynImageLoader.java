package com.justone.android.util;



import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;



public class AsynImageLoader {
	private static final String TAG = "AsynImageLoader";
	// �������ع���ͼƬ��Map
	private Map<String, SoftReference<Bitmap>> caches;
	// �������
	private List<Task> taskQueue;
	private boolean isRunning = false;
	public static String CACHE_DIR = "haidaoteam";
	
	//�����������ض����е�url
	private Set<String> urlSet;
	
	public AsynImageLoader(){
		// ��ʼ������
		caches = new HashMap<String, SoftReference<Bitmap>>();
		urlSet = new HashSet<String>();
		taskQueue = new ArrayList<AsynImageLoader.Task>();
		// ����ͼƬ�����߳�
		isRunning = true;
		new Thread(runnable).start();
	}
	
	/**
	 * 
	 * @param imageView ��Ҫ�ӳټ���ͼƬ�Ķ���
	 * @param url ͼƬ��URL��ַ
	 * @param resId ͼƬ���ع�������ʾ��ͼƬ��Դ
	 */
	public void showImageAsyn(ImageView imageView, String url, View loadingView){
		imageView.setTag(url);
		Bitmap bitmap = loadImageAsyn(url, getImageCallback(imageView,loadingView));
		
		if(bitmap == null){
			Log.i("showImageAsyn","xzxxxxxx");
			loadingView.setVisibility(View.VISIBLE);
		}else{
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}
	}
	
	public Bitmap loadImageAsyn(String path, ImageCallback callback){
		// �жϻ������Ƿ��Ѿ����ڸ�ͼƬ
		if(caches.containsKey(path)){
			// ȡ��������
			SoftReference<Bitmap> rf = caches.get(path);
			// ͨ�������ã���ȡͼƬ
			Bitmap bitmap = rf.get();
			// �����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ���Map���Ƴ���
			if(bitmap == null){
				caches.remove(path);
				urlSet.remove(path);
			}else{
				// ���ͼƬδ���ͷţ�ֱ�ӷ��ظ�ͼƬ
				//Log.i(TAG, "return image in cache" + path);
				return bitmap;
			}
		}
		
			if(urlSet.contains(path))
			{
				Log.i("urlSet","contain");
				return null;
			}
			else
				urlSet.add(path);
			// ��������в����ڸ�ͼƬ���򴴽�ͼƬ��������
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			Log.i(TAG, "new Task ," + path);
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				// �����������ض���
				synchronized (runnable) {
					runnable.notify();
				}
			}
		
		Log.i("xxxxxxx","null");
		// ������û��ͼƬ�򷵻�null
		return null;
	}
	
	/**
	 * 
	 * @param imageView 
	 * @param resId ͼƬ�������ǰ��ʾ��ͼƬ��ԴID
	 * @return
	 */
	private ImageCallback getImageCallback(final ImageView imageView,final View loadingView ){
		return new ImageCallback() {
			
			@Override
			public void loadImage(String path, Bitmap bitmap) {
				if(path.equals(imageView.getTag().toString())){
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
				}else{
					//imageView.setImageResource(resId);
					Log.i("getImageCallback","getImageCallback");
					loadingView.setVisibility(View.VISIBLE);
					
				}
			}
		};
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// ���߳��з��ص�������ɵ�����
			Task task = (Task)msg.obj;
			// ����callback�����loadImage����������ͼƬ·����ͼƬ�ش���adapter
			task.callback.loadImage(task.path, task.bitmap);
		}
		
	};
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			while(isRunning){
				// �������л���δ���������ʱ��ִ����������
				while(taskQueue.size() > 0){
					// ��ȡ��һ�����񣬲���֮�����������ɾ��
					Task task = taskQueue.remove(0);
					// �����ص�ͼƬ��ӵ�����
					//task.bitmap = PicUtil.getbitmap(task.path);
					try {
						task.bitmap = PicUtil.getbitmapAndwrite(task.path);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
					
					if(handler != null){
						// ������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
						Message msg = handler.obtainMessage();
						msg.obj = task;
						// ������Ϣ�����߳�
						handler.sendMessage(msg);
					}
				}
				
				//�������Ϊ��,�����̵߳ȴ�
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	//�ص��ӿ�
	public interface ImageCallback{
		void loadImage(String path, Bitmap bitmap);
	}
	
	class Task{
		// �������������·��
		String path;
		// ���ص�ͼƬ
		Bitmap bitmap;
		// �ص�����
		ImageCallback callback;
		
		@Override
		public boolean equals(Object o) {
			Task task = (Task)o;
			return task.path.equals(path);
		}
	}
}
