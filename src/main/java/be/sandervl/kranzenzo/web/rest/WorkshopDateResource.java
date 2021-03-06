package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.WorkshopDate;
import be.sandervl.kranzenzo.repository.WorkshopDateRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopDateDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopDateMapper;
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
 * REST controller for managing WorkshopDate.
 */
@RestController
@RequestMapping("/api")
public class WorkshopDateResource {

    private static final String ENTITY_NAME = "workshopDate";
    private final Logger log = LoggerFactory.getLogger( WorkshopDateResource.class );
    private final WorkshopDateRepository workshopDateRepository;

    private final WorkshopDateMapper workshopDateMapper;

    public WorkshopDateResource( WorkshopDateRepository workshopDateRepository, WorkshopDateMapper workshopDateMapper ) {
        this.workshopDateRepository = workshopDateRepository;
        this.workshopDateMapper = workshopDateMapper;
    }

    /**
     * POST  /workshop-dates : Create a new workshopDate.
     *
     * @param workshopDateDTO the workshopDateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workshopDateDTO, or with status 400 (Bad Request) if the workshopDate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workshop-dates")
    @Timed
    public ResponseEntity <WorkshopDateDTO> createWorkshopDate( @Valid @RequestBody WorkshopDateDTO workshopDateDTO ) throws URISyntaxException {
        log.debug( "REST request to save WorkshopDate : {}", workshopDateDTO );
        if ( workshopDateDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new workshopDate cannot already have an ID", ENTITY_NAME, "idexists" );
        }

        WorkshopDate workshopDate = workshopDateMapper.toEntity( workshopDateDTO );
        workshopDate = workshopDateRepository.save( workshopDate );
        WorkshopDateDTO result = workshopDateMapper.toDto( workshopDate );
        return ResponseEntity.created( new URI( "/api/workshop-dates/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /workshop-dates : Updates an existing workshopDate.
     *
     * @param workshopDateDTO the workshopDateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workshopDateDTO,
     * or with status 400 (Bad Request) if the workshopDateDTO is not valid,
     * or with status 500 (Internal Server Error) if the workshopDateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workshop-dates")
    @Timed
    public ResponseEntity <WorkshopDateDTO> updateWorkshopDate( @Valid @RequestBody WorkshopDateDTO workshopDateDTO ) throws URISyntaxException {
        log.debug( "REST request to update WorkshopDate : {}", workshopDateDTO );
        if ( workshopDateDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }

        WorkshopDate workshopDate = workshopDateMapper.toEntity( workshopDateDTO );
        workshopDate = workshopDateRepository.save( workshopDate );
        WorkshopDateDTO result = workshopDateMapper.toDto( workshopDate );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, workshopDateDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /workshop-dates : get all the workshopDates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workshopDates in body
     */
    @GetMapping("/workshop-dates")
    @Timed
    public List <WorkshopDateDTO> getAllWorkshopDates() {
        log.debug( "REST request to get all WorkshopDates" );
        List <WorkshopDate> workshopDates = workshopDateRepository.findAll();
        return workshopDateMapper.toDto( workshopDates );
    }

    /**
     * GET  /workshop-dates/:id : get the "id" workshopDate.
     *
     * @param id the id of the workshopDateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workshopDateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workshop-dates/{id}")
    @Timed
    public ResponseEntity <WorkshopDateDTO> getWorkshopDate( @PathVariable Long id ) {
        log.debug( "REST request to get WorkshopDate : {}", id );
        Optional <WorkshopDateDTO> workshopDateDTO = workshopDateRepository.findById( id )
                                                                           .map( workshopDateMapper::toDto );
        return ResponseUtil.wrapOrNotFound( workshopDateDTO );
    }

    /**
     * DELETE  /workshop-dates/:id : delete the "id" workshopDate.
     *
     * @param id the id of the workshopDateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workshop-dates/{id}")
    @Timed
    public ResponseEntity <Void> deleteWorkshopDate( @PathVariable Long id ) {
        log.debug( "REST request to delete WorkshopDate : {}", id );

        workshopDateRepository.deleteById( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
