package com.casin.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.casin.info.Config;

public class ImageGetter{
	
	
	//Outer functions
	public static Bitmap getImage(String url , Map<String , String> cookies) throws Exception{
		ImageGetter obj = new ImageGetter();
		CheckCodeTask getter = obj.new CheckCodeTask();
		getter.cookies = cookies;
		getter.execute(url);
		Bitmap result = getter.get();
		return result;
	}
	
	private Bitmap getImageInner(String url , Map<String , String> cookies) throws IOException{
		Connection imgCon = Jsoup.connect(url);
		Response imgRps = imgCon.cookies(cookies).ignoreContentType(true).timeout(Config.timeout).execute();
		byte[] imgByte = imgRps.bodyAsBytes();
		Bitmap img = BitmapFactory.decodeStream(new ByteArrayInputStream(imgByte));
		return img;
	}
	



	/**
	 * Responsible for the check code download task
	 * @author Casin
	 *
	 */
	private class CheckCodeTask extends AsyncTask<String , Void , Bitmap>{
		Map<String , String> cookies = new HashMap<String , String>();

		@Override
		protected Bitmap doInBackground(String... arg0) {
			Bitmap result = null;
			try {
				result = getImageInner(arg0[0], cookies);
			} catch (IOException e) {
				e.printStackTrace(com.casin.info.Log.err);
			}
			return result;
		}
		
	}
}
