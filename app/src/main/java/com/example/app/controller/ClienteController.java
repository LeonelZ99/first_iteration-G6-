package com.example.app.controller;

import com.example.app.format.ApiResponse;
import com.example.app.model.Cliente;
// import com.example.app.repository.ClienteRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.service.ClienteService;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // @GetMapping
    // public List<Cliente> all() {
    //     return repo.findAll();
    // }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> getClient(@PathVariable Long id) {
        ApiResponse<Cliente> response = new ApiResponse<>(
            "success",
            "success message",
            this.clienteService.getCliente(id)
        );

        return ResponseEntity.ok(response);
    }

    // @PostMapping
    // public Cliente create(@RequestBody Cliente cliente) {
    //     return repo.save(cliente);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
    //     return repo.findById(id).map(db -> {
    //         db.setNombre(cliente.getNombre());
    //         db.setDireccion(cliente.getDireccion());
    //         db.setDni(cliente.getDni());
    //         db.setCuil(cliente.getCuil());
    //         // db.setRol(cliente.getRol());
    //         return ResponseEntity.ok(repo.save(db));
    //     }).orElse(ResponseEntity.notFound().build());
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> delete(@PathVariable Long id) {
    //     if (!repo.existsById(id)) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     repo.deleteById(id);
    //     return ResponseEntity.noContent().build();
    // }
}
