package br.com.nbtraine.apipaint.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaintAlreadyRegisteredException extends Exception  {
    
    public PaintAlreadyRegisteredException(String paintName) {
        super(String.format("Paint with name %s already registered in the system.", paintName));
    }
}
