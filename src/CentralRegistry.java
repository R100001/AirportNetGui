import java.util.ArrayList;

public class CentralRegistry {

	public static ArrayList<Airport> airports = new ArrayList<>();
	public static ArrayList<Flight> flights = new ArrayList<>();
	
	
	//Adds the (airport) to the array of airports
	public static void addAirport(Airport airport) {
		airports.add(airport);
	}
	
	//Adds the (flight) to the array of flights
	//Also updates the arrays of the connected airports and companies they serve
	public static void addFlight(Flight flight) {
		
		flights.add(flight);
		
		Airport airportA = flight.getAirportA();
		Airport airportB = flight.getAirportB();
		
		airportA.addConnectedAirport(airportB);
		airportB.addConnectedAirport(airportA);
		
		if(!airportA.getCompanies().contains(flight.getCompany()))
				airportA.addCompany(flight.getCompany());
		if(!airportB.getCompanies().contains(flight.getCompany()))
			airportB.addCompany(flight.getCompany());
	}
	
	
	//Returns the Airport located to (city)
	public static Airport getAirport(String city){
		
		for(Airport airport: CentralRegistry.airports)
			if(airport.getCity().equals(city))
				return airport;
		
		return null;
	}
	
	
	//Returns the airport with the most planned flights
	public static Airport getLargestHub() {
		
			Airport largestHub = airports.get(0);
			
			for(Airport airport: airports)
				if(largestHub.getConnectedAirports().size() < airport.getConnectedAirports().size())
					largestHub = airport;
			
			return largestHub;
	}
	
	//Returns the flight with the longest duration
	public static Flight getLongestFlight() {
		
		Flight longestFlight = flights.get(0);
		
		for(Flight flight: flights) 
			if(longestFlight.getFlightDuration() < flight.getFlightDuration())
				longestFlight = flight;
		
		return longestFlight;
	}
	
	//Returns a formatted String with the Airports directly connected to (this) Airport
	public static String getDirectlyConnectedAirportsToString(Airport airport) {

		if(!airport.getConnectedAirports().isEmpty()) {
			
			String directlyConnectedAirports = "DIRECT FLIGHTS TO:\r\n";
			int airportCount = 1;
			
			for(Airport directAirport: airport.getConnectedAirports()) {
				
				directlyConnectedAirports += "[" + airportCount + "]" + directAirport.getCity() + "\r\n";
				airportCount++;
			}
			
			return directlyConnectedAirports;
		}
		else
			return null;
	}

	//Returns a formatted String with the Airports indirectly connected to (this) Airport
	//separated for every directly connected Airport
	public static String getIndirectlyConnectedAirportsToString(Airport airport) {
		
		String indirectlyConnectedAirports = "INDIRECT FLIGHTS TO:\r\n\r\n";
		
		if(!airport.getConnectedAirports().isEmpty()) {
			for(Airport directAirport: airport.getConnectedAirports()) {
				
				indirectlyConnectedAirports += "From " + directAirport.getCity() + ":\r\n";
				int airportCount = 1;
				
				if(directAirport.getConnectedAirports().size() > 1) {
					for(Airport indirectAirport: directAirport.getConnectedAirports())
						if(indirectAirport != airport) {
							
							indirectlyConnectedAirports += "[" + airportCount + "]" 
														+ indirectAirport.getCity() + "\r\n";
							airportCount++;
						}
					
					indirectlyConnectedAirports += "\r\n";
				}
				else
					indirectlyConnectedAirports += "There's no indirectly connected airports\r\n\r\n";
			}
			return indirectlyConnectedAirports;
		}
		else
			return null;
		
	}
	
	//Returns a String with all the Direct Flights and their details
	public static String getDirectFlightDetails(Airport airportA, Airport airportB) {
		
		if(airportA.isDirectlyConnectedTo(airportB)) {

			String flightDetails = "DIRECT FLIGHTS DETAILS\r\n";
			int airportCount = 1;
			
			for(Flight flight: CentralRegistry.flights)
				if(flight.getAirportA() == airportA && flight.getAirportB() == airportB ||
				   flight.getAirportB() == airportA && flight.getAirportA() == airportB) {
					
					   flightDetails += "[" + airportCount + "]" + flight.toString() + "\r\n";
					   airportCount++;
				}
			return flightDetails;
		}
		else
			return "NO DIRECT FLIGHTS\r\n";
	}

	//Returns a String with all the Indirect Flights and their details
	public static String getInDirectFlightDetails(Airport airportA, Airport airportB) {
		
		int connectionCount = 1;
		ArrayList<Airport> connections = airportA.getCommonConnections(airportB);
		
		if(!connections.isEmpty()) {
			
			String detailsOfFlights = "INDIRECT FLIGHTS through...\r\n";
			
			for(Airport connection: connections) {
				
					   detailsOfFlights += "[" + connectionCount + "]" 
							   			+ connection.getCity() + "," + connection.getCode() + " airport\r\n";
					   connectionCount++;
				}
			return detailsOfFlights;
		}
		else
			return "NO INDIRECT FLIGHTS\r\n";
	}
}
