package be.sandervl.kransenzo.service.dto;

import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import be.sandervl.kransenzo.domain.enumeration.OrderState;
import be.sandervl.kransenzo.domain.enumeration.PaymentType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Order entity.
 */
public class OrderDTO implements Serializable
{

    private Long id;

    private ZonedDateTime created;

    private ZonedDateTime updated;

    private OrderState state;

    private DeliveryType deliveryType;

    private Boolean includeBatteries;

    @Size(max = 5000)
    private String description;

    @DecimalMin(value = "0")
    private Float deliveryPrice;

    @NotNull
    private PaymentType paymentType;

    private Long customerId;

    private CustomerDTO customer;

    private Long deliveryAddressId;

    private Long productId;

    private String productName;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated( ZonedDateTime created ) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated( ZonedDateTime updated ) {
        this.updated = updated;
    }

    public OrderState getState() {
        return state;
    }

    public void setState( OrderState state ) {
        this.state = state;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType( DeliveryType deliveryType ) {
        this.deliveryType = deliveryType;
    }

    public Boolean isIncludeBatteries() {
        return includeBatteries;
    }

    public void setIncludeBatteries( Boolean includeBatteries ) {
        this.includeBatteries = includeBatteries;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice( Float deliveryPrice ) {
        this.deliveryPrice = deliveryPrice;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType( PaymentType paymentType ) {
        this.paymentType = paymentType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId( Long customerId ) {
        this.customerId = customerId;
    }

    public CustomerDTO getCustomer(){
        return customer;
    }

    public void setCustomer(CustomerDTO customer){
        this.customer = customer;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId( Long locationId ) {
        this.deliveryAddressId = locationId;
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

        OrderDTO orderDTO = (OrderDTO) o;
        if ( orderDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), orderDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", state='" + getState() + "'" +
            ", deliveryType='" + getDeliveryType() + "'" +
            ", includeBatteries='" + isIncludeBatteries() + "'" +
            ", description='" + getDescription() + "'" +
            ", deliveryPrice=" + getDeliveryPrice() +
            ", paymentType='" + getPaymentType() + "'" +
            "}";
    }
}
