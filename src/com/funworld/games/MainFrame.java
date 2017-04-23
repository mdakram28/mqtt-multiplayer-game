package com.funworld.games;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.funworld.components.Environment;
import com.funworld.components.Robot;

public class MainFrame extends JFrame implements KeyListener {
	static int width = 700;
	static int height=700;
	Environment env;
	DrawPanel panel;
	
	boolean move = false;
	boolean turnLeft = false;
	boolean turnRight = false;
	
	User user;
	
	public MainFrame(){
		this.setVisible(true);
		this.setSize(width, height);
		env = new Environment(width,height,this);
		panel = new DrawPanel(env);
		add(panel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addKeyListener(this);
	}
	
	public MainFrame(User user) {
		this();
		this.user = user;
	}

	public void start() {
		for(int i=0;i<41;i++){
			env.addWall(0, i);
			env.addWall(41-i, 0);
			env.addWall(i, 41);
			env.addWall(41, 41-i);
		}
		horizontalLine(10, 30, 10);
		horizontalLine(10, 30, 30);
		verticalLine(10, 30, 20);
		env.respawnSelf();
		panel.repaint();
//		System.out.println("Hey");
	}
	public void horizontalLine(int xs,int xe,int y){
		for(int i=xs;i<=xe;i++){
			env.addWall(i, y);
		}
	}
	
	public void verticalLine(int ys,int ye,int x){
		for(int i=ys;i<=ye;i++){
			env.addWall(x, i);
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			env.moveForward();
			if(env.self.collidesWithWall()){
				env.moveBackward();
			}else{
				env.publishMove();
			}
			break;
		case KeyEvent.VK_LEFT:
			env.rotateLeft();
			env.publishMove();
			break;
		case KeyEvent.VK_RIGHT:
			env.rotateRight();
			env.publishMove();
			break;
		case KeyEvent.VK_DOWN:
			env.rotateRight();
			env.rotateRight();
			env.publishMove();
			break;
		case KeyEvent.VK_SPACE:
			env.self.shootBullet();
		default:
			break;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {
		
	}
}

class DrawPanel extends JPanel{
	Environment env;
	public DrawPanel(Environment env) {
		this.env = env;
	}
	@Override
	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, MainFrame.width, MainFrame.height);
		env.paint(g);
	}
}
