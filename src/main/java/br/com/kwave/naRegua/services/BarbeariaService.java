package br.com.kwave.naRegua.services;

import br.com.kwave.naRegua.models.Barbearia;
import br.com.kwave.naRegua.repositories.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarbeariaService {

    @Autowired
    private BarbeariaRepository barbeariaRepository;

    @Autowired

    public List<Barbearia> getAllBarbearias() {
        return barbeariaRepository.findAll();
    }

    public Optional<Barbearia> findBarbeariaById(Long id) { // <<-- CORRIGIDO: de Opitional para Optional
        return barbeariaRepository.findById(id);
    }

    public Barbearia saveBarbearia(Barbearia barbearia) {
        return barbeariaRepository.save(barbearia);
    }

    public void deleteBarbearia(Long id) {
        barbeariaRepository.deleteById(id);
    }

    public Barbearia authenticate(String usernameOrEmail, String senha) {
        Optional<Barbearia> barbeariaOptional;

        barbeariaOptional = barbeariaRepository.findByEmail(usernameOrEmail);

        if (barbeariaOptional.isPresent()) {
            Barbearia barbearia = barbeariaOptional.get();
            return  barbearia;
        }
        return null;
    }

    public boolean emailExists(String email) {
        return barbeariaRepository.findByEmail(email).isPresent();
    }
}