package com.persona.ms4.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.controller.ClienteController;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>>{

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO cliente) {
        return EntityModel.of(cliente,
            linkTo(methodOn(ClienteController.class).porId(cliente.getId())).withSelfRel(),
            linkTo(methodOn(ClienteController.class).todos()).withRel("clientes"),
            linkTo(methodOn(ClienteController.class).eliminarCliente(cliente.getId())).withRel("Eliminar"),
            linkTo(methodOn(ClienteController.class).actualiarCliente(cliente.getId(), null)).withRel("Actualizar"));
    }

}