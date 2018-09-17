package be.sandervl.kranzenzo.service;

import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.repository.ProductRepository;
import be.sandervl.kranzenzo.service.dto.ProductDTO;
import be.sandervl.kranzenzo.service.mapper.ProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger( ProductService.class );

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ImageRepository imageRepository;

    public ProductService( ProductRepository productRepository, ProductMapper productMapper, ImageRepository imageRepository ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.imageRepository = imageRepository;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    public ProductDTO save( ProductDTO productDTO ) {
        log.debug( "Request to save Product : {}", productDTO );
        final Product product = productMapper.toEntity( productDTO );
        //unlink existing images
        productRepository.findOneWithEagerRelationships( productDTO.getId() )
                         .ifPresent( existing -> existing.getImages()
                                                         .stream()
                                                         .filter( image -> !product.getImages().contains( image ) )
                                                         .peek( image -> image.setProduct( null ) )
                                                         .forEach( imageRepository::save ) );
        //link new images
        productRepository.save( product )
                         .getImages()
                         .stream()
                         .filter( image -> image.getId() != null )
                         //get image from database
                         .map( image -> imageRepository.getOne( image.getId() ) )
                         .peek( image -> image.setProduct( product ) )
                         .forEach( imageRepository::save );
        return productMapper.toDto( product );
    }

    /**
     * Get all the products.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(Boolean activeOnly, String tagName){
        log.debug( "Request to get all Products" );
        List<Product> products;
        if ( activeOnly != null ) {
            products = productRepository.findAllByIsActive( activeOnly );
        }
        else {
            products = productRepository.findAllWithEagerRelationships();
        }
        return products.stream()
                       .filter(p -> StringUtils.isBlank(tagName) ||
                           p.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(tagName)))
                       .map(productMapper::toDto)
                       .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Product with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page <ProductDTO> findAllWithEagerRelationships( Pageable pageable ) {
        return productRepository.findAllWithEagerRelationships( pageable ).map( productMapper::toDto );
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional <ProductDTO> findOne( Long id ) {
        log.debug( "Request to get Product : {}", id );
        return productRepository.findOneWithEagerRelationships( id )
                                .map( productMapper::toDto );
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    public void delete( Long id ) {
        log.debug( "Request to delete Product : {}", id );
        productRepository.deleteById( id );
    }
}