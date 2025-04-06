package com.stride.tracking.commons.persistence.mongo;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@MappedSuperclass
@Getter
@Setter
public abstract class MongoBaseEntity extends MongoAuditEntity {
    @Id
    private String id;
}

