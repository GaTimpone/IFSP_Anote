package com.ifsp.app.controller;

import com.ifsp.app.controller.dto.LoginRequestDTO;
import com.ifsp.app.controller.dto.UsuarioDTO;
import com.ifsp.app.model.Usuario;
import com.ifsp.app.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequestDTO loginRequest) {
        return usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());
    }

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Usuario findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public Usuario create(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.save(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        usuarioService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.update(id, usuarioDTO);
    }
}
