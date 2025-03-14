package br.com.edielsonassis.authuser.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.authuser.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    
    Optional<RoleModel> findByRoleName(String name);
}