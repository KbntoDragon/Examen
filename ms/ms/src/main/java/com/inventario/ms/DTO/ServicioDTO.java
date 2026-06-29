package com.inventario.ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTO {
    private Integer id;
    private String nombreServicio;
    private String descServicio;
    private Double valorDelServicio;
    private Integer boleta_id;
}
