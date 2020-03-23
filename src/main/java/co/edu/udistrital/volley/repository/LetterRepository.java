package co.edu.udistrital.volley.repository;

import co.edu.udistrital.volley.domain.Letter;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Letter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
}
