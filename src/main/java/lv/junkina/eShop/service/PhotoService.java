package lv.junkina.eShop.service;

import lv.junkina.eShop.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    Optional<Photo> findPhotoById(Long id);

    List<Photo> findAllPhotos();

    Photo savePhoto(Photo photo) throws Exception;

    void deletePhotoById(Long id);
}
