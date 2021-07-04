package br.com.nbtraine.apipaint.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.dto.QuantityDTO;
import br.com.nbtraine.apipaint.exception.PaintAlreadyRegisteredException;
import br.com.nbtraine.apipaint.exception.PaintNotFoundException;
import br.com.nbtraine.apipaint.exception.PaintStockExceededException;
import br.com.nbtraine.apipaint.service.PaintService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/paints")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class PaintController implements PaintControllerDocs {
    
    private PaintService paintService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaintDTO createPaint(@RequestBody @Valid PaintDTO paintDTO) throws PaintAlreadyRegisteredException {
        return paintService.createPaint(paintDTO);
    }

    @GetMapping("/{name}")
    public PaintDTO findByName(@PathVariable String name) throws PaintNotFoundException {
        return paintService.findByName(name);
    }

    @GetMapping
    public List<PaintDTO> listPaints() {
        return paintService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws PaintNotFoundException {
        paintService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public PaintDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws PaintNotFoundException, PaintStockExceededException {
        return paintService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public PaintDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws PaintNotFoundException, PaintStockExceededException {
        return paintService.decrement(id, quantityDTO.getQuantity());
    }

}
