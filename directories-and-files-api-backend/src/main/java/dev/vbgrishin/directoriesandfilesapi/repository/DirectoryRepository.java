package dev.vbgrishin.directoriesandfilesapi.repository;

import dev.vbgrishin.directoriesandfilesapi.repository.model.DirectoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DirectoryRepository extends CrudRepository<DirectoryEntity, Long> {
    List<DirectoryEntity> findAll();
}
