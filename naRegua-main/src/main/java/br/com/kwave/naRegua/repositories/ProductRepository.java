package br.com.kwave.naRegua.repositories;

import br.com.kwave.naRegua.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Opcional, mas boa prática
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Você pode adicionar métodos personalizados aqui, como:
    // List<Product> findByNameContainingIgnoreCase(String name);
}