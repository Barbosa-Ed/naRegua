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

    private final Path uploadDir = Paths.get("src/main/resources/static/img/perfil").toAbsolutePath().normalize();

    public BarbeariaService() {
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


    public String saveFotoPerfil(MultipartFile file, Long barbeariaId) throws Exception {
        return saveImage(file, barbeariaId, "perfil");
    }

    public String saveFotoCapa(MultipartFile file, Long barbeariaId) throws Exception {
        return saveImage(file, barbeariaId, "capa");
    }

    private String saveImage(MultipartFile file, Long barbeariaId, String type) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path targetLocation = this.uploadDir.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Optional<Barbearia> barbeariaOptional = barbeariaRepository.findById(barbeariaId);
        if (barbeariaOptional.isPresent()) {
            Barbearia barbearia = barbeariaOptional.get();
            String fileUrl = "/img/perfil/" + uniqueFileName;

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