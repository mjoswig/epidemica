package de.manuel_joswig.epidemica.simulation;

/**
 * Represents an age group
 * 
 * ID | Age range
 * --------------
 * 0  | 0-9
 * 1  | 10-19
 * ...
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class AgeGroup {
	private int groupID;
	
	public AgeGroup(int groupID) {
		this.groupID = groupID;
	}
	
	public int getGroupID() {
		return groupID;
	}
	
	public double getAgeSpecificCoefficient() {
		double coefficient = 1;
		
		switch (groupID) {
			case 0:
				coefficient += 0.23;
				break;
			
			case 1:
				coefficient += 0.12;
				break;
			case 2:
				coefficient += 0.09;
				break;
			case 3:
				coefficient += 0.1;
				break;
			case 4:
				coefficient += 0.14;
				break;
			case 5:
				coefficient += 0.14;
				break;
			case 6:
				coefficient += 0.12;
				break;
			case 7:
				coefficient += 0.12;
				break;
			case 8:
				coefficient += 0.12;
				break;
			case 9:
				coefficient += 0.12;
				break;
		}
		
		return coefficient;
	}
}