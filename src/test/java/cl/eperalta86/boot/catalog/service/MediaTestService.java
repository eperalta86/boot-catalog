package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaType;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaItemRepository repository;

    @InjectMocks
    private MediaService service;

    @Test
    void shouldReturnAllMediaItems() {
        //given
        when(repository.findAll()).thenReturn(List.of(
            new MediaItem("Matrix", MediaType.MOVIE, MediaStatus.FINISHED)
        ));
        //when
        List<MediaItem> result = service.findAll();

        //then
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getTitle());
        verify(repository).findAll(); 
    }

    @Test
    void shouldCreateNewMediaItem() {
        //given
        MediaItem savedItem = new MediaItem("Zelda", MediaType.GAME, MediaStatus.BACKLOG);
        // Simulamos que al guardar devuelve el objeto (podríamos simular que devuelve ID 1)
        when(repository.save(any(MediaItem.class))).thenReturn(savedItem);

        //when
        MediaItem result = service.create("Zelda", MediaType.GAME, MediaStatus.BACKLOG);

        //then
        assertNotNull(result);
        assertEquals("Zelda", result.getTitle());
        verify(repository).save(any(MediaItem.class));
    }
}