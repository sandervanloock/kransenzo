package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.config.DummyS3Configuration;
import be.sandervl.kranzenzo.domain.Image;
import be.sandervl.kranzenzo.domain.image.AwsImageUpload;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.service.dto.ImageDTO;
import be.sandervl.kranzenzo.service.mapper.ImageMapper;
import be.sandervl.kranzenzo.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static be.sandervl.kranzenzo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ImageResource REST controller.
 *
 * @see ImageResource
 */
@SpringBootTest(classes = KranzenzoApp.class)
@RunWith(SpringRunner.class)
@Import(DummyS3Configuration.class)
public class ImageResourceIntTest {

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray( 1, "0" );
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray( 1, "1" );
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImageMockMvc;

    private Image image;
    @Autowired
    private AwsImageUpload awsImageUpload;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity( EntityManager em ) {
        Image image = new Image()
            .dataContentType( DEFAULT_DATA_CONTENT_TYPE );
        return image;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final ImageResource imageResource = new ImageResource( imageRepository, imageMapper, awsImageUpload );
        this.restImageMockMvc = MockMvcBuilders.standaloneSetup( imageResource )
                                               .setCustomArgumentResolvers( pageableArgumentResolver )
                                               .setControllerAdvice( exceptionTranslator )
                                               .setConversionService( createFormattingConversionService() )
                                               .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        image = createEntity( em );
    }

    @Test
    @Transactional
    public void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto( image );
        restImageMockMvc.perform( post( "/api/images" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( imageDTO ) ) )
                        .andExpect( status().isCreated() );

        // Validate the Image in the database
        List <Image> imageList = imageRepository.findAll();
        assertThat( imageList ).hasSize( databaseSizeBeforeCreate + 1 );
        Image testImage = imageList.get( imageList.size() - 1 );
        assertThat( testImage.getDataContentType() ).isEqualTo( DEFAULT_DATA_CONTENT_TYPE );
    }

    @Test
    @Transactional
    public void createImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image with an existing ID
        image.setId( 1L );
        ImageDTO imageDTO = imageMapper.toDto( image );

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc.perform( post( "/api/images" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( imageDTO ) ) )
                        .andExpect( status().isBadRequest() );

        // Validate the Image in the database
        List <Image> imageList = imageRepository.findAll();
        assertThat( imageList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush( image );

        // Get all the imageList
        restImageMockMvc.perform( get( "/api/images?sort=id,desc" ) )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                        .andExpect( jsonPath( "$.[*].id" ).value( hasItem( image.getId().intValue() ) ) )
                        .andExpect( jsonPath( "$.[*].dataContentType" ).value( hasItem( DEFAULT_DATA_CONTENT_TYPE ) ) );
    }

    @Test
    @Transactional
    public void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush( image );

        // Get the image
        restImageMockMvc.perform( get( "/api/images/{id}", image.getId() ) )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                        .andExpect( jsonPath( "$.id" ).value( image.getId().intValue() ) )
                        .andExpect( jsonPath( "$.dataContentType" ).value( DEFAULT_DATA_CONTENT_TYPE ) );
    }

    @Test
    @Transactional
    public void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform( get( "/api/images/{id}", Long.MAX_VALUE ) )
                        .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush( image );

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = imageRepository.findById( image.getId() ).get();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach( updatedImage );
        updatedImage
            .dataContentType( UPDATED_DATA_CONTENT_TYPE );
        ImageDTO imageDTO = imageMapper.toDto( updatedImage );

        restImageMockMvc.perform( put( "/api/images" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( imageDTO ) ) )
                        .andExpect( status().isOk() );

        // Validate the Image in the database
        List <Image> imageList = imageRepository.findAll();
        assertThat( imageList ).hasSize( databaseSizeBeforeUpdate );
        Image testImage = imageList.get( imageList.size() - 1 );
        assertThat( testImage.getDataContentType() ).isEqualTo( UPDATED_DATA_CONTENT_TYPE );
    }

    @Test
    @Transactional
    public void updateNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto( image );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc.perform( put( "/api/images" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( imageDTO ) ) )
                        .andExpect( status().isBadRequest() );

        // Validate the Image in the database
        List <Image> imageList = imageRepository.findAll();
        assertThat( imageList ).hasSize( databaseSizeBeforeUpdate );
    }

    @Test
    @Transactional
    public void deleteImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush( image );

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Get the image
        restImageMockMvc.perform( delete( "/api/images/{id}", image.getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                        .andExpect( status().isOk() );

        // Validate the database is empty
        List <Image> imageList = imageRepository.findAll();
        assertThat( imageList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( Image.class );
        Image image1 = new Image();
        image1.setId( 1L );
        Image image2 = new Image();
        image2.setId( image1.getId() );
        assertThat( image1 ).isEqualTo( image2 );
        image2.setId( 2L );
        assertThat( image1 ).isNotEqualTo( image2 );
        image1.setId( null );
        assertThat( image1 ).isNotEqualTo( image2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( ImageDTO.class );
        ImageDTO imageDTO1 = new ImageDTO();
        imageDTO1.setId( 1L );
        ImageDTO imageDTO2 = new ImageDTO();
        assertThat( imageDTO1 ).isNotEqualTo( imageDTO2 );
        imageDTO2.setId( imageDTO1.getId() );
        assertThat( imageDTO1 ).isEqualTo( imageDTO2 );
        imageDTO2.setId( 2L );
        assertThat( imageDTO1 ).isNotEqualTo( imageDTO2 );
        imageDTO1.setId( null );
        assertThat( imageDTO1 ).isNotEqualTo( imageDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( imageMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( imageMapper.fromId( null ) ).isNull();
    }
}
