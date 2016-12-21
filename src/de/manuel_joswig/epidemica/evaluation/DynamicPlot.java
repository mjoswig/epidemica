package de.manuel_joswig.epidemica.evaluation;

import javax.swing.JFrame;

/**
 * Shows the frame for the dynamic plot
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class DynamicPlot extends JFrame {
	public static final long serialVersionUID = 1L;
	
	public DynamicPlot() {
		setTitle("Dynamische Visualisierung der Daten");
		setSize(725, 475);
		
		add(new DynamicPlotPanel());

		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
