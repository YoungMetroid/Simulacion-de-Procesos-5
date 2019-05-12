package com.elizalde.simulacion.tablas;

import java.util.Vector;

public class TablaDeResultados extends TablaAbstracta{
	
	public Vector<Vector<String>> allInfo = new Vector<Vector<String>>();
	private static String[] columnNames = { "ID", "Operacion", "Resultado" };

	public TablaDeResultados() {
		defaultTable.setColumnIdentifiers(columnNames);
	}



	public void setAllinfo(Vector<Vector<String>> allInfo) {
		this.allInfo = allInfo;
	}

	public void clearAll() {
		allInfo.removeAllElements();
		for (int i = 0; i < defaultTable.getRowCount();)
			defaultTable.removeRow(0);
	}
}
