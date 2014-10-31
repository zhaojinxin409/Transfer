package com.casin.task;

public class StringMinus {
	public static int minus(String a, String b){
		int length = a.length() > b.length() ? a.length() : b.length();
		int sum = 0;
		for(int i = 0 ; i < length ; i++){
			if(i < a.length() && i < b.length()){
				int r = Integer.parseInt(a.charAt(i)+"") - Integer.parseInt(b.charAt(i)+"");
				sum = sum + Math.abs(r);
			}else if(i >= a.length()){
				sum = sum + Integer.parseInt(b.charAt(i)+"");
			}else{
				sum = sum + Integer.parseInt(a.charAt(i)+"");
			}
			
		}
		
		return sum;
	}
	
	public static int EditDistance(String source, String target)  
    {  
        char[] s=source.toCharArray();  
        char[] t=target.toCharArray();  
        int slen=source.length();  
        int tlen=target.length();  
        int d[][]=new int[slen+1][tlen+1];  
        for(int i=0;i<=slen;i++)  
        {  
            d[i][0]=i;  
        }  
        for(int i=0;i<=tlen;i++)  
        {  
            d[0][i]=i;  
        }  
        for(int i=1;i<=slen;i++)  
        {  
            for(int j=1;j<=tlen;j++)  
            {  
                if(s[i-1]==t[j-1])  
                {  
                    d[i][j]=d[i-1][j-1];  
                }else{  
                    int insert=d[i][j-1]+1;  
                    int del=d[i-1][j]+1;  
                    int update=d[i-1][j-1]+1; 
                    d[i][j]=Math.min(insert, del)>Math.min(del, update)?Math.min(del, update):Math.min(insert, del);  
                }  
            }  
        }  
        return d[slen][tlen];  
    }
}
