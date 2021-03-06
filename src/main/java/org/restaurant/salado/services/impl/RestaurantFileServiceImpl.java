package org.restaurant.salado.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.restaurant.salado.dtos.RestaurantFileDTO;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.mappers.RestaurantFileMapper;
import org.restaurant.salado.repositories.RestaurantFileRepository;
import org.restaurant.salado.services.RestaurantFileService;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
@Transactional
public class RestaurantFileServiceImpl implements RestaurantFileService {

    private RestaurantFileRepository restaurantFileRepository;

    private RestaurantFileMapper restaurantFileMapper;

    @Autowired
    public void setRestaurantFileRepository(RestaurantFileRepository restaurantFileRepository) {
        this.restaurantFileRepository = restaurantFileRepository;
    }

    @Autowired
    public void setRestaurantFileMapper(RestaurantFileMapper restaurantFileMapper) {
        this.restaurantFileMapper = restaurantFileMapper;
    }

    @Override
    public RestaurantFile saveRestaurantFile(RestaurantFile file) {
        return this.restaurantFileRepository.save(file);
    }

    @Override
    public RestaurantFile saveRestaurantFile(RestaurantFileDTO file) {
        // Map DTO to Entity And Persist entity
        return this.restaurantFileRepository.save(this.restaurantFileMapper.toModel(file));
    }

    @Override
    public RestaurantFile saveRestaurantFile(MultipartFile file) throws IOException {
        RestaurantFile restaurantFile = new RestaurantFile(null, FilenameUtils.removeExtension(file.getOriginalFilename()), RestaurantUtils.getExtensionByApacheCommonLib(file.getOriginalFilename()), file.getContentType(), file.getBytes(), null);
        return this.restaurantFileRepository.save(restaurantFile);
    }

    @Override
    public RestaurantFile saveRestaurantFile(MultipartFile file, RestaurantFile restaurantFile) throws IOException {
        // Update image data
        restaurantFile.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
        restaurantFile.setExtension(RestaurantUtils.getExtensionByApacheCommonLib(file.getOriginalFilename()));
        restaurantFile.setFile(file.getBytes());
        return this.restaurantFileRepository.save(restaurantFile);
    }

    @Override
    public Boolean deleteRestaurantFile(Long id) {
        this.restaurantFileRepository.deleteById(id);
        return true;
    }

    @Override
    public RestaurantFile getRestaurantFile(Long id) {
        return this.restaurantFileRepository.findById(id).orElse(null);
    }

    @Override
    public RestaurantFile getRestaurantFile(String name) {
        return this.restaurantFileRepository.findByNameContainingIgnoreCase(name).orElse(null);
    }

    @Override
    public Page<RestaurantFile> getRestaurantFiles(int page, int size) {
        return this.restaurantFileRepository.findByOrderByIdDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<RestaurantFile> searchRestaurantFiles(int page, int size, String search) {
        return this.restaurantFileRepository.findByNameContainingIgnoreCaseOrId(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search, search);
    }

    @Override
    public List<RestaurantFile> getRestaurantFiles() {
        return this.restaurantFileRepository.findAll();
    }
}
