package com.tripsplanner.entity;

import java.sql.Date;

public class BasicTrip {
    private Long idTrip;
    private String destination;
    private Date departureDate;
    private Date arrivalDate;
    private String placeIMGUrl;

    public BasicTrip() {}

    public BasicTrip(Long idTrip, String destination, Date departureDate, Date arrivalDate, String placeIMGUrl) {
        this.idTrip = idTrip;
        this.destination = destination;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.placeIMGUrl = placeIMGUrl;
    }

    public String getPlaceIMGUrl() {
        return placeIMGUrl;
    }

    public void setPlaceIMGUrl(String placeIMGUrl) {
        this.placeIMGUrl = placeIMGUrl;
    }

    public Long getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(Long idTrip) {
        this.idTrip = idTrip;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
