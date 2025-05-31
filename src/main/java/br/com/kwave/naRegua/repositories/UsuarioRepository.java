package br.com.kwave.naRegua.repositories;

import br.com.kwave.naRegua.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    // Método para encontrar um usuário pelo email
    Optional<Usuario> findByEmail(String email);
}
