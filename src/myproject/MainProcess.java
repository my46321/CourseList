package myproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*; 

import com.myproject.MyAccess;


public class MainProcess{
	static Color[] c={new Color(219,112,147),new Color(123,104,238),new Color(0,206,209),new Color(255,255,0),new Color(255,99,71)};
	static Random r=new Random();
	static int[] num=new int[100];
	static int[] weekday=new int[100];
	static String[] str=new String[100];
	static int count=0;
	static String dt;
	static int firstDay;
	static int myWeek;
	static MyCalendar mc;
	static StartWindow sw;
	static ScheduleFrame sf;
	static MyAccess ma=new MyAccess();
	static ResultSet rs=null;
	static int[] week=new int[17];
	static int whichWeek(String str){
		week[0]=mc.totalDay(2016, 9, 19);
		for(int i=1;i<17;i++){
			week[i]=week[i-1]+7;
		}
		String[] temp=str.split("-");
		int sum=mc.totalDay(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
		//System.out.println(sum);
		for(int i=0;i<16;i++){
			if(sum>=week[i] && sum<week[i+1])
				return i+1;
		}
		return 0;
	}
	static int whichWeekday(String str){
		String[] temp=str.split("-");
		int sum=mc.weekday(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
		return sum;
		//System.out.println(sum);
	}
	static int  whichCourse(String phase,String time,int weekday){
		if(phase.equals("上午")){
			if(time.equals("1-2")){
				return 0+weekday-1;
			}
			if(time.equals("3-4") || time.equals("3-5")){
				return 7+weekday-1;
			}
		}
		if(phase.equals("下午")){
			if(time.equals("6-7")){
				return 14+weekday-1;
			}
			if(time.equals("8-9") || time.equals("8-10")){
				return 21+weekday-1;
			}
			if(time.equals("6-9")){
				return 14+weekday-1;
			}
		}
		if(phase.equals("晚上")){
			if(time.equals("11-13"))
				return 28+weekday-1;
		}
		return -2;
	}
	static void placeCourse(int week){
		int start,end=0;
		try {
			while(rs.next()){
				String[] temp=rs.getString("week").split("-");
				start=Integer.parseInt(temp[0]);
				end=Integer.parseInt(temp[1]);
				if(week>=start && week<=end){
					int colorNum=r.nextInt(5);
					String phase=rs.getString("phase");
					String time=rs.getString("time");
					int weekday=rs.getInt("weekday");
					String course=rs.getString("courses");
					String place=rs.getString("place");
					try{
						if(!course.startsWith("物理实验"))
						{
							sf.course[whichCourse(phase,time,weekday)].setText(course);
							sf.course[whichCourse(phase,time,weekday)].setToolTipText(place); 
							sf.course[whichCourse(phase,time,weekday)].setBackground(c[colorNum]);
						}
						else if((place.startsWith("(单)") && week%2==1) || (place.startsWith("(双)") && week%2==0)){
							sf.course[whichCourse(phase,time,weekday)].setText(course);
							sf.course[whichCourse(phase,time,weekday)].setToolTipText(place); 
							sf.course[whichCourse(phase,time,weekday)].setBackground(c[colorNum]);
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println(phase+time+weekday);
						break;
					}					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rs=ma.select();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static String getWeek(GetNetwork t,int num){
		String str="week"+t.time[num];
		String regex = "week(.*)周";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) 
			return matcher.group(1).substring(1, matcher.group(1).length());
		return null;		
	}
	static String getTime(GetNetwork t,int num){
		String str="time"+t.time[num];
		String regex = "](.*)节";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) 
			return matcher.group(1);
		return null;		
	}

	
	static Thread linkAccess=new Thread(new Runnable(){
		
		public void run(){
			try {
				ma.link();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sfStart.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0;i<sw.test.courses.length;i++){
				if(sw.test.whichWeekday[i]!=0)
					try {
						ma.add(sw.test.courses[i], getWeek(sw.test,i), sw.test.phase[i],getTime(sw.test,i), sw.test.whichWeekday[i],sw.test.place[i]);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			try {
				rs=ma.select();
				sf.weekDay[whichWeekday(dt)-1].setForeground(c[r.nextInt(5)]);
				placeCourse(whichWeek(dt)); 
				myWeek=whichWeek(dt);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});   
	static Thread sfStart=new Thread(new Runnable(){
		public void run(){
			sw=new StartWindow();
			while(sw.link==false){
				try {
					Thread.sleep(100);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sf=new ScheduleFrame();
			sf.info.append("姓名:"+sw.test.name+"\r\n");
			sf.info.append("院系:"+sw.test.college+"\r\n");
			sf.info.append("专业:"+sw.test.major+"\r\n");
			sf.info.append("学号:"+sw.test.stuId+"\r\n");
			sf.info.append("一卡通号:"+sw.test.cardId+"\r\n");
			dt=sf.date;
			String[] temp=dt.split("-");
			firstDay=mc.totalDay(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]))-whichWeekday(dt)+1;
		}
	});
	static Thread query=new Thread(new Runnable(){
		public void run(){
			try {
				sfStart.join();
				System.out.println(sf.queryFlag);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(sf.queryFlag==true){
					String date=sf.jcb1.getSelectedItem()+"-"+sf.jcb2.getSelectedItem()+"-"+sf.jcb3.getSelectedItem();
					String[] temp=date.split("-");
					firstDay=mc.totalDay(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]))-whichWeekday(date)+1;
					int i=whichWeekday(date);
					System.out.println(i);
					for(int count=0;count<7;count++){
						sf.weekDay[count].setForeground(new Color(0,0,0));
					}
					sf.weekDay[i-1].setForeground(c[r.nextInt(5)]);
					for(int count=0;count<35;count++){
						sf.course[count].setText("");
						sf.course[count].setBackground(new Color(255,255,255));
					}
					placeCourse(whichWeek(date));
					sf.queryFlag=false;
				}
			}
		}
	});
	static Thread timeThread=new Thread(new Runnable(){
		public void run(){
			try {
				linkAccess.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true){
				Date dt1=new Date();
				SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
				String date=matter1.format(dt1);
				if(!dt.equals(date)){
					dt=date;
					int i=whichWeekday(date);
					sf.weekDay[i-1].setForeground(c[r.nextInt(5)]);
					for(int count=0;count<35;count++){
						sf.course[count].setText("");
						sf.course[count].setBackground(new Color(255,255,255));
					}
					placeCourse(whichWeek(dt));
					sf.today.setText(dt);
				}
			}
		}
	});
	static Thread tipThread=new Thread(new Runnable(){
		public void run(){
			while(true){
				try {
					sfStart.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(sf.tipFlag==true){
					str[count]=sf.tip.getText();
					System.out.println(str[count]+1);
					String date=sf.jcb1.getSelectedItem()+"-"+sf.jcb2.getSelectedItem()+"-"+sf.jcb3.getSelectedItem();
					weekday[count]=whichWeekday(date);
					String[] temp=date.split("-");
					num[count++]=mc.totalDay(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
					sf.tipFlag=false;
				}
			}
		}
	});
	static Thread t=new Thread(new Runnable(){
		public void run(){
			while(true){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<num.length;i++){
					if(num[i]!=0){
						if(num[i]>=firstDay && num[i]<=firstDay+6){
							sf.tips[weekday[i]-1].setText(str[i]);
						}
						else
							sf.tips[weekday[i]-1].setText(""  );
					}
				}
			}
		}
	});
	public static void main(String[] args) throws SQLException{
		linkAccess.start();
		sfStart.start();
		query.start();
		timeThread.start();
		tipThread.start();
		t.start();
		//weekChange.start();
	}
}

