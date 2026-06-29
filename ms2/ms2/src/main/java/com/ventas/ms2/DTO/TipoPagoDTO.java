package com.ventas.ms2.DTO;

import java.util.List;

import lombok.Data;

@Data
public class TipoPagoDTO {
    private Integer id;
    private String tipo;
    private List<Integer> boletaId;
}
