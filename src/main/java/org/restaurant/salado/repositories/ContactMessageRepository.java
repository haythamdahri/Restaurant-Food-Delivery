package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource(path = "/contactmessages")
@CrossOrigin("*")
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    @Query(value = "SELECT cm FROM ContactMessage cm where CONCAT(cm.id, '') like %:search% or lower(cm.email) like %:search% or " +
            "lower(cm.content) like %:search% or lower(cm.firstName) like %:search% or lower(cm.lastName) like %:search% or " +
            "lower(cm.phone) like %:search% or CONCAT(cm.responded, '') like %:search%")
    Page<ContactMessage> searchContactMessages(@PageableDefault Pageable pageable, String search);

}
