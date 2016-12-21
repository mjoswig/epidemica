package de.manuel_joswig.epidemica;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Shows the help frame
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class Help extends JFrame {
	public static final long serialVersionUID = 1L;
	
	public Help() {
		setTitle("Hilfe");
		setSize(500, 400);
		
		addGUIComponents();
		
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void addGUIComponents() {
		JLabel lblContent = new JLabel("<html><body><h3>FAQ - Häufig gestellte Fragen</h3><br><b>Was ist Epidemica?</b><br>Epidemica ist eine Software, um die Ausbreitung eines Influenzavirus zu simulieren. Die verschiedenen Optionen und Parameter erlauben eine individuelle Gestaltung des Krankheitsverlaufs.<br><br>"
				+ "<b>Was sind Nachbarschaftsbeziehungen?</b><br>Die Infektionswahrscheinlichkeit einer Zelle ist von der festgelegten Nachbarschaftsbeziehung abhängig. Während die Von-Neumann-Nachbarschaft nur die direkten Nachbarn (oben, unten, links, rechts) beinhaltet, umfasst die Moore-Nachbarschaft auch die dazwischenliegenden Diagonalelemente.<br><br>"
				+ "<b>Was bedeuten die Farben der Zellen?</b><br>Die Farbe steht für den Zustand der jeweiligen Zelle. Grün bedeutet gesund, rot heißt infiziert, violett steht für resistent und weiß für tot.<br><br>"
				+ "<b>Wo finde ich die Auswertung einer Simulation?</b><br>Sämtliche Dateien, welche statistische Daten über die Simulation enthalten, befinden sich in einem mit der ID der Simulation benannten Unterordner des Ordners \"eval\".</body></html>");
		lblContent.setIcon(new ImageIcon(getClass().getResource("/question-icon.png")));
		add(lblContent);
	}
}