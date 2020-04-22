package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource(path = "/contactmessages")
@CrossOrigin("*")
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
