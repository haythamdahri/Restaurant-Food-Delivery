package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource
@CrossOrigin("*")
public interface MessageRepository extends JpaRepository<Message, Long> {
}
