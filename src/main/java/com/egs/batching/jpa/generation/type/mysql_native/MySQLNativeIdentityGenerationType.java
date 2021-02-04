package com.egs.batching.jpa.generation.type.mysql_native;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MySQLNativeIdentityGenerationType {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Version
    private int version;

    private String description;

    public MySQLNativeIdentityGenerationType(String description) {
        this.description = description;
    }

    public MySQLNativeIdentityGenerationType() {

    }
}