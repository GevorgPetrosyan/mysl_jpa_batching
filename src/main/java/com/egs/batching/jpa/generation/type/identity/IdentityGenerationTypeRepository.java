package com.egs.batching.jpa.generation.type.identity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityGenerationTypeRepository extends JpaRepository<IdentityGenerationType, Long> {
}
