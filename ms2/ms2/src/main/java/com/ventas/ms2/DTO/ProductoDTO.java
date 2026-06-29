package com.ventas.ms2.DTO;

import lombok.Data;

/**
 * DTO de solo lectura del recurso Producto que vive en el microservicio de
 * inventario (ms). Se usa para deserializar la respuesta REST al verificar
 * que un producto existe antes de agregarlo a una boleta.
 */
@Data
public class ProductoDTO {
    private Integer id;
    private String nombreProducto;
    private Double precio;
    private Integer stock;
    private String codigoBarras;
    private Integer boleta_id;
}
