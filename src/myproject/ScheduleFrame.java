package myproject;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.text.SimpleDateFormat;
import java.util.Date;;

public class ScheduleFrame extends JFrame{

	void nextCourse()
	{
		queryFlag=true;
	}
	boolean tipFlag=false;
	boolean queryFlag=false;
	JLabel today;
	JComboBox jcb1,jcb2,jcb3;
	String[] t1={"2016","2017"};
	String[] t2={"1","2","3","4","5","6","7","8","9","10","11","12"};
	String[] t3={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	JTextArea[] course=new JTextArea[35];
	JTextArea[] tips=new JTextArea[7];
	JButton jb1=new JButton("查询");
	JButton jb2=new JButton("添加提醒事项:");
	JTextField tip=new JTextField(20);
	String date;
	JLabel[] weekDay=new JLabel[7];
	JTextArea info=new JTextArea();
	ScheduleFrame(){
		jb2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tipFlag=true;
			}
		});
		jb1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				nextCourse();
			}
			
		});
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		date=matter1.format(dt);
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		JPanel p4=new JPanel();
		JPanel p5=new JPanel();
		JLabel graph=new JLabel();
		//ImageIcon image1 = new ImageIcon("C:\\Users\\meng\\Documents\\java\\MyJavaProject\\CourseList\\src\\myproject\\1.jpg");
		ImageIcon image1 = new ImageIcon("1.jpg");
	    graph.setIcon(image1);
	    p5.add(graph);
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,	p1);
		add(BorderLayout.CENTER,p2);
		add(BorderLayout.WEST,p3);
		p1.setBackground(new Color(150,205,205));
		//p1.setBackground(new Color(255,246,243));
		p2.setBorder(new LineBorder(new Color(0,0,0)));
		p1.setLayout(new FlowLayout());
		p1.add(new JLabel("当前日期为："));
		today=new JLabel(date);
		p1.add(today);
		p1.add(new JLabel("       "));
		jcb1=new JComboBox(t1);
		jcb2=new JComboBox(t2);
		jcb3=new JComboBox(t3);
		p1.add(jcb1);
		p1.add(new JLabel("年"));
		p1.add(jcb2);
		p1.add(new JLabel("月"));
		p1.add(jcb3);
		p1.add(new JLabel("日"));
		p1.add(jb1);
		p1.add(jb2);
		p1.add(tip);
		//p1.add(jb1);
		// p1.add(jb2);
		p3.setLayout(new BorderLayout());
		p3.add(BorderLayout.NORTH,p4);
		p3.add(BorderLayout.SOUTH,p5);
		info.setEditable(false);
		info.setBackground(new Color(144,144,144));
		p4.setBackground(new Color(144,144,144));
		p3.setBackground(new Color(144,144,144));
		p4.add(info);
		p2.setLayout(new GridLayout(7,8,1,1));
		p2.add(new JLabel("Daytime"));
		weekDay[0]=new JLabel("Mon");
		weekDay[1]=new JLabel("Tues");
		weekDay[2]=new JLabel("Wed");
		weekDay[3]=new JLabel("Thur");
		weekDay[4]=new JLabel("Fri");
		weekDay[5]=new JLabel("Sat");
		weekDay[6]=new JLabel("Sun");
		p2.add(weekDay[0]);
		p2.add(weekDay[1]);
		p2.add(weekDay[2]);
		p2.add(weekDay[3]);
		p2.add(weekDay[4]);
		p2.add(weekDay[5]);
		p2.add(weekDay[6]);
		int count=0;
		for(int i=0;i<35;i++)
		{
			course[i]=new JTextArea("     ");
			course[i].setLineWrap(true);
			course[i].setEditable(false);
		}
		for(int i=0;i<40;i++)
		{
			if(i%8!=0)
				p2.add(course[count++]);
			else if(i==0){
				JLabel jb=new JLabel("上午");
				p2.add(jb);
			}
			else if(i==8)
				p2.add(new JLabel("上午 3~5"));
			else if(i==16)
				p2.add(new JLabel("下午 6~7"));
			else if(i==24)
				p2.add(new JLabel("下午 8~9"));
			else if(i==32)
				p2.add(new JLabel("晚上 11~13"));
		}
		p2.add(new JLabel("提醒"));
		for(int i=0;i<7;i++){
			tips[i]=new JTextArea("  ");
			tips[i].setEditable(false);
			p2.add(tips[i]);
		}
		setTitle("东南课程表");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setVisible(true);
	}
}
