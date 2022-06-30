package dev.vbgrishin.directoriesandfilesapi.controller;

import dev.vbgrishin.directoriesandfilesapi.controller.domain.DirectoryRequest;
import dev.vbgrishin.directoriesandfilesapi.controller.domain.DirectoryResponse;
import dev.vbgrishin.directoriesandfilesapi.repository.model.DirectoryEntity;
import dev.vbgrishin.directoriesandfilesapi.repository.model.SubdirectoryEntity;
import dev.vbgrishin.directoriesandfilesapi.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/directories")
@RequiredArgsConstructor
public class DirectoryController {
    private final DirectoryService directoryService;

    @GetMapping
    public List<DirectoryResponse> findDirectories() {
        return directoryService.findDirectories();
    }

    @GetMapping("/{id}")
    public DirectoryResponse findDirectoryById(@PathVariable Long id) {
        return directoryService.findDirectoryById(id);
    }

    @GetMapping("/{id}/subdirectories")
    public List<SubdirectoryEntity> findSubDirectoriesByDirectoryId(@PathVariable Long id) {
        return directoryService.findSubDirectoriesByDirectoryId(id);
    }

    @PostMapping
    public DirectoryEntity save(@RequestBody DirectoryRequest directoryRequest) {
        if (!Files.isDirectory(Paths.get(directoryRequest.getPath()))) {
            throw new IncorrectPathException();
        }
        return directoryService.save(directoryRequest);
    }
}
