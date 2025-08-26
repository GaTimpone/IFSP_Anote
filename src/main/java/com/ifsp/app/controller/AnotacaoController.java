package com.ifsp.app.controller;

import com.ifsp.app.controller.dto.AnotacaoDTO;
import com.ifsp.app.model.Anotacao;
import com.ifsp.app.service.AnotacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anotacoes")
public class AnotacaoController {

    private final AnotacaoService anotacaoService;

    public AnotacaoController(AnotacaoService anotacaoService) {
        this.anotacaoService = anotacaoService;
    }

    @GetMapping
    public List<Anotacao> findAll() {
        return anotacaoService.findAll();
    }

    @GetMapping("/{id}")
    public Anotacao findById(@PathVariable Long id) {
        return anotacaoService.findById(id);
    }

    @PostMapping
    public Anotacao create(@RequestBody AnotacaoDTO anotacaoDTO) {
        return anotacaoService.save(anotacaoDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        anotacaoService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Anotacao update(@PathVariable Long id, @RequestBody AnotacaoDTO anotacaoDTO) {
        return anotacaoService.update(id, anotacaoDTO);
    }

    @PostMapping("/{anotacaoId}/tags/{tagId}")
    public Anotacao addTag(@PathVariable Long anotacaoId, @PathVariable Long tagId) {
        return anotacaoService.addTagToAnotacao(anotacaoId, tagId);
    }

    @DeleteMapping("/{anotacaoId}/tags/{tagId}")
    public Anotacao removeTag(@PathVariable Long anotacaoId, @PathVariable Long tagId) {
        return anotacaoService.removeTagFromAnotacao(anotacaoId, tagId);
    }
}
