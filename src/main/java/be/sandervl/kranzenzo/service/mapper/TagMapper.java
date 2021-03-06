package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.Tag;
import be.sandervl.kranzenzo.service.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface TagMapper extends EntityMapper <TagDTO, Tag> {

    @Mapping(source = "image", target = "image")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    TagDTO toDto( Tag tag );

    @Mapping(target = "workshops", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(source = "parentId", target = "parent")
    Tag toEntity( TagDTO tagDTO );

    default Tag fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId( id );
        return tag;
    }
}
