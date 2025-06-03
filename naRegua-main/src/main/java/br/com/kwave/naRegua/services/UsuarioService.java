package br.com.kwave.naRegua.services;

import br.com.kwave.naRegua.models.Usuario;
import br.com.kwave.naRegua.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public Usuario saveUser(Usuario usuario){
        //codificar ???
        return usuarioRepository.save(usuario);
    }

    public void deleteUser(Long id){
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findUserById(Long id){
        return usuarioRepository.findById(id);
    }

    // Novo método para autenticação
    public Usuario authenticate(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            //fazer uma autenticação decente ???
            if (usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null;
    }
}