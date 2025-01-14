package br.com.edielsonassis.course.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.course.models.ModuleModel;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {}