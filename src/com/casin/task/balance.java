package com.casin.task;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

/**
 * 用于查询余额
 * @author Casin
 *
 */
public class balance {
	public static final String url = "http://card.sdu.edu.cn/CardManage/CardInfo/BasicInfo";
	public static String getBalance(Map<String , String> cookies){
		Connection con = Jsoup.connect(url);
		try {
			Response rsp = con.cookies(cookies).timeout(10000).execute();
			String temp = rsp.body();
			Elements spans = Jsoup.parseBodyFragment(temp).getElementsByTag("span");
			List<String> result = new ArrayList<String>();
			for(Element span : spans){
				if(span.text().contains("额：")){
					Element em = span.nextElementSibling();
					result.add(em.text());
				}
			}
			return result.get(0) + "  +  " + result.get(1);
		}catch(SocketTimeoutException ea){
			ea.printStackTrace();
		}catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}catch(Exception ea){
			ea.printStackTrace();
		}
		return "获取失败";
	}
}
