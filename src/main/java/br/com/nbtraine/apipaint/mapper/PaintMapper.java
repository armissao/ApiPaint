package br.com.nbtraine.apipaint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.nbtraine.apipaint.dto.PaintDTO;
import br.com.nbtraine.apipaint.entity.Paint;

@Mapper
public interface PaintMapper {
   
   PaintMapper INSTANCE = Mappers.getMapper(PaintMapper.class);

    Paint toModel(PaintDTO paintDTO);

    PaintDTO toDTO(Paint paint);
}
