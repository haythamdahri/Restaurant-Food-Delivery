package org.restaurant.salado.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @Lob
    private byte[] file;

    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
    }

}
