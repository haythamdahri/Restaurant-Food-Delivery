package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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
    private String mediaType;

    @Lob
    @Column(name = "file")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] file;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = new Date();
    }

}
