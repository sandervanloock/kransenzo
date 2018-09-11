package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.Image;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.service.dto.ImageDTO;
import be.sandervl.kranzenzo.service.mapper.ImageMapper;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Image.
 */
@RestController
@RequestMapping("/api")
public class ImageResource {

    private static final String ENTITY_NAME = "image";
    private final Logger log = LoggerFactory.getLogger( ImageResource.class );
    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    public ImageResource( ImageRepository imageRepository, ImageMapper imageMapper ) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    /**
     * POST  /images : Create a new image.
     *
     * @param imageDTO the imageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imageDTO, or with status 400 (Bad Request) if the image has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/images")
    @Timed
    public ResponseEntity <ImageDTO> createImage( @Valid @RequestBody ImageDTO imageDTO ) throws URISyntaxException {
        log.debug( "REST request to save Image : {}", imageDTO );
        if ( imageDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new image cannot already have an ID", ENTITY_NAME, "idexists" );
        }

        Image image = imageMapper.toEntity( imageDTO );
        image = imageRepository.save( image );
        ImageDTO result = imageMapper.toDto( image );
        return ResponseEntity.created( new URI( "/api/images/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /images : Updates an existing image.
     *
     * @param imageDTO the imageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imageDTO,
     * or with status 400 (Bad Request) if the imageDTO is not valid,
     * or with status 500 (Internal Server Error) if the imageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/images")
    @Timed
    public ResponseEntity <ImageDTO> updateImage( @Valid @RequestBody ImageDTO imageDTO ) throws URISyntaxException {
        log.debug( "REST request to update Image : {}", imageDTO );
        if ( imageDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }

        Image image = imageMapper.toEntity( imageDTO );
        image = imageRepository.save( image );
        ImageDTO result = imageMapper.toDto( image );
        return ResponseEntity.ok()
                             .headers( HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, imageDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /images : get all the images.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of images in body
     */
    @GetMapping("/images")
    @Timed
    public List <ImageDTO> getAllImages() {
        log.debug( "REST request to get all Images" );
        List <Image> images = imageRepository.findAll();
        return imageMapper.toDto( images );
    }

    /**
     * GET  /images/:id : get the "id" image.
     *
     * @param id the id of the imageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/images/{id}")
    @Timed
    public ResponseEntity <ImageDTO> getImage( @PathVariable Long id ) {
        log.debug( "REST request to get Image : {}", id );
        Optional <ImageDTO> imageDTO = imageRepository.findById( id )
                                                      .map( imageMapper::toDto );
        return ResponseUtil.wrapOrNotFound( imageDTO );
    }

    /**
     * DELETE  /images/:id : delete the "id" image.
     *
     * @param id the id of the imageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/images/{id}")
    @Timed
    public ResponseEntity <Void> deleteImage( @PathVariable Long id ) {
        log.debug( "REST request to delete Image : {}", id );

        imageRepository.deleteById( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
