package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;

import cl.eperalta86.boot.catalog.service.MediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType; //spring
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MediaController.class)
class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnListFromService() throws Exception {
        //given
        given(service.findAll()).willReturn(List.of(
            new MediaItem("Inception", MediaItem.MediaType.MOVIE, MediaStatus.FINISHED)
        ));

       //when and then
        mockMvc.perform(get("/api/media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void shouldCreateMediaItem() throws Exception {
        //given
        MediaController.CreateMediaRequest request = 
            new MediaController.CreateMediaRequest("Halo", MediaItem.MediaType.GAME, MediaStatus.BACKLOG);
            
        MediaItem createdItem = new MediaItem("Halo", MediaItem.MediaType.GAME, MediaStatus.BACKLOG);
        
        given(service.create(eq("Halo"), eq(MediaItem.MediaType.GAME), eq(MediaStatus.BACKLOG)))
            .willReturn(createdItem);

        //when and then
        mockMvc.perform(post("/api/media")
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Halo"));
    }
}