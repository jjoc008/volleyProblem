package co.edu.udistrital.volley.repository;

import co.edu.udistrital.volley.domain.Secretary;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Secretary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long> {
}
