package com.funworld.games;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
	JTextField username;
	JTextField password;
	
	JButton button;
	
	JFrame frame;
	JLabel label;
	
	public LoginFrame(final CredentialsHandler handler){
		username = new JTextField(20);
		password = new JTextField(20);
		button = new JButton("Submit");
		label = new JLabel("");
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(new JLabel("Username"));
		add(username);
		add(new JLabel("Password"));
		add(password);
		add(button);
		label.setForeground(Color.red);
		add(label);
		
		setSize(300,200);
		
		frame = this;
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String u = username.getText();
				String p = password.getText();
				
				User user = new User(u,p);
				String res = user.verify();
				if(res.equals("")){
					handler.submit(user);
					frame.setVisible(false);
				}else{
					label.setText(res);
				}
			}
		});
		setVisible(true);
	}
}
