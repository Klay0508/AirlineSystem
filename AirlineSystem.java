/**
 * Class:AirlineSystem
 * Author: Kiyoshi Lay
 * Description: This program is going to simulate an airline reservation system where the user can look for flights based
 * on either the brand,departure date, origin, and destination.The origin and destination are in IATA Code for example:
 * ATL,MIA,etc.The flights are based on what the user chooses and will give them a list of available seats
 * that the user must pick from. Once the user picks a seat it will prompt them for personal information so that
 * we can store the seat and the unique id it comes with to that user.The user is also allowed to cancel their ticket
 * or change their ticket for a different flight
 */
package FlightProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AirlineSystem {
    private PersonalInformation pi = new PersonalInformation();
    public static void main(String[] args) {
        Map<String,PlaneTicket> planeTickets = new HashMap<>();
        List<FlightInformation> purchasedFlight = new ArrayList<>();
        AirlineSystem system = new AirlineSystem();
        Scanner scanner = new Scanner(System.in);


        boolean valid = false;
        while(!valid) {
            System.out.println("""
                    Welcome to TB Airline Systems!
                    Please select from the following options:
                    1.)Search by Airline brand
                    2.)Search for flights by departure time
                    3.)Search for flights by origin airport in IATA Code Ex:ATL
                    4.)Search for flights by destination airport in IATA Code Ex:JFK
                    5.)Check my flight information
                    6.)Modify my ticket
                    7.)Exit""");

            List<FlightInformation> f = system.flightDestinations();

            try {
                switch (scanner.nextInt()) {
                    case 1:
                        List<FlightInformation> selectedFlights = system.flightDestinationsCheck(f);
                        if(selectedFlights.isEmpty()){
                            break;
                        }
                        purchasedFlight = system.purchaseFlight(selectedFlights);
                        if(purchasedFlight.isEmpty()){
                            break;
                        }
                        if(planeTickets.isEmpty()){
                            planeTickets = system.assignSeat(purchasedFlight);
                            system.purchaseSeat(planeTickets,system.pi);
                        }else {
                            System.out.println("Sorry you have already purchased a seat for a flight");
                        }
                        break;
                    case 2:
                        List<FlightInformation> departure = system.departureTime(f);
                        if(departure.isEmpty()){
                            break;
                        }
                        purchasedFlight = system.purchaseFlight(departure);
                        if(purchasedFlight.isEmpty()){
                            break;
                        }
                        if(planeTickets.isEmpty()){
                            planeTickets = system.assignSeat(purchasedFlight);
                            system.purchaseSeat(planeTickets,system.pi);
                        }else {
                            System.out.println("Sorry you have already purchased a seat for a flight");
                        }
                        break;
                    case 3:
                        List<FlightInformation> selectOrigin = system.origin(f);
                        if(selectOrigin.isEmpty()){
                            break;
                        }
                        purchasedFlight = system.purchaseFlight(selectOrigin);
                        if(purchasedFlight.isEmpty()){
                            break;
                        }
                        if(planeTickets.isEmpty()){
                            planeTickets = system.assignSeat(purchasedFlight);
                            system.purchaseSeat(planeTickets,system.pi);
                        }else {
                            System.out.println("Sorry you have already purchased a seat for a flight");
                        }
                        break;
                    case 4:
                        List<FlightInformation> selectDestination = system.destination(f);
                        if(selectDestination.isEmpty()){
                            break;
                        }
                        purchasedFlight = system.purchaseFlight(selectDestination);
                        if(purchasedFlight.isEmpty()){
                            break;
                        }
                        if(planeTickets.isEmpty()){
                            planeTickets = system.assignSeat(purchasedFlight);
                            system.purchaseSeat(planeTickets,system.pi);
                        }else {
                            System.out.println("Sorry you have already purchased a seat for a flight");
                        }
                        break;
                    case 5:
                        if(system.pi.getId() == null){
                            System.out.println("You dont have a current ticket to view");
                        }else {
                            System.out.println("Hello  " + system.pi.getName() +" your flight is:");
                            for(FlightInformation flight : purchasedFlight){
                                System.out.println(flight.toString());
                            }
                            for (Map.Entry<String, PlaneTicket> entry : planeTickets.entrySet()) {
                                if (entry.getValue().getUuid().equals(system.pi.getId())) {
                                    System.out.println(entry.getKey());
                                    break;
                                }
                            }
                        }
                        break;
                    case 6:
                        if(system.pi.getId() == null){
                            System.out.println("You dont have a current ticket to modify");
                        }else {
                            system.modifyTicket(system.pi,planeTickets,f);
                        }
                        break;
                    case 7:
                        try {
                            System.out.println("Good-bye....");
                            Thread.sleep(1000);
                            valid = true;
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                }
            } catch (InputMismatchException ime) {
                System.out.println("Please enter a valid input.");
            }
        }
    }

    /**
     * Description: This will read from a file that contains a list of flights
     *
     * @return returns a list containing flight information
     */
    public List<FlightInformation> flightDestinations() {
        List<FlightInformation> flights = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/FlightProject/FlightList"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == 6) {
                    FlightInformation fl = new FlightInformation();
                    fl.setAirLineBrand(split[0]);
                    fl.setFlightNumber(Integer.parseInt(split[1]));
                    fl.setFlightOrigin(split[2]);
                    fl.setDestination(split[3]);
                    fl.setFlightTime(split[4]);
                    fl.setAvailableSeats(Integer.parseInt(split[5]));

                    flights.add(fl);
                } else {
                    System.out.println("skipped not enough lines");
                }

            }
            br.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
        return flights;
    }

    /**
     * Description: This method will check each flight based on the users input for the brand name and
     * output all the flights with the brand
     *
     * @param flights takes a list of flight information
     */
    public List<FlightInformation> flightDestinationsCheck(List<FlightInformation> flights) {
        List<FlightInformation> flightsCheck = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("""
                Please type which airline you would like to search for or press 0 to exit
                Delta Airlines
                United Airlines
                American Airlines
                Spirit Airlines""");
        boolean valid = false;
        while (!valid) {
            String input = scan.nextLine();
            if(input.equals("0")){
                break;
            }
            for (FlightInformation flight : flights) {
                if (input.equalsIgnoreCase(flight.getAirLineBrand())) {
                    flightsCheck.add(flight);
                    System.out.println(flight);
                    valid = true;
                }
            }
            if (!valid) {
                System.out.println("Sorry no flights found");
            }
        }
        return flightsCheck;
    }

    /**
     * Description: This method will take a list of flight information and asks the user which flight
     * they would like to purchase for based on the flight number
     *
     * @param flights takes a list of flight information
     */
    public List<FlightInformation> purchaseFlight(List<FlightInformation> flights) {
        List<FlightInformation> flightInformations = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("""
                \nWhich Flight would you like to purchase?
                Please type in the flight number
                If you do not want to purchase anything type 0""");
        boolean valid = false;
        while (!valid) {
            int flightNumber = scan.nextInt();
            if (flightNumber == 0) {
                break;
            }
            for (FlightInformation flight : flights) {
                if (flight.getFlightNumber() == flightNumber) {
                    System.out.println("You have selected the flight number " + flightNumber + flight);
                    flightInformations.add(flight);
                    valid = true;
                }
            }
            if (!valid) {
                System.out.println("Sorry flight number does not exist");
            }
        }
        return flightInformations;
    }

    /**
     * Description: This method will create a Map to store the seat number and the availability of the tickets,
     * each seat will have a letter and a number next to it the max a letter can have is 10 so this is going to
     * create the letter and assign it a number for the seat.Once we create the seat letter and number we assign
     * each seat a random availability with a UUID so each one is unique.We store the new information into the Map
     * @param flights list of flight information
     * @return returns a Map with string and plane ticket
     */
    public Map<String, PlaneTicket> assignSeat(List<FlightInformation> flights) {
        Map<String, PlaneTicket> pt = new HashMap<>();
        int totalSeats = flights.getFirst().getAvailableSeats();
        int seatsPerRow = 10;

        for (int i = 0; i <= totalSeats; i++) {
            int row = i / seatsPerRow;
            int seatNumber = (i % seatsPerRow) + 1;

            Character seat = (char) ('A' + row);//we use ascii code to go to next letter
            String seatLabel = String.valueOf(seat) + seatNumber;//turn the char into a string and add the seat number to it
            PlaneTicket planeTicket = new PlaneTicket();//create a plane ticket object
            Random random = new Random();
            planeTicket.setUuid(UUID.randomUUID());//we randomly set a UUID
            planeTicket.setSold(random.nextBoolean());//we randomly set a boolean to each ticket
            pt.put(seatLabel, planeTicket);
        }
//        System.out.println(pt.entrySet());
        return pt;
    }

    /**
     * Description: This method will print out a list of available seats and the user can pick which ever seat they want.
     * Once they pick the seat they want it will ask for there information and give them a unique id for that ticket.
     * This will also mark the ticket as sold so that it won't show up again in the list of seats.
     * @param pt
     */
    public void purchaseSeat(Map<String, PlaneTicket> pt,PersonalInformation personalInformation) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select the seat number you would like to purchase");
        List<String> seats = new ArrayList<>();//list to store seats
