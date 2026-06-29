package com.inventario.ms.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.inventario.ms.DTO.RepuestoDTO;
import com.inventario.ms.controller.RepuestoController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RepuestoAssembler implements RepresentationModelAssembler<RepuestoDTO, EntityModel<RepuestoDTO>> {

    @Override
    public EntityModel<RepuestoDTO> toModel(RepuestoDTO repuesto) {
        return EntityModel.of(repuesto,
            linkTo(methodOn(RepuestoController.class).buscarProductoPorId(repuesto.getId())).withSelfRel(),
            linkTo(methodOn(RepuestoController.class).listarProductos()).withRel("repuestos"),
            linkTo(methodOn(RepuestoController.class).eliminarProducto(repuesto.getId())).withRel("eliminar"),
            linkTo(methodOn(RepuestoController.class).editarRepuestos(repuesto.getId(), null)).withRel("editar"));
    }
}
