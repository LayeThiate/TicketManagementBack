package fr.urss.common.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an {@link Address}.
 *
 * @author lucas.david
 */
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private long identifier;

    private String street;

    private String city;

    @Column(name = "zip_code")
    private String ZIPCode;

    public Address() {}

    public long getIdentifier() {
        return identifier;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZIPCode() {
        return ZIPCode;
    }

    public void setZIPCode(String ZIPCode) {
        this.ZIPCode = ZIPCode;
    }

    @Override
    public boolean equals(Object object) {
        if (Objects.equals(this, object)) return true;
        if (object instanceof Address) {
            final Address address = (Address) object;
            return Objects.equals(street, address.street) && Objects.equals(city, address.city) &&
                    Objects.equals(ZIPCode, address.ZIPCode);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, ZIPCode);
    }

    @Override
    public String toString() {
        return identifier + ' ' + street + ' ' + city + ' ' + ZIPCode;
    }
}