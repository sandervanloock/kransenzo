package be.sandervl.kranzenzo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Image entity.
 */
public class ImageDTO implements Serializable {

    private Long id;
    private String endpoint;

    private String dataContentType;

    private Long workshopId;

    private String workshopName;

    private Long productId;

    private String productName;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType( String dataContentType ) {
        this.dataContentType = dataContentType;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId( Long workshopId ) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName( String workshopName ) {
        this.workshopName = workshopName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId( Long productId ) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName( String productName ) {
        this.productName = productName;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        ImageDTO imageDTO = (ImageDTO) o;
        if ( imageDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), imageDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + getId() +
            ", workshop=" + getWorkshopId() +
            ", workshop='" + getWorkshopName() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductName() + "'" +
            "}";
    }
}
