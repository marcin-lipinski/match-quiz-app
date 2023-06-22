package pl.marcinlipinski.matchquizapp.models.DTOs;

import com.google.gson.annotations.SerializedName;

public class TeamDTO {
    @SerializedName("venue")
    public Venue venue;

    public String getCity() {
        return this.venue.city.en;
    }
}

class Venue {
    public City city;
}

class City {
    public String en;
}
