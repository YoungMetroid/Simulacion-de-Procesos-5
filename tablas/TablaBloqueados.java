package com.elizalde.simulacion.tablas;

import java.util.Vector;
import java.util.concurrent.TimeUnit;



public class TablaBloqueados extends TablaAbstracta implements Runnable{

	
	private static String[] columnNames = {"ID", "Tiempo Transcurido en Bloqueado"};
	
	public Thread blockThread;
	private boolean running = false;
	private Vector<Vector<String>> allInfo = new Vector<Vector<String>>();
	private Vector<Vector<String>> copyInfo = new Vector<Vector<String>>();
	public int counter = 0;
	private boolean waitOver = false;
	public TablaBloqueados()
	{		
		defaultTable.setColumnIdentifiers(columnNames);
	}
	
	public void setAllInfo(Vector<String> oneProcess) {
		
		allInfo.addElement(new Vector<String>(oneProcess));
	//	System.out.println("ID: " + oneProcess.get(0));
	//	System.out.println("WaitTime in Blocked: " + oneProcess.get(9));
		int ten = Integer.parseInt(oneProcess.get(9)) + 10;
		//System.out.println("Wait Time: " + ten);
		oneProcess.set(9, String.valueOf(ten));
	//	System.out.println("New WaitTime in Blocked: " + oneProcess.get(9));
		//System.out.println("Wait Time: " + oneProcess.get(9));
		
		copyInfo.add(new Vector<String>(oneProcess));
		
		
		//System.out.println("En la tabla de Bloqueados:");
		/*for(int i = 0; i < allInfo.size();i++)
		{
			System.out.println(allInfo.get(i));
		}
		*/
		 
		
	}
	public synchronized void startTimer()
	{
		running = true;
		blockThread = new Thread(this,"Blocked");
		blockThread.start();
	}
	public void run()
	{
		//System.out.println("Blocked Table count" + defaultTable.getRowCount());
		while(running)
		{
			
			try{
				TimeUnit.MILLISECONDS.sleep(50);
				while(defaultTable.getRowCount() > 0)
				{
						TimeUnit.SECONDS.sleep(1);
						for(int i = 0; i < defaultTable.getRowCount();i++)
						{
							int timeLeft = Integer.parseInt(defaultTable.getValueAt(i, 1).toString());
							int currentWaitTime = Integer.parseInt(allInfo.get(i).get(9));
							currentWaitTime++;
							allInfo.get(i).set(9, String.valueOf(currentWaitTime));
							defaultTable.setValueAt(timeLeft-1, i, 1);
							if(timeLeft-1 <= 0)
							{
								
								defaultTable.removeRow(i);
								allInfo.remove(0);
								
								i--;
								waitOver = true;
							}
							
						}
						
						
				
				}
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setValues()
	{
		defaultTable.addRow(new Object[]{allInfo.get(allInfo.size()-1).get(0), "10"});
		counter++;
	}
	public Vector<String> getProcess()
	{
		return copyInfo.get(0);
	}
	public boolean getWaitOver()
	{
		return waitOver;
	}
	public void setWaitOver()
	{
		waitOver = (!waitOver);
	}
	public void deleteVector()
	{
		copyInfo.remove(0);
		counter--;
	}
	public Vector<Vector<String>> getAllProcess()
	{
		return allInfo;
	}
}
