package com.casin.task;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.casin.info.Config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class login {
	private String url;
	//part1
	private String signtype = "SynCard";
	private String username ;   //user specified
	private String password ;   //user specified
	private String isUsedKeyPad = "false";
	private String ssigntype = "signtype";
	private String susername = "username";
	private String spassword = "password";
	private String sisUsedKeyPad = "isUsedKeyPad";
	private String scheckcode = "checkcode";
	private float scale_ratio = 2.0f;
	public login(String url , String username , String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Perform the login action and return the cookies after loging in.
	 * @return
	 * @throws Exception
	 */
	public Bitmap loginAction1(Map<String,String> cookies) throws Exception{
		Connection con = Jsoup.connect(url);
		//Document doc = con.get();
		Response rps = con.timeout(Config.timeout).execute();
		
		Document doc = rps.parse();
		cookies.putAll(rps.cookies());
//		for(String key : cookies.keySet()){
//			System.out.println("cookies " + key + " : " + cookies.get(key));
//		}
		
		//Check Image
//		System.out.println(url);
		Element imgTag = doc.select("#imgCheckCode").get(0);
//		System.out.println(imgTag.attr("src"));
		String imgUrl = url + imgTag.attr("src");
//		System.out.println(imgUrl);
		Connection imgCon = Jsoup.connect(imgUrl);
		Response imgRps = imgCon.cookies(cookies).ignoreContentType(true).timeout(Config.timeout).execute();
		byte[] imgByte = imgRps.bodyAsBytes();
//		System.out.println("-----------------" + imgByte);
//		for(int i = 0; i < imgByte.length ; i++){
//			System.out.println(imgByte);
//		}
//		BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgByte));
		Bitmap img = BitmapFactory.decodeStream(new ByteArrayInputStream(imgByte));
		img = ImageUtils.ImageScale(img, scale_ratio);
		//the check code
		//ImageIO.write(img, "gif", new File("C:/hello.gif"));
		return img;
	}
		
	public String loginAction2(Map<String , String> cookies , String checkcode) throws Exception{
		//login
		String loginUrl = url + "/Account/MiniCheckIn";
		Connection login = Jsoup.connect(loginUrl);
		Map<String,String> info = new HashMap<String,String>();
		info.put(ssigntype, signtype);
		info.put(susername, username);
		info.put(spassword, password);
//		info.put(scheckcode, new Scanner(System.in).nextLine());
//		info.put(scheckcode, new ImageFrame().show(img));
		info.put(scheckcode, checkcode);
		info.put(sisUsedKeyPad, isUsedKeyPad);
		Response loginRps = login.timeout(Config.timeout_big).cookies(cookies).data(info).execute();
		cookies.putAll(loginRps.cookies());
		return loginRps.body();
//		for(String key : cookies.keySet()){
//			System.out.println("cookies " + key + " : " + cookies.get(key));
//		}
		
//		return cookies;
	}
	
}
