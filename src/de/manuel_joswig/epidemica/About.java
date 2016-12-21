package de.manuel_joswig.epidemica;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Shows the about frame
 * 
 * @author		Manuel Joswig
 * @copyright	2014 Manuel Joswig
 */
public class About extends JFrame {
	public static final long serialVersionUID = 1L;

	public About() {
		setTitle("Über das Programm");
		setSize(275, 150);
		
		addGUIComponents();
		
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void addGUIComponents() {
		JLabel lblContent = new JLabel("<html><body>Epidemica<br>Version 1.0 (BeLL-Edition)<br>Copyright &copy; 2014 Manuel Joswig</body></html>");
		lblContent.setIcon(new ImageIcon(getClass().getResource("/icon.png")));
		add(lblContent);
	}
}