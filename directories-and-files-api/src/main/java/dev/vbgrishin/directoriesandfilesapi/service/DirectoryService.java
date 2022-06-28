package dev.vbgrishin.directoriesandfilesapi.service;

import dev.vbgrishin.directoriesandfilesapi.controller.NotFoundException;
import dev.vbgrishin.directoriesandfilesapi.controller.domain.DirectoryRequest;
import dev.vbgrishin.directoriesandfilesapi.controller.domain.DirectoryResponse;
import dev.vbgrishin.directoriesandfilesapi.repository.model.DirectoryEntity;
import dev.vbgrishin.directoriesandfilesapi.model.DirectoryComparator;
import dev.vbgrishin.directoriesandfilesapi.repository.model.SubdirectoryEntity;
import dev.vbgrishin.directoriesandfilesapi.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectoryService {
    private final DirectoryRepository repository;

    public List<DirectoryResponse> findDirectories() {
        List<DirectoryResponse> list = new ArrayList<>();
        repository.findAll().forEach(directoryEntity -> {
            list.add(getDirectoryResponse(directoryEntity));
        });
        return list;
    }

    public List<SubdirectoryEntity> findSubDirectoriesByDirectoryId(Integer id) {
        List<SubdirectoryEntity> list = repository.findById(id).orElseThrow().getPaths();
        list.sort(new DirectoryComparator());
        return list;
    }

    public DirectoryEntity save(DirectoryRequest request) {
        var path = request.path();
        List<SubdirectoryEntity> list = getSubdirectoriesByPath(Paths.get(path));
        DirectoryEntity dir = DirectoryEntity.builder()
                .path(path)
                .paths(list)
                .date(LocalDateTime.now())
                .build();
        return repository.save(dir);
    }

    public DirectoryResponse findDirectoryById(Integer id) {
        var directoryEntity = repository.findById(id).orElseThrow(NotFoundException::new);
        return getDirectoryResponse(directoryEntity);
    }

    private List<SubdirectoryEntity> getSubdirectoriesByPath(Path path) {
        List<SubdirectoryEntity> list = new ArrayList<>();
        if (Files.isDirectory(path)) {
            try {
                final List<Path> collect = Files.list(path).collect(Collectors.toList());

                for (Path p : collect) {
                    String size = "";
                    if (Files.isDirectory(p)) {
                        size = "DIR";
                    } else if (Files.isRegularFile(p)) {
                        size = formatSizeView(getSubDirectoriesSize(p), false);
                    }

                    list.add(SubdirectoryEntity.builder()
                            .path(p.toString())
                            .size(size)
                            .build());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private long getDirectoriesAndFilesCount(List<SubdirectoryEntity> list, Function<Path, Boolean> checkPath) {
        return list.stream()
                .filter(subDirectory -> checkPath.apply(Paths.get(subDirectory.getPath())))
                .count();
    }
    // Получаем строку, как у нас будет отображаться размер файла

    private String formatSizeView(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " b";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sb", bytes / Math.pow(unit, exp), pre);
    }

    private Long getSubDirectoriesSize(Path path) {
        long result = 0;
        try {
            result = Files.walk(path)
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .mapToLong(File::length)
                    .sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private DirectoryResponse getDirectoryResponse(DirectoryEntity directoryEntity) {
        var subDirectoriesList = directoryEntity.getPaths();
        var subDirectoriesSize = getSubDirectoriesSize(Paths.get(directoryEntity.getPath()));

        return DirectoryResponse.builder()
                .id(directoryEntity.getId())
                .date(directoryEntity.getDate())
                .path(directoryEntity.getPath())
                .paths(subDirectoriesList)
                .subdirectories(getDirectoriesAndFilesCount(subDirectoriesList, Files::isDirectory))
                .files(getDirectoriesAndFilesCount(subDirectoriesList, Files::isRegularFile))
                .size(formatSizeView(subDirectoriesSize, false))
                .build();
    }
}
