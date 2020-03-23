package co.edu.udistrital.volley.repository;

import co.edu.udistrital.volley.domain.PostalAddress;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PostalAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Long> {
}
