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

public class DayItinerary implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    private Trip trip;
    private List<Route> legs = new ArrayList<Route>();
    
    public DayItinerary() {
        this.legs = null;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Route> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Route> legs) {
        this.legs = legs;
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
        if (!(object instanceof DayItinerary)) {
            return false;
        }
        DayItinerary other = (DayItinerary) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DayItinerary{" + "id=" + id + ", trip=" + trip.getId() + ", legs=" + legs + '}';
    }


    
}
