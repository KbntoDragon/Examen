package com.inventario.ms.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servicios")

public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El Nombre del Servicio es obligatorio")
    @Size(max = 200, message = "Puede tener un maximo de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombreServicio;

    @Column(nullable = false, length = 400)
    private String descServicio;

    @Column(nullable = false, length = 10)
    private Double valorDelServicio;

    private Integer boleta_id;
}
