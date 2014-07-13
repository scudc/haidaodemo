package com.justone.android.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class PicUtil {
	private static final String TAG = "PicUtil";

	/**
	 * ����һ����������(URL)��ȡbitmapDrawableͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getfriendicon(URL imageUri) {

		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imageUri
					.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * ����һ����������(String)��ȡbitmapDrawableͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getcontentPic(String imageUri) {
		URL imgUrl = null;
		try {
			imgUrl = new URL(imageUri);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imgUrl.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * ����һ����������(URL)��ȡbitmapͼ��
	 * 
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getusericon(URL imageUri) {
		// ��ʾ�����ϵ�ͼƬ
		URL myFileUrl = imageUri;
		Bitmap bitmap = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
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

	/**
	 * ����һ����������(String)��ȡbitmapͼ��
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		// ��ʾ�����ϵ�ͼƬ
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.i(TAG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * ����ͼƬ ͬʱд�����ػ����ļ���
	 * 
	 * @param context
	 * @param imageUri
	 * @return
	 * @throws Exception 
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmapAndwrite(String imageUri) throws Exception {
		Bitmap bitmap = null;
		
		try {
			File cacheFile = FileUtil.getCacheFile(imageUri);
			if(!cacheFile.exists())
			{
				// ��ʾ�����ϵ�ͼƬ
				URL myFileUrl = new URL(imageUri);
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
				conn.setDoInput(true);
				conn.connect();

			
				InputStream is = conn.getInputStream();
			
			
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
			Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());

			byte[] buf = new byte[1024];
			int len = 0;
			// �������ϵ�ͼƬ�洢������
			while ((len = is.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}

			is.close();
			bos.close();
			}
			// �ӱ��ؼ���ͼƬ
			bitmap = BitmapFactory.decodeFile(cacheFile.getCanonicalPath());
			String name = MD5Util.getMD5(imageUri);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static boolean downpic(String picName, Bitmap bitmap) {
		boolean nowbol = false;
		try {
			File saveFile = new File("/mnt/sdcard/download/weibopic/" + picName
					+ ".png");
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}
			FileOutputStream saveFileOutputStream;
			saveFileOutputStream = new FileOutputStream(saveFile);
			nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					saveFileOutputStream);
			saveFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowbol;
	}

	public static void writeTofiles(Context context, Bitmap bitmap,
			String filename) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ļ�д�뻺��ϵͳ��
	 * 
	 * @param filename
	 * @param is
	 * @return
	 */
	public static String writefile(Context context, String filename,
			InputStream is) {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(is);
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context.getFilesDir() + "/" + filename + ".jpg";
	}

	// �Ŵ���СͼƬ
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// ��Drawableת��ΪBitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	// ���Բ��ͼƬ�ķ���
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if(bitmap == null){
			return null;
		}
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// ��ô���Ӱ��ͼƬ����
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	
	
	@SuppressLint("SdCardPath")
	public static String getSDPath()  
    {  
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  
        if(hasSDCard)  
        {  
            return Environment.getExternalStorageDirectory().toString() + "/haidaoteam";  
        }  
        else  
            return "/data/data/com.justone.android.main/haidaoteam";  
    }  
      
    public static Bitmap convertViewToBitmap(View view)  
    {  
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        //view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        view.buildDrawingCache();  
        Bitmap bitmap = view.getDrawingCache();  
  
        return bitmap;  
    }      
  
    // first SDCard is in the device, if yes, the pic will be stored in the SDCard, folder "HaHa_Picture"  
    // second if SDCard not exist, the picture will be stored in /data/data/HaHa_Picture  
    // file will be named by the customer  
    public static String saveImage(String strFileName,ImageView m_ivMain )  
    {  
        Bitmap bitmap = convertViewToBitmap(m_ivMain);  
        String strPath = getSDPath();  
        Log.i("saveImage",strPath);
        try  
        {  
            File destDir = new File(strPath);  
            if (!destDir.exists())  
            {  
                Log.d("MagicMirror", "Dir not exist create it " + strPath);  
                destDir.mkdirs();  
                Log.d("MagicMirror", "Make dir success: " + strPath);  
            }  
              
            File imageFile = new File(strPath + "/" + strFileName);  
            imageFile.createNewFile();  
            FileOutputStream fos = new FileOutputStream(imageFile);  
            bitmap.compress(CompressFormat.JPEG, 50, fos);  
            fos.flush();  
            fos.close(); 
            return strPath;
        }  
        catch (FileNotFoundException e)  
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return null;
    }  

}