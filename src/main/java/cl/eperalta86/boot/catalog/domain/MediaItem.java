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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    private LocalDate releaseDate;

    @OneToMany(mappedBy = "mediaItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaImage> images = new ArrayList<>();

    public MediaItem() {
    }

    public MediaItem(String title, Platform platform, MediaStatus status) {
        this.title = title;
        this.platform = platform;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<MediaImage> getImages() {
        return images;
    }

    public void setImages(List<MediaImage> images) {
        this.images = images;
    }

    public enum MediaStatus {
        BACKLOG, IN_PROGRESS, FINISHED
    }
}
