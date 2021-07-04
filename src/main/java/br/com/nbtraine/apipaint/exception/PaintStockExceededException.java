package br.com.nbtraine.apipaint.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaintStockExceededException extends Exception {
    
    public PaintStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Paints with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
