package com.inventario.ms.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.inventario.ms.DTO.ProductoDTO;
import com.inventario.ms.controller.ProductoController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).buscarProductoPorId(producto.getId())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"),
            linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("eliminar"),
            linkTo(methodOn(ProductoController.class).editarProducto(producto.getId(), null)).withRel("editar"));
    }
}
