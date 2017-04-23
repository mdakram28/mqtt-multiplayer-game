package com.funworld.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.funworld.games.S;

public class Robot {
	public static final int size_blocks = 3;
	public static final int FRONT = 0;
	public static final int RIGHT = 1;
	public static final int BACK = 2;
	public static final int LEFT = 3;

	public int id;
	int bx;
	int by;
	int direction = 0;
	Environment env;
	
	Bullet bullet = null;

	public Robot(Environment env) {
		id = 10000 + (int) (Math.random() * 100000);
		this.env = env;
		reset();
	}
	
	
	public Robot(Environment env, int id,int x,int y,int d){
		this.env = env;
		this.id = id;
		bx = x;
		by = y;
		direction = d;
	}

	public void reset() {
		do {
			System.out.println("hello");
			this.bx = (int) (Math.random() * (40));
			this.by = (int) (Math.random() * (40));
		} while (collidesWithWall());
//		this.bx = 10;
//		this.by = 10;
		System.out.println(this);
	}

	public String toString() {
		return "id=" + id + ",x=" + bx + ",y=" + by;
	}

	public boolean collidesWithWall() {
		ArrayList<Wall> walls = env.getWalls();
		for (Wall wall : walls) {
			if (wall.collidesWithRobot(this))
				return true;
		}
		return false;
	}

	public void Robot(int id, int bx, int by, int direction, Environment env) {
		this.id = id;
		this.bx = bx;
		this.by = by;
		this.direction = direction;
		this.env = env;
	}

	public void paint(Graphics g) {
//		System.out.println("direction = "+direction+" : "+(bx + 1) * S.bs +" , "+ by * S.bs);
		g.setColor(Color.blue);
		g.fillRect(bx * S.bs, by * S.bs, S.bs * 3, S.bs * 3);
		g.setColor(Color.white);
//		g.fillRect((bx + 1) * S.bs, by * S.bs, S.bs, S.bs);
//		g.fillRect(bx * S.bs, by * S.bs, S.bs * 2, S.bs * 2);
		switch (direction) {
		case BACK:
			g.fillRect((bx + 1) * S.bs, by * S.bs, S.bs, S.bs);
			g.setColor(Color.blue);
			g.fillRect((bx + 1) * S.bs, (by+3) * S.bs, S.bs, S.bs);
			break;
		case FRONT:
			g.fillRect((bx + 1) * S.bs, (by + 2) * S.bs, S.bs, S.bs);
			g.setColor(Color.blue);
			g.fillRect((bx + 1) * S.bs, (by-1) * S.bs, S.bs, S.bs);
			break;
		case RIGHT:
			g.fillRect((bx) * S.bs, (by + 1) * S.bs, S.bs, S.bs);
			g.setColor(Color.blue);
			g.fillRect((bx + 3) * S.bs, (by+1) * S.bs, S.bs, S.bs);
			break;
		case LEFT:
			g.fillRect((bx + 2) * S.bs, (by + 1) * S.bs, S.bs, S.bs);
			g.setColor(Color.blue);
			g.fillRect((bx - 1) * S.bs, (by+1) * S.bs, S.bs, S.bs);
			break;
		default:
			System.out.println("Invalid direction");
			System.exit(0);
		}
	}

	public void shootBullet() {
		int x=10,y=10;
		switch(direction){
		case FRONT:
			x=bx+1;y=by-1;
			break;
		case BACK:
			x=bx+1;y=by+3;
			break;
		case LEFT:
			x=bx-1;y=by+1;
			break;
		case RIGHT:
			x=bx+3;y=by+1;
			break;
		}
		Bullet b = new Bullet(x,y,direction,env);
		env.bullets.add(b);
		env.publishBullet(b);
		b.start();
	}
}
