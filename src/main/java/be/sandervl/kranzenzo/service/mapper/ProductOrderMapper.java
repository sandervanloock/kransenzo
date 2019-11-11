package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.*;
import be.sandervl.kranzenzo.service.dto.ProductOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrder} and its DTO {@link ProductOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, LocationMapper.class, ProductMapper.class})
public interface ProductOrderMapper extends EntityMapper<ProductOrderDTO, ProductOrder> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ProductOrderDTO toDto(ProductOrder productOrder);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "deliveryAddressId", target = "deliveryAddress")
    @Mapping(source = "productId", target = "product")
    ProductOrder toEntity(ProductOrderDTO productOrderDTO);

    default ProductOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(id);
        return productOrder;
    }
}
