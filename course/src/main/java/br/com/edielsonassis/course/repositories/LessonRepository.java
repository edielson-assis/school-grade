package br.com.edielsonassis.course.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.course.models.LessonModel;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {}