package com.l230954.cinefast;

import java.util.HashMap;

public class Bookings {
    private String movie;
    private String date;
    private String time;
    private int tickets;

    public Bookings() {}
    public Bookings(String movie, String date, String time, int tickets) {
        this.movie = movie;
        this.date = date;
        this.time = time;
        this.tickets = tickets;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("movie", movie);
        result.put("date", date);
        result.put("time", time);
        result.put("tickets", tickets);
        return result;
    }
}
