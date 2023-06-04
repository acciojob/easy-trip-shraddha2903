package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class AirportRepository {
    private Map<String,Airport> airportData=new HashMap<>();
    private Map<Integer, Flight> flightMap = new HashMap<>();
    private Map<Integer,Passenger> passengerMap = new HashMap<>();
    private Map<Integer, List<Integer>> passengeFlightPair = new HashMap<>();//passenge->flights
    private Map<Integer,List<Integer>> flightPassengerDetails = new HashMap<>();//Flight->passenger
    public void addAirport(Airport airport) {
        airportData.put(airport.getAirportName(), airport);
    }

    public List<Airport> getAllAirports() {
       return new ArrayList<>(airportData.values());
    }

    public List<Flight> getAllFlight()
    {
        return new ArrayList<>(flightMap.values());
    }

    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId(),passenger);
    }

    public Optional<Flight> getAirportNameFromFlightId(Integer flightId) {
//        String airport_name = null;
//        for(Map.Entry<Integer,Flight> hm : flightMap.entrySet())
//        {
//            if(hm.getKey().equals(flightId)){
//                airport_name = hm.getValue().getFromCity().name();
//                break;
//            }
//        }
//        return airport_name;
        if(flightMap.containsKey(flightId))
            return Optional.of(flightMap.get(flightId));
        return Optional.empty();

    }

    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightId(),flight);
    }

    public boolean checkPassengerAvailable(Integer passengerId,Integer flightId) {
        List<Integer> list = flightPassengerDetails.get(flightId);
        for(Integer it : list)
        {
            if(it.equals(passengerId))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isFlightAvailable(Integer flightId) {
        Flight flight =flightMap.get(flightId);
        int capacity = flight.getMaxCapacity();
        int currentBooking = flightPassengerDetails.get(flightId).size();
        if(currentBooking>=capacity)
            return false;
        return true;

    }

    public void addFlightPassengerDetails(Integer flightId, Integer passengerId) {
        //passengerMap.get(passengerId);
        //flight->list of passenger
        List<Integer> passenger_list = flightPassengerDetails.getOrDefault(flightId,new ArrayList<>());
        passenger_list.add(passengerId);
        flightPassengerDetails.put(passengerId,passenger_list);

        //passenger->list of flights
        List<Integer> flight_list = passengeFlightPair.getOrDefault(passengerId,new ArrayList<>());
        flight_list.add(flightId);
        passengeFlightPair.put(passengerId,flight_list);


    }

//    public int cancelTicket(Integer flightId, Integer passengerId) {
//       // boolean status=false;
//        if(!flightMap.containsKey(flightId))
//        {
//           return 0;
//        }
//        if(!passengerMap.containsKey(passengerId))
//            return 0;
//        passengerMap.remove(passengerId);
//        List<Integer> list = flightPassengerDetails.get(flightId);
//        for(Integer it : list)
//        {
//            if(it==passengerId)
//                list.remove(it);
//        }
//        return 1;
//    }
    public List<Flight> getFlightOnDate(Date date) {
        List<Flight> flight = new ArrayList<>();
        for(Map.Entry<Integer,Flight> hm : flightMap.entrySet())
        {
            if(hm.getValue().getFlightDate().equals(date))
            {
                flight.add(hm.getValue());
            }
        }
        return flight;
    }
    public int getNumberOfPeopleOn(int flightId) {
        return flightPassengerDetails.get(flightId).size();
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        if(passengeFlightPair.containsKey(passengerId))
            return passengeFlightPair.get(passengerId).size();
        return 0;
    }

    public Optional<Airport> getAirportByName(String airportName) {
        if(airportData.containsKey(airportName))
        {
            return Optional.of(airportData.get(airportName));
        }
        return Optional.empty();
    }

    public List<Integer> getPassengerForFlight(Integer flightId) {
        if(flightPassengerDetails.containsKey(flightId))
            return flightPassengerDetails.get(flightId);
        return Collections.singletonList(0);
    }

    public Optional<Passenger> getPassengerById(Integer passengerId) {
        if(passengerMap.containsKey(passengerId))
            return Optional.of(passengerMap.get(passengerId));
        return Optional.empty();
    }

    public void bookFlight(Integer flightId, Integer passengerId) {
        List<Integer> psg = flightPassengerDetails.getOrDefault(flightId,new ArrayList<>());
        psg.add(passengerId);
        flightPassengerDetails.put(flightId,psg);

    }

    public void cancelFlight(Integer flightId, Integer passengerId) {
        List<Integer> pss = flightPassengerDetails.getOrDefault(flightId,new ArrayList<>());
        pss.remove(passengerId);
        flightPassengerDetails.put(flightId,pss);
    }

    public Map<Integer, List<Integer>> getAllFlightBooking() {
        return flightPassengerDetails;
    }
}
