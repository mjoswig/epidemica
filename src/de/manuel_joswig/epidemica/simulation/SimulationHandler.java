package de.manuel_joswig.epidemica.simulation;

import de.manuel_joswig.epidemica.Epidemica;

import java.awt.Component;

import java.util.UUID;

/**
 * Gives information about the current simulation
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class SimulationHandler {
	private static boolean cellMovement;
	
	private static String uniqueID;
	private static Epidemica simulation;
	private static SimulationState state;
	private static NeighborhoodType neighborhood;
	private static Component[] inactiveComp;
	
	public static String getUniqueID() {
		return uniqueID;
	}
	
	public static SimulationState getState() {
		return state;
	}
	
	public static Epidemica getSimulation() {
		return simulation;
	}
	
	public static NeighborhoodType getNeighborhood() {
		return neighborhood;
	}
	
	public static boolean getCellMovement() {
		return cellMovement;
	}
	
	public static void createUniqueID() {
		uniqueID = UUID.randomUUID().toString();
	}
	
	public static void setSimulation(Epidemica simulation) {
		SimulationHandler.simulation = simulation;
	}
	
	public static void setState(SimulationState state) {
		SimulationHandler.state = state;
	}
	
	public static void setNeighborhood(NeighborhoodType neighborhood) {
		SimulationHandler.neighborhood = neighborhood;
	}
	
	public static void setCellMovement(boolean cellMovement) {
		SimulationHandler.cellMovement = cellMovement;
	}
	
	public static void addToInactiveComponents(Component[] components) {
		for (Component comp : components) {
			comp.setEnabled(false);
		}
		
		inactiveComp = components;
	}
	
	public static void removeInactiveComponents() {
		for (Component comp : inactiveComp) {
			comp.setEnabled(true);
		}
	}
}