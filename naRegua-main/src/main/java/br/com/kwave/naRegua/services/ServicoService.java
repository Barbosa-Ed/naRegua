package br.com.kwave.naRegua.services;

import br.com.kwave.naRegua.models.Servico;
import br.com.kwave.naRegua.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> getServicosByBarbeariaId(Long barbeariaId) {
        return servicoRepository.findByBarbeariaId(barbeariaId);
    }

    public Servico saveServico(Servico servico) {
        return servicoRepository.save(servico);
    }
}
