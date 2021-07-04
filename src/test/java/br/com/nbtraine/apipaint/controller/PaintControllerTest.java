package br.com.nbtraine.apipaint.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.com.nbtraine.apipaint.util.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import br.com.nbtraine.apipaint.builder.PaintDTOBuilder;
import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.dto.QuantityDTO;
import br.com.nbtraine.apipaint.exception.PaintNotFoundException;
import br.com.nbtraine.apipaint.service.PaintService;

/**
 * PaintControllerTest
 */
@ExtendWith(MockitoExtension.class)
public class PaintControllerTest {

    private static final String PAINT_API_URL_PATH = "/api/v1/paints";
    private static final long VALID_PAINT_ID = 1L;
    private static final long INVALID_PAINT_ID = 2l;
    private static final String PAINT_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String PAINT_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private PaintService paintService;

    @InjectMocks
    private PaintController paintController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paintController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        // when
        when(paintService.createPaint(paintDTO)).thenReturn(paintDTO);

        // then
        mockMvc.perform(post(PAINT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paintDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(paintDTO.getName())))
                .andExpect(jsonPath("$.brand", is(paintDTO.getBrand())))
                .andExpect(jsonPath("$.typePaint", is(paintDTO.getTypePaint().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        paintDTO.setBrand(null);

        // then
        mockMvc.perform(post(PAINT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paintDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        //when
        when(paintService.findByName(paintDTO.getName())).thenReturn(paintDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PAINT_API_URL_PATH + "/" + paintDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(paintDTO.getName())))
                .andExpect(jsonPath("$.brand", is(paintDTO.getBrand())))
                .andExpect(jsonPath("$.typePaint", is(paintDTO.getTypePaint().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        //when
        when(paintService.findByName(paintDTO.getName())).thenThrow(PaintNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PAINT_API_URL_PATH + "/" + paintDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        //when
        when(paintService.listAll()).thenReturn(Collections.singletonList(paintDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PAINT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(paintDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(paintDTO.getBrand())))
                .andExpect(jsonPath("$[0].typePaint", is(paintDTO.getTypePaint().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        //when
        when(paintService.listAll()).thenReturn(Collections.singletonList(paintDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PAINT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        //when
        doNothing().when(paintService).deleteById(paintDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PAINT_API_URL_PATH + "/" + paintDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(PaintNotFoundException.class).when(paintService).deleteById(INVALID_PAINT_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PAINT_API_URL_PATH + "/" + INVALID_PAINT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        paintDTO.setQuantity(paintDTO.getQuantity() + quantityDTO.getQuantity());

        when(paintService.increment(VALID_PAINT_ID, quantityDTO.getQuantity())).thenReturn(paintDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(PAINT_API_URL_PATH + "/" + VALID_PAINT_ID + PAINT_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(paintDTO.getName())))
                .andExpect(jsonPath("$.brand", is(paintDTO.getBrand())))
                .andExpect(jsonPath("$.typePaint", is(paintDTO.getTypePaint().toString())))
                .andExpect(jsonPath("$.quantity", is(paintDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        PaintDTO paintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        paintDTO.setQuantity(paintDTO.getQuantity() - quantityDTO.getQuantity());

        when(paintService.decrement(VALID_PAINT_ID, quantityDTO.getQuantity())).thenReturn(paintDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(PAINT_API_URL_PATH + "/" + VALID_PAINT_ID + PAINT_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(paintDTO.getName())))
                .andExpect(jsonPath("$.brand", is(paintDTO.getBrand())))
                .andExpect(jsonPath("$.typePaint", is(paintDTO.getTypePaint().toString())))
                .andExpect(jsonPath("$.quantity", is(paintDTO.getQuantity())));
    }
}