/**
 * ������ȡ���ݵĲ�����
 */
package com.justone.android.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.justone.android.main.JustOne;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * @author hobodong
 * 
 */
public class DataOp {
	
	private String getDataUrl = "http://haidaoteam.sinaapp.com/?datatype=json";
	
	private static final String TAG = "DataOp";
	// �������ع���ͼƬ��Map
	private Map<String, String> caches;
	// �������
	private List<Task> taskQueue;
	private boolean isRunning = false;
	public static String CACHE_DIR = "haidaoteam";
	
	//�����������ض����е�url
	private Set<String> urlSet;
	
	
	private AsynImageLoader asynImageLoader;
	
	public DataOp(AsynImageLoader asynImageLoader){
		
		this.asynImageLoader = asynImageLoader;
		// ��ʼ������
		caches = new HashMap<String,String>();
		urlSet = new HashSet<String>();
		taskQueue = new ArrayList<DataOp.Task>();
		// ����ͼƬ�����߳�
		isRunning = true;
		new Thread(runnable).start();
		
	}
	
	/**
	 * 
	 * @param imageView ��Ҫ�ӳټ���ͼƬ�Ķ���
	 * @param url ͼƬ��URL��ַ
	 * @param resId ͼƬ���ع�������ʾ��ͼƬ��Դ
	 * @throws JSONException 
	 * @throws InterruptedException 
	 */
	public String getDataAsyn(String url) throws JSONException, InterruptedException{
		
		//ac.setContentView(loadingView);
		
		/*Thread mThread = new Thread(new Runnable() {
	        @Override
	        public void run() {

	                try {
	                	
	                	
	                	String data = "{'home':[['new_tView','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['home_share_url','http://caodan.org/516-photo.html','shareUrl']," +
	            				"['fPage_tView','VOL.284','text'],['imageView1','http://photo.yupoo.com/lbhou/Dolq721U/medish.jpg','image']," +
	            				"['imageBelow_tView','����·��','text'],['imageBelow_tView1','xianglong/��ͼ','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
	            				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','�������Ŷӡ��ʣ�����û��ϲ�����ˣ�','text'],['question_answer_title','�����Ŷ�������','text'],['question_answer_content','���ɽ�°�����,����ǧ���޴���.��...��...��...��...���޿������õ�,��̥���Ǳ����.��...��...��...��...һ�����������,����������쳾,��...��...��...��...�����������㻯,�������������,��ѽ����Ӵ,��ѽ����Ӵ','text']" +
	            				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','������������','text'],['one_content_author','hobo','text'],['one_content_article','��˵������Щ��Ϸ��˾���������ˣ�������滹�����ſᡢ���εȡ���Ҫԭ����ȥ�����˾�Ƕ���׼����������У����Ӳ���ϲ�ȥ����Ļ���˾û�취�ˣ���Ū����С�İɡ���','text'],['one_content_author_novel','������ �ƻ�С˵��','text']]}";

	            		data = handleGet(getDataUrl);
	            		//Log.i("getData",new String(data.getBytes("utf-8")));
	            		//data = new String(data.getBytes("utf-8"),"GBK");
	            		//Log.i("getData",data);
	                	Thread.sleep(1000);
	                	ListViewAdapter list = (ListViewAdapter) homeListView.getAdapter();
	                	
	                	setDataToView(list.getDisplayView(),loadData("home",data,ac));
	                	targetView.invalidate();  

	                	

	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            
	        }
	    });
	    mThread.start();   */
		/*String data = "{'home':[['new_tView','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['home_share_url','http://caodan.org/516-photo.html','shareUrl']," +
				"['fPage_tView','VOL.284','text'],['imageView1','http://photo.yupoo.com/lbhou/Dolq721U/medish.jpg','image']," +
				"['imageBelow_tView','����·��','text'],['imageBelow_tView1','xianglong/��ͼ','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','�������Ŷӡ��ʣ�����û��ϲ�����ˣ�','text'],['question_answer_title','�����Ŷ�������','text'],['question_answer_content','���ɽ�°�����,����ǧ���޴���.��...��...��...��...���޿������õ�,��̥���Ǳ����.��...��...��...��...һ�����������,����������쳾,��...��...��...��...�����������㻯,�������������,��ѽ����Ӵ,��ѽ����Ӵ','text']" +
				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','������������','text'],['one_content_author','hobo','text'],['one_content_article','��˵������Щ��Ϸ��˾���������ˣ�������滹�����ſᡢ���εȡ���Ҫԭ����ȥ�����˾�Ƕ���׼����������У����Ӳ���ϲ�ȥ����Ļ���˾û�취�ˣ���Ū����С�İɡ���','text'],['one_content_author_novel','������ �ƻ�С˵��','text']]}";
		*/
		String data = handleGet(url);
		//Log.i("getDataAsyn",data+"xxxx");
		//Log.i("getDataAsynUrl",url);
	    //ac.setContentView(targetView);
		//System.out.println(url);
		//System.out.println(data);
		return data;
		
		//String data = loadDataAsyn(url,getImageCallback(ac,targetView));
		
	
		
		

	}
	
