package com.elizalde.simulacion.tablas;

import java.awt.Dimension;

public class TablaTiempos extends TablaAbstracta{

	private static String[] columnNames = {"ID","Tiempo Llegada", "Tiempo Finalizacion",
			"Tiempo Retorno" , "Tiempo Repuesta", "Tiempo Espera","Tiempo Servicio"};
	public TablaTiempos()
	{
		defaultTable.setColumnIdentifiers(columnNames);
		tabla.setPreferredScrollableViewportSize(new Dimension(400,200));
	}
}
