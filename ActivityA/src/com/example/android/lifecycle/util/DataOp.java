/**
 * ������ȡ���ݵĲ�����
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
		//data = handleGet(this.getDataUrl);
		//Log.i("getData",new String(data.getBytes("utf-8")));
		//data = new String(data.getBytes("utf-8"));
		Log.i("getData",data);
		return data;
	}
	
	/**
	   * �ӷ�����ȡͼƬ
	   * @param url
	   * @return
	*/
	 public static Bitmap getHttpBitmap(String url) {
		 	//ʹ���������
		 	//to do  
		 	/*�ڵ�һ�λ�ȡͼƬ��ʱ����Ӧ�ý��в���
		 	 * 1.���ж�ͼƬ�Ƿ��ڱ��ػ�����
		 	 * 2.����ڵĻ����Ͷ�ȡ���ػ����е�ͼƬ
		 	 * 3.������ڵĻ�����ͨ��urlȥ��ȡͼƬ��Ȼ��д�뱾�ػ��棬�ڷ���ͼƬ��Bitmap����
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
			} catch (Exception e) { }
			return result;
		}
	   
}
