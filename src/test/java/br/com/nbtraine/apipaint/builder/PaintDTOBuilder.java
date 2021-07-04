package br.com.nbtraine.apipaint.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.enums.TypePaint;
import lombok.Builder;

@Builder
public class PaintDTOBuilder {
    
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Viva Cores";

    @Builder.Default
    private String brand = "Suvinil";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private TypePaint typePaint = TypePaint.FLAT;

    public PaintDTO toPaintDTO() {
        return new PaintDTO(id,
                name,
                brand,
                max,
                quantity,
                typePaint);
    }
}
