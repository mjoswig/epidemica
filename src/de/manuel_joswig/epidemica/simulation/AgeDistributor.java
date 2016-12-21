package de.manuel_joswig.epidemica.simulation;

/**
 * Responsible for the age of a cell
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class AgeDistributor {
	private static AgeGroup getAgeGroup() {
		int ageGroupID = 0;
		double x = Math.random();
		
		// 0-9 years : 8,5%
		if (x > 0 && x <= 0.085) {
			ageGroupID = 0;
		} // 10-19 years : 10%
		else if (x > 0.085 && x <= 0.185) {
			ageGroupID = 1;
		} // 20-29 years : 12%
		else if (x > 0.185 && x <= 0.305) {
			ageGroupID = 2;
		} // 30-39 years : 12.2%
		else if (x > 0.305 && x <= 0.427) {
			ageGroupID = 3;
		} // 40-49 years : 17.1%
		else if (x > 0.427 && x <= 0.598) {
			ageGroupID = 4;
		} // 50-59 years : 14.2%
		else if (x > 0.598 && x <= 0.74) {
			ageGroupID = 5;
		} // 60-69 years : 11.2%
		else if (x > 0.74 && x <= 0.852) {
			ageGroupID = 6;
		} // 70-79 years : 9.7%
		else if (x > 0.852 && x <= 0.949) {
			ageGroupID = 7;
		} // 80-89 years : 4.5% 
		else if (x > 0.949 && x <= 0.994) {
			ageGroupID = 8;
		} // 90-99 years : 0.6%
		else if (x > 0.994) {
			ageGroupID = 9;
		}
		
		return new AgeGroup(ageGroupID);
	}
	
	public static int getAge() {
		AgeGroup ageGroup = AgeDistributor.getAgeGroup();
		return (int) (Math.random() * (ageGroup.getGroupID() * 10 + 9) + 10);
	}
}