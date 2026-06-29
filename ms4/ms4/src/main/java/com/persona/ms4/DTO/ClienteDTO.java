package com.persona.ms4.DTO;

import lombok.Data;

@Data
public class ClienteDTO {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private Integer bicicletaId;

}
