package br.com.kwave.naRegua.repositories;

import br.com.kwave.naRegua.models.Barbearia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeariaRepository extends MongoRepository<Barbearia, String> {
    // Pode adicionar buscas personalizadas aqui se quiser
}
