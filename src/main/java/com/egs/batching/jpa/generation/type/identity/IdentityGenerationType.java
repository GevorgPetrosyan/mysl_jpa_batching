package com.egs.batching.jpa.generation.type.identity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IdentityGenerationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public IdentityGenerationType(String description) {
        this.description = description;
    }

    public IdentityGenerationType() {

    }
}