package de.manuel_joswig.epidemica.automaton;

/**
 * Holds all possible cell states
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public enum CellState {
	HEALTHY, EMPTY, INFECTED, RESISTANT, DEAD;
	
    public static CellState getRandomElement(int range) {
        return values()[(int) (Math.random() * range)];
    }
}