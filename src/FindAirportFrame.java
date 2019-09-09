import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FindAirportFrame extends JFrame{

	private AirportPageFrame airportPage;
	private ConnectionGraph graph;
	
	private JPanel panel = new JPanel();
	private JButton searchButton = new JButton("Search");
	private JButton visualizeButton = new JButton("Visualize Network");
	private JComboBox<String> text = new JComboBox<>();
	
	private String airportName;
	
	
	public FindAirportFrame() {

		this.initComponents();
		
		this.setContentPane(panel);
		
		this.setVisible(true);
		this.setSize(300, 100);
		this.setResizable(false);
		this.setTitle("Find Airport");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();				//Locates the frame
		this.setLocation((int)(dimensions.getWidth()/2 - this.getWidth()/2),			//little lower
						 (int)(dimensions.getHeight()/2 - this.getWidth()/2 + 100));	//from the center of the screen
	}


	private void initComponents() {
		
		this.setComponents();
		
		this.setButtons();
		
		this.addComponents();
		
	}

	private void setComponents() {
		
		for(Airport departure: CentralRegistry.airports)
			text.addItem(departure.getCity());
		text.setEditable(true);
	}

	private void setButtons() {
		searchButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				airportName = (String)text.getSelectedItem();
				
				if(airportName.isEmpty())
					JOptionPane.showMessageDialog(null, "Please enter an Airport");
				else if(CentralRegistry.getAirport(airportName) == null)
					JOptionPane.showMessageDialog(null, airportName + " does not have an airport");
				else {
					airportPage.setPage(CentralRegistry.getAirport(airportName));
					graph.resetGraph(CentralRegistry.getAirport(airportName));
					visible(false);
				}
			}
		});
		
		visualizeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.setVisible(true);
			}
		});
	}

	private void addComponents() {
		panel.add(text);
		panel.add(searchButton);
		panel.add(visualizeButton);
	}
	
	
	public void visible(boolean flag) {
		this.setVisible(flag);
	}


	public void linkAirportPage(AirportPageFrame airportPage) {
		this.airportPage = airportPage;
	}
	
	public void linkGraph(ConnectionGraph graph) {
		this.graph = graph;
	}
}