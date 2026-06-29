package com.persona.ms4.modelo;

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
@Table (name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank (message = "El nombre es obligatorio.")
    @Size (min = 3, max = 30, message = "El nombre debe tener minimo 4 caracteres y maximo 20")
    @Column (nullable = false, length = 30)
    private String nombres;

    @NotBlank (message = "El apellido es obligatorio.")
    @Size (min = 3, max = 30, message = "El apellido debe tener minimo 4 caracteres y maximo 20")
    @Column (nullable = false, length = 30)
    private String apellidos;

    @NotBlank (message = "El RUT es obligatorio.")
    @Column (nullable = false)
    private String rut;

    @NotBlank (message = "El correo es obligatorio.")
    @Size (min = 5, max = 100, message = "El corro debe tener minimo 5 caracteres y maximo 100")
    @Column (nullable = false, length = 100)
    private String correo;
    
    @NotBlank (message = "El telefono es obligatorio.")
    @Size (min = 9, max = 12, message = "El telefono debe tener minimo 9 caracteres y maximo 12")
    @Column (nullable = false, length = 12)
    private String telefono;

}
