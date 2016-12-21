package de.manuel_joswig.epidemica.evaluation;

import de.manuel_joswig.epidemica.Epidemica;
import de.manuel_joswig.epidemica.simulation.SimulationHandler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;

/**
 * Plots the datasets in real time
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class DynamicPlotPanel extends JPanel implements ActionListener {
	public static final long serialVersionUID = 1L;
	
	private Epidemica simulation;
	private ChartPanel chartPanel;
	
	public DynamicPlotPanel() {
		this.simulation = SimulationHandler.getSimulation();
		
		setBackground(Color.WHITE);
		
		init();
	}
	
	private void init() {
		simulation.getEvaluationPackage().createChart(false);
		
		chartPanel = new ChartPanel(simulation.getEvaluationPackage().getChart());
		chartPanel.setChart(simulation.getEvaluationPackage().getChart());
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(true);
		chartPanel.setMouseWheelEnabled(true);
		
		add(chartPanel);
		
		Timer timer = new Timer(1, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		simulation.getEvaluationPackage().getChart().getXYPlot().setDataset(simulation.getEvaluationPackage().getUpdatedDataset());
	}
}
