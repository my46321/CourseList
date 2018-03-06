package myproject;


import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*; 


public class  StartWindow extends JFrame{
	JTextField stuID=new JTextField(10);
	JLabel tip=new JLabel("请输入学号或一卡通号");
	JButton logOn=new JButton("查询");
	GetNetwork test;
	boolean link=false;
	StartWindow(){
		logOn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
					try {
						matchId();
						link=true;
						setVisible(false);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}		
		});
		setLayout(new BorderLayout());
		JPanel pLog=new JPanel();
		add(BorderLayout.CENTER,pLog);
		pLog.setLayout(new FlowLayout());
		pLog.add(tip);
		pLog.add(stuID);
		pLog.add(logOn);
		setTitle("东南课程表");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,100);
		setVisible(true);
	}
	void close(){
		this.close();
	}
	void matchId() throws SQLException{
		String url="http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action?queryStudentId="+stuID.getText()+"&queryAcademicYear=16-17-2";
		test=new GetNetwork();
		String strTest=test.getHtmlContent(url,"utf-8");
		try{
			test.analysisContent(strTest);
		}
		catch(Exception e)
		{
			System.out.println("连接失败");
			return;
		}
		//test.print();
		if(test.courses[0]==null){
			System.out.println("error id!");
			return;
		}
	}
}
