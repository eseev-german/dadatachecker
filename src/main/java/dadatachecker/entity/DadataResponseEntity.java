package dadatachecker.entity;

import java.util.List;


public class DadataResponseEntity {
    private List<Address> suggestions;

    public List<Address> getSuggestions() {

        return suggestions;
    }

    public void setSuggestions(List<Address> addresses) {
        this.suggestions = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DadataResponseEntity that = (DadataResponseEntity) o;

        return suggestions != null ? suggestions.equals(that.suggestions) : that.suggestions == null;
    }

    @Override
    public int hashCode() {
        return suggestions != null ? suggestions.hashCode() : 0;
    }
}
