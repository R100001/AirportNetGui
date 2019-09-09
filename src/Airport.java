import java.util.ArrayList;
import java.util.TreeSet;

public class Airport {

	private String name;
	private String code;
	private String city;
	private String country;
	
	private ArrayList<Airport> connectedAirports = new ArrayList<>();
	private TreeSet<String> companies = new TreeSet<>();
	
	
	public Airport(String name, String code, String city, String country) {
		this.name = name;
		this.code = code;
		this.city = city;
		this.country = country;
	}
	
	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getCode() {
		return code;
	}

	public String getCountry() {
		return country;
	}

	public TreeSet<String> getCompanies() {
		return companies;
	}

	public ArrayList<Airport> getConnectedAirports() {
		return connectedAirports;
	}
	
	
	//Updates the array of (this) Airport with the new connection
	public void addConnectedAirport(Airport anAirport) {
		
		if(!connectedAirports.contains(anAirport))
			connectedAirports.add(anAirport);
	}
	
	//Updates the array of (this) Airport with the new company serving it
	public void addCompany(String company) {
		companies.add(company);
	}
	
	
	//Returns true if the two Airports are directly connected
	public boolean isDirectlyConnectedTo(Airport anAirport) {

		for(Flight flight: CentralRegistry.flights) 
			if(flight.getAirportA() == this && flight.getAirportB() == anAirport ||
			   flight.getAirportB() == this && flight.getAirportA() == anAirport)
				  return true;
		return false;
	}
	
	//Returns true if the two Airports are indirectly connected
	public boolean isIndirectlyConnectedTo(Airport anAirport) {
		
		return !this.getCommonConnections(anAirport).isEmpty();
	}
	
	//Returns an array with the Airports connected to (this) Airport and to (anAirport)
	public ArrayList<Airport> getCommonConnections(Airport anAirport) {
		
		ArrayList<Airport> commonConnections = new ArrayList<>();
		
		for(Airport airportA: this.connectedAirports)
			if(anAirport.connectedAirports.contains(airportA))
					commonConnections.add(airportA);
		
		return commonConnections;
	}
	
	
	//Prints the companies serving (this) airport
	public void printCompanies() {
		for(String company: companies)
			System.out.println(company);
	}
	
	
	//This method is overrode only for the cause of labeling the graph
	public String toString() {
		return city;
	}
}
