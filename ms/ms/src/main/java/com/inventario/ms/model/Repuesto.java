package com.inventario.ms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repuestos")
public class Repuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El Nombre del Repuesto es obligatorio")
    @Size(max = 200, message = "Puede tener un maximo de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombreRepuesto;

    @Column(nullable = false)
    private Double precio;
    
    @Min(value = 0, message = "El stock mínimo es 0")
    @Column(nullable = false)
    private Integer stockRepuesto;

    @NotBlank(message = "El Codigo del Producto es obligatorio")
    @Size(max = 100, message = "Puede tener un maximo de 100 caracteres")
    @Column(nullable = true, length = 100)
    private String codigoBarras;

    private Integer boleta_id;
}
