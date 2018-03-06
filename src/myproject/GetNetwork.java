package myproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetNetwork{
	public String getHtmlContent(URL url,String encode){
		StringBuffer content=new StringBuffer();
		int responseCode=-1;
		HttpURLConnection con=null;
		try{
			con=(HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// IE代理进行下载  
	        con.setConnectTimeout(60000);  
	        con.setReadTimeout(60000);  
	            // 获得网页返回信息码  
	        responseCode = con.getResponseCode();  
	        if (responseCode == -1) {  
	                System.out.println(url.toString() + " : connection is failure...");  
	                con.disconnect();  
	                return null;  
	            }  
	        if (responseCode >= 400) // 请求失败  
	            {  
	                System.out.println("请求失败:get response code: " + responseCode);  
	                con.disconnect();  
	                return null;  
	            }  
	        BufferedReader in=new BufferedReader(new InputStreamReader(con.getInputStream(),encode));
	        String getContent=null;
	        while((getContent=in.readLine())!=null)
	        {
	        	content.append(getContent);
	        }
	        
		}
		catch(IOException e)
		{
			e.printStackTrace();
			content=null;
			System.out.println("error:"+url.toString());
		}
		finally{
			con.disconnect();
		}
		return content.toString();
	}
	public String getHtmlContent(String url,String encode)
	{
		if(!url.toLowerCase().startsWith("http://")){
			url="http://"+url;
		}
		try{
			URL Turl=new URL(url);
			return getHtmlContent(Turl,encode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	String[] courses=new String[35];
	String[] time=new String[35];
	String[] place=new String[35];
	int[] whichWeekday=new int[35];
	String[] phase=new String[35];
	String college=null;
	String major=null;
	String stuId=null;
	String cardId=null;
	String name=null;
	void analysisContent(String str){
		int count=0;
		String[] temp1=str.split("上午");
		String[] temp2=temp1[1].split("下午");
		String[] temp3=temp2[1].split("晚上");
		String[] mor=temp2[0].split("&nbsp");
		for(int i=0;i<mor.length;i++){
			if(mor[i].contains("<br>")){
				String[] morFir=mor[i].split("rowspan");
				String[] last=morFir[1].split("r>");
				for(int j=0;j<last.length;j++){
					if(j!=0)
						last[j]=">"+last[j];
					String regex = ">(.*)<b";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(last[j]);
					while (matcher.find()) {
						if(j%3==0)
						{
							courses[count]=matcher.group(1);
							whichWeekday[count]=i+1;
							phase[count]="上午";
						}
						if(j%3==1)
							time[count]=matcher.group(1);
						if(j%3==2)
							place[count++]=matcher.group(1);
					}					
				}				
			}
		}
		String[] aft=temp3[0].split("&nbsp");
		for(int i=0;i<aft.length;i++){
			if(aft[i].contains("<br>")){
				String[] aftFir=aft[i].split("rowspan");
				String[] last=aftFir[1].split("r>");
				for(int j=0;j<last.length;j++){
					if(j!=0)
						last[j]=">"+last[j];
					String regex = ">(.*)<b";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(last[j]);
					while (matcher.find()) {
						if(j%3==0){
							courses[count]=matcher.group(1);
							whichWeekday[count]=i+1;
							phase[count]="下午";
							}
						if(j%3==1)
							time[count]=matcher.group(1);
						if(j%3==2)
							place[count++]=matcher.group(1);
					}			
				}				
			}
		}
		String[] eve=temp3[1].split("&nbsp");
		for(int i=0;i<eve.length;i++){
			if(eve[i].contains("<br>[")){
				String[] eveFir=eve[i].split("rowspan");
				String[] last=eveFir[1].split("r>");
				for(int j=0;j<last.length;j++){
					if(j!=0)
						last[j]=">"+last[j];
					String regex = ">(.*)<b";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(last[j]);
					while (matcher.find()) {
						if(j%3==0){
							courses[count]=matcher.group(1);
							whichWeekday[count]=i+1;
							phase[count]="晚上";	
							}
						if(j%3==1)
							time[count]=matcher.group(1);
						if(j%3==2)
							place[count++]=matcher.group(1);
					}			
				}				
			}
		}
		String regex = "院系:(.*)</td>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()){
			String[] temp=matcher.group(1).split("</td>");
			college=temp[0];
		}
		regex = "专业:(.*)</td>";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()){
			String[] temp=matcher.group(1).split("</td>");
			major=temp[0];
		}
		regex = "学号:(.*)</td>";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()){
			String[] temp=matcher.group(1).split("</td>");
			stuId=temp[0];
		}
		regex = "一卡通号:(.*)</td>";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()){
			String[] temp=matcher.group(1).split("</td>");
			cardId=temp[0];
		}
		regex = "姓名:(.*)</td>";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		while (matcher.find()){
			String[] temp=matcher.group(1).split("</td>");
			name=temp[0];
		}
//		for(int i=0;i<mor.length;i++){
//			int weekday=0;
//			if(mor[i].contains("rowspan"))
//			{
//				weekday++;
//				String[] morFir=mor[i].split("rowspan");
//				mor[i]=morFir[1];
//			}
//			else
//				mor[i]=">"+mor[i];
//			String regex = ">(.*)<b";
//		    Pattern pattern = Pattern.compile(regex);
//		    Matcher matcher = pattern.matcher(mor[i]);
//		    while (matcher.find()) {
//		    	courses[count++]=matcher.group(1);
//		    }
//		    if(count%3==1)
//		    	whichWeekday[count/3]=weekday;
//		}
//		String[] aft=temp3[0].split("r>");
//		for(int i=0;i<aft.length;i++){
//			int weekday=0;
//			if(aft[i].contains("rowspan"))
//			{
//				weekday++;
//				String[] aftFir=aft[i].split("rowspan");
//				aft[i]=aftFir[1];
//			}
//			else
//				aft[i]=">"+aft[i];
//			String regex = ">(.*)<b";
//		    Pattern pattern = Pattern.compile(regex);
//		    Matcher matcher = pattern.matcher(aft[i]);
//		    while (matcher.find()) {
//		        courses[count++]=matcher.group(1);
//		    }
//		    if(count%3==1)
//		    	whichWeekday[count/3]=weekday;
//		}
//		String[] eve=temp3[1].split("r>");
//		for(int i=0;i<eve.length;i++){
//			int weekday=1;
//			if(eve[i].contains("rowspan"))
//			{
//				weekday++;
//				String[] eveFir=eve[i].split("rowspan");
//				eve[i]=eveFir[2];
//			}
//			else
//				eve[i]=">"+eve[i];
//			String regex = ">(.*)<b";
//		    Pattern pattern = Pattern.compile(regex);
//		    Matcher matcher = pattern.matcher(eve[i]);
//		    while (matcher.find()) {
//		        courses[count++]=matcher.group(1);
//		    }
//		    if(count%3==1)
//		    	whichWeekday[count/3]=weekday;
//		}
//		for(int i=0;i<courses.length;i++){
//			if(courses[i]=="null")
//				courses[i]=null;
//		}
		/*char[] morning=temp2[0].toCharArray();
		char[] afternoon=temp3[0].toCharArray();
		char[] evening=temp3[1].toCharArray();
		for(int i=0;i<morning.length;i++)
		{
			if(morning[i]=='>')				
			{
				for(int j=i;j<morning.length;j++)
				{
					if(morning[j]=='<')
					{
						if(morning[j+1]=='b'&&morning[j+2]=='r'){
							if(count%3==0)
								courses[count]=temp2[0].substring(i,j+1);
							if(count%3==1)
								time[count]=temp2[0].substring(i, j+1);
							if(count%3==2)
								location[count++]=temp2[0].substring(i, j+1);
						}
						else
							break;
					}
				}
			}
		}
		for(int i=0;i<afternoon.length;i++)
		{
			if(afternoon[i]=='>')				
			{
				for(int j=i;j<afternoon.length;j++)
				{
					if(afternoon[j]=='<')
					{
						if(afternoon[j+1]=='b'&&afternoon[j+2]=='r'){
							if(count%3==0)
								courses[count]=temp3[0].substring(i,j+1);
							if(count%3==1)
								time[count]=temp3[0].substring(i, j+1);
							if(count%3==2)
								location[count++]=temp3[0].substring(i, j+1);
						}
						else
							break;
					}
				}
			}
		}
		for(int i=0;i<evening.length;i++)
		{
			if(evening[i]=='>')				
			{
				for(int j=i;j<evening.length;j++)
				{
					if(evening[j]=='<')
					{
						if(evening[j+1]=='b'&&evening[j+2]=='r')
						{
							if(count%3==0)
								courses[count]=temp3[1].substring(i,j+1);
							if(count%3==1)
								time[count]=temp3[1].substring(i, j+1);
							if(count%3==2)
								location[count++]=temp3[1].substring(i, j+1);
						}
						else
							break;
					}
				}
			}
		}*/
	}
	void print(){
		for(int i=0;i<courses.length;i++)
		{
			if(courses[i]!=null && courses[i].startsWith(" "))
				courses[i]=null;
			if(courses[i]!=null)
				System.out.println(courses[i]+" "+time[i]+" "+place[i]+" "+whichWeekday[i]+" "+phase[i]);
		}
	}
	public static void main(String[] args){
		//GetNetwork test=new GetNetwork();
		//String str=test.getHtmlContent("http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action?queryStudentId=213151024&queryAcademicYear=16-17-2", "utf-8");
		//test.analysisContent(str);
		//test.print();
	}
}