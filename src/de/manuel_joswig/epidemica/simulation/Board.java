package de.manuel_joswig.epidemica.simulation;

import de.manuel_joswig.epidemica.automaton.Cell;
import de.manuel_joswig.epidemica.automaton.CellState;
import de.manuel_joswig.epidemica.automaton.Grid;
import de.manuel_joswig.epidemica.evaluation.EvaluationPackage;
import de.manuel_joswig.epidemica.simulation.SimulationHandler;
import de.manuel_joswig.epidemica.simulation.SimulationState;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.io.File;

/**
 * Panel for grid and settings
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	public final static long serialVersionUID = 1L;
	
	private Grid grid;
	private Timer timer;
	
	public Board() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		init();
	}
	
	private void init() {
		grid = new Grid(100);
		timer = new Timer(1, this);
		
		timer.start();
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public boolean isMousePointerInGrid(Point mousePosition) {
		int x = mousePosition.x;
		int y = mousePosition.y;
		
		return (x > 0 && x < grid.getSize() && y > 0 && y < grid.getSize());
	}
	
	public Cell getCellByMousePosition(Point mousePosition) {
		int x = mousePosition.x;
		int y = mousePosition.y;
		
		Cell[][] cells = grid.getCells();
		
		return (isMousePointerInGrid(mousePosition)) ? cells[x / Grid.CELL_SIZE][y / Grid.CELL_SIZE] : null;
	}
	
	public void changeCellState(Cell cell, boolean isToggled) {
		if (cell != null) {
			if (isToggled) {
				switch (cell.getState()) {
					case HEALTHY:
						cell.setState(CellState.INFECTED);
						break;
							
					case INFECTED:
						cell.setState(CellState.EMPTY);
						break;
					
					case EMPTY:
					case DEAD:
						cell.setState(CellState.HEALTHY);
						break;
						
					default:
						break;
				}
			}
			else {
				cell.setState(CellState.HEALTHY);
			}
		}
	}
	
	public void setTimerInterval(int interval) {
		timer.setDelay(interval);
	}
	
	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }

	public void mousePressed(MouseEvent e) { }
	
	public void mouseReleased(MouseEvent e) { }
	
	public void mouseMoved(MouseEvent e) { }
	
	public void mouseClicked(MouseEvent e) {
		if (SimulationHandler.getState().equals(SimulationState.NONE) || SimulationHandler.getState().equals(SimulationState.CANCELLED)) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				changeCellState(getCellByMousePosition(e.getPoint()), true);
			}
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (SimulationHandler.getState().equals(SimulationState.NONE) || SimulationHandler.getState().equals(SimulationState.CANCELLED)) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				changeCellState(getCellByMousePosition(e.getPoint()), false);
			}
		}
	}
			
	public void actionPerformed(ActionEvent e) {
		EvaluationPackage evaluationPackage = SimulationHandler.getSimulation().getEvaluationPackage();
		
		if (SimulationHandler.getState().equals(SimulationState.RUNNING)) {
			if (evaluationPackage != null) {
				evaluationPackage.updateDataset(grid.getCellStats());
			}
			
			for (int i = 0; i < grid.getCellDimension(); i++) {
				for (int j = 0; j < grid.getCellDimension(); j++) {
					Cell[][] cells = grid.getCells();
					Cell refCell = cells[i][j];
					int infectedNeighbors = grid.getInfectedNeighborsOfCell(i, j);
					
					refCell.increaseStatePeriod();
					
					switch (refCell.getState()) {
						case HEALTHY:
							// apply rule #1
							if (infectedNeighbors > 0) {
								double infectionRisk = infectedNeighbors * refCell.getAgeGroup().getAgeSpecificCoefficient() * SimulationHandler.getSimulation().getInfectionRisk();
								
								// 100% is the greatest propability
								if (infectionRisk > 1) infectionRisk = 1;
								
								if ((int) (Math.random() * 100) < infectionRisk * 100) {
									refCell.setState(CellState.INFECTED);
								}
							}
							
							break;
							
						case INFECTED:
							// apply rule #3
							if (refCell.getStatePeriod() > SimulationHandler.getSimulation().getPeriodOfConvalescence()) {
								refCell.setState(CellState.RESISTANT);
							}
							
							// apply rule #5
							if ((int) (Math.random() * 100) < SimulationHandler.getSimulation().getMortalityRate() * 100) {
								refCell.setState(CellState.DEAD);
							}
							
							break;
							
						default:
							break;
					}
					
					if (SimulationHandler.getCellMovement() && !refCell.getState().equals(CellState.EMPTY) && !refCell.getState().equals(CellState.DEAD)) {
						// i like to move it :D
						grid.moveCellRandomly(i, j);
					}
				}
			}
		}
		else if (SimulationHandler.getState().equals(SimulationState.CANCELLED)) {
			if (evaluationPackage != null) {
				File simulationEvaluationDirectory = new File("eval/" + SimulationHandler.getUniqueID());
				
				if (!simulationEvaluationDirectory.exists()) {
					evaluationPackage.createDirectory();
				}
			}
		}
		
		// repaint grid
		repaint(0, 0, grid.getSize(), grid.getSize());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		grid.draw(g);
	}
}