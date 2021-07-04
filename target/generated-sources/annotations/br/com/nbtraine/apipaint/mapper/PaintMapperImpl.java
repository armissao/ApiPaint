package br.com.nbtraine.apipaint.mapper;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.dto.PaintDTO.PaintDTOBuilder;
import br.com.nbtraine.apipaint.entity.Paint;
import br.com.nbtraine.apipaint.entity.Paint.PaintBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-02T10:28:07-0300",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.4.0.v20210618-1653, environment: Java 11.0.11 (AdoptOpenJDK)"
)
public class PaintMapperImpl implements PaintMapper {

    @Override
    public Paint toModel(PaintDTO paintDTO) {
        if ( paintDTO == null ) {
            return null;
        }

        PaintBuilder paint = Paint.builder();

        paint.brand( paintDTO.getBrand() );
        paint.id( paintDTO.getId() );
        paint.max( paintDTO.getMax() );
        paint.name( paintDTO.getName() );
        paint.quantity( paintDTO.getQuantity() );
        paint.typePaint( paintDTO.getTypePaint() );

        return paint.build();
    }

    @Override
    public PaintDTO toDTO(Paint paint) {
        if ( paint == null ) {
            return null;
        }

        PaintDTOBuilder paintDTO = PaintDTO.builder();

        paintDTO.brand( paint.getBrand() );
        paintDTO.id( paint.getId() );
        paintDTO.max( paint.getMax() );
        paintDTO.name( paint.getName() );
        paintDTO.quantity( paint.getQuantity() );
        paintDTO.typePaint( paint.getTypePaint() );

        return paintDTO.build();
    }
}
