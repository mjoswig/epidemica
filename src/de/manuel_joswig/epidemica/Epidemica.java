package de.manuel_joswig.epidemica;

import de.manuel_joswig.epidemica.automaton.Grid;
import de.manuel_joswig.epidemica.evaluation.DynamicPlot;
import de.manuel_joswig.epidemica.evaluation.EvaluationPackage;
import de.manuel_joswig.epidemica.simulation.Board;
import de.manuel_joswig.epidemica.simulation.NeighborhoodType;
import de.manuel_joswig.epidemica.simulation.SimulationHandler;
import de.manuel_joswig.epidemica.simulation.SimulationState;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;
import java.util.Hashtable;

/**
 * Initializes the window
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class Epidemica extends JFrame implements ActionListener, ChangeListener, ItemListener {
	public static final long serialVersionUID = 1L;
	
	public static final String FRAME_TITLE = "Epidemica";
	
	// default values for the h1n1 disease
	public static final double H1N1_INFECTION_RISK = 0.001;
	public static final int H1N1_TRANSMISSION_PERIOD = 4;
	public static final int H1N1_PERIOD_OF_CONVALESCENCE = 11;
	public static final double H1N1_MORTALITY_RATE = 0.27;
	
	public static final int TIMER_INTERVAL = 500;
	
	private double infectionRisk, mortalityRate;
	private int transmissionPeriod, periodOfConvalescence, timerInterval;
	
	private Board board;
	private EvaluationPackage evaluationPackage;
	private JButton btnClearGrid, btnStartStop, btnCancel;
	private JCheckBoxMenuItem mainMenuItemArbitraryStates, mainMenuItemCellMovement, mainMenuItemEvaluation;
	private JComboBox<String> cbSubType;
	private JMenu mainMenuOptions;
	private JMenuItem mainMenuItemHelp, mainMenuItemAbout; 
	private JRadioButtonMenuItem mainMenuItemMoore, mainMenuItemNeumann;
	private JSlider sldInfectionRisk, sldTransmissionPeriod, sldPeriodOfConvalescence, sldMortalityRate, sldTimerInterval;
	
	public Epidemica() {
		setTitle(FRAME_TITLE);
		setSize(1000, 652);
		setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
		
		try { 
			// customize the user interface for each operating system 
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) { 
	        System.out.println("Error setting custom user interface: " + e); 
	    }   
		
		addGUIComponents();
		
		board = new Board();
		add(board);
		
		SimulationHandler.setSimulation(this);
		SimulationHandler.setState(SimulationState.NONE);
		SimulationHandler.setNeighborhood(NeighborhoodType.MOORE);
		
		infectionRisk = 0;
		transmissionPeriod = 0;
		periodOfConvalescence = 0;
		mortalityRate = 0;
		timerInterval = TIMER_INTERVAL;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public double getInfectionRisk() {
		return infectionRisk;
	}
	
	public int getTransmissionPeriod() {
		return transmissionPeriod;
	}
	
	public int getPeriodOfConvalescence() {
		return periodOfConvalescence;
	}
	
	public double getMortalityRate() {
		return mortalityRate;
	}
	
	public String getSubType() {
		return cbSubType.getSelectedItem().toString();
	}
	
	public int getTimerInterval() {
		return timerInterval;
	}
	
	public EvaluationPackage getEvaluationPackage() {
		return evaluationPackage;
	}
	
	private void addGUIComponents() {
		JMenuBar mainMenu = new JMenuBar();
		
		mainMenuOptions = new JMenu("Optionen");
		JMenu mainMenuNeighborhood = new JMenu("Nachbarschaftsbeziehung");
		ButtonGroup bgNeighborhood = new ButtonGroup();
		mainMenuItemMoore = new JRadioButtonMenuItem("Moore");
		mainMenuItemMoore.setSelected(true);
		mainMenuItemMoore.addItemListener(this);
		mainMenuItemNeumann = new JRadioButtonMenuItem("Von-Neumann");
		mainMenuItemNeumann.addItemListener(this);
		bgNeighborhood.add(mainMenuItemMoore);
		bgNeighborhood.add(mainMenuItemNeumann);
		mainMenuNeighborhood.add(mainMenuItemMoore);
		mainMenuNeighborhood.add(mainMenuItemNeumann);
		mainMenuItemArbitraryStates = new JCheckBoxMenuItem("Arbiträre Anfangszustände");
		mainMenuItemArbitraryStates.addItemListener(this);
		mainMenuItemCellMovement = new JCheckBoxMenuItem("Bewegung der Zellen");
		mainMenuItemCellMovement.addItemListener(this);
		mainMenuItemEvaluation = new JCheckBoxMenuItem("Statistische Auswertung");
		mainMenuItemEvaluation.setSelected(true);
		mainMenuOptions.add(mainMenuNeighborhood);
		mainMenuOptions.add(mainMenuItemArbitraryStates);
		mainMenuOptions.add(mainMenuItemCellMovement);
		mainMenuOptions.add(mainMenuItemEvaluation);
		
		JMenu mainMenuAdditional = new JMenu("?");
		mainMenuItemHelp = new JMenuItem("Hilfe anzeigen");
		mainMenuItemHelp.addActionListener(this);
		mainMenuItemAbout = new JMenuItem("Über das Programm");
		mainMenuItemAbout.addActionListener(this);
		mainMenuAdditional.add(mainMenuItemHelp);
		mainMenuAdditional.add(mainMenuItemAbout);
		
		mainMenu.add(mainMenuOptions);
		mainMenu.add(mainMenuAdditional);
		
		setJMenuBar(mainMenu);
		
		JLabel lblParameters = new JLabel("Krankheitsspezifische Parameter");
		lblParameters.setBounds(625, 30, 200, 20);
		lblParameters.setFont(UIManager.getDefaults().getFont("TabbedPane.font").deriveFont(Font.BOLD, 12));
		add(lblParameters);
		
		JLabel lblInfectionRisk = new JLabel("Infektionswahrscheinlichkeit (in %)");
		lblInfectionRisk.setBounds(625, 80, 200, 20);
		add(lblInfectionRisk);
		
		sldInfectionRisk = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		sldInfectionRisk.setBounds(625, 105, 300, 50);
		sldInfectionRisk.setMinorTickSpacing(5);
		sldInfectionRisk.setMajorTickSpacing(10);
		sldInfectionRisk.setPaintTicks(true);
		sldInfectionRisk.setPaintLabels(true);
		sldInfectionRisk.addChangeListener(this);
		add(sldInfectionRisk);
		
		JLabel lblTransmissionPeriod = new JLabel("Transmissionsdauer (in Tagen)");
		lblTransmissionPeriod.setBounds(625, 165, 200, 20);
		add(lblTransmissionPeriod);
		
		Hashtable<Integer, JLabel> transmissionPeriodLabels = new Hashtable<Integer, JLabel>();
		transmissionPeriodLabels.put(0, new JLabel("0"));
		transmissionPeriodLabels.put(1, new JLabel("1"));
		transmissionPeriodLabels.put(2, new JLabel("2"));
		transmissionPeriodLabels.put(3, new JLabel("3"));
		transmissionPeriodLabels.put(4, new JLabel("4"));
		transmissionPeriodLabels.put(5, new JLabel("5"));
		transmissionPeriodLabels.put(6, new JLabel("6"));
		transmissionPeriodLabels.put(7, new JLabel("7"));
		transmissionPeriodLabels.put(8, new JLabel("8"));
		transmissionPeriodLabels.put(9, new JLabel("9"));
		transmissionPeriodLabels.put(10, new JLabel("10"));
		transmissionPeriodLabels.put(11, new JLabel("\u221E"));
		
		sldTransmissionPeriod = new JSlider(JSlider.HORIZONTAL, 0, 11, 0);
		sldTransmissionPeriod.setBounds(625, 190, 300, 50);
		sldTransmissionPeriod.setMinorTickSpacing(1);
		sldTransmissionPeriod.setMajorTickSpacing(1);
		sldTransmissionPeriod.setPaintTicks(true);
		sldTransmissionPeriod.setPaintLabels(true);
		sldTransmissionPeriod.setLabelTable(transmissionPeriodLabels);
		sldTransmissionPeriod.addChangeListener(this);
		add(sldTransmissionPeriod);
		
		JLabel lblPeriodOfConvalescence = new JLabel("Rekonvaleszenzzeit (in Tagen)");
		lblPeriodOfConvalescence.setBounds(625, 250, 200, 20);
		add(lblPeriodOfConvalescence);
		
		sldPeriodOfConvalescence = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
		sldPeriodOfConvalescence.setBounds(625, 275, 300, 50);
		sldPeriodOfConvalescence.setMinorTickSpacing(1);
		sldPeriodOfConvalescence.setMajorTickSpacing(2);
		sldPeriodOfConvalescence.setPaintTicks(true);
		sldPeriodOfConvalescence.setPaintLabels(true);
		sldPeriodOfConvalescence.addChangeListener(this);
		add(sldPeriodOfConvalescence);
		
		JLabel lblMortalityRate = new JLabel("Sterblichkeitsrate (in %)");
		lblMortalityRate.setBounds(625, 335, 200, 20);
		add(lblMortalityRate);
		
		sldMortalityRate = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		sldMortalityRate.setBounds(625, 360, 300, 50);
		sldMortalityRate.setMinorTickSpacing(5);
		sldMortalityRate.setMajorTickSpacing(10);
		sldMortalityRate.setPaintTicks(true);
		sldMortalityRate.setPaintLabels(true);
		sldMortalityRate.addChangeListener(this);
		add(sldMortalityRate);
		
		JLabel lblSubType = new JLabel("Subtyp");
		lblSubType.setBounds(625, 420, 200, 20);
		add(lblSubType);
		
		String[] subTypes = {"Eigener Influenzavirus", "Influenza-A-Virus H1N1"};
		cbSubType = new JComboBox<String>(subTypes);
		cbSubType.setBounds(625, 445, 300, 20);
		cbSubType.addActionListener(this);
		add(cbSubType);
		
		JLabel lblTimerInterval = new JLabel("Geschwindigkeit");
		lblTimerInterval.setBounds(625, 500, 200, 20);
		add(lblTimerInterval);
		
		sldTimerInterval = new JSlider(JSlider.HORIZONTAL, -1000, -1, -TIMER_INTERVAL);
		sldTimerInterval.setBounds(625, 525, 300, 20);
		sldTimerInterval.addChangeListener(this);
		add(sldTimerInterval);
		
		btnStartStop = new JButton("Start");
		btnStartStop.setBounds(625, 575, 85, 20);
		btnStartStop.addActionListener(this);
		add(btnStartStop);
		
		btnCancel = new JButton("Abbrechen");
		btnCancel.setBounds(715, 575, 85, 20);
		btnCancel.setEnabled(false);
		btnCancel.addActionListener(this);
		add(btnCancel);
		
		btnClearGrid = new JButton("Gitter zurücksetzen");
		btnClearGrid.setBounds(805, 575, 175, 20);
		btnClearGrid.addActionListener(this);
		add(btnClearGrid);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnClearGrid)) {
			Grid grid = board.getGrid();
			grid.loadCellStates(false);
			
			mainMenuItemArbitraryStates.setSelected(false);
		}
		else if (e.getSource().equals(btnStartStop)) {
			switch (SimulationHandler.getState()) {
				case CANCELLED:
				case NONE:
					board.setTimerInterval(timerInterval);
					SimulationHandler.createUniqueID();
					SimulationHandler.setState(SimulationState.RUNNING);
					
					setTitle(FRAME_TITLE + " | ID: " + SimulationHandler.getUniqueID());
					
					btnStartStop.setText("Pause");
					btnCancel.setEnabled(true);
					
					if (mainMenuItemEvaluation.isSelected()) {
						evaluationPackage = new EvaluationPackage();
						evaluationPackage.createGridScreenshot(board);
						
						File evaluationDirectory = new File("eval");
						
						if (!evaluationDirectory.exists()) {
							new File("eval/").mkdir();
						}
						
						new DynamicPlot();
					}
					
					Component[] components = {mainMenuOptions, btnClearGrid, sldInfectionRisk, sldTransmissionPeriod, sldPeriodOfConvalescence, sldMortalityRate, cbSubType, sldTimerInterval};
					SimulationHandler.addToInactiveComponents(components);
					break;
				
				case RUNNING:
					SimulationHandler.setState(SimulationState.PAUSED);
					btnStartStop.setText("Fortsetzen");
					break;
				
				case PAUSED:
					SimulationHandler.setState(SimulationState.RUNNING);
					btnStartStop.setText("Pause");
					break;
			}
		}
		else if (e.getSource().equals(btnCancel)) {
			board.setTimerInterval(1);
			board.getGrid().resetCellStatePeriods();
			SimulationHandler.setState(SimulationState.CANCELLED);
			btnStartStop.setText("Start");
			btnCancel.setEnabled(false);
			
			SimulationHandler.removeInactiveComponents();
			
			if (getSubType().equals("Influenza-A-Virus H1N1")) {
				Component[] components = {sldInfectionRisk, sldTransmissionPeriod, sldPeriodOfConvalescence, sldMortalityRate};
				SimulationHandler.addToInactiveComponents(components);
			}
		}
		else if (e.getSource().equals(mainMenuItemHelp)) {
			new Help();
		}
		else if (e.getSource().equals(mainMenuItemAbout)) {
			new About();
		}
		else if (e.getSource().equals(cbSubType)) {
			Component[] components = {sldInfectionRisk, sldTransmissionPeriod, sldPeriodOfConvalescence, sldMortalityRate};
			
			if (getSubType().equals("Influenza-A-Virus H1N1")) {
				sldInfectionRisk.setValue((int) (H1N1_INFECTION_RISK * 100));
				sldTransmissionPeriod.setValue(H1N1_TRANSMISSION_PERIOD);
				sldPeriodOfConvalescence.setValue(H1N1_PERIOD_OF_CONVALESCENCE);
				sldMortalityRate.setValue((int) (H1N1_MORTALITY_RATE * 100));
				
				infectionRisk = H1N1_INFECTION_RISK;
				
				SimulationHandler.addToInactiveComponents(components);
			}
			else if (getSubType().equals("Eigener Influenzavirus")) {
				sldInfectionRisk.setValue(0);
				sldTransmissionPeriod.setValue(0);
				sldPeriodOfConvalescence.setValue(0);
				sldMortalityRate.setValue(0);
				
				SimulationHandler.removeInactiveComponents();
			}
		}
	}
	
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(sldInfectionRisk)) {
			infectionRisk = (double) sldInfectionRisk.getValue() / 100;
		}
		else if (e.getSource().equals(sldTransmissionPeriod)) {
			transmissionPeriod = sldTransmissionPeriod.getValue();
		}
		else if (e.getSource().equals(sldPeriodOfConvalescence)) {
			periodOfConvalescence = sldPeriodOfConvalescence.getValue();
		}
		else if (e.getSource().equals(sldMortalityRate)) {
			mortalityRate = (double) sldMortalityRate.getValue() / 100;
		}
		else if (e.getSource().equals(sldTimerInterval)) {
			timerInterval = -sldTimerInterval.getValue();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem().equals((mainMenuItemArbitraryStates))) {
			Grid grid = board.getGrid();
			grid.loadCellStates(mainMenuItemArbitraryStates.isSelected());
		}
		else if (e.getItem().equals(mainMenuItemCellMovement)) {
			SimulationHandler.setCellMovement(mainMenuItemCellMovement.isSelected());
		}
		else if (e.getItem().equals(mainMenuItemMoore)) {
			if (mainMenuItemMoore.isSelected()) {
				SimulationHandler.setNeighborhood(NeighborhoodType.MOORE);
			}
		}
		else if (e.getItem().equals(mainMenuItemNeumann)) {
			if (mainMenuItemNeumann.isSelected()) {
				SimulationHandler.setNeighborhood(NeighborhoodType.VON_NEUMANN);
			}
		}
	}
	
	public static void main(String[] args) {
		new Epidemica();
	}
}