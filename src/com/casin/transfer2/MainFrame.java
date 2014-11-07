package com.casin.transfer2;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import com.casin.info.InfoStorer;
import com.casin.task.balance;
import com.casin.task.login;
import com.casin.task.pay;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainFrame extends Activity {
	
	private String url = "http://card.sdu.edu.cn";
	private String username = "";
	private String password = ""; //login password
	private String amt = "";   //the num of tranfer
	private String bankno = "";
	
	
	
	
	private Button button;	//the login button
	private Button button2;	//the transfer button
	private View vMain;	//the first view
	private View vTransfer;
	private ImageView view;	//the first check code image view
	private ImageView view2;	//the second check code image view
	private EditText checkcode;	//the check code edit text
	private EditText checkcode2;  //the second..you know  :)
	private Bitmap keypad;    //OMG, how can i recognize you -,-!
	private EditText etAccount;	//account name
	private EditText etPassword;	//password
	private EditText etAmtNumber;	//The bank no
	private RadioGroup radioGroup1;
	private RadioButton clickme;
	private TextView tvBalance;
	
	
	private Map<String , String > cookies= new HashMap<String , String>();
	private boolean hasInfo = true;
	//added temp things
	private String temp1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		
		vMain = getLayoutInflater().inflate(R.layout.main_view,null);
		setContentView(vMain);
		vTransfer= getLayoutInflater().inflate( R.layout.confirm, null );
		button = (Button)findViewById(R.id.button);		
		view = (ImageView)findViewById(R.id.imageView1);
		etAccount = (EditText)findViewById(R.id.etAccount);
		etPassword = (EditText)findViewById(R.id.etPassword);
		etAmtNumber = (EditText)findViewById(R.id.etAmtNumber);
		checkcode = (EditText)findViewById(R.id.etCheckCode);
		button.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				String temp = checkcode.getText().toString();
				if(temp == null || temp.length() == 0)
					Toast.makeText(MainFrame.this, "请输入完整的信息", Toast.LENGTH_LONG).show();
				else{
					if(checkInfo()){
               			LoginTask2 login2 = new LoginTask2();
						button.setEnabled(false);
						button.setText(R.string.wating);
						login2.execute(temp);
					}else{
						Toast.makeText(MainFrame.this, "请输入完整的信息", Toast.LENGTH_LONG).show();
					}
					
				}
				
			}
			
		});
		
		init1(true);
	}
	
	/**
	 * Get the check code
	 */
	@SuppressWarnings("unchecked")
	protected void init1(boolean first){
		waitAnim(view);
		if(first){
			InfoStorer info = new InfoStorer(this.getSharedPreferences("info", Activity.MODE_PRIVATE));
			etAmtNumber.setText(info.get(InfoStorer.sBankno));
			if(etAmtNumber.getText().toString().length() == 0 ){
				etAmtNumber.requestFocus();
				hasInfo = false;
			}
			etPassword.setText(info.get(InfoStorer.sPassword));
			if(etPassword.getText().toString().length() == 0 ){
				etPassword.requestFocus();
				hasInfo = false;
			}
			etAccount.setText(info.get(InfoStorer.sAccount));
			if(etAccount.getText().toString().length() == 0 ){
				etAccount.requestFocus();
				hasInfo = false;
			}
		}
		LoginTask1 login1 = new LoginTask1();
		login1.execute(cookies);
		button.setEnabled(false);
	}
	
	/**
	 * let the v play the loading animation
	 * @param v
	 */
	protected void waitAnim(ImageView v){
		v.setBackgroundResource(R.anim.loadanimation);
		final AnimationDrawable animationDrawable = (AnimationDrawable)v.getBackground();
		v.post(new Runnable() {
		    @Override
		        public void run()  {
		            animationDrawable.start();
		        }
		});
	}

	/**
	 * Initialize the widgets of the second view
	 */
	protected void init2(){
		
		button2 = (Button)findViewById(R.id.button1);
		tvBalance = (TextView)MainFrame.this.findViewById(R.id.balance);
		checkcode2 = (EditText)findViewById(R.id.etCheckCode1);
		view2 = (ImageView)findViewById(R.id.imageView2);
		radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
		clickme = (RadioButton)findViewById(R.id.radio3);
		clickme.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				clickme.setChecked(true);
				new NumberDialog(MainFrame.this,clickme).show();
			}
		});
		waitAnim(view2);
		button2.setOnClickListener(new Button.OnClickListener(){
		
			@Override
			public void onClick(View arg0) {
				
				String cc2 = checkcode2.getText().toString();
				if(cc2 == null || cc2.length() == 0);
				else{
					button2.setText(R.string.wating);
					button2.setEnabled(false);
					int buttonId = radioGroup1.getCheckedRadioButtonId();
					RadioButton choosen = (RadioButton)findViewById(buttonId);
					String tempAmt = choosen.getText().toString();
					amt = tempAmt.substring(0,tempAmt.length() - 1);
					transferTask2 transfer2 = new transferTask2();
					transfer2.execute(cc2);
				}
			}
			
		});
	}
	
	/**
	 * check the information needed for logging in.
	 * @return the result of checking
	 */
	public boolean checkInfo(){
		String checkTemp = etAccount.getText().toString();
		if(checkTemp == null || checkTemp.length() == 0)
			return false;
		else{
			username = checkTemp;
		}
		
		checkTemp = etPassword.getText().toString();
		if(checkTemp == null || checkTemp.length() == 0)
			return false;
		else{
			password = checkTemp;
		}
		
		checkTemp = etAmtNumber.getText().toString();
		if(checkTemp == null || checkTemp.length() == 0)
			return false;
		else{
			bankno = checkTemp;
		}
		
		return true;
	}
	
	/////////////////
	protected void setView(){
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)  
    {  
        //得到当前选中的MenuItem的ID,  
        int item_id = item.getItemId();  
  
        switch (item_id)  
        {  
            case R.id.about:  
            	new  AlertDialog.Builder(MainFrame.this)     
            	                .setTitle("联系作者" )  
            	                .setMessage("精简自\ncard.sdu.edu.cn\n如有疑问或者建议，请联系\ncasinww@163.com" )  
            	                .setPositiveButton("确定" ,  null )  
            	                .show();
                break;  
            case R.id.exit:  
                MainFrame.this.finish();
                break;  
        }  
        return true;  
    }  

	
	@Override
	protected void onDestroy(){
		new InfoStorer(this.getSharedPreferences("info", Activity.MODE_PRIVATE)).storeAll(etAccount, etPassword, etAmtNumber);
		super.onDestroy();
		
	}
	
