package br.com.edielsonassis.authuser.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.authuser.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByCpf(String cpf);
}