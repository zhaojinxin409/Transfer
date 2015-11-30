package com.casin.info;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.widget.Toast;

public class Log {
	private static final DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static File logFile;
	private static File errFile;
	private static Log log = new Log();
	public static BufferedWriter buf;
	public static PrintStream err;
	private Log(){
		
	}
	
	public static Log getLogger(){
		return log;
	}
	
	public static Log configLog(Context c){
		String path = null;
		File[] fs = null;
		try{
			fs = c.getExternalFilesDirs(null);
		}catch(NoSuchMethodError e){
			e.printStackTrace();
		}
		if(fs == null){
			path = "/mnt/transfer2/logs/";
		}else if(fs.length > 1){
			path = fs[1].getPath();
		}else{
			path = fs[0].getPath();
		}
		logFile = new File(path + "log.txt");
		errFile = new File(path + "err.txt");
		android.util.Log.d("msg", logFile.getAbsolutePath());
		android.util.Log.d("msg", errFile.getAbsolutePath());
		try {
			buf =  new BufferedWriter(new FileWriter(logFile, true));
			if (!errFile.exists())
			   {
			      try
			      {
			         errFile.createNewFile();
			      } 
			      catch (IOException e)
			      {
			         // TODO Auto-generated catch block
			         e.printStackTrace();
			      }
			   }
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			err = new PrintStream(errFile);
			err.append(format.format(new Date()));
			err.append("----------------------------------\n");
			err.flush();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return log;
	}
	
	public void appendLog(String text)
	{       
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
		  buf.append(format.format(new Date()) + "  ");
	      buf.append(text);
	      buf.newLine();
	      buf.flush();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}
	
	public static void destroy() throws Exception{
		buf.flush();
		buf.close();
		err.flush();
		err.close();
	}


}
