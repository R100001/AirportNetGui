
public class Flight {

	private Airport airportA;
	private Airport airportB;
	private int flightDuration;
	private String company;


	public Flight(Airport airportA, Airport airportB, int flightDuration, String company) {
		this.airportA = airportA;
		this.airportB = airportB;
		this.flightDuration = flightDuration;
		this.company = company;
	}

	public Airport getAirportA() {
		return airportA;
	}

	public Airport getAirportB() {
		return airportB;
	}

	public String getCompany() {
		return company;
	}

	public int getFlightDuration() {
		return flightDuration;
	}
	
	
	public String toString() {
		return "Flight operated by " + company + ", duration " + flightDuration + " minutes";
	}
}