//           int seatNumber = scan.nextInt();
        for (Map.Entry<String, PlaneTicket> entry : pt.entrySet()) {
            if (!entry.getValue().isSold()) {//if the value is false which means its available
                seats.add(entry.getKey());//we add it to the list
                if (seats.size() >= 5) {//we will then print it in rows of 5
                    System.out.println(String.join(" ",seats));
                    seats.clear();//we clear the list so that we are able to print out only 5 at a time
                }
            }
        }
        if(!seats.isEmpty()){//this will get any remaining seats that couldn't be printed if there weren't 5 or more
            System.out.println(String.join(" ",seats));
        }

        boolean valid = false;
        while (!valid) {
            String input = scan.nextLine();
            for (Map.Entry<String, PlaneTicket> entry : pt.entrySet()) {
                if (input.equalsIgnoreCase(entry.getKey()) && !entry.getValue().isSold()) {//user input must match the seat
                    System.out.println("Your seat is: " + entry.getKey());
                    if (pi != null) {
                        /*checking if the pi isn't null, so if the user still has their personal data it will skip
                        creating a personal information object and just store the key and seat in it
                         */
                        pi.setSeat(entry.getKey());
                        pi.setId(entry.getValue().getUuid());
                        entry.getValue().setSold(true);
                        valid = true;
                        System.out.println("Your ticket has been changed!!!");
                    } else {//if it is we create a personal information
                        pi = personalInfo(entry.getValue().getUuid(), entry.getKey());//calls a method to verify the user
                        entry.getValue().setSold(true);//we set the boolean to true so it's marked as unavailable
                        valid = true;
                        break;
                    }
                }
            }if(!valid){
                System.out.println("Sorry no seats found");
            }
        }
    }

    /**
     * Description: This method will create a personal information object to store the users info and assign them
     * the seat and key that they selected
     * @param uuid a UUID
     * @param seat a String with the seat
     * @return returns a personal information obejct
     */
    public PersonalInformation personalInfo(UUID uuid,String seat) {
        System.out.println("To keep this ticket secured to you please enter your personal information:");
        Scanner scan = new Scanner(System.in);
        PersonalInformation pi = new PersonalInformation();

        System.out.println("Please enter your first name:");
        String firstName = scan.nextLine();

        System.out.println("Please enter your last name:");
        String lastName = scan.nextLine();

        System.out.println("Please enter your email address:");
        String email = scan.nextLine();

        pi.setName(firstName);
        pi.setLastName(lastName);
        pi.setEmail(email);

        boolean valid = false;
        while(!valid) {
            /*
            we check for the phone variable in a try and catch to make sure it has 10 digits, I have a custom
            exception which is thrown to make sure the length is 10
             */
            try {
                System.out.println("Please enter your phone number:");
                long phone = scan.nextLong();
                scan.nextLine();

                pi.setPhoneNumber(phone);
                valid = true;
            } catch (PersonalException pe) {
                System.err.println(pe.getMessage());
            }catch (InputMismatchException ime) {
                System.out.println("Please enter a valid input.");
            }
        }
        pi.setSeat(seat);
        pi.setId(uuid);//we set the UUID which is passed in as a parameter
//        System.out.println(pi.getId());
        System.out.println("Successfully added ticket reservation");
        return pi;
    }

    /**
     * Description: This method will search for flights based on the departure time
     * @param flights list of flight information
     * @return returns a list of flight information that stores the  flights based on the time
     */
    public List<FlightInformation> departureTime(List<FlightInformation> flights) {
        List<FlightInformation> departureTimes = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a time to look up flights in 0:00 am/pm format or type 0 to exit");

        boolean valid = false;
        while (!valid) {
            String time = scan.nextLine();
            if(time.equalsIgnoreCase("0")){
                break;
            }
            for (FlightInformation flight : flights) {
                if (time.equalsIgnoreCase(flight.getFlightTime())) {
                    System.out.println(flight);
                    departureTimes.add(flight);
                    valid = true;
                }
            }
            if (!valid) {
                System.out.println("Sorry no available flights at the selected time");
            }
        }
        return departureTimes;
    }

    /**
     * Description: This method wil search for flights bsed on origin in IATA Code
     * @param flights takes a list of flight information
     * @return returns a list of flight information based on origin
     */
    public List<FlightInformation> origin(List<FlightInformation> flights) {
        List<FlightInformation> originFlights = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the origin city in IATA Code or type 0 to exit");

        boolean valid = false;
        while (!valid) {//loops until all same times are printed
            String city = scan.nextLine();
            if(city.equalsIgnoreCase("0")){//exits and goes back to main menu
                break;
            }
            for (FlightInformation flight : flights) {//loops through the flights to get the selected time fpr user
                if(city.equalsIgnoreCase(flight.getFlightOrigin())){
                    System.out.println(flight);
                    originFlights.add(flight);
                    valid = true;
                }
            }
            if (!valid) {
                System.out.println("Sorry no available flights at the selected origin");
            }
        }
        return originFlights;
    }

    /**
     * Description: This method will search for flights based on destination in IATA Code
     * @param flights passes a list of flight information
     * @return returns a list of flight information based on destination
     */
    public List<FlightInformation> destination(List<FlightInformation> flights) {
        List<FlightInformation> destinationFlights = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select a departure city in IATA Code or type 0 to exit");

        boolean valid = false;
        while (!valid) {
            String city = scan.nextLine();
            if(city.equalsIgnoreCase("0")){//exits, and goes back to main menu
                break;
            }
            for (FlightInformation flight : flights) {//loops through list to get selected destinations only
                if(city.equalsIgnoreCase(flight.getDestination())){
                    System.out.println(flight);
                    destinationFlights.add(flight);
                    valid = true;
                }
            }
            if (!valid) {
                System.out.println("Sorry no available flights at the selected destination");
            }
        }
        return destinationFlights;
    }

    /**
     * Description: This method will modify the user ticket if they have one, they can either cancel or change their
     * ticket
     * @param pi passes a personal information object
     * @param tickets passes a map of string and plane Ticker object
     * @param flights passes a list of flight information
     */
    public void modifyTicket(PersonalInformation pi,Map<String,PlaneTicket> tickets,List<FlightInformation> flights) {
        System.out.println("""
                What would you like to do with your ticket?
                1.)Cancel
                2.)Change""");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();

        boolean valid = false;
        while (!valid) {
            switch (choice) {
                case 1:
                    cancelTicket(pi, tickets);//calls a method which will cancel the ticket
                    valid = true;
                    break;
                case 2:
                    changeTicket(pi, tickets, flights);//calls a method which will change the ticket
                    valid = true;
                    break;
                default://if the user doesnt pick 1 or 2 this will run
                    System.out.println("Invalid choice");
            }
        }
    }

    /**
     *
     * @param pi
     * @param tickets
     */
    private void cancelTicket(PersonalInformation pi,Map<String,PlaneTicket> tickets) {
        System.out.println("Cancelling your ticket give me a few seconds...");
        try {
            Thread.sleep(3000);
            String seat = pi.getSeat();
            for (Map.Entry<String, PlaneTicket> entry : tickets.entrySet()) {
                if (entry.getKey().equals(seat)) {
                    entry.getValue().setSold(false);
                    pi.setId(null);
                    pi.setSeat(null);
                    pi.setEmail(null);
                    pi.setName(null);
                    pi.setLastName(null);
                    try {
                        pi.setPhoneNumber(0);
                    }catch (PersonalException pe) {
                        System.out.println(pe.getMessage());
                    }
                    System.out.println("Successfully cancelled ticket!");
                }
            }
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Description: This method will change the ticket for the user, it will check the seat adn the seat in the map to
     * make sure they are the same.Then it will set the uuid and seat stored in the personal information to null to clear
     * it.We than make sure to change the boolean of the seat to false so that it is marked as available again/\.
     * @param pi passes a personal information object
     * @param tickets passes a map of string and plane ticket object
     * @param flights passes a list of flight information
     */
    private void changeTicket(PersonalInformation pi,Map<String,PlaneTicket> tickets,List<FlightInformation> flights) {
        System.out.println("Clearing your ticket give me a few seconds...");
        try{
            Thread.sleep(2000);
            for (Map.Entry<String,PlaneTicket> entry : tickets.entrySet()) {//loop through the entry set of tickets
                if(entry.getKey().equals(pi.getSeat())){//checks to make sure that the entry key matches the pi seat
                    entry.getValue().setSold(false);//we set the boolean to false to make it available
                    pi.setSeat(null);//clears the seat in pi
                    pi.setId(null);//clears the uuid in pi
                }
            }
        }catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Here's the list of flights available today:");
        for(FlightInformation flight : flights){
            System.out.println(flight);
        }
        confirmTicketChange(flights);
    }

    /**
     * Description: This method will give the user all the list of flights they can switch too
     * @param flightInformations passes a list of flight information
     */
    public void confirmTicketChange(List<FlightInformation> flightInformations){
        List<FlightInformation> confirmFlights;
        Map<String,PlaneTicket> tickets;
        confirmFlights = purchaseFlight(flightInformations);//calls the purchaseFlight method and stores it in a list
        tickets = assignSeat(confirmFlights);//calls the assignSeat method and stores it in tickets
        purchaseSeat(tickets,pi);//calls the purchaseSeat method
    }
}