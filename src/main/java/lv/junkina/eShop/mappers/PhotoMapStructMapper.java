package lv.junkina.eShop.mappers;

import lv.junkina.eShop.model.Photo;
import lv.junkina.eShop.repository.model.PhotoDAO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PhotoMapStructMapper {
    PhotoDAO photoToPhotoDAO(Photo photo);

    Photo photoDAOToPhoto(PhotoDAO photoDAO);
}
