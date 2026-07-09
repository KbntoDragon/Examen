package com.inventario.ms.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.ms.DTO.ProductoDTO;
import com.inventario.ms.model.Producto;
import com.inventario.ms.services.ProductoService;
import com.inventario.ms.assemblers.ProductoAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Gestión de productos del inventario")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoAssembler assembler;

    @Operation(summary = "Listar todos los productos",
               description = "Devuelve la lista completa de productos. Responde 204 si no hay ninguno.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
        @ApiResponse(responseCode = "204", description = "No hay productos registrados")
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarProductos() {
        List<ProductoDTO> productos = productoService.obtenerProductos();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<ProductoDTO>> productoModels = productos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productoModels,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @Operation(summary = "Buscar producto por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no existe")
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Integer id) {
        try {
            ProductoDTO producto = productoService.obtenerProductoDTOPorId(id);
            return ResponseEntity.ok(assembler.toModel(producto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar productos por código de barras")
    @GetMapping(value = "/codigo/{codigoBarra}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorCodigoBarra(@PathVariable String codigoBarra) {
        try{
            List<ProductoDTO> productos = productoService.buscarPorCodigoDeBarraDTO(codigoBarra);
            List<EntityModel<ProductoDTO>> productoModels = productos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productoModels,
                    linkTo(methodOn(ProductoController.class).buscarPorCodigoBarra(codigoBarra)).withSelfRel());
            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear un producto",
               description = "Valida que el precio sea mayor a 0 y el stock no sea negativo.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (precio o stock)")
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {
       try {
         Producto producto2 = productoService.guardarProducto(producto);
         ProductoDTO dto = productoService.obtenerProductoDTOPorId(producto2.getId());
         return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
       } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }

    @Operation(summary = "Eliminar producto por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        String resultado = productoService.eliminarProducto(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Buscar productos por nombre (contiene, ignora mayúsculas)")
    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre){
        List<ProductoDTO> productos = productoService.buscarPorNombreDTO(nombre);
        if(productos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<ProductoDTO>> productoModels = productos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productoModels,
                linkTo(methodOn(ProductoController.class).buscarPorNombre(nombre)).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @Operation(summary = "Listar productos sin stock")
    @GetMapping(value = "/sin-stock", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> sinStock() {
        List<ProductoDTO> productos = productoService.obtenerProductoSinStockDTO();
        if (productos == null || productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<ProductoDTO>> productoModels = productos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productoModels,
                linkTo(methodOn(ProductoController.class).sinStock()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar un producto existente")
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> editarProducto(@PathVariable Integer id, @RequestBody Producto producto){
        try {
            Producto editado = productoService.guardarProducto(producto);
            ProductoDTO dto = productoService.obtenerProductoDTOPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
