import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AirportPageFrame extends JFrame{
	
	private FindAirportFrame findAirport;
	private ConnectionGraph graph;

	private JPanel panel = new JPanel()	;
	private JButton findFlightButton = new JButton("Find Flights");
	private JButton backButton = new JButton("Back to Search Screen");
	private JButton exportAllButton = new JButton("Export All");
	private JButton exportTripButton = new JButton("Export Trip");
	private JTextField airportNameText = new JTextField(12);
	private JTextField codeNameText = new JTextField(12);
	private JTextField cityNameText = new JTextField(12);
	private JTextField countryNameText = new JTextField(12);
	private JComboBox<String> findFlightText = new JComboBox<>();
	private JTextArea companies = new JTextArea(2, 10);
	private JTextArea directFlightsText = new JTextArea(8, 28);
	private JTextArea indirectFlightsText = new JTextArea(8, 28);
	private JScrollPane companiesScroll = new JScrollPane(companies);
	private JScrollPane directFlightsScroll = new JScrollPane(directFlightsText);
	private JScrollPane indirectFlightsScroll = new JScrollPane(indirectFlightsText);

	private Airport departure;
	private Airport arrival;
	private String arrivalText;
	
	
	public AirportPageFrame(){
		
		this.initComponents();
		
		this.setContentPane(panel);
		
		this.setSize(700, 400);
		this.setResizable(false);
		this.setTitle("Airport Page");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();				//Locates the frame
		this.setLocation((int)(dimensions.getWidth()/2 - this.getWidth()/2),			//little lower
						 (int)(dimensions.getHeight()/2 - this.getHeight()/2 + 100));	//from the center of the screen
	}
	
	private void initComponents() {
		
		this.setComponents();
		
		this.setButtons();
		
		this.setLocations();
	}
	
	private void setComponents() {
		
		airportNameText.setEditable(false);
		codeNameText.setEditable(false);
		cityNameText.setEditable(false);
		countryNameText.setEditable(false);
		companies.setEditable(false);
		directFlightsText.setEditable(false);
		indirectFlightsText.setEditable(false);
		findFlightText.setEditable(true);
		
		airportNameText.setBackground(Color.WHITE);
		codeNameText.setBackground(Color.WHITE);
		cityNameText.setBackground(Color.WHITE);
		countryNameText.setBackground(Color.WHITE);
	}

	private void setButtons() {
		
		findFlightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				arrivalText = (String)findFlightText.getSelectedItem();
				arrival = CentralRegistry.getAirport(arrivalText);
						
				if(arrivalText.isEmpty())
					JOptionPane.showMessageDialog(null, "Please enter an Airport");
				else if(arrivalText.equals(departure.getCity()))
					JOptionPane.showMessageDialog(null, "Arrival and departure city cannot be the same!");
				else if(CentralRegistry.getAirport(arrivalText) == null)
					JOptionPane.showMessageDialog(null, arrivalText + " does not have an airport");
				else {
					addDetails();
					graph.resetGraph(departure, arrival);
				}
			}
		});
		
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				visible(false);
				graph.setGraph();
				graph.setVisible(false);
				findAirport.visible(true);
				exportTripButton.setVisible(false);
			}
		});
		
		exportTripButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				File file = new File(departure.getCity() + "To" + arrivalText + ".txt");
				
				try {
					FileWriter writer = new FileWriter(file);
					
					writer.write("CITY: " + departure.getCity() + ", " + departure.getCountry() + "\r\n"
							   + "Airport: " + departure.getName() + " (" + departure.getCode() + ")\r\n\r\n"
							   + "DESTINATION: " + arrivalText + "\r\n\r\n"
							   + CentralRegistry.getDirectFlightDetails(departure, arrival) + "\r\n"
							   + CentralRegistry.getInDirectFlightDetails(departure, arrival));
					writer.close();
					JOptionPane.showMessageDialog(null, "File was created successfully");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		exportAllButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File file = new File("From" + departure.getCity() + ".txt");
				
				try {
					FileWriter writer = new FileWriter(file);
					
					writer.write("CITY: " + departure.getCity() + ", " + departure.getCountry() + "\r\n"
							   + "Airport: " + departure.getName() + " (" + departure.getCode() + ")\r\n\r\n"
							   + CentralRegistry.getDirectlyConnectedAirportsToString(departure) + "\r\n"
							   + CentralRegistry.getIndirectlyConnectedAirportsToString(departure));
					writer.close();
					JOptionPane.showMessageDialog(null, "File was created successfully");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void setLocations() {
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(airportNameText, c);

		c.gridx = 1;
		panel.add(codeNameText, c);
		
		c.gridx = 2;
		panel.add(cityNameText, c);
		
		c.gridx = 3;
		panel.add(countryNameText, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		panel.add(findFlightText, c);
		
		c.gridx = 1;
		panel.add(findFlightButton, c);
		
		c.gridx = 2;
		panel.add(companiesScroll, c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(directFlightsScroll, c);

		c.gridx = 2;
		panel.add(indirectFlightsScroll, c);
		
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		panel.add(exportAllButton, c);
		
		c.gridx = 2;
		panel.add(exportTripButton, c);
		exportTripButton.setVisible(false);
		
		c.gridx = 1;
		c.gridy = 4;
		panel.add(backButton, c);
	}
	
	
	//Setting the page with the info of the (departure)
	public void setPage(Airport departure) {
		
		this.departure = departure;
		this.setVisible(true);
		
		airportNameText.setText(departure.getName());
		codeNameText.setText(departure.getCode());
		cityNameText.setText(departure.getCity());
		countryNameText.setText(departure.getCountry());
		
		companies.setText(null);
		Iterator<String> iter = departure.getCompanies().iterator();
		while(iter.hasNext()) {
			companies.setText(companies.getText() + iter.next());
			
			if(iter.hasNext())
				companies.setText(companies.getText() + "\n");
			}
		
		findFlightText.removeAllItems();
		for(Airport destination: CentralRegistry.airports)
			findFlightText.addItem(destination.getCity());
		findFlightText.removeItemAt(CentralRegistry.airports.indexOf(departure));
		
		directFlightsText.setText(CentralRegistry.getDirectlyConnectedAirportsToString(departure));
		indirectFlightsText.setText(CentralRegistry.getIndirectlyConnectedAirportsToString(departure));
	}
	
	//Adding the details about the two Airports
	//Revealing the export button
	private void addDetails() {
		directFlightsText.setText(
				CentralRegistry.getDirectFlightDetails(departure, arrival));
		indirectFlightsText.setText(
				CentralRegistry.getInDirectFlightDetails(departure, arrival));
		exportTripButton.setVisible(true);
	}
	
	
	public void visible(boolean flag) {
		this.setVisible(flag);
	}
	

	public void linkFindAirportPage(FindAirportFrame findAirport) {
		this.findAirport = findAirport;
	}
	
	public void linkGraph(ConnectionGraph graph) {
		this.graph = graph;
	}
}
