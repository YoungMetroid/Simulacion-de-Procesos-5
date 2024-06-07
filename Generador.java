package com.elizalde.simulacion;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Generador {

	int procesos = -1;
	int quantum = -1;
	// Number of process of ID
	// Integer
	// operator
	// Integer
	// Time
	// Time 
	// Time Came 
	// Time Finish
	// Time Return = CurrentTime - Time Came
	// Time Response = Time when in processor- Time Came
	// Wait Time = Time in ready and blocked 
	// Service = Time in processor
	private Vector<String> copyID;
	private Vector<Vector<String>> tableInfo = new Vector<Vector<String>>();
	private int counter = 0;
	public Generador() {

	}
	void question() {
		String pregunta1 = JOptionPane.showInputDialog("Numero de Procesos");
		procesos = Integer.parseInt(pregunta1);
		for (int i = 0; i < procesos; i++) {
			tableInfo.addElement(getGenerateOne());

		}
		
		while(quantum < 1)
		{
			String pregunta2 = JOptionPane.showInputDialog("Tamaño del Quantum");
			quantum = Integer.parseInt(pregunta2);
		}
		
	}
	public Vector<Vector<String>>getTable() {
		return tableInfo;
	}
	public int getQuantum()
	{
		return quantum;
	}
	public Vector<String> getGenerateOne()
	{
		copyID = new Vector<String>();
		Random randomGenerator = new SecureRandom();

		int var1;
		var1 = randomGenerator.nextInt(10 - 01 + 1) + 01;

		randomGenerator = new SecureRandom();
		int var2;
		var2 = randomGenerator.nextInt(10 - 01 + 1) + 01;

		randomGenerator = new SecureRandom();
		int time;
		time = randomGenerator.nextInt(16 - 06 + 1) + 06;

		randomGenerator = new SecureRandom();
		int op;
		op = randomGenerator.nextInt(5 - 01 + 1) + 01;
		String operation;
		switch (op) {
		case 0:
			operation = "+";
			break;
		case 1:
			operation = "-";
			break;
		case 2:
			operation = "*";
			break;
		case 3:
			operation = "/";
			break;
		case 4:
			operation = "^";
			break;
		case 5:
			operation = "%";
			break;
		default:
			operation = "+";
			break;
		}
		copyID.addElement(Integer.toString(counter));     //0	ID
		copyID.addElement(Integer.toString(var1));  //1	Operand	
		copyID.addElement(operation);				//2	Operator
		copyID.addElement(Integer.toString(var2));	//3	Operand
		copyID.addElement(Integer.toString(time));	//4	Max Time
		copyID.addElement("0");						//5	Current Time
		copyID.addElement(Integer.toString(time));	//6	TimeLeft
		copyID.addElement("0");						//7 Arrival Time
		copyID.addElement("0");						//8 First Time Executing
		copyID.addElement("0");						//9 Wait Time
		copyID.addElement("0");						//10
		copyID.addElement("0");	
		counter++;
		return copyID;
	}
}
