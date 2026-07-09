package com.bicicleta.ms3.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "bicicletas")
public class Bicicleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank (message = "El material es obligatorio")
    @Size(max = 50, message = "El material debe tener un maximo de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String material;
    
    
    @ManyToMany
    @JoinTable(
        name = "bicicleta_color",
        joinColumns = @JoinColumn(name = "bicicleta_id"),
        inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private List<Color> colores;

    @ManyToOne
    @JoinTable(
        name = "bicicleta_marca",
        joinColumns = @JoinColumn(name = "bicicleta_id"),
        inverseJoinColumns = @JoinColumn(name = "marca_id")
    )
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @Column(name = "cliente_id")
    private Integer clienteId;
}
