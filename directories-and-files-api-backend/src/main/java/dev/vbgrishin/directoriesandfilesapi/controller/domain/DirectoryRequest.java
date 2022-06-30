package dev.vbgrishin.directoriesandfilesapi.controller.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DirectoryRequest {
    private final String path;
}
