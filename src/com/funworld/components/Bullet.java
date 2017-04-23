package com.funworld.components;

import java.awt.Color;
import java.awt.Graphics;

import com.funworld.games.MainFrame;
import com.funworld.games.S;

public class Bullet extends Thread{
	
	int x,y;
	int direction;
	Environment env;
	boolean real = true;
	
	public Bullet(int x,int y,int d,Environment env){
		this.x = x;
		this.y = y;
		direction = d;
		this.env = env;
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x*S.bs, y*S.bs, S.bs, S.bs);
	}
	
	public void run(){
		while(!collided()){
			switch(direction){
			case Robot.FRONT:
				y-=1;
				break;
			case Robot.BACK:
				y+=1;
				break;
			case Robot.LEFT:
				x-=1;
				break;
			case Robot.RIGHT:
				x+=1;
				break;
			}
			env.mf.repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		env.bullets.remove(this);
		env.mf.repaint();
	}
	
	public boolean collided(){
		for(Robot r : env.robots){
			if(r.bx <= x && (r.bx + 3) > x && r.by <= y && (r.by + 3) > y){
				if(real)
					env.publishKill(r);
//					r.killed();
				return true;
			}
		}
		for(Wall w : env.walls){
			if(w.x==x && w.y==y)return true;
		}
		return false;
	}

}
