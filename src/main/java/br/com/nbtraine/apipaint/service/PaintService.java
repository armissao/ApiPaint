package br.com.nbtraine.apipaint.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.entity.Paint;
import br.com.nbtraine.apipaint.exception.PaintAlreadyRegisteredException;
import br.com.nbtraine.apipaint.exception.PaintNotFoundException;
import br.com.nbtraine.apipaint.exception.PaintStockExceededException;
import br.com.nbtraine.apipaint.mapper.PaintMapper;
import br.com.nbtraine.apipaint.repository.PaintRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PaintService {

    private PaintRepository paintRepository;
    private final PaintMapper paintMapper = PaintMapper.INSTANCE;

    public PaintDTO createPaint(PaintDTO paintDTO) throws PaintAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(paintDTO.getName());
        Paint paint = paintMapper.toModel(paintDTO);
        Paint savedpaint = paintRepository.save(paint);
        return paintMapper.toDTO(savedpaint);
    }

    public PaintDTO findByName(String name) throws PaintNotFoundException {
        Paint foundpaint = paintRepository.findByName(name)
                .orElseThrow(() -> new PaintNotFoundException(name));
        return paintMapper.toDTO(foundpaint);
    }

    public List<PaintDTO> listAll() {
        return paintRepository.findAll()
                .stream()
                .map(paintMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws PaintNotFoundException {
        verifyIfExists(id);
        paintRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws PaintAlreadyRegisteredException {
        Optional<Paint> optSavedpaint = paintRepository.findByName(name);
        if (optSavedpaint.isPresent()) {
            throw new PaintAlreadyRegisteredException(name);
        }
    }

    private Paint verifyIfExists(Long id) throws PaintNotFoundException {
        return paintRepository.findById(id)
                .orElseThrow(() -> new PaintNotFoundException(id));
    }

    public PaintDTO increment(Long id, int quantityToIncrement) throws PaintNotFoundException, PaintStockExceededException {
        Paint paintToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + paintToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= paintToIncrementStock.getMax()) {
            paintToIncrementStock.setQuantity(paintToIncrementStock.getQuantity() + quantityToIncrement);
            Paint incrementedpaintStock = paintRepository.save(paintToIncrementStock);
            return paintMapper.toDTO(incrementedpaintStock);
        }
        throw new PaintStockExceededException(id, quantityToIncrement);
    }

    public PaintDTO decrement(Long id, int quantityToDecrement) throws PaintNotFoundException, PaintStockExceededException {
        Paint paintToDecrementStock = verifyIfExists(id);
        int quantityAfterDecrement = quantityToDecrement - paintToDecrementStock.getQuantity();
        if (!(paintToDecrementStock.getQuantity() == 0) && (quantityAfterDecrement <= 0)) {
            paintToDecrementStock.setQuantity(paintToDecrementStock.getQuantity() - quantityToDecrement);
            Paint decrementedpaintStock = paintRepository.save(paintToDecrementStock);
            return paintMapper.toDTO(decrementedpaintStock);
        }
        throw new PaintStockExceededException(id, quantityToDecrement);
    }
}
