package com.example.android.techeval;

import com.google.gson.annotations.SerializedName;

/**
 * A model object representing a Transport information
 */
public class LocationInfo {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("fromcentral")
    private FromCentral fromcentral;

    @SerializedName("location")
    private Location location;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public FromCentral getFromcentral() {
        return fromcentral;
    }

    public void setFromcentral(FromCentral fromcentral) {
        this.fromcentral = fromcentral;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public class FromCentral {
        @SerializedName("car")
        private String car;
        @SerializedName("train")
        private String train;

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getTrain() {
            return train;
        }

        public void setTrain(String train) {
            this.train = train;
        }
    }

    public class Location {
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