////////////////////////////////////////////////////////////////
////////////////////TASK AREA///////////////////////////////////
////////////////////////////////////////////////////////////////
	private class LoginTask1 extends AsyncTask<Map<String , String>, Void, Bitmap>{

		/**
		 * Get the check code
		 * arg0[0] is the cookie reference
		 */
		protected Bitmap doInBackground(Map<String, String>... arg0) {

			Map<String , String > cookies = arg0[0];
			Bitmap img = null;
			login loginObj = new login(url , username , password);
			try {
				 img = loginObj.loginAction1(cookies);
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			return img;
		}

		protected void onPostExecute(Bitmap img) {
			if(hasInfo){
				checkcode.requestFocus();
			}
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setImageBitmap(img);
//			view.setBackground(new BitmapDrawable(getResources(),img));
			//view.setAnimation(R.anim.loadanimation);
	        button.setEnabled(true);
	    }  

	}

	
	/**
	 * To login with the check code
	 * @author Casin
	 *
	 */
	private class LoginTask2 extends AsyncTask<String,Void,Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			
			login loginObj = new login(url , username , password);
			try {
				String temp = loginObj.loginAction2(cookies, arg0[0]);
				if(temp.charAt(0) != 's')	//The result is success|False if login successfully
					return false;
				temp1 = temp;
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean arg){
			if(arg){
				checkcode.setText("");
				Toast.makeText(MainFrame.this, "登录成功", Toast.LENGTH_LONG).show();
				vMain.startAnimation(AnimationUtils.loadAnimation(MainFrame.this, android.R.anim.slide_out_right));
				vTransfer.setAnimation(AnimationUtils.loadAnimation(MainFrame.this, android.R.anim.slide_in_left));
				setContentView(vTransfer);
				//after logged in
				init2();
				transferTask1 transfer1 = new transferTask1();
				transfer1.execute(true);
				BalanceTask bt = new BalanceTask();
				bt.execute();
				button2.setEnabled(false);	
				button2.setText(R.string.wating);
			}else{
				Toast.makeText(MainFrame.this, temp1, Toast.LENGTH_LONG).show();
				checkcode.setText("");
				waitAnim(view);
				LoginTask1 login1 = new LoginTask1();
				login1.execute(cookies);
			}
			button.setEnabled(true);
			button.setText(R.string.confirm);
		}
	}
	
	/**
	 * get the keypad and the check code image for transferring
	 * @author Casin
	 *
	 */
	private class transferTask1 extends AsyncTask<Boolean,Void,Bitmap[]>{

		@Override
		protected Bitmap[] doInBackground(Boolean... arg0) {
		
			pay payObj = new pay(url,password,bankno);
			try {
				Bitmap[] imgs = payObj.transfer1(cookies,arg0[0]);
				return imgs;
			} catch (Exception e) {
			
				e.printStackTrace();
				return null;
			}
		}
		
		
		@Override
		protected void onPostExecute(Bitmap[] img){
			if(img != null){
				view2.setImageBitmap(img[1]);
				keypad = img[0] == null ? keypad : img[0];
				checkcode2.requestFocus();
				
			}
			button2.setEnabled(true);
			button2.setText(R.string.submit);
		}
	}
	
	
	private class transferTask2 extends AsyncTask<String,Void,Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			
				pay payObj = new pay(url,password,bankno);
				try {
					temp1 = payObj.transfer2(cookies, arg0[0], keypad, amt);
					return true;
				} catch (Exception e) {
					
					e.printStackTrace();
					return false;
				}
				
		}
		
		
		
		
		@Override
		protected void onPostExecute(Boolean flag){
			
			if(flag){
				Toast.makeText(MainFrame.this, temp1.subSequence(19, temp1.length()-1), Toast.LENGTH_LONG).show();
				checkcode2.setText("");
				waitAnim(view2);
				transferTask1 transfer1 = new transferTask1();
				transfer1.execute(false);
				BalanceTask bt = new BalanceTask();
				bt.execute();
			}else{
				checkcode2.setText("");
				Toast.makeText(MainFrame.this, "屏幕键盘识别错误", Toast.LENGTH_LONG).show();
				vTransfer.startAnimation(AnimationUtils.loadAnimation(MainFrame.this, android.R.anim.fade_out));
				vMain.startAnimation(AnimationUtils.loadAnimation(MainFrame.this, android.R.anim.fade_in));
				setContentView(vMain);
				init1(false);
			}
			
		}
	}
	
	
	private class BalanceTask extends AsyncTask<Void , Void , String>{

		@Override
		protected String doInBackground(Void... arg0) {
			return balance.getBalance(cookies);
		}
		
		
		@Override
		protected void onPostExecute(String result){
			tvBalance.setText(result);
		}
	}
	
}
