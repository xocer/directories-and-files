package dev.vbgrishin.directoriesandfilesapi.controller.domain;

import dev.vbgrishin.directoriesandfilesapi.repository.model.SubdirectoryEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DirectoryResponse {
    private Long id;
    private LocalDateTime date;
    private String path;
    private List<SubdirectoryEntity> paths;
    private Long subdirectories;
    private Long files;
    private String size;
}
