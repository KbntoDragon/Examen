package com.persona.ms4.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.controller.EmpleadoController;

@Component
public class EmpleadoModelAssembler implements RepresentationModelAssembler<EmpleadoDTO, EntityModel<EmpleadoDTO>>{

    @Override
    public EntityModel<EmpleadoDTO> toModel(EmpleadoDTO empleado) {
        return EntityModel.of(empleado,
            linkTo(methodOn(EmpleadoController.class).buscarPorId(empleado.getId())).withSelfRel(),
            linkTo(methodOn(EmpleadoController.class).obtenerTodos()).withRel("empleados"),
            linkTo(methodOn(EmpleadoController.class).eliminarEmpleado(empleado.getId())).withRel("Eliminar"),
            linkTo(methodOn(EmpleadoController.class).actualizarEmpleado(empleado.getId(), null)).withRel("Actualizar"));

    }
}
