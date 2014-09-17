/**
 * 用来读取数据的操作类
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
	// 缓存下载过的图片的Map
	private Map<String, String> caches;
	// 任务队列
	private List<Task> taskQueue;
	private boolean isRunning = false;
	public static String CACHE_DIR = "haidaoteam";
	
	//保存正在下载队列中的url
	private Set<String> urlSet;
	
	
	private AsynImageLoader asynImageLoader;
	
	public DataOp(AsynImageLoader asynImageLoader){
		
		this.asynImageLoader = asynImageLoader;
		// 初始化变量
		caches = new HashMap<String,String>();
		urlSet = new HashSet<String>();
		taskQueue = new ArrayList<DataOp.Task>();
		// 启动图片下载线程
		isRunning = true;
		new Thread(runnable).start();
		
	}
	
	/**
	 * 
	 * @param imageView 需要延迟加载图片的对象
	 * @param url 图片的URL地址
	 * @param resId 图片加载过程中显示的图片资源
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
	            				"['imageBelow_tView','我迷路了','text'],['imageBelow_tView1','xianglong/绘图','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
	            				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','【海盗团队】问：你有没有喜欢的人？','text'],['question_answer_title','海盗团队相龙答','text'],['question_answer_content','青城山下白素贞,洞中千年修此身.啊...啊...啊...啊...勤修苦练来得道,脱胎换骨变成人.啊...啊...啊...啊...一心向道无杂念,皈依三宝弃红尘,啊...啊...啊...啊...望求菩萨来点化,渡我素贞出凡尘,嗨呀嗨嗨哟,嗨呀嗨嗨哟','text']" +
	            				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','春风拂醉的晚上','text'],['one_content_author','hobo','text'],['one_content_article','听说近期有些游戏公司打算上市了，后面后面还跟着优酷、遨游等。主要原因是去年基金公司们都在准备阿里的上市，结果硬是上不去。搞的基金公司没办法了，先弄几个小的吧。。','text'],['one_content_author_novel','王相龙 科幻小说家','text']]}";

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
				"['imageBelow_tView','我迷路了','text'],['imageBelow_tView1','xianglong/绘图','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','【海盗团队】问：你有没有喜欢的人？','text'],['question_answer_title','海盗团队相龙答','text'],['question_answer_content','青城山下白素贞,洞中千年修此身.啊...啊...啊...啊...勤修苦练来得道,脱胎换骨变成人.啊...啊...啊...啊...一心向道无杂念,皈依三宝弃红尘,啊...啊...啊...啊...望求菩萨来点化,渡我素贞出凡尘,嗨呀嗨嗨哟,嗨呀嗨嗨哟','text']" +
				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','春风拂醉的晚上','text'],['one_content_author','hobo','text'],['one_content_article','听说近期有些游戏公司打算上市了，后面后面还跟着优酷、遨游等。主要原因是去年基金公司们都在准备阿里的上市，结果硬是上不去。搞的基金公司没办法了，先弄几个小的吧。。','text'],['one_content_author_novel','王相龙 科幻小说家','text']]}";
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
	
	//把data字符串转换成 嵌套map
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
		
		
		
		// 判断缓存中是否已经存在该图片
		if(caches.containsKey(path)){
			// 取出软引用
			String data = caches.get(path);
			// 通过软引用，获取图片
			// 如果该图片已经被释放，则将该path对应的键从Map中移除掉
			if(data == null){
				caches.remove(path);
			}else{
				// 如果图片未被释放，直接返回该图片
				//Log.i(TAG, "return image in cache" + path);
				return data;
			}
		}else{
			if(urlSet.contains(path))
				return null;
			else
				urlSet.add(path);
			// 如果缓存中不常在该图片，则创建图片下载任务
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			//Log.i(TAG, "new Task ," + path);
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				// 唤醒任务下载队列
				synchronized (runnable) {
					runnable.notify();
				}
			}
		}
		
		// 缓存中没有图片则返回null
		return null;
		
		
	}
	
	
	
	public String  getUpdateVersionInfo()
	{
		String updateUrl = "http://haidaoteam.sinaapp.com/?datatype=json&type=version";
		String data = handleGet(updateUrl);
		return data;
	}
	
	
	/*
	 * 传入ID获取 所需要json数据，数据参考文档
	 * https://tower.im/projects/d504e737f90f402c88df69dd10385e85/docs/e477b9b4689d4e7fa5a003c81e987b4e
	 */
	public String getData(String ID) throws UnsupportedEncodingException
	{
		
		String data = "{'home':[['new_tView','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['home_share_url','http://caodan.org/516-photo.html','shareUrl']," +
				"['fPage_tView','VOL.284','text'],['imageView1','http://photo.yupoo.com/lbhou/Dolq721U/medish.jpg','image']," +
				"['imageBelow_tView','我迷路了','text'],['imageBelow_tView1','xianglong/绘图','text'],['date_tView','30','text'],['date1_tView','Dec,2013','text']]," +
				"'QA':[['qa_share_url','http://caodan.org/516-photo.html','shareUrl'],['question_title','xxxxxooooxxxxxoooooxxxxxxooooo','text'],['question_publish_time','January 01,2014','text'],['question_content','【海盗团队】问：你有没有喜欢的人？','text'],['question_answer_title','海盗团队相龙答','text'],['question_answer_content','青城山下白素贞,洞中千年修此身.啊...啊...啊...啊...勤修苦练来得道,脱胎换骨变成人.啊...啊...啊...啊...一心向道无杂念,皈依三宝弃红尘,啊...啊...啊...啊...望求菩萨来点化,渡我素贞出凡尘,嗨呀嗨嗨哟,嗨呀嗨嗨哟','text']" +
				"],'list':[['list_share_url','http://caodan.org/516-photo.html','shareUrl'],['content_publish_time','October 27,2012','text'],['one_content_title','春风拂醉的晚上','text'],['one_content_author','hobo','text'],['one_content_article','听说近期有些游戏公司打算上市了，后面后面还跟着优酷、遨游等。主要原因是去年基金公司们都在准备阿里的上市，结果硬是上不去。搞的基金公司没办法了，先弄几个小的吧。。','text'],['one_content_author_novel','王相龙 科幻小说家','text']]}";
		/*return data;*/
		data = handleGet(this.getDataUrl);
		//Log.i("getData",new String(data.getBytes("utf-8")));
		//data = new String(data.getBytes("utf-8"),"GBK");
		Log.i("getData",data);

		return data;
	}
	
	
	//加载数据的函数
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
			HttpGet request = new HttpGet(strUrl);//实例化一个HttpGet请求(指定URL)
			String result = "";
			DefaultHttpClient client = new DefaultHttpClient();//实例化一个客户端
			try {
				HttpResponse response = client.execute(request);//调用客户端的execute(request)执行请求
				//获取服务器端返回的状态码是否等于200
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toString(response.getEntity());//调用getEntity获取返回值，需通过EntityUtils把实体转成String
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
		 * @param resId 图片加载完成前显示的图片资源ID
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
				// 子线程中返回的下载完成的任务
				Task task = (Task)msg.obj;
				// 调用callback对象的loadImage方法，并将图片路径和图片回传给adapter
				task.callback.loadImage(task.path, task.bitmap);
			}
			
		};
		
		private Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				while(isRunning){
					// 当队列中还有未处理的任务时，执行下载任务
					while(taskQueue.size() > 0){
						// 获取第一个任务，并将之从任务队列中删除
						Task task = taskQueue.remove(0);
						// 将下载的图片添加到缓存
						//task.bitmap = PicUtil.getbitmap(task.path);
						try {
							task.bitmap = PicUtil.getbitmapAndwrite(task.path);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
						
						if(handler != null){
							// 创建消息对象，并将完成的任务添加到消息对象中
							Message msg = handler.obtainMessage();
							msg.obj = task;
							// 发送消息回主线程
							handler.sendMessage(msg);
						}
					}
					
					//如果队列为空,则令线程等待
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
		
		//回调接口
		public interface ImageCallback{
			void loadImage(String path, Bitmap bitmap);
		}
		
		class Task{
			// 下载任务的下载路径
			String path;
			// 下载的图片
			Bitmap bitmap;
			// 回调对象
			ImageCallback callback;
			
			@Override
			public boolean equals(Object o) {
				Task task = (Task)o;
				return task.path.equals(path);
			}
		}
	}

