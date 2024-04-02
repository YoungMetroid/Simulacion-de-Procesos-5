package com.elizalde.simulacion;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

public class Preguntas extends JPanel implements ActionListener {

	/**
	 * 
	 */
	// Process information
	private Vector<String> copyID;
	private Vector<Vector<String>> tableInfo = new Vector<Vector<String>>();
	private JFormattedTextField infoHolder[] = new JFormattedTextField[8];
	private boolean check = false;

	// Gui Componentes
	private JLabel name = new JLabel("Nombre");
	private JLabel ID = new JLabel("ID");
	private JLabel Operacion = new JLabel("Operacion");
	private JLabel Time = new JLabel("Tiempo");
	private JLabel NumeroProcesos = new JLabel("Numero de Procesos: 0");
	private JLabel Space = new JLabel();
	private JLabel number1 = new JLabel("Var 1");
	private JLabel number2 = new JLabel("Var 2");
	private JLabel operator = new JLabel("OP");
	private JButton crearProceso = new JButton("Crear Nuevo Process");
	private JButton agregarProcesos = new JButton("Agregar Procesos");

	private static final long serialVersionUID = 1L;

	public Preguntas() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0; i < infoHolder.length; i++) {
			infoHolder[i] = new JFormattedTextField(new String(""));
		}

		JPanel container = new JPanel(new GridLayout(5, 2, 5, 5));
		JPanel operationSlot = new JPanel(new GridLayout(2, 3, 5, 5));
		JPanel Messages = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel buttons = new JPanel(new GridLayout(2, 1, 5, 5));
		container.add(name);
		container.add(infoHolder[0]);
		container.add(ID);
		container.add(infoHolder[1]);
		container.add(Operacion);

		operationSlot.add(number1);
		operationSlot.add(operator);
		operationSlot.add(number2);
		operationSlot.add(infoHolder[2]);
		operationSlot.add(infoHolder[3]);
		operationSlot.add(infoHolder[4]);

		container.add(operationSlot);
		container.add(Time);
		container.add(infoHolder[5]);
		Messages.add(NumeroProcesos);
		Messages.add(Space);
		container.add(Messages);
		buttons.add(crearProceso);
		buttons.add(agregarProcesos);
		crearProceso.addActionListener(this);
		container.add(buttons);
		add(container);
		setVisible(true);

	}

	public void checkName() {

		if (!infoHolder[0].getText().isEmpty()) {
			for (int i = 0; i < infoHolder[0].getText().length(); i++) {
				try {
					if (!" ".equals(infoHolder[0].getText(i, 1))) {
						check = true;
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (check) {
			checkNumbers();
		} else {
			Space.setText("Name is Empty");
		}

	}

	public void checkNumbers() {
		try {
			Integer.parseInt(infoHolder[1].getText());

			Integer.parseInt(infoHolder[2].getText());
			Integer.parseInt(infoHolder[4].getText());
			Integer.parseInt(infoHolder[5].getText());

			if (Integer.parseInt(infoHolder[5].getText()) >= 1) {
				if ("+".equals(infoHolder[3].getText()) || "-".equals(infoHolder[3].getText())
						|| "*".equals(infoHolder[3].getText()) || "^".equals(infoHolder[3].getText())) {
					check = true;
					checkIDs();
				} else if ("/".equals(infoHolder[3].getText()) || "%".equals(infoHolder[3].getText())) {
					if ((Integer.parseInt(infoHolder[4].getText()) == 0)) {
						check = false;
						Space.setText("!! No se puede dividir entre 0 !!");
					} else {
						check = true;
						checkIDs();
					}
				} else {
					check = false;
					Space.setText("Error in Operation");
				}
			} else {
				check = false;
				Space.setText("Tiempo tiene que ser mayor a 0");
			}
		} catch (NumberFormatException e) {
			Space.setText("Error Only excepts Numbers in ID,Var1,Var2 and Time");
		}
	}

	public void checkIDs() {
		if (!copyID.isEmpty()) {
			for (int i = 0; i < copyID.size(); i++) {

				if (copyID.get(i).equals(infoHolder[1].getText())) {
					check = false;
					Space.setText("Error ID Already Exists");
					break;
				}
			}
			if (check) {
				copyID.add(infoHolder[1].getText());
				tableInfo.add(getInfo());
				NumeroProcesos.setText("Numero de Procesos: " + copyID.size());
				Space.setText("Proceso En Lista " + "Numero " + copyID.size());

			}
		} else {
			copyID.add(infoHolder[1].getText());
			tableInfo.add(getInfo());
			NumeroProcesos.setText("Numero de Procesos: " + copyID.size());
			Space.setText("Proceso En Lista " + "Numero " + copyID.size());

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == crearProceso) {
			Space.setText("");
			checkName();
		}
	}

	public boolean getCheck() {
		return check;
	}

	public JButton getButton() {
		return agregarProcesos;
	}

	public Vector<String> getInfo() {
		Vector<String> info = new Vector<String>();
		for (int i = 0; i < infoHolder.length; i++) {
			info.add(infoHolder[i].getText());
		}
		return info;
	}

	public Vector<Vector<String>> getTable() {
		return tableInfo;
	}

	public void setID(Vector<String> id) {
		copyID = id;
	}

}
