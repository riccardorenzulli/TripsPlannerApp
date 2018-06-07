/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tripsplanner.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Authors: Giovanni Bonetta, Riccardo Renzulli, Gabriele Sartor<br>
 * Università degli Studi di Torino<br>
 * Department of Computer Science<br>
 * Sviluppo Software per Componenti e Servizi Web<br>
 * Date: May 2018<br><br>
 * <p/>
 * giovanni.bonetta@edu.unito.it<br>
 * riccardo.renzulli@edu.unito.it<br>
 * gabriele.sartor@edu.unito.it<br><br>
 */

public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private User owner;
    
    private List<User> collaborators = new ArrayList<>();
    
    private Hotel hotel;
    
    private List<DayItinerary> itineraries;
    
    private Search search;

    public Trip() {
        this.owner = null;
        this.collaborators = null;
        this.itineraries = null;
        this.search = null;
        this.hotel = null;
    }
    
    public Trip(User owner, ArrayList<User> collaborators, ArrayList<DayItinerary> itineraries, Search search, Hotel hotel) {
        this.owner = owner;
        this.collaborators = collaborators;
        this.itineraries = itineraries;
        this.search = search;
        this.hotel = hotel;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<User> collaborators) {
        this.collaborators = collaborators;
    }

    public List<DayItinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(ArrayList<DayItinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trip)) {
            return false;
        }
        Trip other = (Trip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Trip{" + "itineraries=" + itineraries + '}';
    }


    
    /*Return the places to be visited the day specified by the parameter*/
    public List<Place> getDayPlaces(int day) {
        if(day > this.itineraries.size() || day < 0)
            new IllegalArgumentException("The trip is not that long!");
        DayItinerary dayItinerary = this.itineraries.get(day);
        List<Place> result = new ArrayList();
        for(Route route : dayItinerary.getLegs()) {
            result.add(route.getDeparturePlace());
        }
        /*Add the last place*/
        int lastLeg = dayItinerary.getLegs().size()-1;
        result.add(dayItinerary.getLegs().get(lastLeg).getArrivalPlace());
        return result;
    }

    public List<Route> getDayRoute(int day) {
        if(day > this.itineraries.size() || day < 0)
            new IllegalArgumentException("The trip is not that long!");
        DayItinerary dayItinerary = this.itineraries.get(day);
        List<Route> result = new ArrayList();
        /*Add the last place*/
        return dayItinerary.getLegs();
    }
    
}
