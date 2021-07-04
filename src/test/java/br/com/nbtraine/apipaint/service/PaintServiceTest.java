package br.com.nbtraine.apipaint.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.nbtraine.apipaint.builder.PaintDTOBuilder;
import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.entity.Paint;
import br.com.nbtraine.apipaint.exception.PaintAlreadyRegisteredException;
import br.com.nbtraine.apipaint.exception.PaintNotFoundException;
import br.com.nbtraine.apipaint.exception.PaintStockExceededException;
import br.com.nbtraine.apipaint.mapper.PaintMapper;
import br.com.nbtraine.apipaint.repository.PaintRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaintServiceTest {

    private static final long INVALID_Paint_ID = 1L;

    @Mock
    private PaintRepository paintRepository;

    private PaintMapper paintMapper = PaintMapper.INSTANCE;

    @InjectMocks
    private PaintService paintService;

    @Test
    void whenPaintInformedThenItShouldBeCreated() throws PaintAlreadyRegisteredException {
        // given
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedSavedPaint = paintMapper.toModel(expectedPaintDTO);

        // when
        when(paintRepository.findByName(expectedPaintDTO.getName())).thenReturn(Optional.empty());
        when(paintRepository.save(expectedSavedPaint)).thenReturn(expectedSavedPaint);

        //then
        PaintDTO createdPaintDTO = paintService.createPaint(expectedPaintDTO);

        assertThat(createdPaintDTO.getId(), is(equalTo(expectedPaintDTO.getId())));
        assertThat(createdPaintDTO.getName(), is(equalTo(expectedPaintDTO.getName())));
        assertThat(createdPaintDTO.getQuantity(), is(equalTo(expectedPaintDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredPaintInformedThenAnExceptionShouldBeThrown() {
        // given
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint duplicatedPaint = paintMapper.toModel(expectedPaintDTO);

        // when
        when(paintRepository.findByName(expectedPaintDTO.getName())).thenReturn(Optional.of(duplicatedPaint));

        // then
        assertThrows(PaintAlreadyRegisteredException.class, () -> paintService.createPaint(expectedPaintDTO));
    }

    @Test
    void whenValidPaintNameIsGivenThenReturnAPaint() throws PaintNotFoundException {
        // given
        PaintDTO expectedFoundPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedFoundPaint = paintMapper.toModel(expectedFoundPaintDTO);

        // when
        when(paintRepository.findByName(expectedFoundPaint.getName())).thenReturn(Optional.of(expectedFoundPaint));

        // then
        PaintDTO foundPaintDTO = paintService.findByName(expectedFoundPaintDTO.getName());

        assertThat(foundPaintDTO, is(equalTo(expectedFoundPaintDTO)));
    }

    @Test
    void whenNotRegisteredPaintNameIsGivenThenThrowAnException() {
        // given
        PaintDTO expectedFoundPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();

        // when
        when(paintRepository.findByName(expectedFoundPaintDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(PaintNotFoundException.class, () -> paintService.findByName(expectedFoundPaintDTO.getName()));
    }

    @Test
    void whenListPaintIsCalledThenReturnAListOfPaints() {
        // given
        PaintDTO expectedFoundPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedFoundPaint = paintMapper.toModel(expectedFoundPaintDTO);

        //when
        when(paintRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundPaint));

        //then
        List<PaintDTO> foundListPaintsDTO = paintService.listAll();

        assertThat(foundListPaintsDTO, is(not(empty())));
        assertThat(foundListPaintsDTO.get(0), is(equalTo(expectedFoundPaintDTO)));
    }

    @Test
    void whenListPaintIsCalledThenReturnAnEmptyListOfPaints() {
        //when
        when(paintRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<PaintDTO> foundListPaintsDTO = paintService.listAll();

        assertThat(foundListPaintsDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAPaintShouldBeDeleted() throws PaintNotFoundException{
        // given
        PaintDTO expectedDeletedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedDeletedPaint = paintMapper.toModel(expectedDeletedPaintDTO);

        // when
        when(paintRepository.findById(expectedDeletedPaintDTO.getId())).thenReturn(Optional.of(expectedDeletedPaint));
        doNothing().when(paintRepository).deleteById(expectedDeletedPaintDTO.getId());

        // then
        paintService.deleteById(expectedDeletedPaintDTO.getId());

        verify(paintRepository, times(1)).findById(expectedDeletedPaintDTO.getId());
        verify(paintRepository, times(1)).deleteById(expectedDeletedPaintDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementPaintStock() throws PaintNotFoundException, PaintStockExceededException {
        //given
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedPaint = paintMapper.toModel(expectedPaintDTO);

        //when
        when(paintRepository.findById(expectedPaintDTO.getId())).thenReturn(Optional.of(expectedPaint));
        when(paintRepository.save(expectedPaint)).thenReturn(expectedPaint);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedPaintDTO.getQuantity() + quantityToIncrement;

        // then
        PaintDTO incrementedPaintDTO = paintService.increment(expectedPaintDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedPaintDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedPaintDTO.getMax()));
    }

    @Test
    void whenDecrementIsCalledThenDecrementPaintStock() throws PaintNotFoundException, PaintStockExceededException {
        //given
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedPaint = paintMapper.toModel(expectedPaintDTO);

        //when
        when(paintRepository.findById(expectedPaintDTO.getId())).thenReturn(Optional.of(expectedPaint));
        when(paintRepository.save(expectedPaint)).thenReturn(expectedPaint);

        int quantityToDecrement = 10;
        int expectedQuantityAfterDecrement = expectedPaintDTO.getQuantity() - quantityToDecrement;

        // then
        PaintDTO incrementedPaintDTO = paintService.decrement(expectedPaintDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedPaintDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, lessThan(expectedPaintDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedPaint = paintMapper.toModel(expectedPaintDTO);

        when(paintRepository.findById(expectedPaintDTO.getId())).thenReturn(Optional.of(expectedPaint));

        int quantityToIncrement = 80;
        assertThrows(PaintStockExceededException.class, () -> paintService.increment(expectedPaintDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        PaintDTO expectedPaintDTO = PaintDTOBuilder.builder().build().toPaintDTO();
        Paint expectedPaint = paintMapper.toModel(expectedPaintDTO);

        when(paintRepository.findById(expectedPaintDTO.getId())).thenReturn(Optional.of(expectedPaint));

        int quantityToIncrement = 45;
        assertThrows(PaintStockExceededException.class, () -> paintService.increment(expectedPaintDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(paintRepository.findById(INVALID_Paint_ID)).thenReturn(Optional.empty());

        assertThrows(PaintNotFoundException.class, () -> paintService.increment(INVALID_Paint_ID, quantityToIncrement));
    }

}
