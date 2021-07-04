package br.com.nbtraine.apipaint.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.nbtraine.apipaint.enums.TypePaint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaintDTO {
    
    private Long id;
    @NotNull
    @Size(min = 1, max = 200)
    private String name;
    @NotNull
    @Size(min = 1, max = 200)
    private String brand;
    @NotNull
    @Max(500)
    private Integer max;
    @NotNull
    @Max(100)
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TypePaint typePaint;
}
