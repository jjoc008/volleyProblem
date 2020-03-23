package co.edu.udistrital.volley.repository;

import co.edu.udistrital.volley.domain.RegistryBook;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RegistryBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistryBookRepository extends JpaRepository<RegistryBook, Long> {
}
