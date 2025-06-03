package br.com.kwave.naRegua.repositories;

import br.com.kwave.naRegua.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByBarbeariaId(Long barbeariaId);
}
