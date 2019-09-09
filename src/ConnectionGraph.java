import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class ConnectionGraph extends JFrame{
	
	private UndirectedSparseGraph<Airport, Flight> undirectedAirportGraph = new UndirectedSparseGraph<>();
	private BasicVisualizationServer<Airport, Flight> vs;
	private DijkstraDistance<Airport, Flight> graphDistance = new DijkstraDistance<>(undirectedAirportGraph);
	
	private JPanel panel = new JPanel();
	private JTextField distanceStatisticsField;
	
	public ConnectionGraph(){
		
		this.initComponents();
		
		this.setContentPane(panel);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(420, 420);
		this.setResizable(false);
		this.setTitle("City Airport Connections Network");
		this.setVisible(false);
		
		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();			//Locates the frame
		this.setLocation((int)(dimensions.getWidth()/2 - this.getWidth()/2), 0);	//to the top-center
	}

	private void initComponents() {
		
		this.setGraph();
		
		this.setVisualizationServer();
	
		this.setComponents();

		this.addComponents();
	}
	
	private void setVisualizationServer() {
		vs = new BasicVisualizationServer<>(
				new CircleLayout<Airport, Flight>(undirectedAirportGraph), new Dimension(300, 300));
		
		vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		vs.setPreferredSize(new Dimension(350, 350));
	}

	private void setComponents() {
		distanceStatisticsField.setPreferredSize(new Dimension(400, 20));
		distanceStatisticsField.setEditable(false);
		distanceStatisticsField.setBackground(Color.WHITE);
	}

	private void addComponents() {
		panel.add(vs);
		panel.add(distanceStatisticsField);
	}

	
	//Sets the Graph to its initial state
	public void setGraph() {
		for(Airport airport: CentralRegistry.airports)
			if(!undirectedAirportGraph.containsVertex(airport))
				undirectedAirportGraph.addVertex(airport);
		
		for(Flight flight: CentralRegistry.flights) {
			Pair<Airport> airportPair = new Pair<>(flight.getAirportA(), flight.getAirportB());
			if(!undirectedAirportGraph.containsEdge(flight))
				undirectedAirportGraph.addEdge(flight, airportPair);
		}
		
		distanceStatisticsField = new JTextField("Diameter = " +
												 DistanceStatistics.diameter(undirectedAirportGraph));
		this.repaint();
	}
	
	//Removes all the useless Flights and Airports
	public void resetGraph(Airport departure) {
		
		setGraph();

		for(Flight flight: CentralRegistry.flights)
			if(Math.round((double)graphDistance.getDistance(departure, flight.getAirportA())) == 2 &&
			   Math.round((double)graphDistance.getDistance(departure, flight.getAirportB())) == 2)
				undirectedAirportGraph.removeEdge(flight);
		for(Airport airport: CentralRegistry.airports)
			if(airport != departure)
				if((double)graphDistance.getDistance(airport, departure) > 2)
					undirectedAirportGraph.removeVertex(airport);
		
		this.repaint();
	}

	//Removes all the useless Flights and Airports
	public void resetGraph(Airport departure, Airport arrival) {

		resetGraph(departure);
		
		ArrayList<Airport> commonConnections = departure.getCommonConnections(arrival);
		
		for(Airport airport: CentralRegistry.airports)
			if(airport != departure && airport != arrival)
				if(!commonConnections.contains(airport))
					undirectedAirportGraph.removeVertex(airport);
		
		this.repaint();
	}
}