	//��data�ַ���ת���� Ƕ��map
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<HashMap<String, String>> resolveData(String data,String viewName) throws JSONException {
					JSONObject jsonOb = new JSONObject(data);
					ArrayList<HashMap<String, String>> tempResult = new ArrayList<HashMap<String,String>>();
					try {
						String viewData =  jsonOb.getString(viewName);
						JSONArray jsonArray = new JSONArray(viewData);
						

						for (int i = 0; i < jsonArray.length(); i++) {
							HashMap<String, String> tempJson = new HashMap<String, String>();
							JSONObject tempJsonObject = jsonArray.getJSONObject(i);
							tempJson.put("imageBelow_tView", tempJsonObject.getString("imageBelow_tView"));
							tempJson.put("imageView1", tempJsonObject.getString("imageView1"));
							tempJson.put("id", tempJsonObject.getString("id"));
							tempResult.add(tempJson);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Collections.sort(tempResult,new Comparator(){
			            

						@Override
						public int compare(Object arg0, Object arg1) {
							// TODO Auto-generated method stub
							return Integer.parseInt(((HashMap<String, String>)arg0).get("id")) >= Integer.parseInt(((HashMap<String, String>)arg0).get("id")) ? 0 : 1;
						}
			        });
					return tempResult;

		}
	
	
	public String loadDataAsyn(String path,ImageCallback callback){
		
		
		
		// �жϻ������Ƿ��Ѿ����ڸ�ͼƬ
		if(caches.containsKey(path)){
			// ȡ��������
			String data = caches.get(path);
			// ͨ�������ã���ȡͼƬ
			// �����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ���Map���Ƴ���
			if(data == null){
				caches.remove(path);
			}else{
				// ���ͼƬδ���ͷţ�ֱ�ӷ��ظ�ͼƬ
				//Log.i(TAG, "return image in cache" + path);
				return data;
			}
		}else{
			if(urlSet.contains(path))
				return null;
			else
				urlSet.add(path);
			// ��������в����ڸ�ͼƬ���򴴽�ͼƬ��������
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			//Log.i(TAG, "new Task ," + path);
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				// �����������ض���
				synchronized (runnable) {
					runnable.notify();
				}
			}
		}
		
		// ������û��ͼƬ�򷵻�null
		return null;
		
		
	}
	
	
	
	public String  getUpdateVersionInfo()
	{
		String updateUrl = "http://haidaoteam.sinaapp.com/?datatype=json&type=version";
		String data = handleGet(updateUrl);
		return data;
	}
	
	
	/*
	 * ����ID��ȡ ����Ҫjson���ݣ����ݲο��ĵ�
	 * https://tower.im/projects/d504e737f90f402c88df69dd10385e85/docs/e477b9b4689d4e7fa5a003c81e987b4e
	 */
	public String getData(String ID) throws UnsupportedEncodingException
	{
		
		String data = "{'home':[['new_tView','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['home_share_url','http://caodan.org/516-photo.html','shareUrl']," +
				"['fPage_tView','VOL.284','text'],['imageView1','http://photo.yupoo.com/lbhou/Dolq721U/medish.jpg','image']," +
				"['imageBelow_tView','����·��','text'],['imageBelow_tView1','xianglong/��ͼ','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','�������Ŷӡ��ʣ�����û��ϲ�����ˣ�','text'],['question_answer_title','�����Ŷ�������','text'],['question_answer_content','���ɽ�°�����,����ǧ���޴���.��...��...��...��...���޿������õ�,��̥���Ǳ����.��...��...��...��...һ�����������,����������쳾,��...��...��...��...�����������㻯,�������������,��ѽ����Ӵ,��ѽ����Ӵ','text']" +
				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','������������','text'],['one_content_author','hobo','text'],['one_content_article','��˵������Щ��Ϸ��˾���������ˣ�������滹�����ſᡢ���εȡ���Ҫԭ����ȥ�����˾�Ƕ���׼����������У����Ӳ���ϲ�ȥ����Ļ���˾û�취�ˣ���Ū����С�İɡ���','text'],['one_content_author_novel','������ �ƻ�С˵��','text']]}";
		/*return data;*/
		data = handleGet(this.getDataUrl);
		//Log.i("getData",new String(data.getBytes("utf-8")));
		//data = new String(data.getBytes("utf-8"),"GBK");
		Log.i("getData",data);

		return data;
	}
	
	
	//�������ݵĺ���
	private ArrayList<ArrayList<String>> loadData(String viewName,String data,Activity ac) throws JSONException {
				
				
				ArrayList<ArrayList<String>> tempResult = new ArrayList<ArrayList<String>>();
				try {
					String viewData =  new JSONObject(data).getString(viewName);
					JSONArray jsonArray = new JSONArray(viewData);
					

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONArray tempJson = (JSONArray) jsonArray.opt(i);
						ArrayList<String> tempArray = new ArrayList<String>();
						tempArray.add(String.valueOf(nameToIdMap(ac,tempJson
								.getString(0))));
						tempArray.add(String.valueOf(tempJson.getString(1)));
						tempArray.add(String.valueOf(tempJson.getString(2)));
						tempResult.add(tempArray);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				return tempResult;

		}
		
		private int nameToIdMap(Activity ac,String name)
		{
				return ac.getResources().getIdentifier(name, "id", ac.getPackageName());
		}
		
	 public String handleGet(String strUrl) {
			StringBuffer buffer = null;
			HttpGet request = new HttpGet(strUrl);//ʵ����һ��HttpGet����(ָ��URL)
			String result = "";
			DefaultHttpClient client = new DefaultHttpClient();//ʵ����һ���ͻ���
			try {
				HttpResponse response = client.execute(request);//���ÿͻ��˵�execute(request)ִ������
				//��ȡ�������˷��ص�״̬���Ƿ����200
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toString(response.getEntity());//����getEntity��ȡ����ֵ����ͨ��EntityUtils��ʵ��ת��String
				} else {
					result = response.getStatusLine().toString();
				}
			} catch (Exception e) { e.printStackTrace();}
			return result;
		}
	 
	 /*

      * post data to remote host

      */

	 public String postData(String email, String phone, String text){

         String destUrl="http://haidaoteam.sinaapp.com/?feedback=true";

         //instantiate HttpPost object from the url address

         HttpEntityEnclosingRequestBase httpRequest =new HttpPost(destUrl);

         //the post name and value must be used as NameValuePair

         List <NameValuePair> params=new ArrayList<NameValuePair>();

         params.add(new BasicNameValuePair("email",email));
         params.add(new BasicNameValuePair("phone",phone));
         params.add(new BasicNameValuePair("text",text));

         try{
		
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			
			//execute the post and get the response from servers
			
			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
			  
			if(httpResponse.getStatusLine().getStatusCode()==200){
			
				//get the result
				
				String strResult=EntityUtils.toString(httpResponse.getEntity());
				
				return strResult;
			
			}else{
			
				return httpResponse.getStatusLine().toString();
			
			}
		
		 }catch(Exception e){
			 System.out.println("error occurs");
		 }
         
         return "ERROR";

     }

	 
	 /**
		 * 
		 * @param imageView 
		 * @param resId ͼƬ�������ǰ��ʾ��ͼƬ��ԴID
		 * @return
		 */
		private ImageCallback getImageCallback( Activity ac,View v){
			return new ImageCallback() {
				
				@Override
				public void loadImage(String path, Bitmap bitmap) {
				/*	if(path.equals(imageView.getTag().toString())){
						imageView.setImageBitmap(bitmap);
					}else{
						imageView.setImageResource(resId);
					}*/
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
						
						//caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
						
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

