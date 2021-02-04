package com.egs.batching.jpa.generation.type.table;

import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGenerationType {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Version
    private int version;

    private String description;

    public TableGenerationType(String description) {
        this.description = description;
    }

    public TableGenerationType() {

    }
}