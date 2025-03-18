package br.com.edielsonassis.authuser.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.edielsonassis.authuser.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByEmail(String email);

    boolean existsByUserNameCustom(String userName);

    boolean existsByCpf(String cpf);

    @EntityGraph(attributePaths = "permissions", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findByEmail(String email);
}