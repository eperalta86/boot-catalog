package cl.eperalta86.boot.catalog.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

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

    public MediaItem() {}

    public MediaItem(String title, MediaType type, MediaStatus status) {
        this.title = title;
        this.type = type;
        this.status = status;
    }
    
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public enum MediaType { GAME, MOVIE, SERIES }
    public enum MediaStatus { BACKLOG, IN_PROGRESS, FINISHED }
}