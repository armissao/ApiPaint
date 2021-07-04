package br.com.nbtraine.apipaint.controller;


import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.exception.PaintAlreadyRegisteredException;
import br.com.nbtraine.apipaint.exception.PaintNotFoundException;
import io.swagger.annotations.*;

@Api("Manages Paint Stock")
public interface PaintControllerDocs {
    
    @ApiOperation(value = "Paint creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success paint creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    PaintDTO createPaint(PaintDTO beerDTO) throws PaintAlreadyRegisteredException;

    @ApiOperation(value = "Returns paint found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success paint found in the system"),
            @ApiResponse(code = 404, message = "Paint with given name not found.")
    })
    PaintDTO findByName(@PathVariable String name) throws PaintNotFoundException;

    @ApiOperation(value = "Returns a list of all paints registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all paints registered in the system"),
    })
    List<PaintDTO> listPaints();

    @ApiOperation(value = "Delete a paint found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success paints deleted in the system"),
            @ApiResponse(code = 404, message = "Paint with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws PaintNotFoundException;
}
