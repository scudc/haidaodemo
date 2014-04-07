/**
 * 用来读取数据的操作类
 */
package com.example.android.lifecycle.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.android.lifecycle.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author hobodong
 * 
 */
public class DataOp {
	
	private String getDataUrl = "http://haidaoteam.sinaapp.com/?datatype=json";
	public DataOp()
	{
		
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
		//data = handleGet(this.getDataUrl);
		//Log.i("getData",new String(data.getBytes("utf-8")));
		//data = new String(data.getBytes("utf-8"));
		Log.i("getData",data);
		return data;
	}
	
	/**
	   * 从服务器取图片
	   * @param url
	   * @return
	*/
	 public static Bitmap getHttpBitmap(String url) {
		 	//使用这个试试
		 	//to do  
		 	/*在第一次获取图片的时候，你应该进行步骤
		 	 * 1.先判断图片是否在本地缓存中
		 	 * 2.如果在的话，就读取本地缓存中的图片
		 	 * 3.如果不在的话，在通过url去获取图片，然后写入本地缓存，在返回图片的Bitmap内容
		 	 */
	         URL myFileUrl = null;
	         Bitmap bitmap = null;
	         try {
	              myFileUrl = new URL(url);
	         } catch (MalformedURLException e) {
	              e.printStackTrace();
	         }
	         try {
	              HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	              conn.setConnectTimeout(0);
	              conn.setDoInput(true);
	              conn.connect();
	              InputStream is = conn.getInputStream();
	              bitmap = BitmapFactory.decodeStream(is);
	              is.close();
	         } catch (IOException e) {
	              e.printStackTrace();
	         }
	         return bitmap;
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
			} catch (Exception e) { }
			return result;
		}
	   
}
