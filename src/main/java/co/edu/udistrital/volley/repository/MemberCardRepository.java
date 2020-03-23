package co.edu.udistrital.volley.repository;

import co.edu.udistrital.volley.domain.MemberCard;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MemberCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberCardRepository extends JpaRepository<MemberCard, Long> {
}
