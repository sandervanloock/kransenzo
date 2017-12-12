package be.sandervl.kransenzo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable
{

	private Long id;

	private Long addressId;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId( Long locationId ) {
		this.addressId = locationId;
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
				"}";
	}
}
