package com.elizalde.simulacion.tablas;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablaAbstracta {

	public DefaultTableModel defaultTable;
	public JTable tabla;
	public JScrollPane scrollPane;
	private static String[] columnNames = {""};
	
	public TablaAbstracta()
	{
		defaultTable = new DefaultTableModel();
		tabla = new JTable();
		defaultTable.setColumnIdentifiers(columnNames);
		
		tabla.setModel(defaultTable);
		tabla.setPreferredScrollableViewportSize(new Dimension(270,200));
		tabla.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(tabla);
		scrollPane.setEnabled(false);
		
		tabla.setCellSelectionEnabled(false);
		tabla.setEnabled(false);
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	
}
