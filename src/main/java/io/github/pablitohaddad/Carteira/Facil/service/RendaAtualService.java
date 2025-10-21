package io.github.pablitohaddad.Carteira.Facil.service;

import io.github.pablitohaddad.Carteira.Facil.model.RendaAtual;
import io.github.pablitohaddad.Carteira.Facil.repository.RendaAtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RendaAtualService {

    @Autowired
    private RendaAtualRepository rendaAtualRepository;

    /**
     * Busca o registro de renda atual pelo ID do usuário.
     * @param usuarioId O ID do usuário.
     * @return O objeto RendaAtual ou vazio, se não encontrado.
     */
    public Optional<RendaAtual> buscarRendaPorUsuarioId(Long usuarioId) {
        return rendaAtualRepository.findByUsuarioId(usuarioId);
    }
}