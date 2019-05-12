package com.elizalde.simulacion.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[120];

	public boolean p, c, e, w, u, b;
	public boolean up, down, left, right;

	public void update() {
		p = keys[KeyEvent.VK_P];
		c = keys[KeyEvent.VK_C];
		e = keys[KeyEvent.VK_E];
		w = keys[KeyEvent.VK_W];
		u = keys[KeyEvent.VK_U];
		b  =keys[KeyEvent.VK_B];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
