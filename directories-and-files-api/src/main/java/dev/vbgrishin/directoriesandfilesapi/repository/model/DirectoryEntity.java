package dev.vbgrishin.directoriesandfilesapi.repository.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "directory")
public class DirectoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime date;
    private String path;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubdirectoryEntity> paths;
}
