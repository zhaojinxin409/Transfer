package com.casin.task;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {
	
	//Scale the image by the ratio
	public static Bitmap ImageScale(Bitmap img , float ratio){
		Matrix matrix = new Matrix();
		matrix.postScale(ratio, ratio);
		Bitmap temp = Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,true);
		return temp;
	}
}
