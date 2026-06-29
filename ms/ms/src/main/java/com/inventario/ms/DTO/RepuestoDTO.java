package com.inventario.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepuestoDTO {
    private Integer id;
    private String nombreRepuesto;
    private Double precio;
    private Integer stockRepuesto;
    private String codigoBarras;
    private Integer boleta_id;
}