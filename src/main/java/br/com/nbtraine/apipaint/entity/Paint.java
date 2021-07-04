package br.com.nbtraine.apipaint.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.nbtraine.apipaint.enums.TypePaint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Paint
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer max;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePaint typePaint;
}