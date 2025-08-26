package com.ifsp.app.service;

import com.ifsp.app.controller.dto.AnotacaoDTO;
import com.ifsp.app.model.Anotacao;
import com.ifsp.app.model.Caderno;
import com.ifsp.app.model.Tag;
import com.ifsp.app.model.Usuario;
import com.ifsp.app.model.repository.AnotacaoRepository;
import com.ifsp.app.model.repository.CadernoRepository;
import com.ifsp.app.model.repository.TagRepository;
import com.ifsp.app.model.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnotacaoService {

    private final AnotacaoRepository anotacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CadernoRepository cadernoRepository;
    private final TagRepository tagRepository;

    public AnotacaoService(AnotacaoRepository anotacaoRepository,
                           UsuarioRepository usuarioRepository,
                           CadernoRepository cadernoRepository,
                           TagRepository tagRepository) {
        this.anotacaoRepository = anotacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cadernoRepository = cadernoRepository;
        this.tagRepository = tagRepository;
    }

    public List<Anotacao> findAll() {
        return anotacaoRepository.findAll();
    }

    public Anotacao findById(Long id) {
        return anotacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anotação não encontrada"));
    }

    public Anotacao save(AnotacaoDTO anotacaoDTO) {
        Usuario usuario = usuarioRepository.findById(anotacaoDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        Caderno caderno = null;
        if (anotacaoDTO.getCadernoId() != null && anotacaoDTO.getCadernoId() > 0) {
            caderno = cadernoRepository.findById(anotacaoDTO.getCadernoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caderno não encontrado"));
        }
        Anotacao anotacao = new Anotacao();
        anotacao.setTitulo(anotacaoDTO.getTitulo());
        anotacao.setCorpo(anotacaoDTO.getCorpo());
        anotacao.setUsuario(usuario);
        anotacao.setCaderno(caderno);
        return anotacaoRepository.save(anotacao);
    }

    public void deleteById(Long id) {
        if (!anotacaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anotação não encontrada");
        }
        anotacaoRepository.deleteById(id);
    }

    public Anotacao update(Long id, AnotacaoDTO anotacaoDTO) {
        Anotacao anotacao = findById(id);
        if (anotacaoDTO.getTitulo() != null) {
            anotacao.setTitulo(anotacaoDTO.getTitulo());
        }
        if (anotacaoDTO.getCorpo() != null) {
            anotacao.setCorpo(anotacaoDTO.getCorpo());
        }
        if (anotacaoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(anotacaoDTO.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        }
        if (anotacaoDTO.getCadernoId() != null) {
            Caderno caderno = cadernoRepository.findById(anotacaoDTO.getCadernoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caderno não encontrado"));
            anotacao.setCaderno(caderno);
        } else {
            anotacao.setCaderno(null);
        }
        return anotacaoRepository.save(anotacao);
    }

    public Anotacao addTagToAnotacao(Long anotacaoId, Long tagId) {
        Anotacao anotacao = findById(anotacaoId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag não encontrada"));

        if (!anotacao.getTags().contains(tag)) {
            anotacao.getTags().add(tag);
            anotacao = anotacaoRepository.save(anotacao);
        }

        return anotacao;
    }

    public Anotacao removeTagFromAnotacao(Long anotacaoId, Long tagId) {
        Anotacao anotacao = findById(anotacaoId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag não encontrada"));

        if (anotacao.getTags().contains(tag)) {
            anotacao.getTags().remove(tag);
            anotacao = anotacaoRepository.save(anotacao);
        }

        return anotacao;
    }
}