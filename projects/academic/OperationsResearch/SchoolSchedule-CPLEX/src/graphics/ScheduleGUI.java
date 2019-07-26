package graphics;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

public class ScheduleGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IloCplex model;
	private static IloIntVar[][][][] x;
	private Object[][][] tableClassData;
	private Object[][][] tableProfessorData;
	private Object[] columnNames;

	JPanel tablesPanel;

	JButton buttonNext;
	JButton buttonPrevious;
	JButton buttonProfessors;
	JButton buttonClasses;

	private static int page_classes;
	private static int page_professors;
	private static boolean classes;

	private static int P;
	private static int D;
	private static int C;
	private static int S;

	public ScheduleGUI(IloCplex model, IloIntVar[][][][] x, int P, int D, int C, int S)
			throws UnknownObjectException, IloException {
		ScheduleGUI.x = x;
		ScheduleGUI.P = P;
		ScheduleGUI.D = D;
		ScheduleGUI.C = C;
		ScheduleGUI.S = S;
		this.model = model;
		buildTables();
		init();
	}

	public void bring() {
		this.pack();
		this.setVisible(true);
	}

	private void init() {
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Schedule - CPLEX");
		this.setSize(800, 600);
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		ScheduleGUI.page_classes = 0;
		ScheduleGUI.page_professors = 0;
		ScheduleGUI.classes = true;

		this.tablesPanel = new JPanel(new GridBagLayout());
		setupButtons();
		bringNext();
	}

	private void bringNext() {
		this.tablesPanel.removeAll();
		if (ScheduleGUI.classes) {
			for (int cn = 3 * ScheduleGUI.page_classes, plc = 0; cn < 3 * (ScheduleGUI.page_classes + 1)
					&& cn < C; cn++, plc++) {
				JLabel classLabel = new JLabel("Class " + cn);
				JTable scheduleTable = new JTable(this.tableClassData[cn], this.columnNames);
				scheduleTable.setEnabled(false);

				GridBagConstraints cttTables = new GridBagConstraints();
				cttTables.fill = GridBagConstraints.HORIZONTAL;
				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc;
				this.tablesPanel.add(classLabel, cttTables);

				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc + 1;
				this.tablesPanel.add(scheduleTable.getTableHeader(), cttTables);

				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc + 2;
				this.tablesPanel.add(scheduleTable, cttTables);
			}
		} else {
			for (int cn = 3 * ScheduleGUI.page_professors, plc = 0; cn < 3 * (ScheduleGUI.page_professors + 1)
					&& cn < P; cn++, plc++) {
				JLabel classLabel = new JLabel("Professor " + cn);
				JTable scheduleTable = new JTable(this.tableProfessorData[cn], this.columnNames);
				scheduleTable.setEnabled(false);

				GridBagConstraints cttTables = new GridBagConstraints();
				cttTables.fill = GridBagConstraints.HORIZONTAL;
				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc;
				this.tablesPanel.add(classLabel, cttTables);

				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc + 1;
				this.tablesPanel.add(scheduleTable.getTableHeader(), cttTables);

				cttTables.gridx = 0;
				cttTables.gridy = 3 * plc + 2;
				this.tablesPanel.add(scheduleTable, cttTables);
			}
		}
		GridBagConstraints cttPanel = new GridBagConstraints();
		cttPanel.fill = GridBagConstraints.HORIZONTAL;
		cttPanel.gridwidth = 3;
		cttPanel.gridx = 0;
		cttPanel.gridy = 1;
		this.add(this.tablesPanel, cttPanel);
		this.tablesPanel.revalidate();
	}

	private void setupButtons() {
		GridBagConstraints cttFrame = new GridBagConstraints();
		cttFrame.fill = GridBagConstraints.HORIZONTAL;
		this.buttonNext = new JButton("Next");
		this.buttonNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (page_classes * 3 + 3 < C && ScheduleGUI.classes == true) {
					page_classes++;
					bringNext();
				} else if (page_professors * 3 + 3 < P && ScheduleGUI.classes == false) {
					page_professors++;
					bringNext();
				}
			}
		});
		cttFrame.anchor = GridBagConstraints.LAST_LINE_END;
		cttFrame.weightx = 0.5;
		cttFrame.gridy = 2;
		cttFrame.gridx = 2;
		this.add(buttonNext, cttFrame);

		this.buttonPrevious = new JButton("Previous");
		this.buttonPrevious.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (page_classes > 0 && ScheduleGUI.classes == true) {
					page_classes--;
					bringNext();
				} else if (page_professors > 0 && ScheduleGUI.classes == false) {
					page_professors--;
					bringNext();
				}
			}
		});
		cttFrame.anchor = GridBagConstraints.LAST_LINE_START;
		cttFrame.weightx = 0.5;
		cttFrame.gridy = 2;
		cttFrame.gridx = 0;
		this.add(buttonPrevious, cttFrame);

		this.buttonClasses = new JButton("Class Vision");
		this.buttonClasses.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ScheduleGUI.classes = true;
				bringNext();
			}
		});
		cttFrame.anchor = GridBagConstraints.FIRST_LINE_END;
		cttFrame.weightx = 0.5;
		cttFrame.gridy = 0;
		cttFrame.gridx = 0;
		this.add(buttonClasses, cttFrame);

		this.buttonProfessors = new JButton("Professor Vision");
		this.buttonProfessors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ScheduleGUI.classes = false;
				bringNext();
			}
		});
		cttFrame.anchor = GridBagConstraints.FIRST_LINE_START;
		cttFrame.weightx = 0.5;
		cttFrame.gridy = 0;
		cttFrame.gridx = 2;
		this.add(buttonProfessors, cttFrame);
	}

	private void buildTables() throws UnknownObjectException, IloException {

		this.columnNames = new Object[D];
		for (int d = 0; d < D; d++) {
			columnNames[d] = DayOfWeek.of(d + 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		}

		this.tableClassData = new Object[C][S][D];
		for (int c = 0; c < C; c++) {
			for (int d = 0; d < D; d++) {
				for (int s = 0; s < S; s++) {
					for (int p = 0; p < P; p++) {
						if (model.getValue(ScheduleGUI.x[p][d][c][s]) >= 0.5) {
							this.tableClassData[c][s][d] = p;
						}
					}

				}
			}
		}

		this.tableProfessorData = new Object[P][S][D];
		for (int p = 0; p < P; p++) {
			for (int d = 0; d < D; d++) {
				for (int s = 0; s < S; s++) {
					for (int c = 0; c < C; c++) {
						if (model.getValue(ScheduleGUI.x[p][d][c][s]) >= 0.5) {
							this.tableProfessorData[p][s][d] = c;
						}
					}

				}
			}
		}
	}
}
