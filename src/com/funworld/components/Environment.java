package com.funworld.components;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.funworld.games.MainFrame;

public class Environment implements MqttCallback {
	public ArrayList<Robot> robots = new ArrayList<Robot>();
	ArrayList<Wall> walls = new ArrayList<Wall>();
	public Robot self;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public MainFrame mf;

	int w, h;
	public MqttClient client;
	

	public Environment(int w, int h,MainFrame mainFrame) {
		self = new Robot(this);
		this.w = w;
		this.h = h;
		mf = mainFrame;
		
		try {
			client = new MqttClient("tcp://mdakram28-pc:1883", Integer.toString(self.id));
			client.connect();
			client.setCallback(this);
			client.subscribe("MOVE");
			client.subscribe("KILL");
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
		(new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
						publishMove();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		})).start();
	}

	public void addWall(int x, int y) {
		Wall wall = new Wall(x, y);
		walls.add(wall);
	}

	public void respawnSelf() {
		self.reset();
	}
	
	public void publishKill(Robot r){
		try {
			client.publish("KILL", (r.id+"").getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publishBullet(Bullet b){
		try {
			client.publish("BULLET", (b.x+","+b.y+","+b.direction).getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void publishMove(){
		try {
			client.publish("MOVE", (self.id+","+self.bx+","+self.by+","+self.direction).getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void moveForward() {
		switch (self.direction) {
		case Robot.FRONT:
			self.by -= 1;
			break;
		case Robot.BACK:
			self.by += 1;
			break;
		case Robot.LEFT:
			self.bx -= 1;
			break;
		case Robot.RIGHT:
			self.bx += 1;
			break;
		}
	}
	
	public void moveBackward() {
		switch (self.direction) {
		case Robot.FRONT:
			self.by += 1;
			break;
		case Robot.BACK:
			self.by -= 1;
			break;
		case Robot.LEFT:
			self.bx += 1;
			break;
		case Robot.RIGHT:
			self.bx -= 1;
			break;
		}
	}
	public void rotateRight(){
		self.direction = (self.direction+1)%4;
	}
	public void rotateLeft(){
		self.direction = (self.direction-1);
		if(self.direction==-1){
			self.direction = 3;
		}
	}
	
	public void moveRobot(int id,int x,int y,int d){
		Robot robot = null;
		if(id == self.id)return;
		for(Robot r : robots){
			if(r.id == id){
				robot = r;
			}
		}
		if(robot == null){
			robot = new Robot(this,id,x,y,d);
			robots.add(robot);
		}else{
			robot.bx = x;
			robot.by = y;
			robot.direction = d;
		}
	}

	private void killRobot(int id) {
		Robot robot = null;
		if(id == self.id){
			self.reset();
//			publishMove();
			mf.repaint();
			System.exit(0);
			return;
//			JDialog frame = new JDialog(mf, "You were killed", true);
////			frame.getContentPane().add(panel);
//			frame.pack();
//			frame.setVisible(true);
//			return;
		}
		for(Robot r : robots){
			if(r.id == id){
				robot = r;
			}
		}
		if(robot == null){
		}else{
			robots.remove(robot);
			mf.repaint();
		}
	}

	private void createFakeBullet(int x, int y, int d) {
		Bullet b = new Bullet(x,y,d,this);
		b.real = false;
		bullets.add(b);
		b.start();
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public void paint(Graphics g) {
		for (Robot r : robots) {
			r.paint(g);
		}
		for (Wall wall : walls) {
			wall.paint(g);
		}
		for(Bullet bullet : bullets){
			bullet.paint(g);
		}
		self.paint(g);
	}

	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost");
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery complete");
	}

	public void messageArrived(String topic, MqttMessage payload) throws Exception {
		System.out.println(payload.toString());
		String[] data  = payload.toString().split(",");
		if(topic.equals("MOVE")){
			int id = Integer.parseInt(data[0]);
			int x = Integer.parseInt(data[1]);
			int y = Integer.parseInt(data[2]);
			int direction = Integer.parseInt(data[3]);
			moveRobot(id, x, y, direction);
		}else if(topic.equals("KILL")){
			int id = Integer.parseInt(data[0]);
			killRobot(id);
		}else if(topic.equals("BULLET")){
			int x = Integer.parseInt(data[0]);
			int y = Integer.parseInt(data[1]);
			int d = Integer.parseInt(data[2]);
			createFakeBullet(x,y,d);
		}
		mf.repaint();
	}

}
