package com.ventas.ms2.DTO;

import lombok.Data;

@Data
public class ProductoDTO {
    private Integer id;
    private String nombreProducto;
    private Double precio;
    private Integer stock;
    private String codigoBarras;
    private Integer boleta_id;
}
