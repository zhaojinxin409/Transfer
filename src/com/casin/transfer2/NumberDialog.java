package com.casin.transfer2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

public class NumberDialog extends Dialog{
	
	private RadioButton parent;
	private NumberPicker[] pickers = new NumberPicker[3];
	private TextView choosen_money;
	private Button enter;
	public NumberDialog(Context context , RadioButton parent) {
		super(context);
		this.parent = parent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker);
		choosen_money = (TextView)findViewById(R.id.choosen_money);
		choosen_money.setText("0Ԫ");
		pickers[0] = (NumberPicker)findViewById(R.id.numberPicker1);
		pickers[1] = (NumberPicker)findViewById(R.id.numberPicker2);
		pickers[2] = (NumberPicker)findViewById(R.id.numberPicker3);
		enter = (Button)findViewById(R.id.enter);
		enter.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
//				System.out.println(choosen_money.getText());
				parent.setText(choosen_money.getText());
				NumberDialog.this.dismiss();
			}
		});
		
		for(int i = 0 ; i < pickers.length ; i++){
			if( i == 0 ){
				pickers[i].setMaxValue(4);
			}else{
				pickers[i].setMaxValue(9);
			}
			pickers[i].setMinValue(0);
			pickers[i].setValue(0);
			pickers[i].setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
					choosen_money.setText(getNumber());
					
				}
			});
		}
	}
	
	public String getNumber(){
		int temp = pickers[0].getValue() * 100 + pickers[1].getValue() * 10 + pickers[2].getValue();		
		return temp + "Ԫ";
	}
	
	
	

}
