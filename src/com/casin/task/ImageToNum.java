package com.casin.task;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;


public class ImageToNum {
	private final static int[] posX = {4,33,62,92,122,152,182,211,241,270};
	private final static int posY = 3;
	private final static int gap = 125;
	/**
	 * Transfer the oriNum to the newNum according to the img
	 * And then reverse the newNum
	 * @param img
	 * @param oriNum
	 */
	public String ToNumAction(Bitmap img , String oriNum){
		//handle to 0 and 1
		img = preHandle(img);
		//split
		Bitmap[] array = splitImage(img);
		//recognize
		Map<String , String> map = new HashMap<String , String>(19);
		String[] recogStrings = new String[10];
		for(int i = 0 ; i < array.length ; i++){
			String tempString = getPicString(array[i]);
			recogStrings[i] = tempString;
			int recognize = getNum(tempString);
			if(recognize == 7){
				for(int pos = 0 ; pos < tempString.length() ; pos++){
					if(Integer.parseInt(tempString.charAt(pos)+"") > 6){
						recognize = 1;
					}
				}
			}
//			if(recognize == 6){
//				if(tempString.charAt(0) != '7'){
//					recognize = 5;
//				}
//			}
			if(recognize == 6){
				if(map.containsKey(recognize+"")){
					//There's two    6   and the bigger is 6
					String compare_str = recogStrings[Integer.parseInt(map.get(recognize+""))];
					if(compare_str.charAt(0) > tempString.charAt(0)){
						recognize = 5;
					}else{
						map.put(5+"", map.get(recognize+""));
					}
//					System.out.println("1111111111111111111111111111111111111");
				}
			}
			if(recognize == 5){
				if(map.containsKey(recognize+"")){
					//There's two    5 
					String compare_str = recogStrings[Integer.parseInt(map.get(recognize+""))];
					if(compare_str.charAt(0) < tempString.charAt(0)){
						recognize = 6;
					}else{
						map.put(6+"", map.get(recognize+""));
					}
//					System.out.println("222222222222222222222222222222222222222");
				}
			}
//			System.out.print(i + "---" + getPicString(array[i]) + "----");
//			System.out.println(recognize);
			map.put( recognize+"", i+"");
		}
//		for(int i = 0 ; i < array.length ; i++){
//			int recognize = getNum(i,array);
//			System.out.println("The number " + i + " is in picture " + recognize);
//			map.put(i+"", recognize+"");
//		}
		//transform
//		for(String key : map.keySet()){
//			System.out.println(key);
//		}
		if(map.keySet().size() != 10)
			return null;
		StringBuilder temp = new StringBuilder();
		for(int i = 0 ; i < oriNum.length() ; i++){
			//System.out.println(map.get(oriNum.charAt(i)+""));
			temp.append(map.get(oriNum.charAt(i)+""));
		}
		//reverse
		temp = temp.reverse();
		
		return temp.toString();
	}
	
	//预处理
	public Bitmap preHandle(Bitmap img){
		int height = img.getHeight();
		int width = img.getWidth();
		int[][] pixels = new int[width][height];
		int[][] as = new int[width][height];
		int[][] rs = new int[width][height];
		int[][] gs = new int[width][height];
		int[][] bs = new int[width][height];
		for(int x = 0; x < width ; x++){
			for(int y = 0 ; y < height ; y++){
				int pixel = img.getPixel(x, y);
				pixels[x][y] = pixel;
				as[x][y] = (pixel >> 24) & 0xff;
				rs[x][y] = (pixel >> 16) & 0xff;
				gs[x][y] = (pixel >> 8) & 0xff;
				bs[x][y] = pixel & 0xff;
			}
		}
		//将其进行二值处理
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int avg = (rs[x][y] + gs[x][y] + bs[x][y]) / 3;
				if (avg < gap) {
					img.setPixel(x, y, 0xff000000);
				} else {
					img.setPixel(x, y, -1);
				}
			}
		}
		return img;
	}
	
	/**
	 * Split the num pic into an image array
	 * @param img
	 * @return
	 */
	public Bitmap[] splitImage(Bitmap img){
		Bitmap[] array = new Bitmap[posX.length];
		for(int i = 0; i < posX.length ; i++){
			Bitmap temp = Bitmap.createBitmap(img,posX[i], posY, 26, 26);
			array[i] = temp;
		}
		return array;
	}
	
	/**
	 * Get the result from the signatures
	 */
	public int getNum(String pic) {
		int pos = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < PicString.pics.length; i++) {
			int temp = StringMinus.minus(pic, PicString.pics[i]);
			if (min > temp) {
				min = temp;
				pos = i;
			}
		}
		return pos;
	}
	
	public int getNum(int j , Bitmap[] array){
		//TODO 识别下1和7
		int pos = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < PicString.pics.length; i++) {
			int temp = StringMinus.EditDistance(getPicString(array[i]), PicString.pics[j]);
			if (min > temp) {
				min = temp;
				pos = i;
			}
		}
		return pos;
	}
	
	public String getPicString(Bitmap image){
		int width = image.getWidth();
		int height = image.getHeight();
		StringBuilder sb = new StringBuilder();
		for(int x = 0; x < width ; x++){
			int count = 0;
			for(int y = 0 ; y < height ; y++){
				if(image.getPixel(x, y) == 0xff000000)
					count++;
			}
			if(count > 10)
				count = 9;
			if(count != 0)
				sb.append(count);
		}
		for(int y = 0; y < height ; y++){
			int count = 0;
			for(int x = 0 ; x < width ; x++){
				if(image.getPixel(x, y) == 0xff000000)
					count++;
			}
			if(count > 10)
				count = 9;
			if(count != 0)
				sb.append(count);
		}
		return sb.toString();
	}
	
}
