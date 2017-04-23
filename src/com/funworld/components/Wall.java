package com.funworld.components;

import java.awt.Color;
import java.awt.Graphics;

import com.funworld.games.S;

public class Wall {
	int x, y;

	public Wall(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public boolean collidesWithRobot(Robot r) {
		if (r.bx <= x && (r.bx + 3) > x && r.by <= y && (r.by + 3) > y)
			return true;
		return false;
	}

	public void paint(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(x * S.bs, y * S.bs, S.bs, S.bs);
		g.setColor(Color.black);
		g.drawRect(x * S.bs, y * S.bs, S.bs, S.bs);
	}
}
