package br.com.edielsonassis.course.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.course.models.CourseModel;

public interface CourseRepository extends JpaRepository<CourseModel, UUID> {}