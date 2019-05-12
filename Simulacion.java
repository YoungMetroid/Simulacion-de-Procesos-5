/**
 * 
 */
package com.elizalde.simulacion;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.elizalde.simulacion.input.Keyboard;
import com.elizalde.simulacion.tablas.TablaBloqueados;
import com.elizalde.simulacion.tablas.TablaDeResultados;
import com.elizalde.simulacion.tablas.TablaEnEjecucion;
import com.elizalde.simulacion.tablas.TablaLotes;
import com.elizalde.simulacion.tablas.TablaTiempos;
import com.elizalde.simulacion.timer.Timers;

/**
 * @author Elizalde Loera Felipe de Jesus
 *
 */

public class Simulacion extends JFrame implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	// Threads
	public Thread ResultsThread;
	boolean running = false;

	// Gui Components
	public JLabel lotesPendientes;
	public static JLabel Cola_de_Listos = new JLabel("Listos");
	public static JLabel Ejecucion = new JLabel("Ejecutando");
	public static JLabel Cola_de_Bloqueados = new JLabel("Bloqueados");
	public static JLabel Terminados = new JLabel("Terminados");
	public JButton buttonAdd;
	public JButton buttonProcesar;
	public JFrame process;

	// Gui Specs
	public static int width = 1200;
	public static int height = 600;
	public static int scale = 3;
	public static String title = "Simulacion";
	public boolean empty = false;

	// Classes
	public TablaLotes tablaLotes;
	public TablaEnEjecucion segundaTabla;
	public TablaDeResultados tercerTabla;
	public TablaBloqueados cuartaTabla;
	public TablaTiempos quintaTabla;
	public Timers timer;
	Generador generador;

	public boolean isPaused = false;
	public boolean isContinued = false;
	public boolean rowRemoved = false;
	Keyboard key;
	// Data Structures
	public Vector<Vector<String>> info = new Vector<Vector<String>>(), mainTable = new Vector<Vector<String>>();

	public Vector<String>currentBCP = new Vector<String>();
	public Vector<Vector<String>>finalBCP = new Vector<Vector<String>>();
	public Vector<String> ids = new Vector<String>();

	public int globalCounter = 0;
	public int lotCounter = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Simulacion simulacion = new Simulacion();
		simulacion.setResizable(true);
		simulacion.setTitle(Simulacion.title);
		simulacion.pack();
		simulacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulacion.setLocationRelativeTo(null);
		simulacion.setVisible(true);

	}

	public Simulacion() {
		key = new Keyboard();
		addKeyListener(key);
		this.setPreferredSize(new Dimension(width,height));
		int isEmpty = ids.capacity();
		if (isEmpty == 0) {
			System.out.println("Empty");
		}
		//setPreferredSize(new Dimension(width * scale, height*2));

		layout = new GridBagLayout();
		setLayout(layout);
		
		constraints = new GridBagConstraints();

		tablaLotes = new TablaLotes();

		segundaTabla = new TablaEnEjecucion();
		segundaTabla.defaultTable.addRow(new Object[] { "Programa" });
		segundaTabla.defaultTable.addRow(new Object[] { "Operacion" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Maximo Estimado" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Transcurrido" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Restante" });
		segundaTabla.defaultTable.addRow(new Object[] { "Quantum Transcurrido"});
		segundaTabla.defaultTable.addColumn("Datos");

		segundaTabla.tabla.setRowHeight(35);
		tercerTabla = new TablaDeResultados();
		cuartaTabla = new TablaBloqueados();
		quintaTabla = new TablaTiempos();
		lotesPendientes = new JLabel("Numero de procesos en la Cola de Nuevos: 0");
		buttonAdd = new JButton("Agregar Proceso");
		buttonProcesar = new JButton("Procesar");
		timer = new Timers();
		buttonProcesar.addActionListener(this);
		buttonAdd.addActionListener(this);

		// Put all your components in the JPanel
		constraints.fill = GridBagConstraints.PAGE_START;
		
		addComponent(lotesPendientes, 0, 0, 1, 1);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(timer, 0, 4, 1, 1);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(Cola_de_Listos, 1, 0, 1, 1);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(Ejecucion, 1, 2, 1, 1);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(Terminados, 1, 4, 1, 1);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(Cola_de_Bloqueados, 1, 6, 1, 1);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(new JPanel().add(tablaLotes.getScrollPane()), 2, 0, 2, 2);

		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(new JPanel().add(segundaTabla.getScrollPane()), 2, 1, 2, 2);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(new JPanel().add(tercerTabla.getScrollPane()), 2, 3, 2, 2);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(new JPanel().add(cuartaTabla.getScrollPane()), 2, 5, 2, 2);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(new JPanel().add(quintaTabla.getScrollPane()), 5, 0, 8, 2);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(buttonAdd, 7, 0, 4, 2);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(buttonProcesar, 7, 4, 4, 2);
		
		
	}

	public void addComponent(Component component, int row, int column, int width1, int height1) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width1;
		constraints.gridheight = height1;
		layout.setConstraints(component, constraints);
		add(component);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonAdd) {
			this.setVisible(false);
			tercerTabla.clearAll();
			generador = new Generador();
			
			generador.question();
			segundaTabla.setQuantum(generador.getQuantum());
			info = generador.getTable();
			
			copyTable();
		}
		if (e.getSource() == buttonProcesar) {
			if (mainTable.size() > 0) {
				System.out.println("Lote Counter: " + lotCounter);
				segundaTabla.setOneProcess(mainTable.get(0));
				
				for(int i= 0; i < 5; i++)
				{
					updateTable(i);
				}
				tablaLotes.defaultTable.removeRow(0);
				
				timer.startTimer();
				  ///.////////The problem is right here
				//cuartaTabla.setAllinfo(info.get(0));
				segundaTabla.startTimer();
				cuartaTabla.startTimer();
				startTimer();
				buttonProcesar.setEnabled(false);
				buttonAdd.setEnabled(false);
				
			}
		}

	}

	public void copyTable() {
			
		if(info.size() >=5)
		{
			updateNumberLotes(info.size()-5);
		}
		else
		{
			updateNumberLotes(0);
		}
		
		for(int i = 0; i < 5 && info.size() > 0;i++)
		{
			mainTable.addElement(info.get(0));
			System.out.println(mainTable.get(i));
			System.out.println(info.get(0));
			info.remove(0);

		}
			
		this.setVisible(true);
	}

	public void updateTable(int processCounter) {
		if(processCounter > -1)
		if (processCounter < info.size() || processCounter < mainTable.size()) {
			int sectionCounter = 0;
			while (sectionCounter < 1) {
				//System.out.println("MainTable Size: " + mainTable.size());
				//System.out.println(mainTable);
				//System.out.println("Process Counter: " + processCounter);
				if(mainTable.get(processCounter).get(11) == "0")
				{
					mainTable.get(processCounter).set(7, Integer.toString(timer.getTime()));
				}
				mainTable.get(processCounter).set(10, Integer.toString(timer.getTime()));
				tablaLotes.defaultTable.addRow(new Object[] { mainTable.get(processCounter).get(0),
						mainTable.get(processCounter).get(4), mainTable.get(processCounter).get(6)});
				globalCounter++;
				sectionCounter++;
			}
		}
		//System.out.println("Time of 6th element: " + info.get(6).get(7));
	}

	public void updateNumberLotes(int numeroLotes) {
		lotCounter = numeroLotes;
		lotesPendientes.setText("Numero de procesos en la Cola de Nuevos: " + numeroLotes);
	}
	

	public synchronized void startTimer() {
		running = true;
		ResultsThread = new Thread(this, "Results");
		ResultsThread.start();
	}

	public synchronized void stop() {
		running = false;
		info.removeAllElements();
		ids.removeAllElements();
		mainTable.removeAllElements();
		buttonProcesar.setEnabled(true);
		buttonAdd.setEnabled(true);
		try {
			timer.stop();
			segundaTabla.stop();
			ResultsThread.join(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() throws InterruptedException {
		key.update();
		if (key.p == true && isPaused == false) {
			isPaused = true;
			pauseProgram();
		}
		else if (key.c == true && isPaused == true) {
			isPaused = false;
			continueProgram();
		}
		else if(key.b == true && isPaused == false)
		{
			isPaused = true;
			pauseProgram();
			for(int i = 0 ;  i < quintaTabla.defaultTable.getRowCount(); i++)
			{
				quintaTabla.defaultTable.removeRow(0);
				i--;
			}
			
			for(int i = 0; i < info.size(); i++)
			{
				quintaTabla.defaultTable.addRow(new Object[]
						{
							info.get(i).get(0),
							"?",
							"?",
							"?",
							"?",
							"?",
							"?"
						});
			}
			
				if(segundaTabla.getProcessSet())
				{
					quintaTabla.defaultTable.addRow(new Object[]
							{
									
							segundaTabla.getOneProcess().get(0),
							segundaTabla.getOneProcess().get(7),
							"?",
							"?",
							segundaTabla.getOneProcess().get(8),
							segundaTabla.getOneProcess().get(9),
							"?"
							}
					);
					
				}
		
			for(int i = 1; i < mainTable.size();i++)
			{
				if(mainTable.get(i).get(11) == "1")
				quintaTabla.defaultTable.addRow(new Object[]
						{
								mainTable.get(i).get(0),
								mainTable.get(i).get(7),
								"?",
								"?",
								mainTable.get(i).get(8),
								mainTable.get(i).get(9),
								"?"
						});
				else 
					quintaTabla.defaultTable.addRow(new Object[]
							{
									mainTable.get(i).get(0),
									mainTable.get(i).get(7),
									"?",
									"?",
									
									"?",
									mainTable.get(i).get(9),
									"?"
							});
				
			}
			
			//Is Correct  
			for(int i = 0; i < finalBCP.size();i++)
			{
				
				quintaTabla.defaultTable.addRow(new Object[]
				{
						finalBCP.get(i).get(0),
						finalBCP.get(i).get(1),
						finalBCP.get(i).get(2),
						finalBCP.get(i).get(3),
						finalBCP.get(i).get(4),
						finalBCP.get(i).get(5),
						finalBCP.get(i).get(6)			
				});
				
			}
			for(int i = 0; i < cuartaTabla.getAllProcess().size();i++)
			{
				//System.out.println(cuartaTabla.getAllProcess().get(i));
				
				quintaTabla.defaultTable.addRow(new Object[]
				{			
						cuartaTabla.getAllProcess().get(i).get(0),
						cuartaTabla.getAllProcess().get(i).get(7),
						"?",
						"?",
						cuartaTabla.getAllProcess().get(i).get(8),
						cuartaTabla.getAllProcess().get(i).get(9),
						"?"
						
				});
				
			}
			
			
			System.out.println("Final BCP size: " + finalBCP.size());
			System.out.println("MainTable size: " + mainTable.size());
			System.out.println("Info size: " + info.size());
			System.out.println("Blocked Processes size: " + cuartaTabla.getAllProcess().size());
		}
		else if (key.w == true && isPaused == false) {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			segundaTabla.Error();
		} 
		else if(key.u == true)
		{
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if	(mainTable.size()+ 
					cuartaTabla.counter < 5  && segundaTabla.getProcessSet() == true
				)
				{
					mainTable.add(generador.getGenerateOne());
					updateTable(mainTable.size()-1);
				}
			else if (mainTable.size()==0  && segundaTabla.getProcessSet() == false && cuartaTabla.counter < 5 && info.size() == 0)
			{
				Vector <String> temp = generador.getGenerateOne();
				temp.set(7, Integer.toString(timer.getTime()));
				mainTable.addElement(temp);
				mainTable.get(0).set(10, String.valueOf(timer.getTime()));
				segundaTabla.setOneProcess(mainTable.get(0));
				
				segundaTabla.setBlocked(false);
				segundaTabla.running = true;
				
			}
			else {
					info.add(generador.getGenerateOne());
					updateNumberLotes(lotCounter+1);
				}
		}
		else if (key.e == true && info.size() >= 0
				&& tablaLotes.defaultTable.getRowCount() +
				segundaTabla.programCounter +  
				cuartaTabla.counter <=5  && segundaTabla.getProcessSet() == true) {
			
			if( segundaTabla.getProcessSet() == true &&
					Integer.parseInt(segundaTabla.getOneProcess().get(6))> 1 &&
					segundaTabla.getQuantum() < generador.getQuantum()-1)
			{
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				
				segundaTabla.running = false;
				segundaTabla.setBlocked(true);
				
				if(tablaLotes.defaultTable.getRowCount() > 0)
				{
					tablaLotes.defaultTable.removeRow(0);
				}
				
				System.out.println("Before Quantum Counter: " + segundaTabla.getQuantum());
				while(!segundaTabla.isBreaking())
				{
					try {
						TimeUnit.MILLISECONDS.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}
				//System.out.println("One Process:" + segundaTabla.getOneProcess());
				System.out.println("Quantum Counter: " + segundaTabla.getQuantum());
				if(segundaTabla.getQuantum() < generador.getQuantum())
				{
					cuartaTabla.setAllInfo(segundaTabla.getOneProcess());
				
					cuartaTabla.setValues();
					if(!mainTable.isEmpty())
					mainTable.remove(0);
					
					segundaTabla.deleteOneProcess();
					segundaTabla.clearTable();
					
					if(tablaLotes.defaultTable.getRowCount() + segundaTabla.programCounter +  cuartaTabla.counter <=5)
						if(mainTable.size() > 0)
						{System.out.println("Original Value:" + mainTable.get(0));
							segundaTabla.setOneProcess(mainTable.get(0));
							segundaTabla.setBlocked(false);
							segundaTabla.running = true;
						}	
				}
				else{
					segundaTabla.programCounter = 0;
					segundaTabla.setProcessChanged();
					segundaTabla.setBlocked(false);
					segundaTabla.running = true;
					
				}
				
			}
			else
			{
				System.out.println("Here");
			}
			
		}
	}

	@SuppressWarnings("deprecation")
	public void pauseProgram() {
		timer.timerThread.suspend();
		segundaTabla.executionThread.suspend();
		cuartaTabla.blockThread.suspend();
	}

	@SuppressWarnings("deprecation")
	public void continueProgram() {
		timer.timerThread.resume();
		segundaTabla.executionThread.resume();
		cuartaTabla.blockThread.resume();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		requestFocus();
		while (running) {
			try {
				if(cuartaTabla.getWaitOver())
				{
					
					mainTable.addElement(cuartaTabla.getProcess());
					cuartaTabla.deleteVector();
					
					if(segundaTabla.getProcessSet() == false)
					{
						mainTable.get(0).set(10, String.valueOf(timer.getTime()));
						segundaTabla.setOneProcess(mainTable.get(0));	
						
						//mainTable.remove(0);
					}
					else
					updateTable(mainTable.size()-1);
					
					segundaTabla.setBlocked(false);
					segundaTabla.running = true;
					cuartaTabla.setWaitOver();
				}
				update();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				// TimeUnit.SECONDS.sleep(0.5);
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			segundaTabla.setTime(timer.getTime());
			
			if (segundaTabla.getProcessChanged()) {
				
				segundaTabla.running = true;
				//int tableSize = tablaLotes.defaultTable.getRowCount();
			/*	for(int i = 0;i < tablaLotes.defaultTable.getRowCount();i++)
				{
					tablaLotes.defaultTable.removeRow(0);
				}
				
				
				for(int j = 1; j < tableSize && j < mainTable.size() && j < 5;j++)
				{
					tablaLotes.defaultTable.addRow(new Object[]
							{mainTable.get(j).get(0),
								mainTable.get(j).get(4),
								mainTable.get(j).get(6)
							});
				}
				
				*/
				int timeLeft  = Integer.parseInt(segundaTabla.getOneProcess().get(6));
				System.out.println("TimeLeft: " + timeLeft);
				if(timeLeft <= 0)
				{
					
					
					tercerTabla.defaultTable.addRow(new Object[] {
							mainTable.get(0).get(0), mainTable.get(0).get(1)
									+ mainTable.get(0).get(2) + mainTable.get(0).get(3),
							segundaTabla.getResult() });
					
					int finishTime = timer.getTime();
					mainTable.set(0, segundaTabla.getOneProcess());
					
					/*quintaTabla.defaultTable.addRow(new Object[]{
							mainTable.get(0).get(0),
							mainTable.get(0).get(7),
							finishTime,
							finishTime-Integer.parseInt(mainTable.get(0).get(7)),
							mainTable.get(0).get(8),
							mainTable.get(0).get(9),
							mainTable.get(0).get(5)});
							*/
					
					Vector<String> processFinished = new Vector<String>();
					processFinished.addElement(mainTable.get(0).get(0));
					processFinished.addElement(mainTable.get(0).get(7));
					processFinished.addElement(String.valueOf(finishTime));
					processFinished.addElement(String.valueOf(finishTime-Integer.parseInt(mainTable.get(0).get(7))));
					processFinished.addElement(mainTable.get(0).get(8));
						int correctWaitTime = finishTime-Integer.parseInt(mainTable.get(0).get(7))-Integer.parseInt(mainTable.get(0).get(5));
					processFinished.addElement(String.valueOf(correctWaitTime));
					//processFinished.addElement(mainTable.get(0).get(9));
					processFinished.addElement(mainTable.get(0).get(5));
					finalBCP.addElement(processFinished);
					//quintaTabla.defaultTable.addRow(processFinished);
					
					mainTable.remove(0);
					segundaTabla.setProcessChanged();
					if(info.size() > 0)
					{
						mainTable.addElement(info.get(0));
						info.remove(0);
						segundaTabla.setOneProcess(mainTable.get(0));
					}
					else if(mainTable.size() > 0)
					{
						segundaTabla.setOneProcess(mainTable.get(0));
					}
					
					//System.out.println("Info: " + info);
					if(tablaLotes.defaultTable.getRowCount() > 0)
						tablaLotes.defaultTable.removeRow(0);
					
					/*System.out.println("Counter: " + (tablaLotes.defaultTable.getRowCount() +
					segundaTabla.programCounter +  
					cuartaTabla.counter));
					*/
					if(tablaLotes.defaultTable.getRowCount() +
							segundaTabla.programCounter +  
							cuartaTabla.counter <=5  && mainTable.size() > 0 && lotCounter > 0)
					{
	//					System.out.println("Main Table Size: " + mainTable.size()); 
						
						
						updateTable(mainTable.size()-1);
						//System.out.println("Setting up table");
						//System.out.println("MainTable: " + mainTable);
						//System.out.println("Counter: " + (tablaLotes.defaultTable.getRowCount() +
	//						segundaTabla.programCounter +  
		//					cuartaTabla.counter));
					}
					if(segundaTabla.getProcessSet() == true)
					{
						if(tablaLotes.defaultTable.getRowCount() > 0)
						if(segundaTabla.getProcessSet() == true && tablaLotes.defaultTable.getValueAt(0, 0) == segundaTabla.getOneProcess().get(0))
						{
							tablaLotes.defaultTable.removeRow(0);
						}
					}
					
					
						if(lotCounter > 0 )
						updateNumberLotes(lotCounter - 1);
						
					if (info.size()==0 && mainTable.size() == 0 && cuartaTabla.defaultTable.getRowCount() == 0) {
						System.out.println("Stopped all processes");
						for(int i = 0 ;  i < quintaTabla.defaultTable.getRowCount(); i++)
						{
							quintaTabla.defaultTable.removeRow(0);
							i--;
						}
						for(int i = 0; i < finalBCP.size();i++)
						{
							quintaTabla.defaultTable.addRow(finalBCP.get(i));
						}
						stop();
					}
				}
				else
				{
					if(tablaLotes.defaultTable.getRowCount() >0)
						tablaLotes.defaultTable.removeRow(0);
					Vector <String>temp = segundaTabla.getOneProcess();
					mainTable.remove(0);
					segundaTabla.setProcessChanged();
					mainTable.add(temp);
					segundaTabla.setOneProcess(mainTable.get(0));
					
					updateTable(mainTable.size()-1);
					
				}
			}

		}
	}

}
