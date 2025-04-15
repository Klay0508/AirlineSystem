package FlightProject;

public class FlightInformation {
    private String airLineBrand;
    private int flightNumber;
    private String flightOrigin;
    private String Destination;
    private String flightTime;
    private int availableSeats;

    public FlightInformation() {
    }


    public FlightInformation(String airLineBrand, int flightNumber, String flightOrigin, String destination, String flightTime, int availableSeats) {
        this.airLineBrand = airLineBrand;
        this.flightNumber = flightNumber;
        this.flightOrigin = flightOrigin;
        Destination = destination;
        this.flightTime = flightTime;
        this.availableSeats = availableSeats;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getFlightOrigin() {
        return flightOrigin;
    }

    public void setFlightOrigin(String flightOrigin) {
        this.flightOrigin = flightOrigin;
    }

    public String getAirLineBrand() {
        return airLineBrand;
    }

    public void setAirLineBrand(String airLineBrand) {
        this.airLineBrand = airLineBrand;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return  "\nAirLineBrand: " + airLineBrand  +
                "\nFlightNumber: " + flightNumber +
                "\nFlightOrigin: " + flightOrigin +
                "\nDestination: " + Destination +
                "\nFlightTime: " + flightTime +
                "\nSeats: " + availableSeats;
    }
}
