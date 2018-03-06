package myproject;

import java.util.Calendar;

public class MyCalendar {
	
	public static int totalDay(int ye){
		int totalDay=0;
		for(int x=1800;x<ye;x++){
			if(x%400==0 || (x%4==0 && x%100!=0))
				totalDay+=366;
			else
				totalDay+=365;
		}
		return totalDay;
	}//输入年份1.1距离1800.1.1的总天数
	public static int totalDay(int y,int m,int d){
		int sum=totalDay(y);
		for(int i=1;i<m;i++){
			sum+=monthDays(y,i);
		}
		sum+=d;
		return sum;
	}
	public static int monthDays(int year,int mon){
		int sum=0;
		switch(mon){
		case 1:case 3:case 5:case 7:case 8:case 10:case 12:
			sum=31;
			break;
		case 4:case 6:case 9:case 11:
			sum=30;
			break;
		case 2:
			if(year%400==0 || (year%4==0 && year%100!=0))
				sum=29;
			else
				sum=28;
			break;
		}
		return sum;
	}//返回某年某月份天数
	public static int weekday(int year,int month,int day){
		int sum=0;
		for(int i=1;i<month;i++){
			sum+=monthDays(year,i);
		}
		sum+=day;
		int totalDay=sum+totalDay(year);
		int weekday=(totalDay+2)%7;
		if(weekday==0)
			weekday=7;
		return weekday;
	}//计算今天是星期几
}
