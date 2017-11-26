package dadatachecker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    @JsonIgnore
    private String name;
    private Coordinates data;

    public Address() {
    }

    public Address(String name, Coordinates data) {
        this.name = name;
        this.data = data;
    }

    public Coordinates getData() {
        return data;
    }

    public void setData(Coordinates data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address that = (Address) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + data.hashCode();
        return result;
    }
}
