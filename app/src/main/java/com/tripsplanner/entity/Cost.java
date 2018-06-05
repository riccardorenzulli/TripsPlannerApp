/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tripsplanner.entity;

import java.io.Serializable;

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

public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Double flight;
    private Double hotel;
    private Double transit;

    public Cost(Double flight, Double hotel, Double transit) {
        this.flight = flight;
        this.hotel = hotel;
        this.transit = transit;
    }

    public Cost() {
        this.flight = null;
        this.hotel = null;
        this.transit = null;
    }   
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFlight() {
        return flight;
    }

    public void setFlight(Double flight) {
        this.flight = flight;
    }

    public Double getHotel() {
        return hotel;
    }

    public void setHotel(Double hotel) {
        this.hotel = hotel;
    }

    public Double getTransit() {
        return transit;
    }

    public void setTransit(Double transit) {
        this.transit = transit;
    }
    
    public Double getTot() {
        return this.flight + this.hotel + this.transit;
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
        if (!(object instanceof Cost)) {
            return false;
        }
        Cost other = (Cost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tripsplanner.model.entity.Cost[ id=" + id + " ]";
    }
    
}
