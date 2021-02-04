package com.egs.batching.jpa.generation.type.mysql_native;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLNativeIdentityGenerationTypeRepository extends JpaRepository<MySQLNativeIdentityGenerationType, Long> {
}
