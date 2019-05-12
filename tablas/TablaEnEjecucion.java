package com.elizalde.simulacion.tablas;

import java.util.Vector;
import java.util.concurrent.TimeUnit;



public class TablaEnEjecucion extends TablaAbstracta implements Runnable {

	private static String[] columnNames = { "Proceso en Ejecucion", };
	public Object[][] info;
	public Vector<Vector<String>> allInfo = new Vector<Vector<String>>();
	public Thread executionThread;
	private Vector<String> oneProcess = new Vector<String>();
	String result;
	public String realTimeLeft = "0";
	public boolean running = false;
	boolean processChanged = false;
	boolean processSet = false; 
	boolean operated = false;
	boolean error = false;
	boolean blocked = false;
	private boolean breaking = false;
	int counter = 0;
	int globalCounter;
	public int programCounter = 0;
	private int time = 0;
	int process;
	private int quantum;
	private int quantumCounter;

	public TablaEnEjecucion() {
		
		defaultTable.setColumnIdentifiers(columnNames);
	}
	public synchronized void startTimer() {
		running = true;
		executionThread = new Thread(this, "Execution");
		executionThread.start();

	}
	public void setTime(int time)
	{
		this.time = time;
	}
	public void setOneProcess(Vector<String> one)
	{
		oneProcess = new Vector<String>(one);
		//System.out.println("Process set : " + oneProcess);
		processSet = true;
		programCounter = 1;
		
	}
	public boolean getProcessSet()
	{
		return processSet;
	}
	public Vector<String> getOneProcess()
	{
		return oneProcess;
	}
	public void deleteOneProcess()
	{
		oneProcess.removeAllElements();
		oneProcess = new Vector<String>();
	}
	public void setQuantum(int quantum)
	{
		this.quantum = quantum;
	}
	public int getQuantum()
	{
		return quantumCounter;
	}
	
	@Override
	public void run() {
		while(true)
		{
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Outside");
			while (running) {
					setBreaking(false);
					//System.out.println("OnePressSet: " + processSet);
					
					try {
						TimeUnit.MILLISECONDS.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				//	System.out.println("One process size: " + oneProcess.size());
				//System.out.println("Process Set:"+processSet);
					//System.out.println("Before: if : " + oneProcess);
					if(processSet == true && oneProcess.size()> 0)
					{
					//	System.out.println("Before: " + oneProcess);
					//	System.out.println("Blocked: " + blocked);
						setValues();
						quantumCounter = 0;
						//System.out.println("Process: " + oneProcess);
						try {
							while (blocked == false && Integer.parseInt(defaultTable.getValueAt(4, 1).toString()) > 0) {
								TimeUnit.SECONDS.sleep(1);
								quantumCounter++;
								if (!operated) {
									doOperation();
									//setOperated();
								}
								int timeLeft = Integer.parseInt(defaultTable.getValueAt(4, 1).toString());
								int currentTime = Integer.parseInt(defaultTable.getValueAt(3, 1).toString());
								// This part simulates a copy of the time decreasing
								defaultTable.setValueAt(timeLeft - 1, 4, 1);
								defaultTable.setValueAt(currentTime + 1, 3, 1);
								defaultTable.setValueAt(quantumCounter, 5, 1);
								oneProcess.set(5, String.valueOf(currentTime+1));
								oneProcess.set(6, String.valueOf(timeLeft-1));	
								if(quantumCounter == quantum)
								{
									break;
								}
								
							}
							
							
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						processSet = false;
						if(blocked)
						{
							setBreaking(true);
							quantumCounter = 0;
							break;
						}
						quantumCounter = 0;
						if (error == true) {
							setResult("?");
							error = false;
						}
						
						
						if(blocked == false)
						{
							processSet = false;
							programCounter = 0;
							setProcessChanged();
						}
						
						//System.out.println("One processsize : " + oneProcess.size());
					}
				}
			}

	}

	public synchronized void stop() {
		running = false;
		oneProcess.removeAllElements();
		try {
			executionThread.join(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void Error() {
		defaultTable.setValueAt(0, 5, 1);
		error = true;
		defaultTable.setValueAt(0, 4, 1);
	}

	public void setBlocked( boolean change){
		blocked = change;
		defaultTable.setValueAt(0, 5, 1);
	}

	public void setAllinfo(Vector<Vector<String>> allInfo) {
		this.allInfo = allInfo;
	}

	public void setValues() {
		defaultTable.setValueAt(oneProcess.get(0), 0, 1);
		defaultTable.setValueAt(oneProcess.get(1) +
				oneProcess.get(2)+
				oneProcess.get(3), 1, 1);
		defaultTable.setValueAt(oneProcess.get(4), 2, 1);
		defaultTable.setValueAt(oneProcess.get(5), 3, 1);
		defaultTable.setValueAt(oneProcess.get(6), 4, 1);
	
		//System.out.println("Values Set: " + oneProcess);
	
		System.out.println("ID: " + oneProcess.get(0));
		int waitTime = time - Integer.parseInt(oneProcess.get(10));
		System.out.println("Wait Time: " + waitTime);
		int currentWaitTime = Integer.parseInt(oneProcess.get(9));
		System.out.println("Current Wait Time: " + currentWaitTime);
		waitTime += currentWaitTime;
		System.out.println("New WaitTime: " + waitTime);
		oneProcess.set(9, String.valueOf(waitTime));
		
		//oneProcess.set(10,Integer.toString(waitTime));
		//oneProcess.set(8, "0");
		
		if(oneProcess.get(11) == "0")
		{
		//	System.out.println("\n\n\nTime: " + time);
			int firstResponse = time-Integer.parseInt(oneProcess.get(7));
			
			oneProcess.set(8,String.valueOf(firstResponse));
			//System.out.println("One Process: " + oneProcess.get(8));
			//oneProcess.set(9, Integer.toString((time - Integer.parseInt(oneProcess.get(7)))));			
			oneProcess.set(11,"1");
		}
		
	}

	public void clearTable() {
		defaultTable.setValueAt(new String(""), 0, 1);
		defaultTable.setValueAt(new String(""), 1, 1);
		defaultTable.setValueAt(new String(""), 2, 1);
		defaultTable.setValueAt(new String(""), 3, 1);
		defaultTable.setValueAt(new String(""), 4, 1);
	}

	public boolean getProcessChanged() {
		return processChanged;
	}

	public void setProcessChanged() {
		processChanged = (!processChanged);
		setOperated();
	}

	public void doOperation() {
		double a = Integer.parseInt(oneProcess.get(1));
		double b = Integer.parseInt(oneProcess.get(3));
		String operator = new String(oneProcess.get(2));
		switch (operator) {
		case "+":
			result = Double.toString(a + b);
			break;
		case "-":
			result = Double.toString(a - b);
			break;
		case "*":
			result = Double.toString(a * b);
			break;
		case "/":
			result = Double.toString(a / b);
			break;
		case "%":
			result = Double.toString(a % b);
			break;
		case "^":
			result = Double.toString((int) Math.pow(a, b));
			break;
		default:
			result = "0";
			break;
		}
	}

	public boolean operated() {
		return operated;
	}

	public void setOperated() {
		operated = (!operated);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String newResult) {
		result = newResult;
	}
	public boolean isBreaking() {
		return breaking;
	}
	public void setBreaking(boolean breaking) {
		this.breaking = breaking;
	}
}
