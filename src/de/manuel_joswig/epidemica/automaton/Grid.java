package de.manuel_joswig.epidemica.automaton;

import de.manuel_joswig.epidemica.simulation.AgeDistributor;
import de.manuel_joswig.epidemica.simulation.NeighborhoodType;
import de.manuel_joswig.epidemica.simulation.SimulationHandler;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the quadratic grid
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class Grid {
	public static final int CELL_SIZE = 6;
	
	private int cellDimension;
	private Cell[][] cells;
	
	public Grid(int cellDimension) {
		this.cellDimension = cellDimension;
		cells = new Cell[cellDimension][cellDimension];
		
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				cells[i][j] = new Cell(CellState.EMPTY, AgeDistributor.getAge());
			}
		}
	}
	
	public int getCellDimension() {
		return cellDimension;
	}
	
	public int getSize() {
		return CELL_SIZE * cellDimension;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public Integer[] getCellStats() {
		int healthyCells = 0, infectedCells = 0, resistantCells = 0, deadCells = 0, emptyCells = 0;
		
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				switch (cells[i][j].getState()) {
					case HEALTHY:
						healthyCells++;
						break;
					
					case INFECTED:
						infectedCells++;
						break;
						
					case RESISTANT:
						resistantCells++;
						break;
						
					case DEAD:
						deadCells++;
						break;
					
					case EMPTY:
						emptyCells++;
						break;
				}
			}
		}
		
		Integer[] cellStats = {healthyCells, infectedCells, resistantCells, deadCells, emptyCells};
		
		return cellStats;
	}
	
	// return the number of infected neighbors who are able to transmit the disease
	public int getInfectedNeighborsOfCell(int x, int y) {
		int neighbors = 0;
		int neighborLimit = 4;
		int arrayCellDimension = cellDimension - 1;
		Cell[] neighborList = new Cell[8];

		neighborList[0] = (x != arrayCellDimension) ? cells[x + 1][y] : null;
		neighborList[1] = (x != 0) ? cells[x - 1][y] : null;
		neighborList[2] = (y != arrayCellDimension) ? cells[x][y + 1] : null;
		neighborList[3] = (y != 0) ? cells[x][y - 1] : null;
		
		if (SimulationHandler.getNeighborhood().equals(NeighborhoodType.MOORE)) {
			// add the four additional neighbors...
			neighborList[4] = (x != arrayCellDimension && y != arrayCellDimension) ? cells[x + 1][y + 1] : null;
			neighborList[5] = (x != 0 && y != 0) ? cells[x - 1][y - 1] : null;
			neighborList[6] = (x != arrayCellDimension && y != 0) ? cells[x + 1][y - 1] : null;
			neighborList[7] = (x != 0 && y != arrayCellDimension) ? cells[x - 1][y + 1] : null;
			
			neighborLimit = 8;
		}

		for (int i = 0; i < neighborLimit; i++) {
			if (neighborList[i] != null) {
				// notice: resistant cells are also able to infect healthy cells
				if (neighborList[i].getState().equals(CellState.INFECTED) || neighborList[i].getState().equals(CellState.RESISTANT)) {
					// apply rule #2
					if (SimulationHandler.getSimulation().getTransmissionPeriod() == 11 || neighborList[i].getStatePeriod() < SimulationHandler.getSimulation().getTransmissionPeriod()) {
						neighbors++;
					}
				}
			}
		}
		
		return neighbors;
	}
	
	public void moveCellRandomly(int x, int y) {
		int direction = (int) (Math.random() * 8);
		int arrayCellDimension = cellDimension - 1;
		
		switch (direction) {
			case 0:
				if (x < arrayCellDimension) {
					Cell c = cells[x + 1][y];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x + 1][y] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 1:
				if (x > 0) {
					Cell c = cells[x - 1][y];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x - 1][y] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 2:
				if (y < arrayCellDimension) {
					Cell c = cells[x][y + 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x][y + 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 3:
				if (y > 0) {
					Cell c = cells[x][y - 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x][y - 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 4:
				if (x < arrayCellDimension && y < arrayCellDimension) {
					Cell c = cells[x + 1][y + 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x + 1][y + 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 5:
				if (x > 0 && y > 0) {
					Cell c = cells[x - 1][y - 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x - 1][y - 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 6:
				if (x < arrayCellDimension && y > 0) {
					Cell c = cells[x + 1][y - 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x + 1][y - 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
				
			case 7:
				if (x > 0 && y < arrayCellDimension) {
					Cell c = cells[x - 1][y + 1];
					
					if (c.getState().equals(CellState.EMPTY) || c.getState().equals(CellState.DEAD)) {
						cells[x - 1][y + 1] = cells[x][y];
						cells[x][y] = c;
					}
				}
				
				break;
		}
	}
	
	public void loadCellStates(boolean isArbitrary) {
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				if (isArbitrary) {
					cells[i][j] = new Cell(CellState.getRandomElement(2), AgeDistributor.getAge());
					int z = (int) (Math.random() * 1000);
					
					// a cell is infected with p = 0.1%
					if (z == 500) {
						cells[i][j].setState(CellState.INFECTED);
					}
				}
				else {
					cells[i][j] = new Cell(CellState.EMPTY, AgeDistributor.getAge());
				}
			}
		}
	}
	
	public void resetCellStatePeriods() {
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				cells[i][j].setState(cells[i][j].getState());
			}
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getSize(), getSize());
		
		for (int i = 0; i < cellDimension; i++) {
			for (int j = 0; j < cellDimension; j++) {
				Cell refCell = cells[i][j];
				
				switch (refCell.getState()) {
					case HEALTHY:
						g.setColor(Color.GREEN);
						break;
						
					case INFECTED:
						g.setColor(Color.RED);
						break;
						
					case RESISTANT:
						g.setColor(Color.MAGENTA);
						break;
						
					case DEAD:
					case EMPTY:
						g.setColor(Color.WHITE);
						break;
				}
				
				g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				
				g.setColor(Color.BLACK);
				g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}
}