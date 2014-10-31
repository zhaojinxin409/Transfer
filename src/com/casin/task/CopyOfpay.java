package com.casin.task;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CopyOfpay {
	private String url;
	private String spwd = "pwd";
	private String pwd ;
	private String samt = "amt";
	private String sfrom = "sfrom";
	private String from = "Bank";
	private String sfromtype = "fromtype";
	private String fromtype = "Bank";
	private String sto = "to";
	private String to = "Card";
	private String stotype = "totype";
	private String totype = "Card";
	private String sbankno = "bankno";
	private String bankno = "6217856000009755223";
	private String sbankpwd = "bankpwd";
	private String bankpwd = "";
	private String scheckcode = "checkcode";
	
	
	public CopyOfpay(String url , String password){
		this.url = url;
		this.pwd = password;
	}

	public Bitmap[] transfer1(Map<String,String> cookies) throws Exception{
		//you have to get the tranfer page first
		Response tp = Jsoup.connect(url + "/CardManage/CardInfo/Transfer").cookies(cookies).ignoreContentType(true).execute();
		Document d_tp = tp.parse();
		Element tag_img = d_tp.select("#img_WSTransferCheckCode").get(0);
		//get the key pad
		Response kpRps = Jsoup.connect(url + "/Account/GetNumKeyPadImg").cookies(cookies).ignoreContentType(true).execute();
		byte[] kpByte = kpRps.bodyAsBytes();
		Bitmap kp = BitmapFactory.decodeStream(new ByteArrayInputStream(kpByte));
		System.out.println("The recognize result is :" + new ImageToNum().ToNumAction(kp, pwd));
//		ImageIO.write(kp, "jpeg", new File("C:/keypad.jpeg"));
		
		//get the checkcode
		String imgUrl = url + tag_img.attr("src");
		System.out.println(imgUrl);
		Connection imgCon = Jsoup.connect(imgUrl);
		Response imgRps = imgCon.cookies(cookies).ignoreContentType(true).execute();
		byte[] imgByte = imgRps.bodyAsBytes();
		Bitmap img = BitmapFactory.decodeStream(new ByteArrayInputStream(imgByte));
//		ImageIO.write(img, "gif", new File("C:/hello2.gif"));
		
		return new Bitmap[]{kp,img};
	}
	
	
	public void transfer2(Map<String,String> cookies , String checkcode ,Bitmap kp, String amt){
		Map<String,String> info = new HashMap<String,String>();
		//Scanner sc = new Scanner(System.in);
		//info.put(spwd,sc.nextLine());
		info.put(spwd, new ImageToNum().ToNumAction(kp, pwd));
//		info.put(scheckcode, sc.nextLine());
		info.put(scheckcode, checkcode);
		info.put(samt, amt);
		info.put(sfrom, from);
		info.put(sfromtype, fromtype);
		info.put(sto, to);
		info.put(stotype, totype);
		info.put(sbankno, bankno);
		info.put(sbankpwd, bankpwd);
		
		Connection trans = Jsoup.connect(url + "/CardManage/CardInfo/DoTransferPay");
		Response ret;
		try {
			ret = trans.cookies(cookies).data(info).ignoreContentType(true).execute();
			System.out.println(ret.body());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	
	
}
