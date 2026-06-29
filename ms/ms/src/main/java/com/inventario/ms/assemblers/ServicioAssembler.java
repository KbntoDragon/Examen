package com.inventario.ms.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.inventario.ms.DTO.ServicioDTO;
import com.inventario.ms.controller.ServicioController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ServicioAssembler implements RepresentationModelAssembler<ServicioDTO, EntityModel<ServicioDTO>> {

    @Override
    public EntityModel<ServicioDTO> toModel(ServicioDTO servicio) {
        return EntityModel.of(servicio,
            linkTo(methodOn(ServicioController.class).buscarProductoPorId(servicio.getId())).withSelfRel(),
            linkTo(methodOn(ServicioController.class).listarServicios()).withRel("servicios"),
            linkTo(methodOn(ServicioController.class).eliminarServicio(servicio.getId())).withRel("eliminar"),
            linkTo(methodOn(ServicioController.class).editarServicio(servicio.getId(), null)).withRel("editar"));
    }
}
