package lv.junkina.eShop.service.impl;

import lombok.extern.log4j.Log4j2;
import lv.junkina.eShop.mappers.PhotoMapStructMapper;
import lv.junkina.eShop.mappers.ProductMapStructMapper;
import lv.junkina.eShop.model.Photo;
import lv.junkina.eShop.model.Product;
import lv.junkina.eShop.repository.PhotoRepository;
import lv.junkina.eShop.repository.ProductRepository;
import lv.junkina.eShop.repository.model.PhotoDAO;
import lv.junkina.eShop.repository.model.ProductDAO;
import lv.junkina.eShop.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PhotoServiceImplementation implements PhotoService {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    PhotoMapStructMapper photoMapper;
    @Override
    public Optional<Photo> findPhotoById(Long id) {
        Optional<Photo> photoById = photoRepository.findById(id)
                .flatMap(photo -> Optional.ofNullable(photoMapper.photoDAOToPhoto(photo)));
        log.info("Product with id {} is {}", id, photoById);
        return photoById;
    }

    @Override
    public List<Photo> findAllPhotos() {
        List<PhotoDAO> photoDAOList = photoRepository.findAll();
        log.info("Get photo list. Size is: {}", photoDAOList::size);
        return photoDAOList.stream().map(photoMapper::photoDAOToPhoto).collect(Collectors.toList());
    }
    @Override
    public Photo savePhoto(Photo photo) throws Exception {
        if (!hasNoMatch(photo)) {
            log.error("Photo conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        PhotoDAO photoSaved = photoRepository.save(photoMapper.photoToPhotoDAO(photo));
        log.info("New photo saved: {}", () -> photoSaved);
        return photoMapper.photoDAOToPhoto(photoSaved);
    }

    @Override
    public void deletePhotoById(Long id) {
        photoRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }

    public boolean hasNoMatch(Photo photo) {
        return photoRepository.findAll().stream()
                .noneMatch(t -> !t.getId().equals(photo.getId()) &&
                        t.getName().equals(photo.getName()));
    }
}
