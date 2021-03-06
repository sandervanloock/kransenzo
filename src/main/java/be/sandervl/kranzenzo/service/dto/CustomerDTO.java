package be.sandervl.kranzenzo.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    private UserDTO user;

    private String street;

    private String city;

    @Min(value = 1000)
    @Max(value = 9999)
    private Integer zipCode;

    private String province;

    private String phoneNumber;

    private Float latitude;
    private Float longitude;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }
    public void setUser( UserDTO user ) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode( Integer zipCode ) {
        this.zipCode = zipCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince( String province ) {
        this.province = province;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude( Float latitude ) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude( Float longitude ) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if ( customerDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), customerDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", zipCode=" + getZipCode() +
            ", province='" + getProvince() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
