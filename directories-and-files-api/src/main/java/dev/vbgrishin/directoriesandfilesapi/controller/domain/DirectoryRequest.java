package dev.vbgrishin.directoriesandfilesapi.controller.domain;

import lombok.Builder;

@Builder
public record DirectoryRequest (String path){
}
