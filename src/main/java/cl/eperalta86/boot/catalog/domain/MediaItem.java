package cl.eperalta86.boot.catalog.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "media_items")
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private MediaType type; // ENUM: GAME, MOVIE, SERIES

    @Enumerated(EnumType.STRING)
    private MediaStatus status; // ENUM: BACKLOG, IN_PROGRESS, FINISHED

    private LocalDate releaseDate;

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaImage> images = new ArrayList<>();

    public MediaItem() {}

    public MediaItem(String title, MediaType type, MediaStatus status) {
        this.title = title;
        this.type = type;
        this.status = status;
    }
    
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<MediaImage> getImages() { return images; }
    public void setImages(List<MediaImage> images) { this.images = images; }
    
    public MediaType getType() { return type; }
    public void setType(MediaType type) { this.type = type; }

    public MediaStatus getStatus() { return status; }
    public void setStatus(MediaStatus status) { this.status = status; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public enum MediaType { GAME, MOVIE, SERIES }
    public enum MediaStatus { BACKLOG, IN_PROGRESS, FINISHED }
}