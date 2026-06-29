package com.persona.ms4.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.modelo.Cliente;
import com.persona.ms4.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> findAll() {
        List<ClienteDTO> listaDTOs = new ArrayList<>();
        List<Cliente> clientesReales = clienteRepository.findAll();
        for (Cliente cli : clientesReales) {
            listaDTOs.add(convertirADTO(cli));
        }
        return listaDTOs;
    }

    public ClienteDTO buscarPorId(Integer id){
        Cliente cli = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrada con id: " + id));
        return convertirADTO(cli);
    }

    public ClienteDTO guardarCliente(Cliente nuevoCliente) {
        Cliente guardado = clienteRepository.save(nuevoCliente);
        return convertirADTO(guardado);
    }

    public String eliminarPorId(Integer id){
        try{
            Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede eliminar, el cliente no existe con el id:" + id));
            clienteRepository.delete(cliente);
            return "Cliente eliminado con exito";
        } catch (RuntimeException e){
            return e.getMessage();
        }
    }

    public ClienteDTO buscarPorCorreo(String correo) {
    return clienteRepository.findByCorreo(correo)
            .map(this::convertirADTO)
            .orElseThrow(() -> new RuntimeException("No se encontró un cliente con el correo: " + correo));
}
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = clienteRepository.findByNombres(nombre);
        if (clientes.isEmpty()) {
          throw new RuntimeException("No se encontraron clientes que coincidan con: " + nombre);
    }
        return clientes;
    }

    public ClienteDTO convertirADTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombres(cliente.getNombres());
        dto.setApellidos(cliente.getApellidos());
        dto.setCorreo(cliente.getCorreo());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }

}
