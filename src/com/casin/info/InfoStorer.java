package com.casin.info;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;

public class InfoStorer {
	public static final String sAccount = "account";
	public static final String sPassword = "password";
	public static final String sBankno = "bankno";
	
	SharedPreferences userInfo;
	public InfoStorer(SharedPreferences userInfo){
		this.userInfo = userInfo;
	}
	public void store(String key , String value){
		Editor editor = userInfo.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String get(String key){
		return userInfo.getString(key, "");
	}
	
	
	public void storeAll(EditText account , EditText password , EditText bankno){
		String temp = account.getText().toString();
		if(temp != null && temp.length() != 0 ){
			store(sAccount,temp);
		}
		temp = password.getText().toString();
		if(temp != null && temp.length() != 0 ){
			store(sPassword,temp);
		}
		temp = bankno.getText().toString();
		if(temp != null && temp.length() != 0 ){
			store(sBankno,temp);
		}
	}
	
	
	
}
