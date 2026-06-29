package com.bicicleta.ms3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "modelos")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre del modelo es obligatorio")
    @Size (min = 2, max = 100, message = "El nombre del modelo debe tener 2 caracteres como minimo y 100 maximo")
    @Column (nullable = false, length = 100)
    private String nombreModelo;
    
    @NotBlank (message = "El tipo de suspension es obligatorio")
    @Size (min = 3, max = 50, message = "El tipo de suspension debe tener 3 caracteres como minimo y 50 maximo")
    @Column (nullable = false, length = 50)
    private String tipoSuspension;

    @NotBlank (message = "El nombre del modelo es obligatorio")
    @Size (min = 1, max = 10, message = "El nombre del modelo debe tener 1 caracteres como minimo y 10 maximo")
    @Column (nullable = false, length = 10)
    private String tallaCuadro;

    @ManyToOne
    @JoinColumn (name = "marca_id")
    private Marca marca;
}
