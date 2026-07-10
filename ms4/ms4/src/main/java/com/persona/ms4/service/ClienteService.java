package com.persona.ms4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.modelo.Cliente;
import com.persona.ms4.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> obtenerTodos() {
        log.info("Obteniendo todos los datos de los Clientes");
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ClienteDTO buscarPorId(Integer id) {
        log.info("Buscando al Cliente por ID", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado o no existe"));
        return convertirADTO(cliente);
    }

    public ClienteDTO guardarCliente(Cliente cliente) {
        log.info("Eliminando un Cliente del sistema");
        return convertirADTO(clienteRepository.save(cliente));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando un Cliente del sistema");
        try {
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("No es posible eliminar, el Cliente con el ID: " + id + "no existe"));
            clienteRepository.delete(cliente);
            return "El Cliente " + cliente.getNombres() + " ha sido eliminado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public ClienteDTO actualizarCliente(Integer id, Cliente clienteActualizado) {
        log.info("Actualizando informacion del Cliente con ID: ", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombres(clienteActualizado.getNombres());
        cliente.setApellidos(clienteActualizado.getApellidos());
        cliente.setRut(clienteActualizado.getRut());
        cliente.setCorreo(clienteActualizado.getCorreo());
        cliente.setTelefono(clienteActualizado.getTelefono());
        return convertirADTO(clienteRepository.save(cliente));
    }

    public List<ClienteDTO> buscarPorNombres(String nombre) {
        return clienteRepository.findByNombres(nombre).stream()
               .map(this::convertirADTO)
               .toList();
    }

    public List<ClienteDTO> buscarPorCorreos(String correo) {
        return clienteRepository.findByCorreo(correo).stream()
               .map(this::convertirADTO)
               .toList();
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();

        dto.setId(cliente.getId());
        dto.setNombres(cliente.getNombres());
        dto.setApellidos(cliente.getApellidos());
        dto.setCorreo(cliente.getCorreo());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }
}