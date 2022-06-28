package dev.vbgrishin.directoriesandfilesapi.repository;

import dev.vbgrishin.directoriesandfilesapi.repository.model.DirectoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface DirectoryRepository extends CrudRepository<DirectoryEntity, Integer> {
}
