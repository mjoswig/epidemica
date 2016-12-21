package de.manuel_joswig.epidemica.automaton;

import de.manuel_joswig.epidemica.simulation.AgeGroup;

/**
 * Represents a single cell
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class Cell {
	private CellState state;
	private AgeGroup ageGroup;
	private int statePeriod, age;
	
	public Cell(CellState state, int age) {
		this.state = state;
		this.age = age;
		this.ageGroup = new AgeGroup((age - (age % 10)) / 10);
		
		statePeriod = 0;
	}
	
	public CellState getState() {
		return state;
	}
	
	public int getStatePeriod() {
		return statePeriod;
	}
	
	public int getAge() {
		return age;
	}
	
	public AgeGroup getAgeGroup() {
		return ageGroup;
	}
	
	public void setState(CellState state) {
		this.state = state;
		statePeriod = 0;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void increaseStatePeriod() {
		statePeriod++;
	}
}