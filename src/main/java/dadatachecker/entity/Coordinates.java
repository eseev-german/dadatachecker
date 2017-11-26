package dadatachecker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.Math.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {
    @JsonProperty("geo_lat")
    private Double latitude;
    @JsonProperty("geo_lon")
    private Double longitude;

    public Coordinates() {
    }

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(String latitude, String longitude) {
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (!latitude.equals(that.latitude)) return false;
        return longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;
    }


    public double distanceToPoint(Coordinates coordinates) {
        //111,2×arccos(sin φ1 × sin φ2 + cos φ1 × cos φ2 × cos (L2-L1));
        return 111.2 * acos(sin(latitude) * sin(coordinates.getLatitude()) + cos(latitude) * cos(coordinates.getLatitude()) * cos(coordinates.getLongitude() - longitude));
    }


}
