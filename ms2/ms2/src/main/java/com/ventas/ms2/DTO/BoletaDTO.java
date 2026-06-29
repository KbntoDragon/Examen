package com.ventas.ms2.DTO;

import java.util.List;
import lombok.Data;

@Data
public class BoletaDTO {
    private Integer id;
    private Double precio;
    private String tipoPago;
    private List<Integer> empleadoId;
    private List<Integer> productoId;
    private List<Integer> repuestoId;
    private List<Integer> servicioId;
}
