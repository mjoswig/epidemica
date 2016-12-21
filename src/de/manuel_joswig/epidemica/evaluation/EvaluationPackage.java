package de.manuel_joswig.epidemica.evaluation;

import de.manuel_joswig.epidemica.simulation.Board;
import de.manuel_joswig.epidemica.simulation.SimulationHandler;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Contains analytical data about a simulation
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class EvaluationPackage {
	private List<Integer[]> dataset;
	private BufferedImage gridScreenshot;
	private JFreeChart chart;
	
	public EvaluationPackage() {
		dataset = new ArrayList<Integer[]>();
		chart = null;
	}
	
	public XYSeriesCollection getUpdatedDataset() {
		XYSeries seriesHealthy = new XYSeries("gesund");
		
		for (int i = 0; i < dataset.size(); i++) {
			Integer[] cellStats = dataset.get(i);
			seriesHealthy.add(i, cellStats[0]);
		}
		
		XYSeries seriesInfected = new XYSeries("infiziert");
		
		for (int i = 0; i < dataset.size(); i++) {
			Integer[] cellStats = dataset.get(i);
			seriesInfected.add(i, cellStats[1]);
		}
		
		XYSeries seriesResistant = new XYSeries("resistent");
		
		for (int i = 0; i < dataset.size(); i++) {
			Integer[] cellStats = dataset.get(i);
			seriesResistant.add(i, cellStats[2]);
		}
		
		XYSeries seriesDead = new XYSeries("tot");
		
		for (int i = 0; i < dataset.size(); i++) {
			Integer[] cellStats = dataset.get(i);
			seriesDead.add(i, cellStats[3]);
		}
		
		XYSeriesCollection seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(seriesHealthy);
		seriesCollection.addSeries(seriesInfected);
		seriesCollection.addSeries(seriesResistant);
		seriesCollection.addSeries(seriesDead);
		
		return seriesCollection;
	}
	
	public JFreeChart getChart() {
		return chart;
	}
	
	public void createChart(boolean saveAsImage) {
		XYSeriesCollection seriesCollection = getUpdatedDataset();
		
		chart = ChartFactory.createXYLineChart("Statistische Auswertung", "Tage", "Anzahl von Zellen", seriesCollection);
		chart.getPlot().setBackgroundPaint(Color.BLACK);
		
		if (saveAsImage) {
			try {
				// add a chart of the cell state distribution
				ChartUtilities.saveChartAsPNG(new File("eval/" + SimulationHandler.getUniqueID() + "/Diagramm.png"), chart, 680, 420);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createParameterFile() {
		String transmissionPeriod = String.valueOf(SimulationHandler.getSimulation().getTransmissionPeriod());
		
		// 11 is the integer value for infinity
		if (transmissionPeriod.equals("11")) {
			transmissionPeriod = "\u221E";
		}
		
		try {
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter("eval/" + SimulationHandler.getUniqueID() + "/Parameter.txt", "UTF-8");
				
				printWriter.println("Nachbarschaftsbeziehung: " + SimulationHandler.getNeighborhood());
				printWriter.println("Bewegung der Zellen: " + SimulationHandler.getCellMovement());
				printWriter.println("Infektionswahrscheinlichkeit: " + SimulationHandler.getSimulation().getInfectionRisk() * 100 + "%");
				printWriter.println("Transmissionsdauer: " + transmissionPeriod + " Tage");
				printWriter.println("Rekonvaleszenzzeit: " + SimulationHandler.getSimulation().getPeriodOfConvalescence() + " Tage");
				printWriter.println("Sterblichkeitsrate: " + SimulationHandler.getSimulation().getMortalityRate() * 100 + "%");
				printWriter.println("Subtyp: " + SimulationHandler.getSimulation().getSubType());
				printWriter.close();
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createSpreadSheet() {
		try {
			PrintWriter printWriter = new PrintWriter("eval/" + SimulationHandler.getUniqueID() + "/Wertetabelle.csv");
			printWriter.println("Tag;gesund;infiziert;resistent;tot");
			
			for (int i = 0; i < dataset.size(); i++) {
				Integer[] cellStats = dataset.get(i);
				printWriter.println(i + ";" + cellStats[0] + ";" + cellStats[1] + ";" + cellStats[2] + ";" + cellStats[3]);
			}
			
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void createGridScreenshot(Board board) {
		gridScreenshot = new BufferedImage(board.getGrid().getSize(), board.getGrid().getSize(), BufferedImage.TYPE_INT_ARGB);
		SimulationHandler.getSimulation().getContentPane().paint(gridScreenshot.getGraphics());
	}
	
	public void updateDataset(Integer[] cellStats) {
		dataset.add(cellStats);
	}
	
	public void createDirectory() {
		new File("eval/" + SimulationHandler.getUniqueID()).mkdir();
		
		try {
			ImageIO.write(gridScreenshot, "PNG", new File("eval/" + SimulationHandler.getUniqueID() + "/Zellkonfiguration.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createChart(true);
		createParameterFile();
		createSpreadSheet();
	}
}