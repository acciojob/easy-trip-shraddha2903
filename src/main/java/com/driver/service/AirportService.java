package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AirportService {
    @Autowired(required=true)
    private AirportRepository airportRepository = new AirportRepository();
    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        List<Airport> airports = airportRepository.getAllAirports();
        int max = Integer.MIN_VALUE;
        String name=airports.get(0).getAirportName();
        for(Airport port : airports)
        {
            if(port.getNoOfTerminals()>max)
            {
                max=port.getNoOfTerminals();
                name= port.getAirportName();
            }
            if(port.getNoOfTerminals()==max)
            {
                if(name.compareTo(port.getAirportName())>0)
                {
                    name=port.getAirportName();
                }
            }
        }
        return name;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flights = airportRepository.getAllFlight();

        double time = Double.MAX_VALUE;
        for(Flight ft : flights)
        {
            String from_city = ft.getFromCity().name();
            String to_city = ft.getToCity().name();
            if(from_city.equals(fromCity.name()) && to_city.equals(toCity.name()))
            {
                time = Math.min(time,ft.getDuration());
            }
        }
        if(time == Double.MAX_VALUE)
            return Double.valueOf("-1");
        return  time;
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }
    public boolean isAlreadyBooked(Integer passengerId,Integer flightId)
    {
        return airportRepository.checkPassengerAvailable(passengerId,flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        boolean isPassengerAlreadyBooked = isAlreadyBooked(passengerId,flightId);
        boolean is_flight_available = isFlightAvaiable(flightId);
        if(is_flight_available && !isPassengerAlreadyBooked)
        {
            airportRepository.addFlightPassengerDetails(flightId,passengerId);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    private boolean isFlightAvaiable(Integer flightId) {
        return airportRepository.isFlightAvailable(flightId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        int status = airportRepository.cancelTicket(flightId,passengerId);
        if(status == 0)return "FAILURE";
        return "SUCCESS";

    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        Optional<Airport> airportOpt = airportRepository.getAirportByName(airportName);
        if(airportOpt.isEmpty())
            throw new RuntimeException("airport not present");
        City city = airportOpt.get().getCity();
        List<Flight>flights = airportRepository.getAllFlight();
        int count=0;
        for(Flight fl: flights)
        {
            if(fl.getFlightDate().equals(date) && (fl.getFromCity().equals(city)) || fl.getToCity().equals(city))
            {
                count+=fl.getMaxCapacity();
            }
        }
        return count;
//       List<Flight> flights = flightOnGivenDate(date);
//       int count=0;
//       for(Flight fl: flights)
//       {
//           if(fl.getFromCity().equals(airportName))
//           {
//               count+= airportRepository.getNumberOfPeopleOn(fl.getFlightId());
//           }
//           if(fl.getToCity().equals(airportName))
//           {
//               count+= airportRepository.getNumberOfPeopleOn(fl.getFlightId());
//           }
//       }
//       return count;
    }
    public List<Flight> flightOnGivenDate(Date date)
    {
        List<Flight> flight =airportRepository.getFlightOnDate(date);
        if(!flight.isEmpty())
            return flight;
        throw new RuntimeException("No flight on particular Date: "+date);

    }

    public int calculateFlightFare(Integer flightId) {
        int totalPassenger=airportRepository.getNumberOfPeopleOn(flightId);
        return (3000 + totalPassenger * 50);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }
}
