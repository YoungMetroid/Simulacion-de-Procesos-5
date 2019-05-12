package com.elizalde.simulacion.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timers extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel timerLabel = new JLabel("Tiempo Corrido: 0");
	public Thread timerThread;
	public Timer timer;
	public TimerTask tasknew;
	public int time;
	public boolean running = false;

	public Timers() {
		add(timerLabel);
		setVisible(true);
	}

	public synchronized void startTimer() {
		time = 0;
		running = true;
		timerThread = new Thread(this, "Timer");
		timerLabel.setText("Tiempo Corrido: 0");
		timerThread.start();
	}

	@Override
	public void run() {

		timerLabel.setText("Started");
		while (running) {
			try {
				TimeUnit.SECONDS.sleep(1);
				time++;
				timerLabel.setText("Tiempo Corrido: " + time);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}

	public synchronized void stop() {

		running = false;
		try {
			timerThread.join(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	public int getTime()	
	{
		return time;
	}
}
