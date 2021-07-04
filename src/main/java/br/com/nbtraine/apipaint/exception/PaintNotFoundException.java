package br.com.nbtraine.apipaint.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaintNotFoundException extends Exception {
    
    public PaintNotFoundException(String paintName) {
        super(String.format("Paint with name %s not found in the system.", paintName));
    }

    public PaintNotFoundException(Long id) {
        super(String.format("Paint with id %s not found in the system.", id));
    }
}
