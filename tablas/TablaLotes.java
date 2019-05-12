package com.elizalde.simulacion.tablas;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablaLotes {

	public DefaultTableModel defaultTable;
	public JTable tablaLotes;
	public JScrollPane scrollPane;
	private static String[] columnNames = { "ID", "Tiempo", "Tiempo Restante" };

	public TablaLotes(){
		defaultTable = new DefaultTableModel();
		tablaLotes = new JTable();
		defaultTable.setColumnIdentifiers(columnNames);

		tablaLotes.setModel(defaultTable);
		tablaLotes.setPreferredScrollableViewportSize(new Dimension(270, 200));
		tablaLotes.setAutoscrolls(true);
		scrollPane = new JScrollPane(tablaLotes);
		scrollPane.setEnabled(false);
		tablaLotes.setCellSelectionEnabled(false);
		tablaLotes.setEnabled(false);
		
		
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

}
