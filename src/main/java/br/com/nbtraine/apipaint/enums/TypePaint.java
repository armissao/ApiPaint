package br.com.nbtraine.apipaint.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TypePaint
 */
@Getter
@AllArgsConstructor
public enum TypePaint {

    EGGSHELL("Eggshell"),
    FLAT("Flat"),
    GLOSS("Gloss"),
    LATEX("Latex-Based"),
    MATTE("Matte"),
    OIL("Oil-Based"),
    PRIMER("Primer"),
    SATIN("Satin"),
    SEMIGLOSS("Semi-Gloss"),
    WATER("Water-Based");

    private final String description;
}