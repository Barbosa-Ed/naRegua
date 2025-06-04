package br.com.kwave.naRegua.services;

import br.com.kwave.naRegua.models.Barbearia;
import br.com.kwave.naRegua.repositories.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // Importar
import org.springframework.util.StringUtils; // Importar
import java.nio.file.Files; // Importar
import java.nio.file.Path; // Importar
import java.nio.file.Paths; // Importar
import java.nio.file.StandardCopyOption; // Importar

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Importar para gerar nomes de arquivo únicos

@Service
public class BarbeariaService {

    @Autowired
    private BarbeariaRepository barbeariaRepository;

    // Define o diretório base para upload de imagens
    // CUIDADO: Em produção, você pode querer um caminho absoluto ou um serviço de storage em nuvem
    private final Path uploadDir = Paths.get("src/main/resources/static/img/perfil").toAbsolutePath().normalize();

    public BarbeariaService() {
        // Garante que o diretório de upload exista
        try {
            Files.createDirectories(this.uploadDir);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório de upload!", ex);
        }
    }

    public List<Barbearia> getAllBarbearias() {
        return barbeariaRepository.findAll();
    }

    public Optional<Barbearia> findBarbeariaById(Long id) {
        return barbeariaRepository.findById(id);
    }

    public Barbearia saveBarbearia(Barbearia barbearia) {
        return barbeariaRepository.save(barbearia);
    }

    public void deleteBarbearia(Long id) {
        barbeariaRepository.deleteById(id);
    }

    public Barbearia authenticate(String email, String senha) {
        Optional<Barbearia> barbeariaOptional = barbeariaRepository.findByEmail(email);

        if (barbeariaOptional.isPresent()) {
            Barbearia barbearia = barbeariaOptional.get();
            if (barbearia.getSenha().equals(senha)) {
                return barbearia;
            }
        }
        return null;
    }

    public boolean emailExists(String email) {
        return barbeariaRepository.findByEmail(email).isPresent();
    }

    // --- NOVOS MÉTODOS PARA UPLOAD DE IMAGEM ---

    public String saveFotoPerfil(MultipartFile file, Long barbeariaId) throws Exception {
        return saveImage(file, barbeariaId, "perfil");
    }

    public String saveFotoCapa(MultipartFile file, Long barbeariaId) throws Exception {
        return saveImage(file, barbeariaId, "capa");
    }

    private String saveImage(MultipartFile file, Long barbeariaId, String type) throws Exception {
        // Normaliza o nome do arquivo
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Gera um nome de arquivo único para evitar colisões
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path targetLocation = this.uploadDir.resolve(uniqueFileName);

        // Copia o arquivo para o destino
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Atualiza a URL no banco de dados para a barbearia
        Optional<Barbearia> barbeariaOptional = barbeariaRepository.findById(barbeariaId);
        if (barbeariaOptional.isPresent()) {
            Barbearia barbearia = barbeariaOptional.get();
            String fileUrl = "/img/perfil/" + uniqueFileName; // URL para acesso via web

            if ("perfil".equals(type)) {
                barbearia.setUrlFotoPerfil(fileUrl);
            } else if ("capa".equals(type)) {
                barbearia.setUrlFotoCapa(fileUrl);
            }
            barbeariaRepository.save(barbearia);
            return fileUrl;
        } else {
            throw new RuntimeException("Barbearia não encontrada com o ID: " + barbeariaId);
        }
    }
}